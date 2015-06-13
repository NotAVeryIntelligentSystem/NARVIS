/*
 * The MIT License
 *
 * Copyright 2015 uwy.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.narvis.frontend.twitter.input;

import com.narvis.common.debug.NarvisLogger;
import com.narvis.dataaccess.exception.PersistException;
import com.narvis.engine.NarvisEngine;
import com.narvis.frontend.MessageInOut;
import com.narvis.frontend.interfaces.IFrontEnd;
import com.narvis.frontend.interfaces.IInput;
import com.narvis.frontend.twitter.AccessTwitter;
import com.narvis.frontend.twitter.TwitterMessageInOut;
import java.util.List;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import twitter4j.*;

/**
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class Input implements IInput  {

    private final static int REFRESH_PERIOD_SECOND = 70;
    public String internalName = "nakJarvis";
    private final Timer listenloop;
    private final Twitter twitterLink;
    private final AccessTwitter accessTwitter;

    public Input(Twitter twit, AccessTwitter accessTwitter) {
        this.twitterLink = twit;
        this.listenloop = new Timer("Twitter listen");
        this.accessTwitter = accessTwitter;
    }

    private String getOtherResponseName(String tweet) {
        String recepiantsResponse = "";
        String[] parts = tweet.split(" ");
        for (String s : parts) {
            if (s.charAt(0) == '@') { // is a name
                if (!s.split("@")[1].equals(this.internalName)) { // is not NARVIS
                    recepiantsResponse += ";" + s.split("@")[1];
                }
            }
        }
        return recepiantsResponse;
    }

    /**
     * Remove all specific Twitter vocabulary from the tweet to transform it in
     * a simple sentence
     *
     * @param tweet : The tweet to transform
     * @return A sentence withour Twitter specificity
     */
    private String getCleanTweet(String tweet) {
        String cleanTweet = "";
        String[] parts = tweet.replaceAll("\\s+", " ").split(" ");
        for (String s : parts) {
            switch (s.charAt(0)) {
                /* If it's a @ (ex: @nakJarvis), that's mean it's a person. 
                 To avoid abiguitie, we replace the name of the personne with "someone".
                 The FrontEnd twitter already know who answer to. */
                case '@':
                    cleanTweet += " someone";
                    break;
                /* If it's a hashtag, we remove the # and use the it like a normal word */
                case '#':
                    cleanTweet += " " + s.substring(1);
                    break;
                /* Otherwise, we just put the word at the end of the sentence */
                default:
                    cleanTweet += " " + s;
            }
        }
        return cleanTweet;
    }

    private String[] tweetParser(Status status) {
        String[] returnValue = new String[2];
        returnValue[0] = getCleanTweet(status.getText());
        returnValue[1] = status.getUser().getScreenName() + getOtherResponseName(status.getText());
        return returnValue;
    }

    public Iterable<MessageInOut> getInput()  {
        Stack<MessageInOut> retVal = new Stack<>();
        try {
            List<Status> statuses = this.twitterLink.getMentionsTimeline();
            long lastMessageId = Long.parseLong(accessTwitter.getConf().getConf().getData("LastTwitterMessageId"));
            Status lastStatus = statuses.get(0);
            this.accessTwitter.getConf().getConf().setData("LastTwitterMessageId", Long.toString(lastStatus.getId()));
            this.accessTwitter.getConf().persist();
            if(lastMessageId == 0) {
                String[] tmp = this.tweetParser(lastStatus);
                retVal.push(new TwitterMessageInOut(accessTwitter.getConf().getName(), tmp[0], tmp[1], accessTwitter, lastStatus.getId()));
            }
            else {
                int messageIndex = 0;
                while(lastStatus.getId() !=  lastMessageId && messageIndex < statuses.size()) {
                    String[] tmp = this.tweetParser(lastStatus);
                    retVal.push(new TwitterMessageInOut(accessTwitter.getConf().getName(), tmp[0], tmp[1], accessTwitter, lastStatus.getId()));
                    messageIndex++;
                    lastStatus = statuses.get(messageIndex);
                }
            }
            return retVal;
        } catch (PersistException | TwitterException ex) {
            NarvisLogger.logException(ex);
        }
        return retVal;
    }

    @Override
    public void start() {
        this.listenloop.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    for(MessageInOut lastMessage : getInput()) {
                        NarvisLogger.logInfo("From : " + lastMessage.getAnswerTo());
                        NarvisLogger.logInfo("Received message : " + lastMessage.getContent());
                        NarvisEngine.getInstance().getMessage(lastMessage);
                    }
                } catch (Exception ex) {
                    NarvisLogger.logException(ex);
                }
            }
        }, 0, REFRESH_PERIOD_SECOND * 1000);
    }

    @Override
    public void close() {
        this.listenloop.cancel();
    }

    @Override
    public IFrontEnd getFrontEnd() {
        return accessTwitter;
    }
}

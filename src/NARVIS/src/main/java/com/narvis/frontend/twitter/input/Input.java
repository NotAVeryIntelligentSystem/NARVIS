/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import java.util.Timer;
import java.util.TimerTask;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 *
 * @author Alban
 */
public class Input implements IInput {

    private final static int REFRESH_PERIOD_SECOND = 120;
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
        String[] parts = tweet.split(" ");
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

    public MessageInOut getInput() throws PersistException {
        try {
            List<Status> statuses = this.twitterLink.getMentionsTimeline();
            Status lastStatus = statuses.get(0);
            NarvisLogger.logInfo("Received following status : " + lastStatus.getText());
            if (lastStatus.getId() != Long.parseLong(accessTwitter.getConf().getConf().getData("LastTwitterMessageId"))) {
                accessTwitter.getConf().getConf().setData("LastTwitterMessageId", Long.toString(lastStatus.getId()));
                accessTwitter.getConf().persist();
                String[] tmp = this.tweetParser(lastStatus);
                return new TwitterMessageInOut(accessTwitter.getConf().getName(), tmp[0], tmp[1], accessTwitter, lastStatus.getId());
            } else {
                NarvisLogger.logInfo("Ignoring tweet because identical to previous one");

            }
        } catch (TwitterException ex) {
            NarvisLogger.logException(ex);
        }
        return null;
    }

    @Override
    public void start() {
        this.listenloop.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    MessageInOut lastMessage = getInput();
                    if (lastMessage != null) {

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

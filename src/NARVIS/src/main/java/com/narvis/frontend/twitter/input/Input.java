/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.frontend.twitter.input;

import com.narvis.common.debug.NarvisLogger;
import com.narvis.dataaccess.exception.PersistException;
import com.narvis.dataaccess.impl.FrontEndConfigurationDataProvider;
import com.narvis.engine.NarvisEngine;
import com.narvis.frontend.MessageInOut;
import com.narvis.frontend.interfaces.IInput;
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

    public String internalName = "nakJarvis";
    private final Timer listenloop;
    private final Twitter twitterLink;
    private FrontEndConfigurationDataProvider conf;

    public Input(Twitter twit, FrontEndConfigurationDataProvider conf) {
        this.twitterLink = twit;
        this.listenloop = new Timer("Twitter listen");
        this.conf = conf;
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

    private String getCleanTweet(String tweet) {
        String cleanTweet = "";
        String[] parts = tweet.split(" ");
        for (String s : parts) {
            if (!(s.charAt(0) == '@')) {
                if (s.charAt(0) == '#') {
                    cleanTweet += " " + s.replace("#", "");
                } else {
                    cleanTweet += " " + s;
                }
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
            if(lastStatus.getId() != Long.parseLong(this.conf.getConf().getData("LastTwitterMessageId"))) {
                this.conf.getConf().setData("LastTwitterMessageId", Long.toString(lastStatus.getId()));
                this.conf.persist();
                String[] tmp = this.tweetParser(lastStatus);
                return new MessageInOut(this.conf.getName(), tmp[0], tmp[1]);
            }
            else {
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
                }
                catch (Exception ex) {
                    NarvisLogger.logException(ex);
                }
            }
        }, 0, 10 * 1000);
    }

    @Override
    public void close() {
        this.listenloop.cancel();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.frontend.twitter.input;

import com.narvis.common.debug.NarvisLogger;
import com.narvis.common.tools.executer.Executer;
import com.narvis.engine.NarvisEngine;
import com.narvis.frontend.MessageInOut;
import com.narvis.frontend.interfaces.IInput;
import com.narvis.frontend.twitter.AccessTwitter;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 *
 * @author Alban
 */
public class Input implements IInput {

    private final Timer listenloop;
    public String nameAPI = "Twitter";
    public String internalName = "nakJarvis";
    private final Twitter twitterLink;
    private List<MessageInOut> messageList;
    private long lastMessageId; // Meh
    private long lastMessageIdMinusOne; // Meh

    public Input(Twitter twit, String lastId) {
        this.twitterLink = twit;
        this.listenloop = new Timer("Twitter listen");
        this.lastMessageId = Long.getLong(lastId);
    }

    public void getMessages() throws TwitterException {
        List<Status> statuses = this.twitterLink.getMentionsTimeline();
        this.lastMessageIdMinusOne = this.lastMessageId; // meh
        this.lastMessageId = statuses.get(0).getId(); // meh
        List<MessageInOut> messageList = new ArrayList<>();
        MessageInOut temp;
        String[] tempParser;
        for (Status status : statuses) {
            if (status != null) {
                tempParser = this.tweetParser(status);
                temp = new MessageInOut(this.nameAPI, tempParser[0], tempParser[1]);
                messageList.add(temp);
            }
        }
        this.messageList = messageList;
    }

    public List<MessageInOut> getInputs() {
        return messageList;
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

    public MessageInOut getInput() {
        try {
            this.getMessages();
            if (this.lastMessageId != this.lastMessageIdMinusOne) // Meh
            {
                return messageList.get(0);
            } else {
                return null;
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
                MessageInOut lastMessage = getInput();
                if (lastMessage != null) {
                    try {
                        NarvisEngine.getInstance().getMessage(lastMessage);
                    } catch (Exception ex) {
                        NarvisLogger.logException(ex);
                    }
                }
            }
        }, 60 * 1000, 60 * 1000);
    }

    @Override
    public void close() {
        this.listenloop.cancel();
    }
}

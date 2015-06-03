/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.frontend.twitter.input;

import com.narvis.engine.Narvis;
import com.narvis.frontend.MessageInOut;
import com.narvis.frontend.interfaces.IInput;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.minesales.infres6.narvisAPITwiterConsole.communications.twitter.AccessTwitter;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 *
 * @author Alban
 */
public class TwitterInput implements IInput, Runnable{
    public String nameAPI = "Twitter";
    public String internalName = "nakJarvis";
    private Twitter twitterLink;
    private List<MessageInOut> messageList;
    private long lastMessageId = 0; // Meh
    private long lastMessageIdMinusOne; // Meh
    
    public TwitterInput(){
        this.twitterLink = AccessTwitter.loadAccessTwitter();
    }
    
    public void getMessages() throws TwitterException{
        List<Status> statuses = this.twitterLink.getMentionsTimeline();
        this.lastMessageIdMinusOne = this.lastMessageId; // meh
        this.lastMessageId = statuses.get(0).getId(); // meh
        List<MessageInOut> messageList = new ArrayList<>();
        MessageInOut temp;
        String[] tempParser;
        for (Status status : statuses) {
            if(status != null){
                tempParser = this.tweetParser(status);
                temp = new MessageInOut(this.nameAPI,tempParser[0],tempParser[1]);
                messageList.add(temp);
            }
        }
        this.messageList = messageList;
    }

    public List<MessageInOut> getInputs() {
        return messageList;
    }
    
    private String getOtherResponseName(String tweet){
        String recepiantsResponse = "";
        String[] parts = tweet.split(" ");
        for(String s : parts){
            if(s.charAt(0) == '@'){ // is a name
                if(!s.split("@")[1].equals(this.internalName)){ // is not NARVIS
                    recepiantsResponse += ";" + s.split("@")[1];
                }
            }
        }
        return recepiantsResponse;
    }
    
    private String getCleanTweet(String tweet){
        String cleanTweet = "";
        String[] parts = tweet.split(" ");
        for(String s : parts){
            if(!(s.charAt(0) == '@')){
                if(s.charAt(0) == '#'){
                    cleanTweet += " " + s.replace("#","");
                } else {
                    cleanTweet += " " + s;
                }
            }            
        }
        return cleanTweet;
    }
    
    private String[] tweetParser(Status status){
        String[] returnValue = new String[2];
        returnValue[0] = getCleanTweet(status.getText());
        returnValue[1] = status.getUser().getScreenName() + getOtherResponseName(status.getText());
        return returnValue;
    }

    public MessageInOut getInput() {
        try {
            this.getMessages();
            if(this.lastMessageId != this.lastMessageIdMinusOne) // Meh
                return messageList.get(0);
            else
                return null;
        } catch (TwitterException ex) {
            Logger.getLogger(TwitterInput.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void run() {
        MessageInOut lastMessage = null;
        while(!Thread.currentThread().isInterrupted()){
            lastMessage = this.getInput();
            if(lastMessage != null){
                Narvis.getMessage(lastMessage);
            }
            try {
                sleep(60000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TwitterInput.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void start() {
        this.run();
    }
}

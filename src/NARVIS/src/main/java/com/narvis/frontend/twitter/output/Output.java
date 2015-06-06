/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.frontend.twitter.output;

import com.narvis.common.debug.NarvisLogger;
import com.narvis.common.extensions.StringExts;
import com.narvis.frontend.MessageInOut;
import com.narvis.frontend.interfaces.IOutput;
import com.narvis.frontend.twitter.AccessTwitter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 *
 * @author Alban
 */
public class Output implements IOutput {
    private static final int MAX_TWEET_LENGTH = 140;
    public String nameAPI = "Twitter";
    public String internalName = "nakJarvis";
    private final Twitter twitterLink;

    public Output(Twitter twitter) {
        this.twitterLink = twitter;
    }

    @Override
    public void setOuput(MessageInOut m) {
        try {
            for(String s : this.getTweetList(m)){
                Status status = this.twitterLink.updateStatus(s);
            }
        } catch (TwitterException ex) {
            NarvisLogger.logException(ex);
        }
    }
    
    private String getAnswerTo(MessageInOut tweetOut) {
        return "@" + tweetOut.getAnswerTo().split(";")[0] + " "; // The space is here to avoid problems, also adding it here will help it be taken in consideration when calculating output size tweets
    }
    
    private String[] putAtSymbol(String... tweeterPeople) {
        for(int i = 0 ; i < tweeterPeople.length ; i++) {
            tweeterPeople[i] = "@" + tweeterPeople[i];
        }
        return tweeterPeople;
    }
    
    private String getCC(MessageInOut tweetOut) {
        String[] answerTo = tweetOut.getAnswerTo().split(";");
        if(answerTo.length > 1) {
            return " cc " + String.join(" ", putAtSymbol(StringExts.skipFirst(tweetOut.getAnswerTo().split(";"), 1)));
        }
        return "";        
    }
   
    public List<String> getTweetList(MessageInOut tweetOut){
        String answerTo = this.getAnswerTo(tweetOut);
        String ccTo = this.getCC(tweetOut);
        // This is totally arbitrary and is intended to avoid MASS CC SPAM
        if(answerTo.length() + ccTo.length() >= (MAX_TWEET_LENGTH / 2)) {
            ccTo = "";
        }
        String appendMessage = " [...]";
        List<String> retVal = StringExts.split(tweetOut.getContent(),  MAX_TWEET_LENGTH - (answerTo.length() + ccTo.length() + appendMessage.length()), appendMessage);
        for(int i = 0 ; i < retVal.size() ; i++) {
            retVal.set(i, answerTo + retVal.get(i) + ccTo) ;
        }
        return retVal;
    }
}

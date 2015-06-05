/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.frontend.twitter.output;

import com.narvis.frontend.MessageInOut;
import com.narvis.frontend.interfaces.IOutput;
import com.narvis.frontend.twitter.AccessTwitter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 *
 * @author Alban
 */
public class Output implements IOutput {

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
            Logger.getLogger(Output.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String returnNameAndCCToString(MessageInOut tweetOut){
        String retVal = "";
        String answerTo = tweetOut.getAnswerTo().split(";")[0];
        retVal += " @" + answerTo;
        if(tweetOut.getAnswerTo().split(";").length > 1){
            retVal += " cc ";
        }
        for(String s : tweetOut.getAnswerTo().split(";")){
            if(!s.equals(tweetOut.getAnswerTo().split(";")[0] )){
                if((retVal + s).length() <= 140)
                    retVal += " @" + s;
                else
                    break;
            }
        }
        return retVal;
    }
    
    public List<String> getWordList(MessageInOut tweetOut){
        List<String> retVal = Arrays.asList(tweetOut.getContent().split(" "));
        return retVal;
    }
    
    public List<String> getTweetList(MessageInOut tweetOut){
        List<String> wordList = this.getWordList(tweetOut);
        List<String> tweetList = new ArrayList<>();
        String tweet = "";
        boolean putNameOnIt = false;
        int i = 0;
        while(i < wordList.size() || !tweet.equals("")){
            if((tweet + this.returnNameAndCCToString(tweetOut)).length() < 140 && !putNameOnIt){
                if((tweet + this.returnNameAndCCToString(tweetOut) + wordList.get(i) + 1).length() < 140){
                    if(tweet.equals(""))
                        tweet += wordList.get(i);
                    else
                        tweet += " " + wordList.get(i);
                } else {
                    tweet += this.returnNameAndCCToString(tweetOut);
                    putNameOnIt = true;
                }
            } else {
                tweetList.add(tweet);
                tweet = "";
                putNameOnIt = false;
            }
            i++;
        }
        return tweetList;
    }
}

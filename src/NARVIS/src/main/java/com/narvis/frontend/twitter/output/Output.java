/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.frontend.twitter.output;

import com.narvis.frontend.MessageInOut;
import com.narvis.frontend.interfaces.IOutput;
import com.narvis.frontend.twitter.AccessTwitter;
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
            Status status = this.twitterLink.updateStatus(constructTweetResponse(m));
        } catch (TwitterException ex) {
            Logger.getLogger(Output.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String constructTweetResponse(MessageInOut m) {
        String response = "";
        String answerTo = m.getAnswerTo().split(";")[0];
        response += "@" + answerTo + " " + m.getContent();
        if (m.getAnswerTo().split(";").length > 1) {
            response += " cc ";
        }
        for (String s : m.getAnswerTo().split(";")) {
            if (!s.equals(m.getAnswerTo().split(";")[0])) {
                if (response.length() + s.length() + 2 < 140) {
                    response += " @" + s;
                }
            }
        }
        return response;
    }
}

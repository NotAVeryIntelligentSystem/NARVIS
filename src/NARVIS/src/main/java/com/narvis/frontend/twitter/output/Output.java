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
package com.narvis.frontend.twitter.output;

import com.narvis.common.debug.NarvisLogger;
import com.narvis.common.extensions.StringExts;
import com.narvis.frontend.MessageInOut;
import com.narvis.frontend.interfaces.IOutput;
import com.narvis.frontend.twitter.TwitterMessageInOut;
import java.util.List;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class Output implements IOutput {

    private static final int MAX_TWEET_LENGTH = 140;
    private final Twitter twitterLink;

    public Output(Twitter twitter) {
        this.twitterLink = twitter;
    }

    @Override
    public void setOuput(MessageInOut m) {
        try {
            for(String s : this.getTweetList(m)){
                StatusUpdate status = new StatusUpdate(s);
                status.setInReplyToStatusId(((TwitterMessageInOut) m).getIdResponseTo());
                this.twitterLink.updateStatus(status);
            }
        } catch (TwitterException ex) {
            NarvisLogger.logException(ex);
        }
    }

    private String getAnswerTo(MessageInOut tweetOut) {
        return "@" + tweetOut.getAnswerTo().split(";")[0] + " "; // The space is here to avoid problems, also adding it here will help it be taken in consideration when calculating output size tweets
    }

    private String[] putAtSymbol(String... tweeterPeople) {
        for (int i = 0; i < tweeterPeople.length; i++) {
            tweeterPeople[i] = "@" + tweeterPeople[i];
        }
        return tweeterPeople;
    }

    private String getCC(MessageInOut tweetOut) {
        String[] answerTo = tweetOut.getAnswerTo().split(";");
        if (answerTo.length > 1) {
            return " cc " + String.join(" ", putAtSymbol(StringExts.skipFirst(tweetOut.getAnswerTo().split(";"), 1)));
        }
        return "";
    }

    public List<String> getTweetList(MessageInOut tweetOut) {
        String answerTo = this.getAnswerTo(tweetOut);
        String ccTo = this.getCC(tweetOut);
        // This is totally arbitrary and is intended to avoid MASS CC SPAM
        if (answerTo.length() + ccTo.length() >= (MAX_TWEET_LENGTH / 2)) {
            ccTo = "";
        }
        String appendMessage = " [...]";
        List<String> retVal = StringExts.split(tweetOut.getContent(), MAX_TWEET_LENGTH - (answerTo.length() + ccTo.length() + appendMessage.length()), appendMessage);
        for (int i = 0; i < retVal.size(); i++) {
            retVal.set(i, answerTo + retVal.get(i) + ccTo);
        }
        return retVal;
    }
}

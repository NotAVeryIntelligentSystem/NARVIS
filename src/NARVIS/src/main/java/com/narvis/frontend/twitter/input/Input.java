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
import com.narvis.dataaccess.exception.IllegalKeywordException;
import com.narvis.engine.NarvisEngine;
import com.narvis.frontend.interfaces.*;
import com.narvis.frontend.twitter.*;
import java.util.ArrayList;
import java.util.List;
import twitter4j.*;

/**
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class Input implements IInput, UserStreamListener {
    private final AccessTwitter accessTwitter;
    private final TwitterStream stream;

    public Input(AccessTwitter accessTwitter, TwitterStream stream) {
        this.accessTwitter = accessTwitter;
        this.stream = stream;
    }

    private String getOtherResponseName(String tweet) throws IllegalKeywordException {
        List<String> retVal = new ArrayList();
        for (String s : tweet.split("\\s+")) {
            if(s.matches("@.*") && !s.matches("@" + this.accessTwitter.getConf().getData("Conf", "Username"))) {
                retVal.add(s);
            }
        }
        return String.join(";", retVal);
    }

    /**
     * Remove all specific Twitter vocabulary from the tweet to transform it in
     * a simple sentence
     *
     * @param tweet : The tweet to transform
     * @return A sentence withour Twitter specificity
     */
    private String getCleanTweet(String tweet) {
        StringBuilder retVal = new StringBuilder();
        for (String s :  tweet.split("\\s+")) {
            if(s.matches("@.*")) {
                retVal.append(" someone");
            }
            else if(s.matches("#.*")) {
                retVal.append(s.replace("#", ""));
            } 
            else {
                retVal.append(" ").append(s);
            }
        }
        return retVal.toString();
    }
    
    private boolean isMentionnedIn(UserMentionEntity[] mentions) throws TwitterException {
        if(mentions != null) {
            for(UserMentionEntity mention : mentions) {
                if(mention.getId() == this.stream.getId()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean shouldAnswerTweet(Status status) throws TwitterException {
        // Checking for favorite is useless here
        return (status.getUser().getId() != this.stream.getId()) && !status.isRetweet() && isMentionnedIn(status.getUserMentionEntities());
    }

    private TwitterMessageInOut createMessageFromTweet(Status status) throws IllegalKeywordException {
        return new TwitterMessageInOut(this.accessTwitter.getConf().getName(), 
                getCleanTweet(status.getText()), status.getUser().getScreenName() + getOtherResponseName(status.getText()),
                this.accessTwitter, status.getId());
    }

    @Override
    public void start() {
        this.stream.addListener(this);
        this.stream.user(); // I don't really know what I'm doing by doing this
    }

    @Override
    public void close() {
        this.stream.removeListener(this);
    }

    @Override
    public IFrontEnd getFrontEnd() {
        return accessTwitter;
    }

    @Override
    public void onStatus(Status status) {
        try {
            if(shouldAnswerTweet(status)) {
                TwitterMessageInOut message = this.createMessageFromTweet(status);
                NarvisLogger.logInfo("From : " + message.getAnswerTo());
                NarvisLogger.logInfo("Received message : " + message.getContent());
                NarvisEngine.getInstance().getMessage(message);
            }
            else {
                NarvisLogger.logInfo("Ignoring following status : " + status.getText());
            }
        } catch (Exception ex) {
            NarvisLogger.logException(ex);
        }
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice sdn) {
        NarvisLogger.logInfo("User " + sdn.getUserId() + " deleted tweet : " + sdn.getStatusId());
    }

    @Override
    public void onTrackLimitationNotice(int i) {
        NarvisLogger.logInfo("Track limitation notice, remaining : " + i);
    }

    @Override
    public void onScrubGeo(long l, long l1) {
        NarvisLogger.logInfo("Scrub geo l :" + l + " l1 : " + l1);
    }

    @Override
    public void onStallWarning(StallWarning sw) {
        NarvisLogger.logInfo("Stallwarning received, code : " + sw.getCode() + " message : " + sw.getMessage());
    }

    @Override
    public void onException(Exception excptn) {
        NarvisLogger.logInfo("Received exception.");
        NarvisLogger.logException(excptn);
    }

    @Override
    public void onDeletionNotice(long directMessageId, long userId) {
        NarvisLogger.logInfo("Received deletion notice from user : " + userId + " direct message id : " + directMessageId);
    }

    @Override
    public void onFriendList(long[] friendIds) {
        for(long id : friendIds) {
            NarvisLogger.logInfo("onFriendList : " + id);
        }
    }

    @Override
    public void onFavorite(User source, User target, Status favoritedStatus) {
        NarvisLogger.logInfo("Received fav notice from user : " + source.getId() + " to user : " + target.getId() + " to following message : " + favoritedStatus.getText());
    }

    @Override
    public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
        NarvisLogger.logInfo("Received unfav notice from user : " + source.getId() + " to user : " + target.getId() + " to following message : " + unfavoritedStatus.getText());
    }

    @Override
    public void onFollow(User source, User followedUser) {
        NarvisLogger.logInfo("Received follow notice from user : " + source.getId() + " to followed user : " + followedUser.getId());
    }

    @Override
    public void onUnfollow(User source, User unfollowedUser) {
        NarvisLogger.logInfo("Received unfollow notice from user : " + source.getId() + " to followed user : " + unfollowedUser.getId());
    }

    @Override
    public void onDirectMessage(DirectMessage directMessage) {
        NarvisLogger.logInfo("Received directmessage from user : " + directMessage.getSenderId() + " with content : " + directMessage.getText());
    }

    @Override
    public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
        NarvisLogger.logInfo("onUserListMemberAddition");
    }

    @Override
    public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
        NarvisLogger.logInfo("onUserListMemberDeletion");
    }

    @Override
    public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
        NarvisLogger.logInfo("onUserListSubscription");
    }

    @Override
    public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
        NarvisLogger.logInfo("onUserListSubscription");
    }

    @Override
    public void onUserListCreation(User listOwner, UserList list) {
        NarvisLogger.logInfo("onUserListCreation");
    }

    @Override
    public void onUserListUpdate(User listOwner, UserList list) {
        NarvisLogger.logInfo("onUserListUpdate");
    }

    @Override
    public void onUserListDeletion(User listOwner, UserList list) {
        NarvisLogger.logInfo("onUserListDeletion");
    }

    @Override
    public void onUserProfileUpdate(User updatedUser) {
        NarvisLogger.logInfo("onUserProfileUpdate");
    }

    @Override
    public void onUserSuspension(long suspendedUser) {
        NarvisLogger.logInfo("onUserSuspension");
    }

    @Override
    public void onUserDeletion(long deletedUser) {
        NarvisLogger.logInfo("onUserDeletion");
    }

    @Override
    public void onBlock(User source, User blockedUser) {
        NarvisLogger.logInfo("onBlock");
    }

    @Override
    public void onUnblock(User source, User unblockedUser) {
        NarvisLogger.logInfo("onUnblock");
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.minesales.infres6.narvisAPITwiterConsole.communications.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * TODO : REFACTOR AND SECURISE
 * @author Alban
 */
public class AccessTwitter {

    public static Twitter loadAccessTwitter(){
        String token = "2754794936-VRDsXE6ZxnBQeUTbprYHGEOVQcYJc1mvyNaBfyC";
        String tokenSecret = "Sb7nwzWtlmOw6CRcqRoSlXkvonZGXiLTOp9sBAlv4fDOy";
        AccessToken accessToken = new AccessToken(token, tokenSecret);
        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        twitter.setOAuthConsumer("CkQjh66TbG4FYfry1OTrTb4tS", "v8vbUPtFAkIfSUN2KICf8GDEZ91fumVuY5mzMAmT84Ag2bVnvD");
        twitter.setOAuthAccessToken(accessToken);
        return twitter;
    }
}

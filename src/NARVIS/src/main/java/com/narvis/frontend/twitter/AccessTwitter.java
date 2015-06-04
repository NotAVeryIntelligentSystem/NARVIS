/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.frontend.twitter;

import com.narvis.common.debug.NarvisLogger;
import com.narvis.dataaccess.impl.FrontEndConfigurationDataProvider;
import com.narvis.dataaccess.impl.ModuleConfigurationDataProvider;
import com.narvis.frontend.interfaces.*;
import com.narvis.frontend.twitter.input.Input;
import com.narvis.frontend.twitter.output.Output;
import java.util.logging.Level;
import twitter4j.*;
import twitter4j.auth.AccessToken;

/**
 * TODO : REFACTOR AND SECURISE
 * @author Alban
 */
public class AccessTwitter implements IFrontEnd {
    private final FrontEndConfigurationDataProvider conf;
    private IInput input;
    private IOutput output;

    private Twitter twitter;

    public AccessTwitter(FrontEndConfigurationDataProvider conf) {
        this.conf = conf;
    }
    
    private Twitter loadAccessTwitter(String token, String tokenSecret, String consumerKey, String consumerSecret ){
        AccessToken accessToken = new AccessToken(token, tokenSecret);
        TwitterFactory factory = new TwitterFactory();
        Twitter retVal = factory.getInstance();
        retVal.setOAuthConsumer(consumerKey, consumerSecret);
        retVal.setOAuthAccessToken(accessToken);
        return retVal;
    }

    @Override
    public void start() {
        if(this.twitter != null) {
            throw new IllegalArgumentException("Front end has already started !");
        }
        this.twitter = this.loadAccessTwitter(this.conf.getApiKeys().getData("token"), this.conf.getApiKeys().getData("tokenSecret"), this.conf.getApiKeys().getData("consumerKey"), this.conf.getApiKeys().getData("consumerSecret"));
        this.input = new Input(this.twitter);
        this.output = new Output(this.twitter);
        this.input.start();
    }
    
    
    public Twitter getTwitter() {
        return twitter;
    }
    

    @Override
    public void close() {
        try {
            this.input.close();
            this.twitter = null;
            this.input = null;
            this.output = null;
        } catch (Exception ex) {
            NarvisLogger.getInstance().log(Level.SEVERE, ex.toString());
        }
    }

    @Override
    public IInput getInput() {
        return this.input;
    }

    @Override
    public IOutput getOutput() {
        return this.output;
    }
}

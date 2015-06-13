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
package com.narvis.frontend.twitter;

import com.narvis.common.debug.NarvisLogger;
import com.narvis.common.generics.Pair;
import com.narvis.dataaccess.impl.FrontEndConfigurationDataProvider;
import com.narvis.frontend.interfaces.*;
import com.narvis.frontend.twitter.input.Input;
import com.narvis.frontend.twitter.output.Output;
import java.util.logging.Level;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class AccessTwitter implements IFrontEnd {

    private final FrontEndConfigurationDataProvider conf;
    private IInput input;
    private IOutput output;

    private final TwitterStream stream;
    private final Twitter answer;

    public AccessTwitter(FrontEndConfigurationDataProvider conf) {
        this.conf = conf;
        Pair<TwitterStream, Twitter> pair = this.loadAccessTwitter(
                this.conf.getApiKeys().getData("token"), 
                this.conf.getApiKeys().getData("tokenSecret"), 
                this.conf.getApiKeys().getData("consumerKey"), 
                this.conf.getApiKeys().getData("consumerSecret"));
        this.stream = pair.item1;
        this.answer = pair.item2;
        
    }

    private Pair<TwitterStream, Twitter> loadAccessTwitter(String token, String tokenSecret, String consumerKey, String consumerSecret) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(consumerKey);
        cb.setOAuthConsumerSecret(consumerSecret);
        cb.setOAuthAccessToken(token);
        cb.setOAuthAccessTokenSecret(tokenSecret);
        Configuration authConf = cb.build();
        return new Pair(new TwitterStreamFactory(authConf).getInstance(), new TwitterFactory(authConf).getInstance());
    }

    @Override
    public void start() {

        this.input = new Input(this, this.stream);
        this.output = new Output(this.answer);
        try {
            this.answer.updateStatus("I am now online, ask me something !");
        } catch (TwitterException ex) {
            NarvisLogger.logException(ex);
        }
        this.input.start();
    }

    @Override
    public void close() {
        try {
            this.answer.updateStatus("I'm switching offline for a moment. See you soon !");
            this.input.close();
            this.input = null;
            this.output = null;
        } catch (Exception ex) {
            NarvisLogger.logException(ex);
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

    public FrontEndConfigurationDataProvider getConf() {
        return this.conf;
    }
}

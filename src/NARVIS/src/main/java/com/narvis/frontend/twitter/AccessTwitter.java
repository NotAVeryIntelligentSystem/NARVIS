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
import com.narvis.dataaccess.impl.FrontEndConfigurationDataProvider;
import com.narvis.frontend.interfaces.*;
import com.narvis.frontend.twitter.input.Input;
import com.narvis.frontend.twitter.output.Output;
import twitter4j.*;
import twitter4j.auth.AccessToken;

/**
 * TODO : REFACTOR AND SECURISE
 *
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

    private Twitter loadAccessTwitter(String token, String tokenSecret, String consumerKey, String consumerSecret) {
        AccessToken accessToken = new AccessToken(token, tokenSecret);
        TwitterFactory factory = new TwitterFactory();
        Twitter retVal = factory.getInstance();
        retVal.setOAuthConsumer(consumerKey, consumerSecret);
        retVal.setOAuthAccessToken(accessToken);
        return retVal;
    }

    @Override
    public void start() {
        if (this.twitter != null) {
            throw new IllegalArgumentException("Front end has already started !");
        }
        this.twitter = this.loadAccessTwitter(this.conf.getApiKeys().getData("token"), this.conf.getApiKeys().getData("tokenSecret"), this.conf.getApiKeys().getData("consumerKey"), this.conf.getApiKeys().getData("consumerSecret"));
        this.input = new Input(this.twitter, this);
        this.output = new Output(this.twitter, this.conf.getName());
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

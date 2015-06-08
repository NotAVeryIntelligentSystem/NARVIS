/*
 * The MIT License
 *
 * Copyright 2015 puma.
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
package com.narvis.dataaccess.exception;

/**
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class ProviderException extends Exception {

    private final String narvisErrorMessage;

    /**
     * Get the error message
     * @return the message
     */
    public String getNarvisErrorMessage() {
        return narvisErrorMessage;
    }

    /**
     * Constructor
     * @param msg the message
     * @param narvisErrorMessage narvis error message
     */
    public ProviderException(String msg, String narvisErrorMessage) {
        super(msg);
        this.narvisErrorMessage = narvisErrorMessage;
    }

    
    /**
     * Cosntructor
     * @param providerName .class of the provider
     * @param msg the message
     * @param narvisErrorMessage narvis error message
     */
    public ProviderException(Class<?> providerName, String msg, String narvisErrorMessage) {
        super("Provider : " + providerName.getCanonicalName() + " " + msg);
        this.narvisErrorMessage = narvisErrorMessage;

    }
    /**
     * Constructor
     * @param thrwbl the throwable exception
     * @param narvisErrorMessage narvis error message
     */
    public ProviderException(Throwable thrwbl, String narvisErrorMessage) {
        super(thrwbl);
        this.narvisErrorMessage = narvisErrorMessage;

    }

    /**
     * Constructor
     * @param providerName .class of the provider
     * @param string message
     * @param thrwbl throwable exception
     * @param narvisErrorMessage narvis error message
     */
    public ProviderException(Class<?> providerName, String string, Throwable thrwbl, String narvisErrorMessage) {
        super("Provider : " + providerName.getCanonicalName() + " " + string, thrwbl);
        this.narvisErrorMessage = narvisErrorMessage;

    }

}

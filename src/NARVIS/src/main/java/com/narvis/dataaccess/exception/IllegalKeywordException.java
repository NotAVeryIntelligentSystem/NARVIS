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
package com.narvis.dataaccess.exception;

/**
 *
 * @author uwy
 */
public class IllegalKeywordException extends ProviderException {

    /**
     * Constructs an instance of <code>IllegalKeywordException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     * @param narvisErrorMessage
     */
    public IllegalKeywordException(String msg, String narvisErrorMessage) {
        super(msg, narvisErrorMessage);
    }

    public IllegalKeywordException(Class<?> providerName, String[] keywords, String desc, String narvisErrorMessage) {
        super(providerName, desc + " " + String.join(",", keywords), narvisErrorMessage);
    }

    public IllegalKeywordException(Class<?> providerName, String[] keywords, String desc, Throwable thrwbl, String narvisErrorMessage) {
        super(providerName, desc + " " + String.join(",", keywords), thrwbl, narvisErrorMessage);
    }

}

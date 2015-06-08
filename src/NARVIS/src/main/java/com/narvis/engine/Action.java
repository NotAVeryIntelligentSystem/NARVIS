/*
 * The MIT License
 *
 * Copyright 2015 Zack.
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
package com.narvis.engine;

import java.util.LinkedList;
import java.util.List;

/**
 * Represent an action that have to be executerd
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class Action {

    private String providerName;     // The provider name
    private List<String> precisions; // Possible precisions about the expected datas
    private List<String> details;    // List of details words that can be used

    /**
     * Default constructor
     */
    public Action() {
        providerName = "";
        precisions = new LinkedList<>();
        details = new LinkedList<>();
    }

    /**
     * Constructor
     * @param pProviderName The provider name
     * @param pPrecisions Possible precisions about the expected datas
     * @param pDetails  List of details words that can be used
     */
    public Action(String pProviderName, List<String> pPrecisions, List<String> pDetails) {
        providerName = pProviderName;
        precisions = pPrecisions;
        details = pDetails;
    }

    /**
     * Accessor for the provider name
     * @return  the provider name
     */
    public String getProviderName() {
        return providerName;
    }

    /**
     * Accessor for the precisions list
     * @return the precision list
     */
    public List<String> getPrecisions() {
        return precisions;
    }

    /**
     * Accessor for the details list
     * @return the details list
     */
    public List<String> getDetails() {
        return details;
    }
}

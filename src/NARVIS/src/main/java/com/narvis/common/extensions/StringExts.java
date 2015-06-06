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
package com.narvis.common.extensions;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author uwy
 */
public class StringExts {
    
    /**
     * Regroups words into multiple sentences not exceeding given maximum size given in parameter
     * @param sentence The sentence that needs to be splitted
     * @param maxPacketSize The maximum size for a sentence
     * @param appendMessage The message to append at the end of every sentence (NOT INCLUDED IN maxPacketSize !!!)
     * @return A list of sentences/packets not exceeding the maxPacketSize
     */
    public static List<String> split(String sentence, int maxPacketSize, String appendMessage) {
        return split(sentence.split(" "), maxPacketSize, appendMessage);
    }
    
    /**
     * Regroups words into multiple sentences not exceeding given maximum size
     * @param words The words aka a sentence splitted without space in words
     * @param maxPacketSize The maximum size for a sentence
     * @param appendMessage The message to append at the end of every sentence (NOT INCLUDED IN maxPacketSize !!!)
     * @return A list of sentences/packets not exceeding the maxPacketSize
     */
    public static List<String> split(String[] words, int maxPacketSize, String appendMessage) {
        if(maxPacketSize <= 0) {
            throw new IllegalArgumentException("I can't split with negative or 0 sized packets !");
        }
        List<String> retVal = new ArrayList<>(); // This will be the list of packets returned
        int currentPacketSize = 0;
        List<String> current = new ArrayList<>();
        for(String word : words) {
            // Here current.size() is used to calculates spaces which aren't included in words
            if((currentPacketSize + word.length() + current.size()) <= maxPacketSize) {
                // We don't care about space since they are taken in count in the condition right up there
                currentPacketSize += word.length(); 
                current.add(word);
            }
            else {
                // We renew the packet if the previous one isn't fitting.
                retVal.add(String.join(" ", current) + appendMessage); 
                currentPacketSize = 0;
                current = new ArrayList<>();
                if(word.length() > maxPacketSize) {
                    throw new IllegalArgumentException("Can't split this because a word is too large for the max packet size, word : [" + word + "] max size : " +  maxPacketSize);
                }
            }
        }
        return retVal;
    }
    
    public static String[] skipFirst(String[] items, int count) {
        String[] retVal = new String[items.length - count];
        for (int i = count; i < items.length; i++) {
            retVal[i - count] = items[i];
        }
        return retVal;
    }
}

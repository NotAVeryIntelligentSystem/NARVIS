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
package com.narvis.dataaccess.interfaces;

import java.util.List;
import java.util.Map;

/**
 *
 * @author puma
 */
public interface IAnswserBuilder {
    
    /**
     * Read the XML answer file to retrieve the answer attached to the given command
     * @param provider The name of the calling provider
     * @param command the command 
     * @return The Answer from the XML file 
     */
    String readAnswerForCommand(String provider, String command);
    
    /**
     * Retrieve all the params needed to fulfill the answer
     * @param answerFromXml The answer returned by readAnswerForCommand method
     * @return A list containing all the params
     */
    List<String> getListOfRequiredParams(String answerFromXml);
    
    /**
     * Finally build the answer by replacing all the occurence of a param with its value from the map
     * @param paramsToValue the map which link each param with its value
     * @param answerFromXml the answer from the XML file
     * @return the final answer
     */
    String buildAnswer(Map<String, String> paramsToValue, String answerFromXml);
    
}

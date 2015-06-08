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
package com.narvis.frontend;

import com.narvis.frontend.interfaces.IFrontEnd;

/**
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class MessageInOut {

    private String inputAPIClass; // "ModuleAPIName"
    private String content; // "Message"
    private String answerTo; // "recepiant1;recepiant2" ex : "nakou;uwybbq"

    private final IFrontEnd frontEnd;

    public MessageInOut(String inputAPIClass, String content, String answerTo, IFrontEnd frontEnd) {
        this.content = content;
        this.inputAPIClass = inputAPIClass;
        this.answerTo = answerTo;
        this.frontEnd = frontEnd;
    }

    public String getInputAPI() {
        return inputAPIClass;
    }

    public void setInputAPI(String inputAPI) {
        this.inputAPIClass = inputAPI;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswerTo() {
        return answerTo;
    }

    public void setAnswerTo(String answerTo) {
        this.answerTo = answerTo;
    }

    public IFrontEnd getFrontEnd() {
        return this.frontEnd;
    }

    public void sendToOutput(String message) {
        this.content = message;
        this.frontEnd.getOutput().setOuput(this);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.frontend.twitter;

import com.narvis.frontend.MessageInOut;
import com.narvis.frontend.interfaces.IFrontEnd;

/**
 *
 * @author Nakou
 */
public class TwitterMessageInOut extends MessageInOut {

    private long idResponseTo;

    public TwitterMessageInOut(String inputAPIClass, String content, String answerTo, IFrontEnd frontEnd) {
        super(inputAPIClass, content, answerTo, frontEnd);
    }

    public TwitterMessageInOut(String inputAPIClass, String content, String answerTo, IFrontEnd frontEnd, long idResponseTo) {
        super(inputAPIClass, content, answerTo, frontEnd);
        this.idResponseTo = idResponseTo;
    }

    public long getIdResponseTo() {
        return idResponseTo;
    }
}

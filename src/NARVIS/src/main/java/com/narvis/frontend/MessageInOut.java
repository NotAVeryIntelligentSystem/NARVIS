/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.frontend;

import com.narvis.frontend.interfaces.IFrontEnd;

/**
 *
 * @author Alban
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
    
    public IFrontEnd getFrontEnd(){
        return this.frontEnd;
    }

    public void sendToOutput(String message){
        this.content = message;
        this.frontEnd.getOutput().setOuput(this);
    }
}

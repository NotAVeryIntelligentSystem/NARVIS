/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.frontend;

/**
 *
 * @author Alban
 */
public class MessageInOut {

    private String inputAPIClass; // "ModuleAPIName"
    private String content; // "Message"
    private String answerTo; // "recepiant1;recepiant2" ex : "nakou;uwybbq"

    public MessageInOut(String inputAPIClass, String content, String answerTo) {
        this.content = content;
        this.inputAPIClass = inputAPIClass;
        this.answerTo = answerTo;
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

}

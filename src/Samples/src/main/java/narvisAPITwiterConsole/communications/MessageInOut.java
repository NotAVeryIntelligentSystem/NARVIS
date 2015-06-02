/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.minesales.infres6.narvisAPITwiterConsole.communications;

/**
 *
 * @author Alban
 */
public class MessageInOut {
    private String inputAPI; // "ModuleAPIName"
    private String content; // "Message"
    private String answerTo; // "recepiant1;recepiant2" ex : "nakou;uwybbq"
    
    public MessageInOut(String inputAPI, String content, String answerTo){
        this.content = content;
        this.inputAPI = inputAPI;
        this.answerTo = answerTo;
    }

    public String getInputAPI() {
        return inputAPI;
    }

    public void setInputAPI(String inputAPI) {
        this.inputAPI = inputAPI;
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

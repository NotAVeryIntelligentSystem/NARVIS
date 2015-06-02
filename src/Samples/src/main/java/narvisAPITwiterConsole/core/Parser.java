/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.minesales.infres6.narvisAPITwiterConsole.core;

import org.minesales.infres6.narvisAPITwiterConsole.communications.MessageInOut;

/**
 *
 * @author Alban
 */
public class Parser {
    
    public Parser(MessageInOut message){
        System.out.println("Yay! I've got mail!" + message.getContent());
    }
    
}

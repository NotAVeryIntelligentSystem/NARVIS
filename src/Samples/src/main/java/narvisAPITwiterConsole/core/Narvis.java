/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.minesales.infres6.narvisAPITwiterConsole.core;

import org.minesales.infres6.narvisAPITwiterConsole.communications.InputListner;
import org.minesales.infres6.narvisAPITwiterConsole.communications.MessageInOut;
import org.minesales.infres6.narvisAPITwiterConsole.communications.console.input.ConsoleInput;
import org.minesales.infres6.narvisAPITwiterConsole.communications.console.output.ConsoleOutput;
import org.minesales.infres6.narvisAPITwiterConsole.communications.twitter.input.TwitterInput;
import org.minesales.infres6.narvisAPITwiterConsole.communications.twitter.output.TwitterOutput;

/**
 *
 * @author Alban
 */
public class Narvis {
    
    private InputListner inputs;
    
    public Narvis(){
        this.loadConfiguration();
        this.startInitialisation();
        this.startCoreSystems();
        this.startListening();
    }

    private void loadConfiguration() {
    }

    private void startInitialisation() {
    }

    private void startCoreSystems() {
    }

    private void startListening() {
        /*TwitterInput input = new TwitterInput();
        TwitterOutput output = new TwitterOutput();*/
        ConsoleInput input = new ConsoleInput();
        ConsoleOutput output = new ConsoleOutput();
        inputs = new InputListner(input,output);
    }
    
    public static void getMessage(MessageInOut message){
        Parser p = new Parser(message);
    }
    
}

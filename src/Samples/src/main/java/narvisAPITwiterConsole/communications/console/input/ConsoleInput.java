/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.minesales.infres6.narvisAPITwiterConsole.communications.console.input;

import java.util.List;
import java.util.Scanner;
import org.minesales.infres6.narvisAPITwiterConsole.communications.Iinput;
import org.minesales.infres6.narvisAPITwiterConsole.communications.MessageInOut;
import org.minesales.infres6.narvisAPITwiterConsole.core.Narvis;

/**
 *
 * @author Nakou
 */
public class ConsoleInput implements Iinput {

    private MessageInOut getMessage(String s){
        return new MessageInOut("Console",s,"localhost");
    }
    
    @Override
    public void start() {
        Scanner sc = new Scanner(System.in);
        System.out.println("NARVIS/READY/>");
        String s = sc.nextLine();
        Narvis.getMessage(this.getMessage(s));
    }
    
}

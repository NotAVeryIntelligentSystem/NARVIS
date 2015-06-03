/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.frontend.console.input;

import com.narvis.engine.Narvis;
import com.narvis.frontend.MessageInOut;
import com.narvis.frontend.interfaces.Iinput;
import java.util.List;
import java.util.Scanner;

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

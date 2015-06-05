/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.frontend.console.input;

import com.narvis.engine.NarvisEngine;
import com.narvis.frontend.MessageInOut;
import com.narvis.frontend.interfaces.IInput;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nakou
 */
public class Input implements IInput {

    private MessageInOut getMessage(String s) {
        return new MessageInOut("Console", s, "localhost");
    }

    @Override
    public void start() {
        Scanner sc = new Scanner(System.in);
        System.out.println("NARVIS/READY/>");
        String s = sc.nextLine();
        try {
            NarvisEngine.getInstance().getMessage(this.getMessage(s));
        } catch (Exception ex) {
            Logger.getLogger(Input.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void close() throws Exception {

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.frontend.console.input;

import com.narvis.common.debug.NarvisLogger;
import com.narvis.engine.NarvisEngine;
import com.narvis.frontend.MessageInOut;
import com.narvis.frontend.interfaces.IInput;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

/**
 *
 * @author Nakou
 */
public class Input implements IInput {

    private final String moduleName;
    private Timer listenloop;
    public Input(String moduleName) {
        this.moduleName = moduleName;
        this.listenloop = new Timer("Console front end");
    }
    
    private MessageInOut getMessage(String s) {
        return new MessageInOut(this.moduleName, s, "localhost");
    }

    @Override
    public void start() {
        this.listenloop.schedule(new TimerTask() {
            @Override
            public void run() {
                Scanner sc = new Scanner(System.in);
                System.out.print("NARVIS/READY/>");
                String s = sc.nextLine();

                try {
                    NarvisEngine.getInstance().getMessage(getMessage(s));
                } catch (Exception ex) {
                    NarvisLogger.getInstance().getLogger().log(Level.SEVERE, ex.getMessage());
                }
            }
        }, 0, 1000);

    }

    @Override
    public void close() throws Exception {

    }

}

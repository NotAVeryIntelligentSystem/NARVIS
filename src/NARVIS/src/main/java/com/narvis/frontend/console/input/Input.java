/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.frontend.console.input;

import com.narvis.common.debug.NarvisLogger;
import com.narvis.engine.NarvisEngine;
import com.narvis.frontend.MessageInOut;
import com.narvis.frontend.console.AccessConsole;
import com.narvis.frontend.interfaces.IFrontEnd;
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
    private final static int REFRESH_PERIOD_SECOND = 1;

    private final AccessConsole accessConsole;
    
    private final String moduleName;
    private final Timer listenloop;
    
    public Input(String moduleName, AccessConsole accessConsole) {
        this.moduleName = moduleName;
        this.accessConsole = accessConsole;
        this.listenloop = new Timer("Console front end");
    }
    
    private MessageInOut getMessage(String s) {
        return new MessageInOut(this.moduleName, s, "localhost", accessConsole);
    }

    @Override
    public void start() {
        this.listenloop.schedule(new TimerTask() {
            @Override
            public void run() {
                Scanner sc = new Scanner(System.in);
                String s = sc.nextLine();

                try {
                    NarvisEngine.getInstance().getMessage(getMessage(s));
                } catch (Exception ex) {
                    NarvisLogger.getInstance().getLogger().log(Level.SEVERE, ex.getMessage());
                }
            }
        }, 0, REFRESH_PERIOD_SECOND * 1000);

    }

    @Override
    public void close() throws Exception {

    }
    
    @Override
    public IFrontEnd getFrontEnd(){
        return accessConsole;
    }
}

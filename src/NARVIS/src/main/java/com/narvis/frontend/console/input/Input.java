/*
 * The MIT License
 *
 * Copyright 2015 uwy.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class Input implements IInput {

    private final static int REFRESH_PERIOD_SECOND = 1;

    private final AccessConsole accessConsole;

    private final String moduleName;
    private final Thread listenloop;

    public Input(String moduleName, AccessConsole accessConsole) {
        this.moduleName = moduleName;
        this.accessConsole = accessConsole;
        this.listenloop = new Thread("Console front end") {
            @Override
            public void run() {
                while (true) {
                    Scanner sc = new Scanner(System.in);
                    String s = sc.nextLine();

                    try {
                        NarvisEngine.getInstance().getMessage(getMessage(s));
                    } catch (Exception ex) {
                        NarvisLogger.getInstance().getLogger().log(Level.SEVERE, ex.getMessage());
                    }
                }
            }
        };
    }

    private MessageInOut getMessage(String s) {
        return new MessageInOut(this.moduleName, s, "localhost", accessConsole);
    }

    @Override
    public void start() {
        this.listenloop.start();
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public IFrontEnd getFrontEnd() {
        return accessConsole;
    }
}

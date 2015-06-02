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
package com.narvis.common.tools.executer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Execute 
 * @author uwy
 */
public class Executer implements AutoCloseable {
    private final BlockingQueue<Runnable> toExecute;
    private final Thread executionLoop;
    
    @SuppressWarnings("CallToThreadStartDuringObjectConstruction") // this is intended
    public Executer(String name) {
        this.toExecute = new LinkedBlockingQueue<>();
        this.executionLoop = new Thread(name){
            @Override
            public void run() {
                while(true) {
                    try {
                        // Automatically interrupted when this thread is interrupted so we shouldn't worry much
                        toExecute.take().run();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Executer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        this.executionLoop.start();
    }
    
    public <T> T addToExecute(RunnableFuture<T> executable) throws ExecuterException {
        try {
            this.toExecute.put(executable);
            return executable.get();
        } catch (InterruptedException | ExecutionException ex) {
            throw new ExecuterException(ex);
        }
    }

    @Override
    public void close() throws Exception {
        this.executionLoop.interrupt();
    }
    
}
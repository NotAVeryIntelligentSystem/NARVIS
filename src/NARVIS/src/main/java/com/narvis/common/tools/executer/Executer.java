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

import com.narvis.common.debug.NarvisLogger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Execute
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class Executer implements AutoCloseable {

    private final BlockingQueue<Runnable> toExecute;
    private final Thread executionLoop;

    /**
     * Constructor
     * @param name set the name of the thread
     */
    public Executer(String name) {
        this.toExecute = new LinkedBlockingQueue<>();
        this.executionLoop = new Thread(name) {
            @Override
            public void run() {
                while (!this.isInterrupted()) {
                    try {
                        // Automatically interrupted when this thread is interrupted so we shouldn't worry much
                        toExecute.take().run();
                    } catch (InterruptedException ex) {
                        NarvisLogger.logException(ex);
                    }
                }
            }
        };
    }

    /**
     * Start the thread
     */
    public void start() {
        this.executionLoop.start();

    }

    /**
     * add an executable with a specific type of object to the pile of the executor
     * @param <T> type of object to thread
     * @param executable runnable executable
     * @return the object
     * @throws ExecuterException 
     */
    public <T> T addToExecute(RunnableFuture<T> executable) throws ExecuterException {
        try {
            this.toExecute.put(executable);
            return executable.get();
        } catch (InterruptedException | ExecutionException ex) {
            throw new ExecuterException(ex);
        }
    }

    /**
     * add an executable to the pile of the executor
     * @param executable runnable executable
     * @throws ExecuterException 
     */
    public void addToExecute(Runnable executable) throws ExecuterException {
        try {
            this.toExecute.put(executable);
        } catch (InterruptedException ex) {
            throw new ExecuterException(ex);
        }
    }

    /**
     * Interupt the execution
     * @throws Exception 
     */
    @Override
    public void close() throws Exception {
        this.executionLoop.interrupt();
    }

}

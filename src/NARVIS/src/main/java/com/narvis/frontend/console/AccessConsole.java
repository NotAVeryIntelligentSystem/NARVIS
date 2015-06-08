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
package com.narvis.frontend.console;

import com.narvis.dataaccess.impl.FrontEndConfigurationDataProvider;
import com.narvis.frontend.console.input.Input;
import com.narvis.frontend.console.output.Output;
import com.narvis.frontend.interfaces.IFrontEnd;
import com.narvis.frontend.interfaces.IInput;
import com.narvis.frontend.interfaces.IOutput;

/**
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class AccessConsole implements IFrontEnd {

    private final FrontEndConfigurationDataProvider conf;
    private IInput input;
    private IOutput output;

    public AccessConsole(FrontEndConfigurationDataProvider conf) {
        this.conf = conf;
    }

    @Override
    public void start() {
        this.input = new Input(this.conf.getName(), this);
        this.output = new Output(this);

        this.printReady();
        this.input.start();
    }

    @Override
    public void close() {
    }

    @Override
    public IInput getInput() {
        return this.input;
    }

    @Override
    public IOutput getOutput() {
        return this.output;
    }

    public void printReady() {
        System.out.print("NARVIS/READY/>");
    }
}

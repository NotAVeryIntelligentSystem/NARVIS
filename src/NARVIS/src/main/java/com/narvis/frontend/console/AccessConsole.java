/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author Nakou
 */
public class AccessConsole implements IFrontEnd{

    private final FrontEndConfigurationDataProvider conf; 
    private IInput input;
    private IOutput output;
    
    public AccessConsole(FrontEndConfigurationDataProvider conf) {
        this.conf = conf;
    }
    
    @Override
    public void start() {
        this.input = new Input();
        this.output = new Output();
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
    
}

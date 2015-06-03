/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.frontend;

import com.narvis.frontend.interfaces.IOutput;
import com.narvis.frontend.interfaces.IInput;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alban
 */
public class InputListner{
    private IInput input;
    private IOutput output;
    private boolean running;
    
    public InputListner(IInput i, IOutput o){ // #I/O #SWAG #GOOGLE
        this.input = i;
        this.output = o;
        this.input.start();
    }
}

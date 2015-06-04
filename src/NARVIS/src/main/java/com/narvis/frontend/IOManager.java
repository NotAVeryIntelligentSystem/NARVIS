/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.frontend;

import com.narvis.common.tools.reflection.Factory;
import com.narvis.common.tools.reflection.FactoryException;
import com.narvis.frontend.interfaces.IOutput;
import com.narvis.frontend.interfaces.IInput;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alban
 */
public class IOManager{
    private IInput input;
    private IOutput output;
    private boolean running;
    
    public IOManager(String IOname){ 
        try {
            this.input = Factory.fromName("com.narvis.frontend." + IOname + ".input.Input");
            this.output = Factory.fromName("com.narvis.frontend." + IOname + ".output.Output");
            this.input.start();
        } catch (FactoryException ex) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

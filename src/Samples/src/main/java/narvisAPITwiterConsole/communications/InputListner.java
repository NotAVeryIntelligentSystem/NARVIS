/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.minesales.infres6.narvisAPITwiterConsole.communications;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.minesales.infres6.narvisAPITwiterConsole.core.Narvis;

/**
 *
 * @author Alban
 */
public class InputListner{
    private Iinput input;
    private IOutput output;
    private boolean running;
    
    public InputListner(Iinput i, IOutput o){ // #I/O #SWAG #GOOGLE
        this.input = i;
        this.output = o;
        this.input.start();
    }
}

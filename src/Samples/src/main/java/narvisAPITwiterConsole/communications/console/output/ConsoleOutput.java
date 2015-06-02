/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.minesales.infres6.narvisAPITwiterConsole.communications.console.output;

import org.minesales.infres6.narvisAPITwiterConsole.communications.IOutput;
import org.minesales.infres6.narvisAPITwiterConsole.communications.MessageInOut;

/**
 *
 * @author Nakou
 */
public class ConsoleOutput implements IOutput{

    @Override
    public void setOuput(MessageInOut m) {
        System.out.println(m.getContent());
    }
    
}

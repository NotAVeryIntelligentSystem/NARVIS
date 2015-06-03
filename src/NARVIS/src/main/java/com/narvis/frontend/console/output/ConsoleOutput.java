/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.frontend.console.output;

import com.narvis.frontend.MessageInOut;
import com.narvis.frontend.interfaces.IOutput;

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

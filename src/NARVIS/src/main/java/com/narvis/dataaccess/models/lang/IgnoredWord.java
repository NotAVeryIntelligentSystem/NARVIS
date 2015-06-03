/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.models.lang;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author Zack
 */
@Root(name="IgnoredWord")
public class IgnoredWord{
    @Element(name="Value", type = String.class)
    public String value;
    
    public IgnoredWord()
    {
        value = "";
    }
    
    public IgnoredWord(@Element(name="Value") String value)
    {
        this.value = value;
    }
    
    public String getValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

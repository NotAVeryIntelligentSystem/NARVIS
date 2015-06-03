/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.models.lang.word;

import java.util.LinkedList;
import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 *
 * @author Zack
 */
@Root(name="Word")
public class Word {
    @Attribute(name="IsIgnored", required = false, empty = "false")
    boolean isIgnored;    
    
    @Element(name="Value", type = String.class)
    String value;
    
    @ElementList (name="InformationTypes", type = String.class)
    List<String> informationTypes;
    
    public Word()
    {
        informationTypes = new LinkedList<>();
    }
    
    public boolean isIgnored()
    {
        return this.isIgnored;
    }
    
    public String getValue()
    {
        return this.value;
    }
    
    public List<String> getInformationTypes()
    {
        return this.informationTypes;
    }
    
    public boolean containInformationType(String informationType)
    {
        informationType.toLowerCase();
        return informationTypes.contains(informationType);
    }
}

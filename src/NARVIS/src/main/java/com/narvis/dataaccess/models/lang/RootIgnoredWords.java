/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.models.lang;

import java.util.LinkedList;
import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 *
 * @author Zack
 */
@Root(name="RootIgnoredWords")
public class RootIgnoredWords {
    
    @ElementList(name="IgnoredWords", type = IgnoredWord.class)
    public List<IgnoredWord> ignoredWords;
    
    public RootIgnoredWords()
    {
        ignoredWords = new LinkedList<>();
    }
    
    public List<IgnoredWord> getIgnoredWords() {
        return ignoredWords;
    }    
}

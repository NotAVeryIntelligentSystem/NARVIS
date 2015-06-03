/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.models.lang;

import com.narvis.dataaccess.interfaces.models.lang.IIgnoredWord;
import com.narvis.dataaccess.interfaces.models.lang.IRootIgnoredWords;
import java.util.LinkedList;
import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 *
 * @author Zack
 */
@Root(name="RootIgnoredWords")
public class RootIgnoredWords implements IRootIgnoredWords {
    @ElementList(name="IgnoredWords", type = IgnoredWord.class)
    public List<IIgnoredWord> ignoredWords;
    
    public RootIgnoredWords()
    {
        ignoredWords = new LinkedList<>();
    }
    
    public RootIgnoredWords(@ElementList(name="IgnoredWords") List<IIgnoredWord> ignoredWords)
    {
        this.ignoredWords = ignoredWords;
    }
    
    @Override
    public List<IIgnoredWord> getIgnoredWords() {
        return ignoredWords;
    }    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.models.lang.word;

import com.narvis.common.generics.NarvisLogger;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 *
 * @author Zack
 */
@Root(name="Dictionary")
public class Dictionary {
    @ElementList(name="Words")
    List<Word> words;
    
    public Dictionary()
    {
        words = new LinkedList<>();
    }
    
    public List<Word> getWords()
    {
        return this.words;
    }
    
    public Word getWordByValue(String value)
    {
        Word word = null;
        for(Word w : this.words)
        {
            if(w.getValue().equalsIgnoreCase(value))
            {
                word = w;
                if(word != null)
                {
                    NarvisLogger.getInstance().log(Level.WARNING, "Word duplication : {0}", w.getValue());
                }
            }
        }
        
        return word;
    }
    
    public List<Word> getWordsByInformationType(String informationType)
    {
        List<Word> returnWords;
        returnWords = new LinkedList<>();
        informationType.toLowerCase();
        
        for(Word w : this.words)
        {
            if(w.getInformationTypes().contains(informationType))
            {
                returnWords.add(w);
            }
        }
        
        return returnWords;
    }
    
    public List<Word> getIgnoredWords()
    {
        List<Word> returnWord;
        returnWord = new LinkedList<>();
        
        for(Word w : this.words)
        {
            if(w.isIgnored())
            {
                returnWord.add(w);
            }
        }
        
        return returnWord;
    }
}

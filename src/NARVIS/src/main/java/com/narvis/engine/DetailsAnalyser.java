/*
 * The MIT License
 *
 * Copyright 2015 uwy.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.narvis.engine;

import com.narvis.dataaccess.DataAccessFactory;
import com.narvis.dataaccess.interfaces.IDataModelProvider;
import com.narvis.dataaccess.models.lang.word.Dictionary;
import com.narvis.dataaccess.models.lang.word.Word;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Nakou
 */
public final class DetailsAnalyser {
    private final IDataModelProvider<Dictionary> dictionary; 
    Map<String, String> wordsAssociations = new HashMap<>();
    
    public DetailsAnalyser(List<String> words) throws Exception{
        this.dictionary = (IDataModelProvider<Dictionary>) DataAccessFactory.getMetaDataProvider().getDataProvider("Dictionary");
        
        boolean nextIsLocation = false;
        for(String s : words){
            Word currentWord = this.dictionary.getModel().getWordByValue(s);
            
            if(this.nextProbablyLocation(currentWord)){
                nextIsLocation = true;
            } else {
                this.isALocation(currentWord);
            }
            
            if(nextIsLocation){
                wordsAssociations.put(currentWord.getValue(),"location");
                nextIsLocation = false;
            }
            
        }
        for(Entry<String, String> entry : wordsAssociations.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        
    }
    
    public boolean isALocation(Word word){
        boolean isACity = false;
        if(word != null){
            if(word.containInformationType("location"))
            {
                wordsAssociations.put(word.getValue(), "location");
                isACity = true;    
            }
        }
        return isACity;
    }
    
    public boolean nextProbablyLocation(Word word){
        boolean isProbablyLocation = false;
        if(word.containInformationType("locationHint"))
        {
            isProbablyLocation = true;
        }
        return isProbablyLocation;
        
    }
}

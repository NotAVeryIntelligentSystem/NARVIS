/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

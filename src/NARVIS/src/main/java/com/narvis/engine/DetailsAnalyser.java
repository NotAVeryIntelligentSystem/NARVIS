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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nakou
 */
public class DetailsAnalyser {
    private final IDataModelProvider<Dictionary> dictionary; 
    Map<String, String> wordsAssociations = new HashMap<>();
    public DetailsAnalyser(ArrayList<String> words, String providerName) throws Exception{
        this.dictionary = (IDataModelProvider<Dictionary>) DataAccessFactory.getMetaDataProvider().getDataProvider("Dictionary");
        boolean nextIsLocation = false;
        for(String s : words){
            if(this.nextProbablyLocation(s)){
                nextIsLocation = true;
            } else {
                this.isALocation(s);
            }
            if(nextIsLocation){
                wordsAssociations.put(s,"location");
                nextIsLocation = false;
            }
            
        }
        for(Entry<String, String> entry : wordsAssociations.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        
    }
    
    public boolean isALocation(String value){
        boolean isACity = false;
        Word word = this.dictionary.getModel().getWordByValue(value);
        if(word != null){
            if(word.containInformationType("location")){
                wordsAssociations.put(value, "location");
                isACity = true;    
            }
        }
        return isACity;
    }
    
    public boolean nextProbablyLocation(String word){
        boolean isProbablyLocation = false;
        if(word.equals("in"))
            isProbablyLocation = true;
        return isProbablyLocation;
        
    }
}

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
    
    public DetailsAnalyser() throws Exception{
        this.dictionary = (IDataModelProvider<Dictionary>) DataAccessFactory.getMetaDataProvider().getDataProvider("Dictionary");
    }
    
    public Map<String, String> getDetailsTypes(List<String> details){
        List<String> hintList = new ArrayList<>();
        boolean isTypeFinded = false;
        for(String detail : details){
            Word w = this.dictionary.getModel().getWordByValue(detail);
            if(w != null){ // Le mot existe dans le dictionnaire
                for(String hint : hintList){
                    if(w.containInformationType(hint)){
                        this.wordsAssociations.put(w.getValue(), hint);
                        isTypeFinded = true;
                        break;
                    }
                }
                if(!isTypeFinded && hintList.size() > 0){
                    this.wordsAssociations.put(w.getValue(), hintList.get(0));
                } else {
                    this.wordsAssociations.put(w.getValue(), w.getInformationTypes().get(0));
                }
                hintList = w.getHints();
            } else {
                if(hintList.size() > 0){
                    this.wordsAssociations.put(detail, hintList.get(0));
                }
                hintList.clear();
            }
        }
        return this.wordsAssociations;
    }
}

package com.narvis.engine;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.narvis.dataaccess.DataAccessFactory;
import com.narvis.dataaccess.interfaces.IDataModelProvider;
import com.narvis.dataaccess.models.lang.word.Dictionary;
import com.narvis.dataaccess.models.lang.word.DictionaryProvider;
import com.narvis.dataaccess.models.lang.word.Word;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
/**
 * Permet de parser une chaine de caractère en une liste de mots.
 * @author Zack
 */
public class Parser {
    private final static Logger LOGGER = Logger.getLogger(Parser.class.getName());
    
    private final IDataModelProvider<Dictionary> dictionaryProvider;
    
    /**
     * Constructeur par défaut du Parser
     * @throws java.lang.Exception
     */
    public Parser() throws Exception
    {
        // Récupération du RoutesProvider
        this.dictionaryProvider = (IDataModelProvider<Dictionary>) DataAccessFactory.getMetaDataProvider().getDataProvider("Dictionary");
    }
    
    /**
     * Transforme une chaine de caractères en une liste de mots en retirant
     * les mots inutiles
     * @param sentence : Chaine de caractères contenant le message à parser
     * @return Le message parsé en une liste de mots
     */
    public List<String> Parse(String sentence)
    {
        ArrayList<String> parsedMessage = new ArrayList<>();
        parsedMessage.addAll(Arrays.asList(sentence.split(" ")));

        transformSpaceInQuoteWithUnderscore(sentence);        
        
        List<Word> ignoredWords = dictionaryProvider.getModel().getIgnoredWords();
        parsedMessage.removeAll(wordsToStrings(ignoredWords));
        
        replaceUndescoreBySpace(parsedMessage);
        
        return parsedMessage;
    }
    
    /**
     * Remplace les espaces entre les mots entourés de " par des _ afin qu'ils soient concidérés comme un ensemble de mots
     * @param sentence 
     */
    private void transformSpaceInQuoteWithUnderscore(String sentence)
    {
        boolean replaceSpace = false;
        
        char[] sentenceChar = sentence.toCharArray();

        for(int i=0; i < sentenceChar.length; i++)
        {
            if(sentenceChar[i] == '"')
            {
                replaceSpace = !replaceSpace;
            }else if (sentenceChar[i] == ' ')
            {
                sentenceChar[i] = '_';
            }
        }
        
        sentence = String.copyValueOf(sentenceChar);
        
        sentence.replaceAll("\"", "");
    }
    
    /**
     * Remplace dans tous les mots d'une liste de mots les underscore par des espaces
     * @param parsedSentence  : La liste de mot à traiter
     */
    private void replaceUndescoreBySpace(List<String> parsedSentence)
    {
        for(String word : parsedSentence)
        {
            word.replace("_", " ");
        }
    }
    
    /**
     * Transforme une liste de Word en une liste de String correspondant aux attributs "Value"
     * @param words : Liste de mots (Word) à convertir
     * @return Liste de String correspondante
     */
    private List<String> wordsToStrings(List<Word> words)
    {
        List<String> strings = new LinkedList<>();
        
        for(Word word : words)
        {
            strings.add(word.getValue());
        }
        
        return strings;
    }
}

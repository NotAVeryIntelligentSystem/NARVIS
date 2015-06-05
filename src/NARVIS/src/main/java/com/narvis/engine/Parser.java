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
import com.narvis.dataaccess.exception.NoDataException;
import com.narvis.dataaccess.interfaces.IDataModelProvider;
import com.narvis.dataaccess.interfaces.IMetaDataProvider;
import com.narvis.dataaccess.models.lang.word.Dictionary;
import com.narvis.dataaccess.models.lang.word.Word;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Permet de parser une chaine de caractère en une liste de mots.
 *
 * @author Zack
 */
public class Parser {

    private final static Logger LOGGER = Logger.getLogger(Parser.class.getName());

    private final IDataModelProvider<Dictionary> dictionaryProvider;

    /**
     * Constructeur par défaut du Parser
     *
     * @throws java.lang.Exception
     */
    public Parser() throws Exception {
        // Récupération du RoutesProvider
        IMetaDataProvider metaDataProvider = DataAccessFactory.getMetaDataProvider();
        this.dictionaryProvider = (IDataModelProvider<Dictionary>) DataAccessFactory.getMetaDataProvider().getDataProvider("Dictionary");
    }

    /**
     * Transforme une chaine de caractères en une liste de mots en retirant les
     * mots inutiles
     *
     * @param sentence : Chaine de caractères contenant le message à parser
     * @return Le message parsé en une liste de mots
     * @throws com.narvis.dataaccess.exception.NoDataException
     */
    public List<String> parse(String sentence) throws NoDataException {
        List<String> parsedMessage = new ArrayList<>();
        sentence = sentence.toLowerCase();


        sentence = transformSpaceInQuoteWithUnderscore(sentence);
        
        parsedMessage.addAll(Arrays.asList(sentence.split(" ")));
        
        List<Word> ignoredWords = dictionaryProvider.getModel().getIgnoredWords();
        parsedMessage.removeAll(wordsToStrings(ignoredWords));
        
        parsedMessage = replaceUndescoreBySpace(parsedMessage);
        return parsedMessage;
    }

    /**
     * Remplace les espaces entre les mots entourés de " par des _ afin qu'ils
     * soient concidérés comme un ensemble de mots
     *
     * @param sentence
     */
    private String transformSpaceInQuoteWithUnderscore(String sentence) {
        boolean replaceSpace = false;

        char[] sentenceChar = sentence.toCharArray();

        for (int i = 0; i < sentenceChar.length; i++) {
            if (sentenceChar[i] == '"') {
                replaceSpace = !replaceSpace;
            }else if (replaceSpace && sentenceChar[i] == ' ')
            {
                sentenceChar[i] = '_';
            }
        }

        sentence = String.copyValueOf(sentenceChar);
        
        sentence = sentence.replaceAll("\"", "");
        
        return sentence;
    }

    /**
     * Remplace dans tous les mots d'une liste de mots les underscore par des espaces
     * @param parsedSentence  : La liste de mot à traiter
     * @return
     */
    private List<String> replaceUndescoreBySpace(List<String> parsedSentence)
    {
        List<String> newParsedSentence = new LinkedList<>();
        for(String word : parsedSentence)
        {
            word = word.replace("_", " ");
            newParsedSentence.add(word);
        }
        return newParsedSentence;
    }

    /**
     * Transforme une liste de Word en une liste de String correspondant aux
     * attributs "Value"
     *
     * @param words : Liste de mots (Word) à convertir
     * @return Liste de String correspondante
     */
    private List<String> wordsToStrings(List<Word> words) {
        List<String> strings = new ArrayList<>();
        
        for(Word word : words)
        {
            strings.add(word.getValue());
        }

        return strings;
    }
}

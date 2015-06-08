/*
 * The MIT License
 *
 * Copyright 2015 Zack.
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
import com.narvis.dataaccess.interfaces.dataproviders.IDataModelProvider;
import com.narvis.dataaccess.models.lang.word.Dictionary;
import com.narvis.dataaccess.models.lang.word.Word;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Parse a string sentence into a list of words
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class Parser {

    private final IDataModelProvider<Dictionary> dictionaryProvider;

    /**
     * Default constructor
     *
     * @throws java.lang.Exception
     */
    public Parser() throws Exception {
        // Récupération du RoutesProvider
        this.dictionaryProvider = (IDataModelProvider<Dictionary>) DataAccessFactory.getMetaDataProvider().getDataProvider("Dictionary");
    }

    /**
     * Transform a string in a list of words without useless words
     *
     * @param sentence String with the sentence
     * @return The parsed sentence
     * @throws com.narvis.dataaccess.exception.NoDataException
     */
    public List<String> parse(String sentence) throws NoDataException {
        List<String> parsedMessage = new ArrayList<>();

        /* We put the sentence to lowercase and remove all the non-alphanumeric caracters.
         This prevents us to the differents way users can write there messages (with punctuation or not, etc.)*/
        sentence = sentence.toLowerCase().replaceAll("[^a-z0-9|\" ]", "");

        /* We replace doubl-space that could be caused by the suppression of a single caracter with one space */
        sentence = sentence.toLowerCase().replaceAll("\\s+", " ");

        sentence = transformSpaceInQuoteWithUnderscore(sentence);

        parsedMessage.addAll(Arrays.asList(sentence.split(" ")));

        List<Word> ignoredWords = dictionaryProvider.getModel().getIgnoredWords();
        parsedMessage.removeAll(wordsToStrings(ignoredWords));

        parsedMessage = replaceUndescoreBySpace(parsedMessage);
        
        /* Remove empty words in word list */
        parsedMessage.removeAll(Arrays.asList("", null));
        return parsedMessage;
    }

    /**
     * Brows details map to find details that are sentences and parse these
     * sentences.
     *
     * @param detailsToValue : Details map to scan that contain at least 2 sentences (ex: key="give me the weather" => value = "")
     * @return Parsed sentences
     * @throws com.narvis.dataaccess.exception.NoDataException
     */
    public List<List<String>> getParsedSentencesFromDetails(Map<String, String> detailsToValue) throws NoDataException {
        List<List<String>> parsedSentences = new LinkedList<>();

        Set keys = detailsToValue.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();

            /* If the key contain spaces, this is actualy a sentence */
            if (key.contains(" ")) {
                List<String> currentParsedSentence = this.parse(key);
                parsedSentences.add(currentParsedSentence);
            }
        }
        return parsedSentences;
    }

    /**
     * Replace spaces between words bordered by " with _ so it not gonna be considered like a word
     *
     * @param sentence The string to annalyse
     */
    private String transformSpaceInQuoteWithUnderscore(String sentence) {
        boolean replaceSpace = false;

        char[] sentenceChar = sentence.toCharArray();

        for (int i = 0; i < sentenceChar.length; i++) {
            if (sentenceChar[i] == '"') {
                replaceSpace = !replaceSpace;
            } else if (replaceSpace && sentenceChar[i] == ' ') {
                sentenceChar[i] = '_';
            }
        }

        sentence = String.copyValueOf(sentenceChar);

        sentence = sentence.replaceAll("\"", "");

        return sentence;
    }

    /**
     * Replace in each words of the list underscores by spaces
     *
     * @param parsedSentence The list of words
     * @return The modified list
     */
    private List<String> replaceUndescoreBySpace(List<String> parsedSentence) {
        List<String> newParsedSentence = new LinkedList<>();
        for (String word : parsedSentence) {
            word = word.replace("_", " ");
            newParsedSentence.add(word);
        }
        return newParsedSentence;
    }

    /**
     * Convert a list of words to a list of strings
     * 
     * @param words List of Words to convert
     * @return List of strings
     */
    private List<String> wordsToStrings(List<Word> words) {
        List<String> strings = new ArrayList<>();

        for (Word word : words) {
            strings.add(word.getValue());
        }

        return strings;
    }
}

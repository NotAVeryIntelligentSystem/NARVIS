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
package com.narvis.dataaccess.models.lang.word;

import com.narvis.common.debug.NarvisLogger;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 *
 * @author Zack
 */
@Root(name = "Dictionary")
public class Dictionary {

    @ElementList(name = "Words", required = false)
    @SuppressWarnings("FieldMayBeFinal")
    private List<Word> words;

    public Dictionary() {
        words = new LinkedList<>();
    }

    public List<Word> getWords() {
        return this.words;
    }

    public Word getWordByValue(String value) {
        Word word = null;
        for (Word w : this.words) {
            if (w.getValue().equalsIgnoreCase(value)) {
                if (word != null) {
                    NarvisLogger.getInstance().log(Level.WARNING, "Word duplication : " + w.getValue());
                }
                word = w;
            }
        }

        return word;
    }

    public List<Word> getWordsByInformationType(String informationType) {
        List<Word> returnWords;
        returnWords = new LinkedList<>();
        informationType = informationType.toLowerCase();

        for (Word w : this.words) {
            if (w.getInformationTypes().contains(informationType)) {
                returnWords.add(w);
            }
        }

        return returnWords;
    }

    public List<Word> getIgnoredWords() {
        List<Word> returnWord;
        returnWord = new LinkedList<>();

        for (Word w : this.words) {
            if (w.isIgnored()) {
                returnWord.add(w);
            }
        }

        return returnWord;
    }

    public void addWord(Word word) {
        words.add(word);
    }
}

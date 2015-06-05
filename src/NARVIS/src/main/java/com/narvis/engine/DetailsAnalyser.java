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

    public DetailsAnalyser() throws Exception {
        this.dictionary = (IDataModelProvider<Dictionary>) DataAccessFactory.getMetaDataProvider().getDataProvider("Dictionary");
    }

    public Map<String, String> getDetailsTypes(List<String> details) throws NoDataException {
        List<String> hintList = new ArrayList<>();
        boolean isTypeFinded = false;
        for (String detail : details) {
            Word w = this.dictionary.getModel().getWordByValue(detail);
            if (w != null) { // Le mot existe dans le dictionnaire
                for (String hint : hintList) {
                    if (w.containInformationType(hint)) {
                        this.wordsAssociations.put(w.getValue(), hint);
                        isTypeFinded = true;
                        break;
                    }
                }
                if (!isTypeFinded && hintList.size() > 0) {
                    this.wordsAssociations.put(w.getValue(), hintList.get(0));
                } else {
                    this.wordsAssociations.put(w.getValue(), w.getInformationTypes().get(0));
                }
                hintList = w.getHints();
            } else {
                if (hintList.size() > 0) {
                    this.wordsAssociations.put(detail, hintList.get(0));
                } else {
                    this.wordsAssociations.put(detail, "");
                }
                hintList.clear();
            }
        }
        return this.wordsAssociations;
    }
}

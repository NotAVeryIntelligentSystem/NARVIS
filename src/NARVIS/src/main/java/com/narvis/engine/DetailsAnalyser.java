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
import com.narvis.dataaccess.models.user.UserData;
import com.narvis.dataaccess.models.user.UsersData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Define the type of information passed by words
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public final class DetailsAnalyser {

    private final IDataModelProvider<Dictionary> dictionary;
    private final IDataModelProvider<UsersData> usersDataProvider;

    /**
     * Default constructor
     * @throws Exception 
     */
    public DetailsAnalyser() throws Exception {
        this.dictionary = (IDataModelProvider<Dictionary>) DataAccessFactory.getMetaDataProvider().getDataProvider("Dictionary");
        this.usersDataProvider = (IDataModelProvider<UsersData>) DataAccessFactory.getMetaDataProvider().getDataProvider("Users");
    }

    /**
     * Define for each words the type of information pass by it.
     * It also add informations that we know about the user if the type of information doesn't appeard in the sentence.
     * @param details List of words that are details in the sentence
     * @param username The user name we need to find his informations
     * @return A Map that contain the words and there type (ex: "london" => "location")
     * @throws NoDataException 
     */
    public Map<String, String> getDetailsTypes(List<String> details, String username) throws NoDataException {
        List<String> hintList = new ArrayList<>();
        boolean isTypeFinded = false;

        Map<String, String> wordsAssociations = new HashMap<>();

        for (String detail : details) {
            Word w = this.dictionary.getModel().getWordByValue(detail);
            isTypeFinded = false;
            if (w != null) { // Le mot existe dans le dictionnaire
                for (String hint : hintList) {
                    if (w.containInformationType(hint)) {
                        wordsAssociations.put(w.getValue(), hint);
                        isTypeFinded = true;
                        break;
                    }
                }
                if (!isTypeFinded && hintList.size() > 0) {
                    wordsAssociations.put(w.getValue(), hintList.get(0));

                } else if (!isTypeFinded && w.getInformationTypes().size() > 0) {
                    wordsAssociations.put(w.getValue(), w.getInformationTypes().get(0));

                }
                hintList = new ArrayList<>(w.getHints());
            } else {
                if (hintList.size() > 0) {
                    wordsAssociations.put(detail, hintList.get(0));
                } else {
                    wordsAssociations.put(detail, "");
                }
                hintList.clear();
            }
        }

        wordsAssociations.put(username, "username");
        addUsersData(username, wordsAssociations);

        return wordsAssociations;
    }

    /**
     * If a type of information is not contained in the words assocations and we know it by the user informations, we add it in the words associations
     * @param username The user name we need to find his informations
     * @param wordsAssociations The map of words and type associations (ex: "london" => "location")
     * @throws NoDataException 
     */
    private void addUsersData(String username, Map<String, String> wordsAssociations) throws NoDataException {
        UsersData usersData = this.usersDataProvider.getModel();

        UserData userData;

        if ((userData = usersData.getUser(username)) != null) {
            for (Entry<String, String> data : userData.getAllData().entrySet()) {
                /* If we can't find a type of information in details and we know it with the user data,
                 we put it in the details list */
                if (!wordsAssociations.containsValue(data.getKey())) {
                    wordsAssociations.put(data.getValue(), data.getKey());
                }
            }
        }
    }

    /**
     * Accessor for the user data provider
     * @return the user data provider
     */
    public IDataModelProvider<UsersData> getUserDataProvider() {
        return this.usersDataProvider;
    }
}

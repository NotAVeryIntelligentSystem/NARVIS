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
package com.narvis.dataaccess.impl;

import com.narvis.common.extensions.filefilters.FolderNameFileFilter;
import com.narvis.common.functions.serialization.XmlSerializer;
import com.narvis.dataaccess.interfaces.IDataProvider;
import com.narvis.dataaccess.models.conf.*;
import java.io.File;
import java.util.Map;

/**
 *
 * @author uwy
 */
public class ModuleConfigurationDataProvider implements IDataProvider {
    private static final String CONF_FOLDER_NAME = "conf";
    private static final String LAYOUTS_FOLDER_NAME = "layouts";
    private static final String ANSWERS_FILE_NAME = "answers.xml";
    private static final String API_KEY_FILE_NAME = "api.key";
    private static final String MODULE_CONF_FILE_NAME = "module.conf";
    
    public static final String API_KEYWORD = "Api";
    public static final String CONF_KEYWORD = "Conf";
    public static final String ANSWERS_KEYWORD = "Answer";
    
    private final ApiKeys apiKeys;
    private final ModuleConf conf;
    private final AnswersLayout answersLayout;

    public ModuleConfigurationDataProvider(File moduleFolder) throws Exception {
        File apiFile = null;
        File confFile = null;
        File answerFile = null;
        for(File file : moduleFolder.listFiles(new FolderNameFileFilter(CONF_FOLDER_NAME))[0].listFiles()) {
            switch (file.getName()) {
                case API_KEY_FILE_NAME:
                    if(apiFile != null) {
                        throw new IllegalArgumentException("Api key file found twice !");
                    }   
                    apiFile = file;
                    break;
                case MODULE_CONF_FILE_NAME:
                    if(confFile != null) {
                        throw new IllegalArgumentException("Module conf file found twice !");
                    }   
                    confFile = file;
                    break;
            }
        }
        for(File file : moduleFolder.listFiles(new FolderNameFileFilter(LAYOUTS_FOLDER_NAME))[0].listFiles()) {
            switch (file.getName()) {
                case ANSWERS_FILE_NAME:
                    if(answerFile != null) {
                        throw new IllegalArgumentException("Api key files found twice !");
                    }   
                    answerFile = file;
                    break;
            }
        }
        this.apiKeys = apiFile == null ? null : XmlSerializer.fromFile(ApiKeys.class, apiFile);
        this.conf = confFile == null ? null : XmlSerializer.fromFile(ModuleConf.class, confFile);
        this.answersLayout = answerFile == null ? null : XmlSerializer.fromFile(AnswersLayout.class, answerFile);
    }
    
    public ApiKeys getApiKeys() {
        return this.apiKeys;
    }
    
    public ModuleConf getConf() {
        return this.conf;
    }
    
    public AnswersLayout getAnswersLayout() {
        return this.answersLayout;
    }
   
    @Override
    public String getData(String... keywords) {
        String [] nextKeywords = new String[keywords.length - 1];
        for(int i = 1 ; i < keywords.length ; i++) {
            nextKeywords[i - 1] = keywords[i];
        }
        switch(keywords[0]) {
            case API_KEYWORD:
                return this.apiKeys.getData(nextKeywords);
            case CONF_KEYWORD:
                return null; // nothing to do right now
                
            case ANSWERS_KEYWORD:
                return null; // To fucking do
        }
        return null; // todo : Handle error maybe ?
    }

    @Override
    public String getData(Map<String, String> details, String... keywords) {
        throw new UnsupportedOperationException("Not supported"); //To change body of generated methods, choose Tools | Templates.
    }
    
}

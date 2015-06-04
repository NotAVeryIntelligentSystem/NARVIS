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
import com.narvis.common.tools.serialization.XmlFileAccess;
import com.narvis.dataaccess.interfaces.IDataProvider;
import com.narvis.dataaccess.models.conf.*;
import java.io.File;

/**
 *
 * @author uwy
 */
public class ModuleConfigurationDataProvider implements IDataProvider {
    public static final String CONF_FOLDER_NAME = "conf";
    public static final String LAYOUTS_FOLDER_NAME = "layouts";
    public static final String DATA_FOLDER_NAME = "data";

    public static final String ANSWERS_FILE_NAME = "answers.xml";
    public static final String API_KEY_FILE_NAME = "api.key";
    public static final String MODULE_CONF_FILE_NAME = "module.conf";
    
    public static final String API_KEYWORD = "Api";
    public static final String CONF_KEYWORD = "Conf";
    public static final String ANSWERS_KEYWORD = "Answer";
    
    private final ApiKeys apiKeys;
    private final ModuleConf conf;
    private final AnswersLayout answersLayout;
    private final File moduleDataFolder;

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
        this.moduleDataFolder = new File(moduleFolder, DATA_FOLDER_NAME);
        this.apiKeys = apiFile == null ? null : XmlFileAccess.fromFile(ApiKeys.class, apiFile);
        this.conf = confFile == null ? null : XmlFileAccess.fromFile(ModuleConf.class, confFile);
        this.answersLayout = answerFile == null ? null : XmlFileAccess.fromFile(AnswersLayout.class, answerFile);
    }
    
    public File getDataFolder() {
        return this.moduleDataFolder;
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
                return this.conf.getData(nextKeywords);
                
            case ANSWERS_KEYWORD:
                return this.answersLayout.getData(nextKeywords);
        }
        return null;
    }

    
}

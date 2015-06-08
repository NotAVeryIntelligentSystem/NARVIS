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

import com.narvis.common.debug.NarvisLogger;
import com.narvis.common.extensions.StringExts;
import com.narvis.common.tools.serialization.XmlFileAccess;
import com.narvis.common.tools.serialization.XmlFileAccessException;
import com.narvis.dataaccess.exception.IllegalKeywordException;
import com.narvis.dataaccess.exception.NoDataException;
import com.narvis.dataaccess.exception.PersistException;
import com.narvis.dataaccess.exception.ProviderException;
import com.narvis.dataaccess.interfaces.dataproviders.IDataProvider;
import com.narvis.dataaccess.models.conf.*;
import com.narvis.dataaccess.models.layouts.ModuleAnswers;
import com.narvis.dataaccess.models.layouts.ModuleErrors;
import java.io.File;

/**
 *
 * @author uwy
 */
public final class ModuleConfigurationDataProvider implements IDataProvider {

    public static final String CONF_FOLDER_NAME = "conf";
    public static final String LAYOUTS_FOLDER_NAME = "layouts";
    public static final String DATA_FOLDER_NAME = "data";

    public static final String ANSWERS_FILE_NAME = "answers.xml";
    public static final String ERRORS_FILE_NAME = "errors.xml";

    public static final String API_KEY_FILE_NAME = "api.key";
    public static final String MODULE_CONF_FILE_NAME = "module.conf";

    public static final String API_KEYWORD = "Api";
    public static final String CONF_KEYWORD = "Conf";
    public static final String ANSWERS_KEYWORD = "Answer";
    public static final String ERRORS_KEYWORD = "Error";

    private final File moduleFolder;
    private final ApiKeys apiKeys;
    private final ModuleConf conf;
    private final ModuleAnswers answersLayout;
    private final ModuleErrors errorsLayout;

    public ModuleConfigurationDataProvider(File moduleFolder) throws ProviderException {
        this.moduleFolder = moduleFolder;
        try {
            File confFolder = new File(moduleFolder, CONF_FOLDER_NAME);
            File layoutFolder = new File(moduleFolder, LAYOUTS_FOLDER_NAME);
            
            File apiFile = confFolder.canRead() ? new File(confFolder, API_KEY_FILE_NAME) : null;
            File confFile = confFolder.canRead() ? new File(confFolder, MODULE_CONF_FILE_NAME) : null;
            File answerFile = layoutFolder.canRead() ? new File(layoutFolder, ANSWERS_FILE_NAME) : null;
            File errorFile = layoutFolder.canRead() ? new File(layoutFolder, ERRORS_FILE_NAME) : null;

            this.apiKeys = apiFile != null && apiFile.canRead() ?  XmlFileAccess.fromFile(ApiKeys.class, apiFile) : null;
            this.conf = confFile != null && confFile.canRead() ? XmlFileAccess.fromFile(ModuleConf.class, confFile) : null;
            this.answersLayout = answerFile != null && answerFile.canRead() ? XmlFileAccess.fromFile(ModuleAnswers.class, answerFile) : null;
            this.errorsLayout = errorFile != null && errorFile.canRead() ? XmlFileAccess.fromFile(ModuleErrors.class, errorFile) : null;
        } catch (XmlFileAccessException ex) {
            NarvisLogger.logException(ex);
            throw new ProviderException(ModuleConfigurationDataProvider.class, "Could not deserialize a file, more info in intern exception", ex, this.getErrorsLayout().getData("data"));
        }

    }

    public File getDataFolder() {
        return new File(this.moduleFolder, DATA_FOLDER_NAME);
    }

    public ApiKeys getApiKeys() {
        return this.apiKeys;
    }

    public ModuleConf getConf() {
        return this.conf;
    }

    public ModuleAnswers getAnswersLayout() {
        return this.answersLayout;
    }

    public ModuleErrors getErrorsLayout() {
        return this.errorsLayout;
    }

    public void persist() throws PersistException {
        try {
            XmlFileAccess.toFile(this.apiKeys, new File(new File(this.moduleFolder, CONF_FOLDER_NAME), API_KEY_FILE_NAME));
            XmlFileAccess.toFile(this.conf, new File(new File(this.moduleFolder, CONF_FOLDER_NAME), MODULE_CONF_FILE_NAME));
            XmlFileAccess.toFile(this.answersLayout, new File(new File(this.moduleFolder, LAYOUTS_FOLDER_NAME), ANSWERS_FILE_NAME));
            XmlFileAccess.toFile(this.errorsLayout, new File(new File(this.moduleFolder, LAYOUTS_FOLDER_NAME), ERRORS_FILE_NAME));
        } catch (XmlFileAccessException ex) {
            NarvisLogger.logException(ex);
            throw new PersistException(ModuleConfigurationDataProvider.class, "Could not persist conf files", ex, "general");
        }
    }

    @Override
    public String getData(String... keywords) throws IllegalKeywordException, NoDataException {
        if (keywords.length < 1) {
            throw new IllegalKeywordException(ModuleConfigurationDataProvider.class, keywords, "keywords.length < 1", this.getErrorsLayout().getData("engine"));
        }
        String[] nextKeywords = StringExts.skipFirst(keywords, 1);
        try {
            switch (keywords[0]) {
                case API_KEYWORD:
                    return this.apiKeys.getData(nextKeywords);
                case CONF_KEYWORD:
                    return this.conf.getData(nextKeywords);
                case ANSWERS_KEYWORD:
                    return this.answersLayout.getData(nextKeywords);
                case ERRORS_KEYWORD:
                    return this.errorsLayout.getData(nextKeywords);
            }
        } catch (NullPointerException ex) {
            NarvisLogger.logException(ex);
            throw new NoDataException(ModuleConfigurationDataProvider.class, "Keyword matches, but conf could not be loaded.", ex, this.getErrorsLayout().getData("engine"));
        }
        throw new IllegalKeywordException(FrontEndConfigurationDataProvider.class, keywords, keywords[0] + " does not match " + String.join(", ", API_KEY_FILE_NAME, MODULE_CONF_FILE_NAME), this.getErrorsLayout().getData("engine"));
    }

}

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
import com.narvis.dataaccess.exception.PersistException;
import com.narvis.dataaccess.exception.ProviderException;
import com.narvis.dataaccess.interfaces.dataproviders.IDataProvider;
import com.narvis.dataaccess.models.conf.ApiKeys;
import com.narvis.dataaccess.models.conf.ModuleConf;
import com.narvis.dataaccess.models.layouts.ModuleErrors;
import java.io.File;

/**
 *
 * @author uwy
 */
public final class FrontEndConfigurationDataProvider implements IDataProvider {

    public static final String CONF_FOLDER_NAME = "conf";
    public static final String LAYOUTS_FOLDER_NAME = "layouts";

    public static final String API_KEY_FILE_NAME = "api.key";
    public static final String MODULE_CONF_FILE_NAME = "module.conf";
    
    public static final String ERRORS_FILE_NAME = "errors.xml";


    public static final String API_KEYWORD = "Api";
    public static final String CONF_KEYWORD = "Conf";
    public static final String ERRORS_KEYWORD = "Error";

    private final File moduleFolder;
    private final ApiKeys apiKeys;
    private final ModuleConf conf;
    private final ModuleErrors errorsLayout;

    public FrontEndConfigurationDataProvider(File frontendFolder) throws ProviderException {
        this.moduleFolder = frontendFolder;
        File apiFile = null;
        File confFile = null;
        File errorFile = null;
        for (File file : new File(frontendFolder, CONF_FOLDER_NAME).listFiles()) {
            switch (file.getName()) {
                case API_KEY_FILE_NAME:
                    if (apiFile != null) {
                        throw new ProviderException(FrontEndConfigurationDataProvider.class, "Api key file found twice !", "Ouch");
                    }
                    apiFile = file;
                    break;
                case MODULE_CONF_FILE_NAME:
                    if (confFile != null) {
                        throw new ProviderException(FrontEndConfigurationDataProvider.class, "Module conf file found twice !", "Ouch");
                    }
                    confFile = file;
                    break;
            }
        }
        for (File file : new File(frontendFolder, LAYOUTS_FOLDER_NAME).listFiles()) {
            switch (file.getName()) {
                case ERRORS_FILE_NAME:
                    if(errorFile != null) {
                        throw new ProviderException(FrontEndConfigurationDataProvider.class, "Errors layout file found twice !", "Ouch");
                    }
                    errorFile = file;
                    break;
            }
        }
        try {
            this.apiKeys = apiFile == null ? null : XmlFileAccess.fromFile(ApiKeys.class, apiFile);
            this.conf = confFile == null ? null : XmlFileAccess.fromFile(ModuleConf.class, confFile);
            this.errorsLayout = errorFile == null ? null : XmlFileAccess.fromFile(ModuleErrors.class, errorFile);
        } catch (XmlFileAccessException ex) {
            NarvisLogger.logException(ex);
            throw new ProviderException(FrontEndConfigurationDataProvider.class, "Can not deserialize some files !", "Ouch");
        }

    }

    public ApiKeys getApiKeys() {
        return this.apiKeys;
    }

    public ModuleConf getConf() {
        return this.conf;
    }
    
    public ModuleErrors getErrorsLayout() {
        return this.errorsLayout;
    }
    
    public void persist() throws PersistException {
        try {
            XmlFileAccess.toFile(this.apiKeys, new File(new File(this.moduleFolder, CONF_FOLDER_NAME), API_KEY_FILE_NAME));
            XmlFileAccess.toFile(this.conf, new File(new File(this.moduleFolder, CONF_FOLDER_NAME), MODULE_CONF_FILE_NAME));
            XmlFileAccess.toFile(this.errorsLayout, new File(new File(this.moduleFolder, LAYOUTS_FOLDER_NAME), ERRORS_FILE_NAME));
        } catch (XmlFileAccessException ex) {
            NarvisLogger.logException(ex);
            throw new PersistException(FrontEndConfigurationDataProvider.class, "Could not persist conf files", ex, "general");
        }
    }

    @Override
    public String getData(String... keywords) throws IllegalKeywordException {
        if(keywords.length < 1) {
            throw new IllegalKeywordException(FrontEndConfigurationDataProvider.class, keywords, "keywords.length < 1", this.errorsLayout.getData("engine"));
        }
        String[] nextkeywords = StringExts.skipFirst(keywords, 1);
        switch(keywords[0]) {
            case API_KEY_FILE_NAME:
                return this.apiKeys.getData(nextkeywords);
            case MODULE_CONF_FILE_NAME:
                return this.conf.getData(nextkeywords);
        }
        throw new IllegalKeywordException(FrontEndConfigurationDataProvider.class, keywords, keywords[0] + " does not match " + String.join(", ", API_KEY_FILE_NAME, MODULE_CONF_FILE_NAME), this.errorsLayout.getData("engine"));
    }

}

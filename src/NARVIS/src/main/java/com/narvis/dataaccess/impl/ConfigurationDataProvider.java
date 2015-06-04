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

import com.narvis.common.extensions.filefilters.*;
import com.narvis.common.functions.serialization.XmlSerializer;
import com.narvis.common.tools.reflection.Factory;
import com.narvis.common.tools.reflection.FactoryException;
import com.narvis.dataaccess.interfaces.IDataProvider;
import com.narvis.dataaccess.models.conf.*;
import java.io.File;
import java.util.*;
import java.util.Map.*;

/**
 *
 * @author uwy
 */
public class ConfigurationDataProvider implements IDataProvider {
    public static final String GLOBAL_CONF_PATH = "../";
    public static final String CONF_FOLDER_NAME = "conf";
    public static final String CONF_FILE_NAME = "narvis.conf";
    public static final String MODULES_FOLDER_NAME = "modules";
    
    public static final String NARVIS_CONF_KEYWORD = "NarvisConf";

    private final NarvisConf narvisConf;
    private final Map<String, ModuleConfigurationDataProvider> modulesConfs;
    
    public ConfigurationDataProvider() throws Exception {
        this.modulesConfs = new HashMap<>();
        File globalFolder = new File(CONF_FOLDER_NAME);
        assert globalFolder.isDirectory() == true  : "Path for global folder isn't a folder !";
        this.narvisConf = XmlSerializer.fromFile(NarvisConf.class, globalFolder.listFiles(new FolderNameFileFilter(CONF_FOLDER_NAME))[0].listFiles(new FileNameFileFilter(CONF_FILE_NAME))[0]);
        for(File moduleFolder : globalFolder.listFiles(new FolderNameFileFilter(MODULES_FOLDER_NAME))) {
            if(moduleFolder.isDirectory()) {
                this.modulesConfs.put(moduleFolder.getName(), new ModuleConfigurationDataProvider(moduleFolder));
            }
        }
    }
    

    
    // Returns the MODULES not the configuration
    public Map<String, IDataProvider> getDataProviders() throws FactoryException {
        Map<String, IDataProvider> retVal = new HashMap<>();
        for(Entry<String, ModuleConfigurationDataProvider> entry : this.modulesConfs.entrySet()) {
            retVal.put(entry.getKey(), (IDataProvider) Factory.fromName(entry.getValue().getConf().getModuleClassPath(), entry.getValue(), ModuleConfigurationDataProvider.class));
        }
        return retVal;
    } 

    @Override
    public String getData(String... keywords) {
        String[] nextKeywords = new String[keywords.length - 1];
        for (int i = 1; i < keywords.length; i++) {
            nextKeywords[i - 1] = keywords[i];
        }
        if(NARVIS_CONF_KEYWORD.equals(keywords[0])) {
            return this.narvisConf.getData(nextKeywords);
        }
        return this.modulesConfs.get(keywords[0]).getData(nextKeywords);
    }

    @Override
    public String getData(Map<String, String> details, String... keywords) {
        throw new UnsupportedOperationException("Not supported"); //To change body of generated methods, choose Tools | Templates.
    }
    
}

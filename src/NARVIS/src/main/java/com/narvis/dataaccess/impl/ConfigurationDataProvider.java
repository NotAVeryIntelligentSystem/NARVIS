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
import com.narvis.common.tools.serialization.XmlFileAccess;
import com.narvis.common.tools.reflection.Factory;
import com.narvis.common.tools.reflection.FactoryException;
import com.narvis.common.tools.serialization.XmlFileAccessException;
import com.narvis.dataaccess.exception.IllegalKeywordException;
import com.narvis.dataaccess.exception.NoDataException;
import com.narvis.dataaccess.interfaces.dataproviders.IDataProvider;
import com.narvis.dataaccess.models.conf.*;
import com.narvis.frontend.interfaces.IFrontEnd;
import java.io.File;
import java.util.*;
import java.util.Map.*;

/**
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLINs
 */
public class ConfigurationDataProvider implements IDataProvider {

    public static final String GLOBAL_CONF_PATH = "../../";
    public static final String CONF_FOLDER_NAME = "conf";

    public static final String CONF_FILE_NAME = "narvis.conf";
    public static final String MODULES_FOLDER_NAME = "modules";
    public static final String FRONTENDS_FOLDER_NAME = "frontends";

    public static final String NARVIS_CONF_KEYWORD = "NarvisConf";

    private final NarvisConf narvisConf;
    private final Map<String, ModuleConfigurationDataProvider> modulesConfs;
    private final Map<String, FrontEndConfigurationDataProvider> frontEndConfs;

    /**
     * Constructor
     * @throws XmlFileAccessException
     * @throws Exception 
     */
    public ConfigurationDataProvider() throws XmlFileAccessException, Exception {
        this.modulesConfs = new HashMap<>();
        this.frontEndConfs = new HashMap<>();
        File globalFolder = new File(GLOBAL_CONF_PATH);
        if (!globalFolder.isDirectory()) {
            throw new IllegalArgumentException("Path for global folder isn't a folder !");
        }
        this.narvisConf = XmlFileAccess.fromFile(NarvisConf.class, new File(new File(globalFolder, CONF_FOLDER_NAME), CONF_FILE_NAME));
        for (File moduleFolder : new File(globalFolder, MODULES_FOLDER_NAME).listFiles()) {
            if (moduleFolder.isDirectory()) {
                this.modulesConfs.put(moduleFolder.getName(), new ModuleConfigurationDataProvider(moduleFolder));
            }
        }
        for (File frontendFolder : new File(globalFolder, FRONTENDS_FOLDER_NAME).listFiles()) {
            if (frontendFolder.isDirectory()) {
                this.frontEndConfs.put(frontendFolder.getName(), new FrontEndConfigurationDataProvider(frontendFolder));
            }
        }
    }

    /**
     * Returns the dataproviders MODULES not the configuration 
     * @return the map of modules from names
     * @throws FactoryException 
     */
    public Map<String, IDataProvider> createDataProviders() throws FactoryException {
        Map<String, IDataProvider> retVal = new HashMap<>();
        for (Entry<String, ModuleConfigurationDataProvider> entry : this.modulesConfs.entrySet()) {
            retVal.put(entry.getKey(), (IDataProvider) Factory.fromName(entry.getValue().getConf().getModuleClassPath(), entry.getValue(), ModuleConfigurationDataProvider.class));
        }
        return retVal;
    }
    /**
     * Returns the dataproviders Frontend not the configuration 
     * @return the map of FrontEnd from names
     * @throws FactoryException 
     */
    public Map<String, IFrontEnd> createFrontEnds() throws FactoryException {
        Map<String, IFrontEnd> retVal = new HashMap<>();
        for (Entry<String, FrontEndConfigurationDataProvider> entry : this.frontEndConfs.entrySet()) {
            retVal.put(entry.getKey(), (IFrontEnd) Factory.fromName(entry.getValue().getConf().getModuleClassPath(), entry.getValue(), FrontEndConfigurationDataProvider.class));
        }
        return retVal;
    }

    @Override
    public String getData(String... keywords) throws NoDataException {
        try {
            String[] nextKeywords = new String[keywords.length - 1];
            for (int i = 1; i < keywords.length; i++) {
                nextKeywords[i - 1] = keywords[i];
            }
            if (NARVIS_CONF_KEYWORD.equals(keywords[0])) {
                return this.narvisConf.getData(nextKeywords);
            }
            if (this.frontEndConfs.containsKey(keywords[0])) {
                return this.frontEndConfs.get(keywords[0]).getData(nextKeywords);
            }
            return this.modulesConfs.get(keywords[0]).getData(nextKeywords);
        } catch (IllegalKeywordException | NoDataException ex) {
            NarvisLogger.logException(ex);
            throw new NoDataException(ConfigurationDataProvider.class, "Could not find data from keywords : " + String.join(", ", keywords), "I am in a deep pain.");
        }
    }

}

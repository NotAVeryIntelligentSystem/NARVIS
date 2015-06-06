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
import com.narvis.common.tools.serialization.XmlFileAccessException;
import com.narvis.dataaccess.exception.*;
import com.narvis.dataaccess.interfaces.dataproviders.IDataModelProvider;
import com.narvis.dataaccess.models.lang.word.Dictionary;
import java.io.File;

/**
 *
 * @author Zack
 */
public class DictionaryProvider implements IDataModelProvider<Dictionary> {

    private final Dictionary dictionary;
    private final ModuleConfigurationDataProvider conf;

    public DictionaryProvider(ModuleConfigurationDataProvider conf) throws ProviderException {
        this.conf = conf;
        try {
            this.dictionary = XmlFileAccess.fromFile(Dictionary.class, new File(this.conf.getDataFolder(), this.getDictionaryDataPath()));
        } catch (XmlFileAccessException ex) {
            NarvisLogger.logException(ex);
            throw new ProviderException(DictionaryProvider.class, "In constructor", ex, this.conf.getErrorsLayout().getData("general"));
        }
    }

    private String getDictionaryDataPath() throws IllegalKeywordException, NoDataException {
        return this.conf.getData("Conf", "DictionaryDataPath");
    }

    @Override
    public Dictionary getModel(String... keywords) throws NoDataException {
        if (this.dictionary == null) {
            throw new NoDataException("Dictionary hasn't been deserialized, this is bad", this.conf.getErrorsLayout().getData("general"));
        }
        return this.dictionary;
    }

    @Override
    public void persist() throws PersistException {
        try {
            XmlFileAccess.toFile(this.dictionary, new File(this.conf.getDataFolder(), this.getDictionaryDataPath()));
        } catch (IllegalKeywordException | NoDataException | XmlFileAccessException ex) {
            NarvisLogger.logException(ex);
            throw new PersistException(DictionaryProvider.class, "Could not persist the model, see intern exception for details", ex, this.conf.getErrorsLayout().getData("persist"));
        }
    }

    @Override
    public String getData(String... keywords) {
        return this.dictionary.toString();
    }
}

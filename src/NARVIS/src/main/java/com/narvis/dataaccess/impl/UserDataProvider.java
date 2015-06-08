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
import com.narvis.dataaccess.exception.IllegalKeywordException;
import com.narvis.dataaccess.exception.NoDataException;
import com.narvis.dataaccess.exception.PersistException;
import com.narvis.dataaccess.exception.ProviderException;
import com.narvis.dataaccess.interfaces.dataproviders.IDataModelProvider;
import com.narvis.dataaccess.models.user.UsersData;
import java.io.File;

/**
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class UserDataProvider implements IDataModelProvider<UsersData> {

    private final ModuleConfigurationDataProvider conf;
    private final UsersData data;

    public UserDataProvider(ModuleConfigurationDataProvider confProvider) throws IllegalKeywordException, NoDataException, ProviderException {

        this.conf = confProvider;
        try {
            this.data = XmlFileAccess.fromFile(UsersData.class, new File(this.conf.getDataFolder(), this.getUsersDataPath()));
        } catch (XmlFileAccessException ex) {
            NarvisLogger.logException(ex);
            throw new ProviderException(UserDataProvider.class, "Could not deserialize data model !", ex, this.conf.getErrorsLayout().getData("general"));
        } catch (IllegalKeywordException | NoDataException ex) {
            NarvisLogger.logException(ex);
            throw ex;
        }

    }

    private String getUsersDataPath() throws IllegalKeywordException, NoDataException {
        return this.conf.getData("Conf", "UsersDataPath");
    }

    @Override
    public String getData(String... keywords) throws NoDataException, IllegalKeywordException {
        if (keywords.length < 2) {
            throw new IllegalKeywordException("Not enough keywords to get data, check your inputs !", this.conf.getErrorsLayout().getData("engine"));
        }
        return this.data.getUser(keywords[0]).getData(keywords[1]);
    }

    @Override
    public UsersData getModel(String... keywords) throws NoDataException {
        return this.data;
    }

    @Override
    public void persist() throws PersistException {
        try {
            XmlFileAccess.toFile(this.data, new File(this.conf.getDataFolder(), this.getUsersDataPath()));
        } catch (IllegalKeywordException | NoDataException | XmlFileAccessException ex) {
            NarvisLogger.logException(ex);
            throw new PersistException(RoutesProvider.class, "Could not persist file, see details in exception", ex, this.conf.getErrorsLayout().getData("persist"));
        }
    }

}

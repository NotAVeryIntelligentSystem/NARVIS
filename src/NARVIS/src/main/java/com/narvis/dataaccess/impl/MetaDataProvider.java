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

import com.narvis.common.generics.Pair;
import com.narvis.dataaccess.interfaces.dataproviders.IDataProvider;
import com.narvis.dataaccess.interfaces.IMetaDataProvider;
import com.narvis.frontend.interfaces.IFrontEnd;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class MetaDataProvider implements IMetaDataProvider {

    private final ConfigurationDataProvider config;
    private final Map<String, IDataProvider> providers;
    private final Map<String, IFrontEnd> frontEnds;
    public static final String CONF_KEYWORD = "Conf";
    public static final String FRONTEND_KEYWORD = "FrontEnd";

    /**
     * Constructor
     * @throws Exception 
     */
    public MetaDataProvider() throws Exception {
        this.config = new ConfigurationDataProvider();
        this.providers = this.config.createDataProviders();
        this.frontEnds = this.config.createFrontEnds();
    }
    
    @Override
    public IDataProvider getDataProvider(String... keywords) {
        if (CONF_KEYWORD.equals(keywords[0])) {
            return this.config;
        }
        return this.providers.get(keywords[0]);
    }

    @Override
    public IFrontEnd getFrontEnd(String... keywords) {
        return this.frontEnds.get(keywords[0]);
    }

    @Override
    public Set<String> getAvailableDataProviders() {
        return this.providers.keySet();
    }

    @Override
    public Set<String> getAvailableFrontEnds() {
        return this.frontEnds.keySet();
    }

}

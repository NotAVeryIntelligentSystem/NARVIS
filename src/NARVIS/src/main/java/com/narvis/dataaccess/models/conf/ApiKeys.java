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
package com.narvis.dataaccess.models.conf;

import com.narvis.dataaccess.interfaces.dataproviders.IDataProvider;
import com.narvis.dataaccess.interfaces.*;
import java.util.*;
import org.simpleframework.xml.*;

/**
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
@Root(name = "ApiKeys")
public class ApiKeys implements IDataProvider {

    public static final String NAME_KEYWORD = "Name";

    @Element(name = "Name")
    private String name;

    @ElementMap(entry = "ApiKey", key = "Name", attribute = true, inline = true, required = false)
    @SuppressWarnings("FieldMayBeFinal")
    private Map<String, String> apiKeys;

    public Map<String, String> getApiKeys() {
        return this.apiKeys;
    }

    public ApiKeys() {
        this.apiKeys = new HashMap<>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // NAME constant to get this object name
    // Otherwise returns the key value from the name an ApiKey is associated with
    @Override
    public String getData(String... keywords) {
        if (keywords.length != 1 || !this.apiKeys.containsKey(keywords[0])) {
            throw new IllegalArgumentException("Invalid number of argument or wrong ApiKey name in keywords");
        }
        if (ApiKeys.NAME_KEYWORD.equals(keywords[0])) {
            return this.name;
        }
        return this.apiKeys.get(keywords[0]);
    }

}

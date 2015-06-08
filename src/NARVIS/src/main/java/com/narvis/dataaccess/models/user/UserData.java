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
package com.narvis.dataaccess.models.user;

import java.util.HashMap;
import java.util.Map;
import org.simpleframework.xml.ElementMap;

/**
 *
 * @author uwy
 */
public class UserData {

    @ElementMap(entry = "DataEntry", key = "key", attribute = true, inline = true, required = false)
    @SuppressWarnings("FieldMayBeFinal")
    private Map<String, String> dataEntries;

    public UserData() {
        this.dataEntries = new HashMap<>();
    }

    public String getData(String key) {
        return this.dataEntries.get(key);
    }

    public void addData(String key, String value) {
        this.dataEntries.put(key, value);
    }

    public Map<String, String> getAllData() {
        return dataEntries;
    }
}

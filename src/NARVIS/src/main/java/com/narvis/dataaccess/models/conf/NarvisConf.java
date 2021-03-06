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

import com.narvis.dataaccess.interfaces.dataproviders.IConfDataProvider;
import java.util.*;
import org.simpleframework.xml.*;

/**
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
@Root(name = "NarvisConf")
public class NarvisConf implements IConfDataProvider {

    @ElementMap(entry = "entry", key = "key", attribute = true, inline = true, required = false)
    @SuppressWarnings("FieldMayBeFinal")
    private Map<String, String> entries;

    public NarvisConf() {
        this.entries = new HashMap<>();
    }

    @Override
    public String getData(String... keywords) {
        return this.entries.get(keywords[0]);
    }
    // Nothing here yet

    @Override
    public void setData(String... keywords) {
        this.entries.replace(keywords[0], keywords[1]);
    }

}

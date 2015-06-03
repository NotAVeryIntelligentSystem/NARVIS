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
package com.narvis.common.functions.serialization;

import java.io.File;
import java.io.OutputStream;
import org.simpleframework.xml.core.*;

/**
 *
 * @author uwy
 */
public class XmlSerializer {
    private static final Persister persister = new Persister(); // Make it only once since we need a single global settings
    
    
    public static <T> void toFile(T toSerialize, String file) throws Exception {
        XmlSerializer.toFile(toSerialize, new File(file));
    }
    
    public static <T> void toFile(T toSerialize, File file ) throws Exception {
        persister.write(toSerialize, file);
    }
    
    public static <T> void toStream(T toSerialize, OutputStream stream) throws Exception {
        persister.write(toSerialize, stream);
    }
    
    public static <T> T fromFile(Class<T> type, File file) throws Exception {
        return persister.read(type, file);
    }
}

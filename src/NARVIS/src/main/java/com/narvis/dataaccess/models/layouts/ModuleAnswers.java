/*
 * The MIT License
 *
 * Copyright 2015 puma.
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
package com.narvis.dataaccess.models.layouts;

import com.narvis.dataaccess.interfaces.IDataProvider;
import java.util.HashMap;
import java.util.Map;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

/**
 * Represent the XML file which contains the answers for each command
 *
 * @author puma
 */
@Root(name = "Answers")
public class ModuleAnswers implements IDataProvider {
    @ElementMap(entry = "Sentence", key = "command", attribute = true, inline = true)
    @SuppressWarnings("FieldMayBeFinal")
    private Map<String, String> map;
    
    
    public ModuleAnswers() {
        this.map = new HashMap<>();
    }
    


    public Map<String, String> getMap() {
        return map;
    }

    @Override
    public String getData(String... keywords) {
        if (this.map.containsKey(keywords[0])) {
            return map.get(keywords[0]);
        } 
        return null;
    }

}

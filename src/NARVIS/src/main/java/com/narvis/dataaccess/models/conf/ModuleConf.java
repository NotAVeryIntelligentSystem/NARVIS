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

import com.narvis.dataaccess.interfaces.IDataProvider;
import java.util.HashMap;
import java.util.Map;
import org.simpleframework.xml.*;

/**
 *
 * @author uwy
 */
@Root(name="ModuleConf")
public class ModuleConf implements IDataProvider {
    @Element(name = "ModuleClassPath")
    private String moduleClassPath;
    
    @ElementMap(entry="entry", key="key", attribute=true, inline=true)
    private final Map<String, String> entries;
  
    public static String MODULE_CLASS_PATH_KEYWORD = "ModuleClassPath";
        
    public ModuleConf() {
        this.entries = new HashMap<>();
    }
    
    public String getModuleClassPath() {
        return this.moduleClassPath;
    }
    
    public void setModuleClassPaht(String val) {
        this.moduleClassPath = val;
    }
    
    @Override
    public String getData(String... keywords) {
        if(keywords[0].equals(MODULE_CLASS_PATH_KEYWORD)) {
            return this.moduleClassPath;
        }
        return this.entries.get(keywords[0]);
    }
    //Todo
}

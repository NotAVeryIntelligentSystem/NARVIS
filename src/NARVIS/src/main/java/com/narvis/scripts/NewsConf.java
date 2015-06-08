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
package com.narvis.scripts;

import com.narvis.dataaccess.models.conf.ModuleConf;
import com.narvis.dataaccess.models.layouts.ModuleAnswers;
import com.narvis.dataaccess.models.layouts.ModuleErrors;
import com.narvis.dataaccess.news.NewsProvider;

/**
 *
 * @author puma
 */
public class NewsConf {
    
    public final static String MODULE_CLASS_PATH = NewsProvider.class.getCanonicalName();
    
    public ModuleAnswers createAnswerLayout()
    {
        
        ModuleAnswers retVal = new ModuleAnswers();
        
        retVal.getMap().put("news", "[title] link : [link]");

        return retVal;
    }
    
    public ModuleErrors createErrorsLayout()
    {
        
        ModuleErrors retVal = new ModuleErrors();
        retVal.getMap().put("error", "No news for your city sorry guys !"); 
        return retVal;
    }
    
    public ModuleConf createModuleConf() {
        ModuleConf retVal = new ModuleConf();
        retVal.setModuleClassPath(MODULE_CLASS_PATH);
            
        return retVal;
    }
    
}

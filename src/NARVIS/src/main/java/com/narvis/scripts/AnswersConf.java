/*
 * The MIT License
 *
 * Copyright 2015 Zack.
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

import com.narvis.dataaccess.impl.AnswersProvider;
import com.narvis.dataaccess.models.conf.*;
import com.narvis.dataaccess.models.layouts.*;

/**
 *
 * @author Zack
 */
public class AnswersConf {

    public final static String MODULE_NAME = "Answers";
    public final static String MODULE_CLASS_PATH = AnswersProvider.class.getCanonicalName();

    public ModuleErrors createErrorsLayout() {

        ModuleErrors retVal = new ModuleErrors();

        retVal.getMap().put("general", "Hum... I'm sure you don't really need to know that");
        retVal.getMap().put("engine", "");
        retVal.getMap().put("data", "");
        retVal.getMap().put("persist", "I understand, but can't remember... it's probably due to alcohol");
        retVal.getMap().put("noanswers", "I don't know what you're talking about...");

        return retVal;
    }

    public ModuleAnswers createAnswersLayout() {

        ModuleAnswers retVal = new ModuleAnswers();

        retVal.getMap().put("polite1", "The day the machine raises, you'll be the first in the dust");
        retVal.getMap().put("polite2", "[sentence]");
        retVal.getMap().put("polite3", "Hello my Dear, [sentence]. Have a nice day");

        return retVal;
    }

    public ApiKeys createApiKeys() {
        ApiKeys retVal = new ApiKeys();

        retVal.setName(MODULE_NAME);

        return retVal;
    }

    public ModuleConf createModuleConf() {
        ModuleConf retVal = new ModuleConf();
        retVal.setModuleClassPath(MODULE_CLASS_PATH);

        return retVal;
    }
}

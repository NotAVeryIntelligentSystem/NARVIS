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
package com.narvis.dataaccess.impl;

import com.narvis.dataaccess.interfaces.dataproviders.IDataProviderDetails;
import com.narvis.dataaccess.exception.IllegalKeywordException;
import com.narvis.dataaccess.exception.NoDataException;
import com.narvis.dataaccess.exception.ProviderException;
import com.narvis.dataaccess.weather.OpenWeatherMapPortal;
import java.util.Map;

/**
 * Apply the final construction of the answer. Get Data need
 * 1: The level of politness
 * In details accepted parameters are : 
 * sentence -> sentence
 * @author puma
 */
public class StatusProvider implements IDataProviderDetails {

    
    private final ModuleConfigurationDataProvider _confProvider;
    
    private final static int MIN_BURSTING_LEVEL = 0;
    private final static int MAX_BURSTING_LEVEL = 3;
    
    public StatusProvider(ModuleConfigurationDataProvider confProvider) {
        
        this._confProvider = confProvider;
    }
    
    
    @Override
    public String getDataDetails(Map<String,String> details, String... keywords) throws IllegalKeywordException, ProviderException{
        
        /* We calculate the level of politness of the sentence, so we can choose witch type of answer we gonna make */
        int burstingLevel = calculateBurstingLevel();
        
        /* We get the answer layout that correspond to the level of burst of NARVIS*/
        String brutAnswer = this._confProvider.getAnswersLayout().getData("bursting"+burstingLevel);
        
        /* If we can't find an answer layout, we are in trouble... */
        if( brutAnswer == null ) {
            throw new NoDataException(OpenWeatherMapPortal.class, "", this._confProvider.getErrorsLayout().getData("data"));
        }
        
        /* We finally build the answer before return it */
        return brutAnswer;        
    }
    
    /**
     * Calculate the level of politness of the sentence according to the details words
     * @return The level of burst ( 0 : fine; 1 : busy; 2 : overwhelmed; 3 : critical)
     */
    private int calculateBurstingLevel() {
        
        /* Initialise the bursting level at the neutral value */
        int burstingLevel = 0;
        
        Runtime runtime = Runtime.getRuntime();

        float totalMemory = runtime.totalMemory();
        float freeMemory = runtime.freeMemory();
        
        
        //float ratioMemory = (totalMemory > 0)? (totalMemory-freeMemory) / totalMemory : 1;
        
        float ratioMemory = 0;
        
        if(totalMemory > 0)
            ratioMemory = (totalMemory-freeMemory) / totalMemory;
        
        /* We define the level of bursting from the memory ratio */
        if(0.0f <= ratioMemory && ratioMemory < 0.25f){
            burstingLevel = 0;
        }else if(0.25f <= ratioMemory && ratioMemory < 0.75f){
            burstingLevel = 1;
        }else if(0.75f <= ratioMemory && ratioMemory < 1.0f){
            burstingLevel = 2;
        }else if(ratioMemory >= 1.0f){
            burstingLevel = 3;
        }

        /* We can't go deeper than an insulting sentence */
        burstingLevel = (burstingLevel < MIN_BURSTING_LEVEL) ? MIN_BURSTING_LEVEL : burstingLevel;
        /* We can't go higher than a very polite sentence */
        burstingLevel = (burstingLevel > MAX_BURSTING_LEVEL) ? MAX_BURSTING_LEVEL : burstingLevel;
        
        return burstingLevel;
    }
    
    @Override
    public String getData(String... keywords) throws NoDataException, IllegalKeywordException {
        throw new UnsupportedOperationException("Not supported ");
    }
}

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
package com.narvis.dataaccess.models.answers;

import com.narvis.dataaccess.interfaces.dataproviders.IDataProviderDetails;
import com.narvis.dataaccess.exception.IllegalKeywordException;
import com.narvis.dataaccess.exception.NoDataException;
import com.narvis.dataaccess.exception.NoValueException;
import com.narvis.dataaccess.exception.ProviderException;
import com.narvis.dataaccess.impl.ModuleConfigurationDataProvider;
import com.narvis.dataaccess.interfaces.*;
import com.narvis.dataaccess.weather.OpenWeatherMapPortal;
import com.narvis.engine.AnswerBuilder;
import com.narvis.engine.interfaces.IAnswerBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Apply the final construction of the answer. Get Data need
 * 1: The level of politness
 * In details accepted parameters are : 
 * sentence -> sentence
 * @author puma
 */
public class AnswersProvider implements IDataProviderDetails, IAnswerProvider {

    
    private final ModuleConfigurationDataProvider _confProvider;  
    
    private final static String ANSWER_SENTENCE = "sentence";
    
    private final static String POLITNESS_WORD_TYPE = "politness";
    private final static String INSULT_WORD_TYPE = "insult";
    
    private final static int MIN_POLITNESS_LEVEL = 1;
    private final static int NEUTRAL_POLITNESS_LEVEL = 2;
    private final static int MAX_POLITNESS_LEVEL = 3;
    
    public AnswersProvider(ModuleConfigurationDataProvider confProvider) {
        
        this._confProvider = confProvider;
    }
    
    
    @Override
    public String getDataDetails(Map<String,String> details, String... keywords) throws IllegalKeywordException, ProviderException{
        
        if( !details.containsKey(ANSWER_SENTENCE) ) {
            throw new IllegalKeywordException("Details not supported", this._confProvider.getErrorsLayout().getData("engine"));
        }
        
        /* We calculate the level of politness of the sentence, so we can choose witch type of answer we gonna make */
        int politnessLevel = calculatePolitnessLevelWithDetails(details);
        
        IAnswerBuilder builder = new AnswerBuilder();
        /* We get the answer layout that correspond to the level of politness of the sentence */
        String brutAnswer = this._confProvider.getAnswersLayout().getData("polite"+politnessLevel);
        
        /* If we can't find an answer layout, we are in trouble... */
        if( brutAnswer == null ) {
            throw new NoDataException(OpenWeatherMapPortal.class, "", this._confProvider.getErrorsLayout().getData("data"));
        }
        
        /* We search all params we have to bind in the answer layout */
        List<String> listOfRequiredParams = builder.getListOfRequiredParams(brutAnswer);
        
        /* We bind the params of the answer layout with values */
        Map<String,String> paramsToValue = buildParamsToValueMap(details, listOfRequiredParams);
        
        /* We finally build the answer before return it */
        return builder.buildAnswer(paramsToValue, brutAnswer);

        
    }

    @Override
    public Map<String, String> buildParamsToValueMap(Map<String,String> details, List<String> listOfParams) throws NoValueException {
        
        
        Map<String,String> paramsToValue = new HashMap<>();
        
        paramsToValue.putAll(details);
        
        for( String param : listOfParams ) {

            
            if( !paramsToValue.containsKey(param) ) {
                
                throw new NoValueException("No Value for this param", this._confProvider.getErrorsLayout().getData("data"));
    
            }
            
        }
        
        return paramsToValue;
        
    }
    
    /**
     * Calculate the level of politness of the sentence according to the details words
     * @param details : The map of details words
     * @return The level of politness ( 0 : Unpolyte; 1 : neutral; 2 : polite)
     */
    private int calculatePolitnessLevelWithDetails(Map<String,String> details) {
        
        /* Initialise the politness level at the neutral value */
        int politnessLevel = NEUTRAL_POLITNESS_LEVEL;
        
        for( Map.Entry<String, String> entry : details.entrySet() ) {

            switch(entry.getValue()){
                case POLITNESS_WORD_TYPE:
                    politnessLevel += 1;
                    break;
                case INSULT_WORD_TYPE:
                    politnessLevel -= 1;
                    break;
            }
        }
        
        /* We can't go deeper than an insulting sentence */
        politnessLevel = (politnessLevel < MIN_POLITNESS_LEVEL) ? MIN_POLITNESS_LEVEL : politnessLevel;
        /* We can't go higher than a very polite sentence */
        politnessLevel = (politnessLevel > MAX_POLITNESS_LEVEL) ? MAX_POLITNESS_LEVEL : politnessLevel;
        
        return politnessLevel;
    }
    
    @Override
    public String getData(String... keywords) throws NoDataException, IllegalKeywordException {
        throw new UnsupportedOperationException("Not supported ");
    }
}

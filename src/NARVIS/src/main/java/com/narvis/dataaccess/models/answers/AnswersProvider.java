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

    
    private ModuleConfigurationDataProvider _confProvider;
    private int _maxLeverOfPolitness;   
    
    private String paramSentence = "sentence";
    
    private Map<String,String> _details = new HashMap<>();
    
    
    public AnswersProvider(ModuleConfigurationDataProvider confProvider) {
        
        this._confProvider = confProvider;
        this._maxLeverOfPolitness = 3;
    }
    
    
    @Override
    public String getDataDetails(Map<String,String> details, String... keywords) throws IllegalKeywordException, ProviderException{
    
        if( keywords.length < 2 || keywords[0] == null && keywords[1] == null ) {
            throw new IllegalKeywordException(OpenWeatherMapPortal.class, keywords, "Not enough keywords", this._confProvider.getErrorsLayout().getData("engine"));
        }
        
        if( !details.containsKey(this.paramSentence) ) {
            throw new IllegalKeywordException("Details not supported", this._confProvider.getErrorsLayout().getData("engine"));
        }
        
        this._details = details;
    
        IAnswerBuilder builder = new AnswerBuilder();
        String brutAnswer = builder.readAnswerForCommand(this._confProvider.getAnswersLayout(), keywords[0] );
        
        if( brutAnswer == null ) {
            throw new IllegalKeywordException(OpenWeatherMapPortal.class, keywords, "", this._confProvider.getErrorsLayout().getData("engine"));
        }
        
        List<String> listOfRequiredParams = builder.getListOfRequiredParams(brutAnswer);     
        Map<String,String> paramsToValue = buildParamsToValueMap(listOfRequiredParams);
        
        return builder.buildAnswer(paramsToValue, brutAnswer);

        
    }

    @Override
    public Map<String, String> buildParamsToValueMap(List<String> listOfParams) throws NoValueException {
        
        
        Map<String,String> paramsToValue = new HashMap<>();
        
        paramsToValue.putAll(_details);
        
        
        for( String param : listOfParams ) {

            
            if( !paramsToValue.containsKey(param) ) {
                
                throw new NoValueException("No Value for this param", this._confProvider.getErrorsLayout().getData("data"));
    
            }
            
        }
        
        return paramsToValue;
        
    }
    
    @Override
    public String getData(String... keywords) throws NoDataException, IllegalKeywordException {
        throw new UnsupportedOperationException("Not supported ");
    }
    
    
   
    
    
    
    
}

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

import com.narvis.dataaccess.interfaces.IAnswserBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author puma
 */
public class AnswerBuilder implements IAnswserBuilder {

    @Override
    public String readAnswerForCommand(ModuleConfigurationDataProvider providerConf, String command) {
     
        String answerFromXmlFile = providerConf.getAnswersLayout().getData(command);
        return answerFromXmlFile;
        
    }

    @Override
    public List<String> getListOfRequiredParams(String answerFromXml) {
        
        if( answerFromXml == null ) {
            return null;
        }

        //Get all the required params
        Pattern pattern = Pattern.compile("\\[[a-z]*\\]");
        String[] response = answerFromXml.split(pattern.pattern());

        return Arrays.asList(response);

    }

    @Override
    public String buildAnswer(Map<String, String> paramsToValue, String answerFromXml) {
     
        String finalAnswer = answerFromXml;
        
        for( Map.Entry<String,String> paramToValue : paramsToValue.entrySet() ) {
               
            //We need to make sur the param name is delimited with bracket
            //Our standard way to delimit parameters
            String paramName = paramToValue.getKey();
            paramName = AddBracketToParamName(paramName);
            
            
            //Replace every occurence of a params with its value
            finalAnswer = finalAnswer.replace( paramName, paramToValue.getValue());
            
        }
        
        return finalAnswer;
        
    }

    /**
     * Add the bracket around the param name. Do nothing if there is already bracket
     */   
    private String AddBracketToParamName(String paramName) {

        
        //No bracket at the end, add it
        if( !paramName.endsWith("]") )
        {
            paramName = paramName.concat("]");
        }
        
        
        //No bracket at the beginning, add it
        if( paramName.charAt(0) != '[' ) 
        {
            paramName = "[".concat(paramName);  
        }
        
        return paramName;
        
    }

    
}

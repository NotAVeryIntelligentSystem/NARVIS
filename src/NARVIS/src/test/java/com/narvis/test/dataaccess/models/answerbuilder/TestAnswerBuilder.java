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
package com.narvis.test.dataaccess.models.answerbuilder;

import com.narvis.dataaccess.impl.AnswerBuilder;
import com.narvis.dataaccess.interfaces.IAnswserBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author puma
 */
public class TestAnswerBuilder {
    
    public TestAnswerBuilder() {
    }

    
    @Test
    public void TestAnswer() {
        
        IAnswserBuilder builder = new AnswerBuilder();
        
        String message = "The temperature is [temperature]°C";
        Map<String,String> paramToValue = new HashMap();
        
        paramToValue.put("temperature", "25.3");
        
                
        String answer = builder.buildAnswer(paramToValue, message);
        
        assertEquals("The temperature is 25.3°C", answer);
        
    }
    
    
    @Test
    public void testRequiredParameters() {
        
        IAnswserBuilder builder = new AnswerBuilder();
        
        String message = "The temperature in [city] is [temperature]°C and the cloud percentage is [cloud]%";
        
        List<String> expectedParameters = new ArrayList<>();
        expectedParameters.add("city");
        expectedParameters.add("temperature");
        expectedParameters.add("cloud");
        
        List<String> actuals = builder.getListOfRequiredParams(message);
        
        assertArrayEquals(expectedParameters.toArray(), actuals.toArray());
        
    }
}

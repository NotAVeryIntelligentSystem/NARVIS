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
package com.narvis.test.dataaccess.models;

import com.narvis.common.debug.NarvisLogger;
import com.narvis.dataaccess.DataAccessFactory;
import com.narvis.dataaccess.interfaces.dataproviders.IDataModelProvider;
import com.narvis.dataaccess.interfaces.dataproviders.IDataProviderDetails;
import com.narvis.dataaccess.models.route.RouteNode;
import com.narvis.engine.Action;
import com.narvis.engine.DetailsAnalyser;
import com.narvis.engine.FondamentalAnalyser;
import com.narvis.engine.Parser;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Zack
 */
public class TestRoutesProvider {
    
    public TestRoutesProvider() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    
    @Test
    public void testInstanciation() throws Exception
    {
        try {
            IDataModelProvider<RouteNode> myRoutesProvider = (IDataModelProvider<RouteNode>) DataAccessFactory.getMetaDataProvider().getDataProvider("Routes");
        } catch (Exception ex) {
            NarvisLogger.getInstance().logException(ex);
            throw ex;
        }
    }
    
    @Test
    public void testGetDataDetails() throws Exception
    {
        /* Input data */
        String inputSentence = "\"Give someone weather\" mean \"what is the weather\"";
        
        List<String> testParsedSentence = new LinkedList<>();
        testParsedSentence.add("what");
        testParsedSentence.add("is");
        testParsedSentence.add("weather");
        /**/
        
        /* Expected results */
        /**/
        
        Action myAction;
        try {
            Parser myParser = new Parser();
            List<String> myParsedSentence = myParser.parse(inputSentence);
            
            FondamentalAnalyser myFondamentalAnalyser = new FondamentalAnalyser();
            
            myAction = myFondamentalAnalyser.findAction(testParsedSentence);
            assertNull(myAction);
            
            myAction = myFondamentalAnalyser.findAction(myParsedSentence);
            
            DetailsAnalyser myDetailsAnalyser = new DetailsAnalyser();
            Map<String, String> myDetails = myDetailsAnalyser.getDetailsTypes(myAction.getDetails(), "nobody");
            
            IDataModelProvider<RouteNode> myRoutesProvider = (IDataModelProvider<RouteNode>) DataAccessFactory.getMetaDataProvider().getDataProvider("Routes");
            
            String[] askFor = new String[1];
            askFor[0] = myAction.getPrecisions().get(0);
            
            String answer = ((IDataProviderDetails)myRoutesProvider).getDataDetails(myDetails, askFor);
            
            
            myAction = myFondamentalAnalyser.findAction(testParsedSentence);
            assertEquals(myAction.getProviderName(), "weather");
            
        } catch (Exception ex) {
            NarvisLogger.logException(ex);
            throw ex;
        }
    }
}

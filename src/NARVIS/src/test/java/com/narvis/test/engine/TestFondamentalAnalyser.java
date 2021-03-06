
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
package com.narvis.test.engine;

import com.narvis.engine.Action;
import com.narvis.engine.FondamentalAnalyser;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

/**
 *
 * @author Zack
 */
public class TestFondamentalAnalyser {

    public TestFondamentalAnalyser() {
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

    @Test
    public void TestConstructor() {
        try {
            FondamentalAnalyser myFondamentalAnalyser = new FondamentalAnalyser();

        } catch (SAXException ex) {
            fail(ex.getMessage());
        } catch (IOException ex) {
            fail(ex.getMessage());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void TestSearchPath() {
        /* Input Values */
        List<String> parsedSentence = new LinkedList<>();
        parsedSentence.add("give");
        parsedSentence.add("me");
        parsedSentence.add("weather");
        parsedSentence.add("in");
        parsedSentence.add("london");

        List<String> parsedSentence2 = new LinkedList<>();
        parsedSentence2.add("please");
        parsedSentence2.add("give");
        parsedSentence2.add("me");
        parsedSentence2.add("weather");
        parsedSentence2.add("in");
        parsedSentence2.add("london");
        /**/

        /* Expected Result */
        String expectedProviderName = "OpenWeatherMap";

        List<String> expectedDetails = new LinkedList<>();
        expectedDetails.add("me");
        expectedDetails.add("in");
        expectedDetails.add("london");

        List<String> expectedDetails2 = new LinkedList<>();
        expectedDetails2.add("please");
        expectedDetails2.add("me");
        expectedDetails2.add("in");
        expectedDetails2.add("london");

        List<String> expectedAskFor = new LinkedList<>();
        /**/

        Action myAction;

        try {

            /* Test with sentence that match a route */
            FondamentalAnalyser myFondamentalAnalyser = new FondamentalAnalyser();
            myAction = myFondamentalAnalyser.findAction(parsedSentence);

            assertEquals(myAction.getProviderName(), expectedProviderName);
            assertArrayEquals(myAction.getDetails().toArray(), expectedDetails.toArray());
            assertArrayEquals(myAction.getPrecisions().toArray(), expectedAskFor.toArray());

            /* Test with a sentence that has good pattern but with some words before */
            myAction = myFondamentalAnalyser.findAction(parsedSentence2);
            assertEquals(myAction.getProviderName(), expectedProviderName);
            assertArrayEquals(myAction.getDetails().toArray(), expectedDetails2.toArray());
            assertArrayEquals(myAction.getPrecisions().toArray(), expectedAskFor.toArray());

            /* Test with a NULL value *
             myAction = myFondamentalAnalyser.findAction(null);
             assertNull(myAction);
             /**/

            /* Test with an empty sentence *
             myAction = myFondamentalAnalyser.findAction(new LinkedList<String>());
             assertNull(myAction);
             /**/
        } catch (SAXException ex) {
            fail(ex.getMessage());
        } catch (IOException ex) {
            fail(ex.getMessage());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
}




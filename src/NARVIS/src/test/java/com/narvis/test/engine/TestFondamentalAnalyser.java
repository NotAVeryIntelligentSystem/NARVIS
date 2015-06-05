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
import java.util.logging.Level;
import java.util.logging.Logger;
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

        List<String> wrongParsedSentence = new LinkedList<>();
        wrongParsedSentence.add("this");
        wrongParsedSentence.add("is");
        wrongParsedSentence.add("sparta");
        /**/

        /* Expected Result */
        String expectedProviderName = "weather";

        List<String> expectedDetails = new LinkedList<>();
        expectedDetails.add("me");
        expectedDetails.add("in");
        expectedDetails.add("london");

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

            /* Test with a sentence that match no route */
            myAction = myFondamentalAnalyser.findAction(wrongParsedSentence);
            assertNull(myAction);

            /* Test with a NULL value */
            myAction = myFondamentalAnalyser.findAction(null);
            assertNull(myAction);

            /* Test with an empty sentence */
            myAction = myFondamentalAnalyser.findAction(new LinkedList<String>());
            assertNull(myAction);

        } catch (SAXException ex) {
            fail(ex.getMessage());
        } catch (IOException ex) {
            fail(ex.getMessage());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void TestCreateSimilarityBetween() {
        /* Input Values */
        List<List<String>> parsedSentences = new LinkedList<>();

        List<String> knownParsedSentence = new LinkedList<>();
        knownParsedSentence.add("give");
        knownParsedSentence.add("someone");
        knownParsedSentence.add("weather");
        parsedSentences.add(knownParsedSentence);

        List<String> newParsedSentence = new LinkedList<>();
        newParsedSentence.add("bring");
        newParsedSentence.add("someone");
        newParsedSentence.add("weather");
        parsedSentences.add(newParsedSentence);
        
        List<String> newParsedSentence2 = new LinkedList<>();
        newParsedSentence2.add("bring");
        newParsedSentence2.add("someone");
        newParsedSentence2.add("weather");
        List<String> expectedDetails = new LinkedList<>();
        expectedDetails.add("someone");
        /**/

        /* Expected Result */
        String expectedProviderName = "weather";
        /**/

        Action myAction;

        try {
            FondamentalAnalyser myFondamentalAnalyser = new FondamentalAnalyser();

            myAction = myFondamentalAnalyser.findAction(newParsedSentence);
            assertNull(myAction); // La route ne doit pas être trouvée

            /* Ajoute la nouvelle route */
            myFondamentalAnalyser.createSimilarityBetween(parsedSentences);
            
            myAction = myFondamentalAnalyser.findAction(newParsedSentence2);
            assertNotNull(myAction); // La route doit être trouvée

            assertEquals(myAction.getProviderName(), expectedProviderName);
            assertArrayEquals(myAction.getDetails().toArray(), expectedDetails.toArray());
            assertTrue(myAction.getPrecisions().isEmpty());

        } catch (SAXException ex) {
            fail(ex.getMessage());
        } catch (IOException ex) {
            fail(ex.getMessage());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
}

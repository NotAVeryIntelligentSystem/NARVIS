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

import com.narvis.common.debug.NarvisLogger;
import com.narvis.engine.Parser;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
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
public class TestParser {

    public TestParser() {
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
    public void TestConstructor() throws Exception {
        try {
            Parser myParser = new Parser();
        } catch (Exception ex) {
            NarvisLogger.logException(ex);

            throw ex;
        }
    }

    @Test
    public void TestParser() throws Exception {
        /* Input values */
        String sentence = "Give me the weather in London";
        /**/

        /* Expected results */
        List<String> expectedParsedSentence = new LinkedList<>();
        expectedParsedSentence.add("give");
        expectedParsedSentence.add("me");
        expectedParsedSentence.add("weather");
        expectedParsedSentence.add("in");
        expectedParsedSentence.add("london");
        /**/

        try {
            Parser myParser = new Parser();
            List<String> parsedSentence = myParser.parse(sentence);

            assertArrayEquals(parsedSentence.toArray(), expectedParsedSentence.toArray());
        } catch (Exception ex) {
            NarvisLogger.getInstance().logException(ex);

            throw ex;
        }

    }

    @Test
    public void TestParserWithQuotes() throws Exception {
        /* Input values */
        String inputSentence = "\"Give someone weather\" mean \"what is the weather\"";
        /**/

        /* Expected results */
        List<String> expectedParsedSentence = new LinkedList<>();
        expectedParsedSentence.add("give someone weather");
        expectedParsedSentence.add("mean");
        expectedParsedSentence.add("what is the weather");
        /**/

        try {
            Parser myParser = new Parser();
            List<String> parsedSentence = myParser.parse(inputSentence);

            assertArrayEquals(parsedSentence.toArray(), expectedParsedSentence.toArray());
        } catch (Exception ex) {
            fail(ex.getMessage());
            NarvisLogger.logException(ex);

            throw ex;
        }

    }
}

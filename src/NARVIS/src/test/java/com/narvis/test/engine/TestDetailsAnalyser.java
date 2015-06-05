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

import com.narvis.engine.DetailsAnalyser;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class TestDetailsAnalyser {

    public TestDetailsAnalyser() {
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
    public void TestConstructor() {
        try {
            DetailsAnalyser myDetailsAnalyser = new DetailsAnalyser();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void TestGetDetailsTypes() {
        /* Input values */
        List<String> details = new LinkedList<>();
        details.add("me");
        details.add("in");
        details.add("london");
        /**/

        /* Expected results */
        Map<String, String> expectedDetailsTypes = new HashMap<>();
        expectedDetailsTypes.put("in", "preposition");
        expectedDetailsTypes.put("london", "location");
        /**/

        try {
            DetailsAnalyser myDetailsAnalyser = new DetailsAnalyser();

            Map<String, String> myDetailsTypes = myDetailsAnalyser.getDetailsTypes(details);

            if (!myDetailsTypes.isEmpty()) {
                assertTrue(myDetailsTypes.containsKey("in"));
                assertEquals(myDetailsTypes.get("in"), expectedDetailsTypes.get("in"));

                assertTrue(myDetailsTypes.containsKey("london"));
                assertEquals(myDetailsTypes.get("london"), expectedDetailsTypes.get("london"));

            } else {
                fail("Entry expected in 'myDetailsTypes'");
            }
            assertEquals(details, details);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
}

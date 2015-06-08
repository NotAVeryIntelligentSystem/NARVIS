/*
 * The MIT License
 *
 * Copyright 2015 uwy.
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
package com.narvis.test.common.extensions;

import com.narvis.common.extensions.StringExts;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author uwy
 */
public class StringExtsTest {

    public StringExtsTest() {
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

    /**
     * Test of split method, of class StringExts.
     */
    @Test
    public void testSplit_3args_1() {
        System.out.println("split");
        //                                                         |
        String sentence = "This is a very long sentence that need to be splitted !";
        int maxPacketSize = 40;
        String appendMessage = " [...]";
        List<String> expResult = new ArrayList<>();
        expResult.add("This is a very long sentence that need [...]");
        expResult.add("to be splitted !");
        List<String> result = StringExts.split(sentence, maxPacketSize, appendMessage);
        for (int i = 0; i < result.size(); i++) {
            assertEquals(expResult.get(i), result.get(i));
        }
    }

    /**
     * Test of split method, of class StringExts.
     */
    @Test
    public void testSplit_3args_2() {
        System.out.println("split");
        //                                                        |
        String[] words = "This is a very long sentence that need to be splitted !".split(" ");
        int maxPacketSize = 40;
        String appendMessage = " [...]";
        List<String> expResult = new ArrayList<>();
        expResult.add("This is a very long sentence that need [...]");
        expResult.add("to be splitted !");
        List<String> result = StringExts.split(words, maxPacketSize, appendMessage);
        for (int i = 0; i < result.size(); i++) {
            assertEquals(expResult.get(i), result.get(i));
        }
    }

    /**
     * Test of skipFirst method, of class StringExts.
     */
    @Test
    public void testSkipFirst() {
        System.out.println("skipFirst");
        String[] items = new String[]{"1", "2", "3"};
        int count = 1;
        String[] expResult = new String[]{"2", "3"};
        String[] result = StringExts.skipFirst(items, count);
        assertArrayEquals(expResult, result);
    }

}

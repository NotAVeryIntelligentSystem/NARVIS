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
package com.narvis.test.frontend.twitter;

import com.narvis.frontend.MessageInOut;
import com.narvis.frontend.twitter.output.Output;
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
 * @author Nakou
 */
public class TwitterTest {

    public TwitterTest() {
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
    public void testInstance() {
        try {
            Output out = new Output(null, "Twitter");
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testCut() {
        List<String> tweetsObtains = new ArrayList<>();
        MessageInOut m = new MessageInOut("Twitter", "Coucouco o pjp jp jpj jopj  oj pjo o jp jj popj pjop j joop pj ojppo ojpop j opojp poop jp pojopo op jopj opjp oj j opj pj poj opoihfiooih h p in fbzefbpzefzii pzzh hzfh zihz^f hzpzhp hfzhfzpihpzhzh phphh p hphph ophpo oh  h", "nakou;uwy;lolone;true", null);
        Output out = new Output(null, "Twitter");
        tweetsObtains = out.getTweetList(m);
        assertTrue(tweetsObtains.get(0).length() <= 140);
        assertTrue(tweetsObtains.get(1).length() <= 140);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}

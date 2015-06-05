/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
            Output out = new Output(null);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testCut() {
        List<String> tweetsObtains = new ArrayList<>();
        MessageInOut m = new MessageInOut("Twitter", "Coucouco o pjp jp jpj jopj  oj pjo o jp jj popj pjop j joop pj ojppo ojpop j opojp poop jp pojopo op jopj opjp oj j opj pj poj opoihfiooih h p in fbzefbpzefzii pzzh hzfh zihz^f hzpzhp hfzhfzpihpzhzh phphh p hphph ophpo oh  h", "nakou;uwy;lolone;true");
        Output out = new Output(null);
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

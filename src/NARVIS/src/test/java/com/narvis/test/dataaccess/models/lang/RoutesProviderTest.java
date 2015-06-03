package com.narvis.test.dataaccess.models.lang;

import com.narvis.dataaccess.models.lang.IgnoredWord;
import com.narvis.dataaccess.models.lang.IgnoredWordsProvider;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zack
 */
public class RoutesProviderTest {
     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            IgnoredWordsProvider myIgnoredWordsProvider = new IgnoredWordsProvider();
            List<IgnoredWord> ignoredWords = myIgnoredWordsProvider.getIgnoredWords();
            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(RoutesProviderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

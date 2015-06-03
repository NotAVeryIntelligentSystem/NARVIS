package com.narvis.test.dataaccess.models.lang;

import com.narvis.dataaccess.interfaces.models.lang.IIgnoredWord;
import com.narvis.dataaccess.interfaces.models.lang.IIgnoredWordsProvider;
import com.narvis.dataaccess.interfaces.models.route.IRouteNode;
import com.narvis.dataaccess.interfaces.models.route.IRoutesProvider;
import com.narvis.dataaccess.interfaces.models.route.IWordNode;
import com.narvis.dataaccess.models.lang.IgnoredWordsProvider;
import com.narvis.dataaccess.models.route.RoutesProvider;
import com.narvis.dataaccess.models.route.WordNode;
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
            IIgnoredWordsProvider myIgnoredWordsProvider = new IgnoredWordsProvider();
            List<IIgnoredWord> ignoredWords = myIgnoredWordsProvider.getIgnoredWords();
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(RoutesProviderTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(RoutesProviderTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RoutesProviderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

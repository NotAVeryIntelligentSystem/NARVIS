/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.models.lang;

import com.narvis.dataaccess.models.route.RoutesProvider;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.simpleframework.xml.core.Persister;
import org.xml.sax.SAXException;

/**
 *
 * @author Zack
 */
public class IgnoredWordsProvider {
    private final static String ROUTESPATH = "src\\test\\java\\com\\narvis\\test\\dataaccess\\models\\lang\\ignoredwords.xml"; // Le chemin d'accès au fichier XML contenant les routes

    Persister persister;
    File file;
    
    public IgnoredWordsProvider() throws ParserConfigurationException, SAXException, IOException{
        persister = new Persister();
        file = new File(ROUTESPATH);
    }
    
    public List<IgnoredWord> getIgnoredWords() {
        List<IgnoredWord> ignoredWords = new LinkedList<>();
                
        try {
            RootIgnoredWords rootIgnoredWords = persister.read(RootIgnoredWords.class, file);
            ignoredWords = rootIgnoredWords.getIgnoredWords();
        } catch (Exception ex) {
            Logger.getLogger(RoutesProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ignoredWords;
    }

    public String getData(String... keywords) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.conf;

import com.narvis.dataaccess.interfaces.IDataProvider;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Implements the IDataProvider interface for API Keys.
 * It depends from the Models. The first parameters of the 
 * must be the name of the file containing the api.key
 * the other parameter must be the name of the XML tag containing the wanted api key
 * 
 * 
 * @author puma
 */
public class ApiKeyProvider implements IDataProvider{

    private final String CONF_FILE_LOCATION = "../../Conf/Models/";
    
    
    @Override
    public String getData(String... keyWords) {
    
        try {
            
            String confFileLocation = CONF_FILE_LOCATION + keyWords[0] + "/api.key";
            File apiKeyXml = new File(confFileLocation);
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(apiKeyXml);
                        
            
            NodeList nList = doc.getElementsByTagName(keyWords[1]);
            
            if( nList.getLength() > 0 )
            {
                return nList.item(0).getTextContent();
            }
            else
            {
                return null;
            }
            
            
        } catch ( SAXException | IOException | ParserConfigurationException ex) {
            return null;
        }
    }
    
  
    
    
    
    
}

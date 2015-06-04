/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.models.lang.word;

import com.narvis.common.tools.serialization.XmlFileAccess;
import com.narvis.dataaccess.impl.ModuleConfigurationDataProvider;
import com.narvis.dataaccess.interfaces.IDataModelProvider;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Zack
 */
public class DictionaryProvider implements IDataModelProvider<Dictionary> {
    
    private final Dictionary dictionary;
    private final ModuleConfigurationDataProvider conf;

    public DictionaryProvider(ModuleConfigurationDataProvider conf) throws ParserConfigurationException, SAXException, IOException, Exception{
        this.conf = conf;
        this.dictionary = XmlFileAccess.fromFile(Dictionary.class, this.getRoutesDataPath());
    }
    
    private String getRoutesDataPath() {
        return this.conf.getData("RoutesDataPath");
    }
    
    @Override
    public Dictionary getModel(String... keywords) {
        return this.dictionary;
    }

    @Override
    public void persist() {
        // NOPE NOPE NOPE
    }

    @Override
    public String getData(String... keywords) {
        return this.dictionary.toString();
    }
}

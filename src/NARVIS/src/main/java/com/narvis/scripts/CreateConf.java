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
package com.narvis.scripts;

import com.narvis.common.debug.NarvisLogger;
import com.narvis.common.tools.serialization.XmlFileAccess;
import com.narvis.common.generics.*;
import com.narvis.common.tools.serialization.XmlFileAccessException;
import com.narvis.dataaccess.impl.*;
import com.narvis.dataaccess.models.conf.*;
import com.narvis.dataaccess.models.lang.word.Dictionary;
import com.narvis.dataaccess.models.lang.word.Word;
import com.narvis.dataaccess.models.route.*;
import com.narvis.dataaccess.weather.*;
import com.narvis.frontend.console.AccessConsole;
import com.narvis.frontend.twitter.AccessTwitter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

/**
 *
 * @author uwy
 */
public class CreateConf {
    
    public static void main(String [] args) {
        
        try {
            System.out.println("Starting conf creation");
            File baseFolder = createFolder("../../release");
            File confFolder = createFolder(baseFolder, ConfigurationDataProvider.CONF_FOLDER_NAME);
            XmlFileAccess.toFile(createNarvisConf(), new File(confFolder, ConfigurationDataProvider.CONF_FILE_NAME));
            File modulesFolder = createFolder(baseFolder, ConfigurationDataProvider.MODULES_FOLDER_NAME);
            //createWeatherModuleFolder(modulesFolder);
            //createRoutesModuleFolder(modulesFolder);
            File frontendsFolder = createFolder(baseFolder, ConfigurationDataProvider.FRONTENDS_FOLDER_NAME);
            //createTwitterFrontEndFolder(frontendsFolder);
            createConsoleFrontEndFolder(frontendsFolder);
            createDictionaryModuleFolder(modulesFolder);
            //createTwitterFrontEndFolder(frontendsFolder); 
            System.out.println("Finished conf creation");
        } 
        catch (Exception ex) {
            NarvisLogger.getInstance().log(Level.SEVERE, null, ex);
        }

    }
    
    public static void createTwitterFrontEndFolder(File frontendsFolder) throws IOException, XmlFileAccessException {
        File twitterFolder = createFolder(frontendsFolder, "Twitter");
        File confModuleFolder = createFolder(twitterFolder, FrontEndConfigurationDataProvider.CONF_FOLDER_NAME);
        XmlFileAccess.toFile(createModuleConf(AccessTwitter.class.getCanonicalName()), new File(confModuleFolder, FrontEndConfigurationDataProvider.MODULE_CONF_FILE_NAME));
        XmlFileAccess.toFile(createApiKeys("Twitter", 
                new Pair("token", "askNakou"), 
                new Pair("tokenSecret", "askNakou"), 
                new Pair("consumerKey", "askNakou"), 
                new Pair("consumerSecret", "askNakou")), new File(confModuleFolder, FrontEndConfigurationDataProvider.API_KEY_FILE_NAME));

//etData("token"), this.conf.getApiKeys().getData("tokenSecret"), this.conf.getApiKeys().getData("consumerKey"), this.conf.getApiKeys().getData("consumerSecret")
    }
    
    public static void createConsoleFrontEndFolder(File frontendsFolder) throws IOException, XmlFileAccessException {
        File consoleFolder = createFolder(frontendsFolder, "Console");
        File confModuleFolder = createFolder(consoleFolder, FrontEndConfigurationDataProvider.CONF_FOLDER_NAME);
        XmlFileAccess.toFile(createModuleConf(AccessConsole.class.getCanonicalName()), new File(confModuleFolder, FrontEndConfigurationDataProvider.MODULE_CONF_FILE_NAME));
        XmlFileAccess.toFile(createApiKeys("Console"), new File(confModuleFolder, FrontEndConfigurationDataProvider.API_KEY_FILE_NAME));

//etData("token"), this.conf.getApiKeys().getData("tokenSecret"), this.conf.getApiKeys().getData("consumerKey"), this.conf.getApiKeys().getData("consumerSecret")
    }
    
    
    /* Routes */
    
    public static void createRoutesModuleFolder(File modulesFolder) throws Exception {
        File moduleFolder = createFolder(modulesFolder, "Routes");
        File confModuleFolder = createFolder(moduleFolder, ModuleConfigurationDataProvider.CONF_FOLDER_NAME);
        XmlFileAccess.toFile(createModuleConf(RoutesProvider.class.getCanonicalName(), new Pair<>("RoutesDataPath", "routes.xml")), new File(confModuleFolder, ModuleConfigurationDataProvider.MODULE_CONF_FILE_NAME));
        XmlFileAccess.toFile(createApiKeys("Routes"), new File(confModuleFolder, ModuleConfigurationDataProvider.API_KEY_FILE_NAME));
        File dataFolder = createFolder(moduleFolder, ModuleConfigurationDataProvider.DATA_FOLDER_NAME);
        XmlFileAccess.toFile(createRouteNode(), new File(dataFolder, "routes.xml"));
        createFolder(moduleFolder, ModuleConfigurationDataProvider.LAYOUTS_FOLDER_NAME);   

    }
    
    public static RouteNode createRouteNode() {
        RouteNode retVal = new RouteNode();
        retVal.addWord(
                createWordNode("give", 
                        createWordNode(null, 
                                createWordNode("weather",new ActionNode("weather")))));
        retVal.addWord(
                createWordNode("bring", 
                        createWordNode(null, 
                                createWordNode("weather",new ActionNode("weather")))));
        return retVal;    
    }
    
    public static WordNode createWordNode(String name, ActionNode... actions) {
        WordNode retVal = new WordNode(name);
        for(ActionNode action : actions) {
            retVal.addAction(action);
        }
        return retVal;
    }
    
    public static WordNode createWordNode(String name, WordNode... words) {
        WordNode retVal = new WordNode(name);
        for(WordNode word : words) {
            retVal.addWord(word);
        }
        return retVal;
    }
    
    
    /* Dictionary */
    
    public static void createDictionaryModuleFolder(File modulesFolder) throws Exception {
        File moduleFolder = createFolder(modulesFolder, "Dictionary");
        File confModuleFolder = createFolder(moduleFolder, ModuleConfigurationDataProvider.CONF_FOLDER_NAME);
        XmlFileAccess.toFile(createModuleConf(DictionaryProvider.class.getCanonicalName(), new Pair<>("DictionaryDataPath", "dictionary.xml")), new File(confModuleFolder, ModuleConfigurationDataProvider.MODULE_CONF_FILE_NAME));
        XmlFileAccess.toFile(createApiKeys("Dictionary"), new File(confModuleFolder, ModuleConfigurationDataProvider.API_KEY_FILE_NAME));
        File dataFolder = createFolder(moduleFolder, ModuleConfigurationDataProvider.DATA_FOLDER_NAME);
        XmlFileAccess.toFile(createDictionary(), new File(dataFolder, "dictionary.xml"));
        createFolder(moduleFolder, ModuleConfigurationDataProvider.LAYOUTS_FOLDER_NAME);   
    }
    
    public static Dictionary createDictionary() {
        Dictionary retVal = new Dictionary();
        
        String[] informationTypes = new String[1];
        String[] hints = new String[1];
        
        informationTypes[0] = "preposition";
        hints[0] = "location";        
        retVal.addWord(createWord("in", informationTypes, hints));
        
        informationTypes[0] = "location";
        retVal.addWord(createWord("london", informationTypes, null));

        return retVal;    
    }
    
    public static Word createWord(String name, String[] informationTypes, String[] hints) {
        Word retVal = new Word();
        
        retVal.setValue(name);
        
        for(String informationType : informationTypes) {
            retVal.addInformationType(informationType);
        }
        
        if(hints != null)
        {
            for(String hint : hints) {
                retVal.addHint(hint);
            }
        }
        
        return retVal;
    }
    
    
    
    public static void createWeatherModuleFolder(File modulesFolder) throws Exception {
        File moduleFolder = createFolder(modulesFolder, "OpenWeatherMap");
        File confModuleFolder = createFolder(moduleFolder, ModuleConfigurationDataProvider.CONF_FOLDER_NAME);
        XmlFileAccess.toFile(createModuleConf(OpenWeatherMapPortal.class.getCanonicalName()), new File(confModuleFolder, ModuleConfigurationDataProvider.MODULE_CONF_FILE_NAME));
        XmlFileAccess.toFile(createApiKeys("OpenWeatherMap", new Pair("key", "askNakou")), new File(confModuleFolder, ModuleConfigurationDataProvider.API_KEY_FILE_NAME));
        createFolder(moduleFolder, ModuleConfigurationDataProvider.DATA_FOLDER_NAME);
        createFolder(moduleFolder, ModuleConfigurationDataProvider.LAYOUTS_FOLDER_NAME);            
    }
                           
    public static File createFolder(String folderPath) throws IOException {
        File retVal = new File(folderPath);
        Files.createDirectories(retVal.toPath());
        return retVal;
    }
    
    public static File createFolder(File folder, String subfolderPath) throws IOException {
        File retVal = new File(folder, subfolderPath);
        Files.createDirectories(retVal.toPath());
        return retVal;
    }
    
    
    public static NarvisConf createNarvisConf() {
        NarvisConf retVal = new NarvisConf();
        return retVal;
    }
    
    public static ModuleConf createModuleConf(String moduleClassPath, Pair<String, String>... keyValueEntries ) {
        ModuleConf retVal = new ModuleConf();
        retVal.setModuleClassPath(moduleClassPath);
        for(Pair<String, String> entry : keyValueEntries) {
            retVal.getEntries().put(entry.item1, entry.item2);
        }
        return retVal;
    }
    
    public static ApiKeys createApiKeys(String name, Pair<String, String>... nameApiKeypairs) {
        ApiKeys retVal = new ApiKeys();
        retVal.setName(name);
        for(Pair<String, String> pair : nameApiKeypairs ) {
            retVal.getApiKeys().put(pair.item1, pair.item2);
        }
        return retVal;
    }
}

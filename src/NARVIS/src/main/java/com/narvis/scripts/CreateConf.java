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

import com.narvis.common.debug.*;
import com.narvis.common.tools.serialization.*;
import com.narvis.dataaccess.impl.*;
import com.narvis.dataaccess.models.conf.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 *
 * @author uwy
 */
public class CreateConf {
    public static final String ROOT_FOLDER = "../../release";

    public static void main(String[] args) throws Exception {

        try {
            System.out.println("Starting conf creation");
            
            File baseFolder = createFolder(ROOT_FOLDER);
            File confFolder = createFolder(baseFolder, ConfigurationDataProvider.CONF_FOLDER_NAME);
            XmlFileAccess.toFile(createNarvisConf(), new File(confFolder, ConfigurationDataProvider.CONF_FILE_NAME));
            // Modules
            File modulesFolder = createFolder(baseFolder, ConfigurationDataProvider.MODULES_FOLDER_NAME);
            createAnswersModuleFolder(modulesFolder);
            createStatusModuleFolder(modulesFolder);
            createWeatherModuleFolder(modulesFolder);
            createRoutesModuleFolder(modulesFolder);
            createDictionaryModuleFolder(modulesFolder);
            createUsersModuleFolder(modulesFolder);
            // Front ends
            File frontendsFolder = createFolder(baseFolder, ConfigurationDataProvider.FRONTENDS_FOLDER_NAME);
            createTwitterFrontEndFolder(frontendsFolder);
            createConsoleFrontEndFolder(frontendsFolder);
            
            System.out.println("Finished conf creation");
            
        } catch (IOException | XmlFileAccessException ex) {
            NarvisLogger.logException(ex);
        }
    }
    

    /* Twitter */
    public static void createTwitterFrontEndFolder(File frontendsFolder) throws IOException, XmlFileAccessException {
        TwitterConf myTwitterConf = new TwitterConf();
        
        File twitterFolder = createFolder(frontendsFolder, TwitterConf.MODULE_NAME);
        File confModuleFolder = createFolder(twitterFolder, FrontEndConfigurationDataProvider.CONF_FOLDER_NAME);
        File layoutFolder = createFolder(twitterFolder, FrontEndConfigurationDataProvider.LAYOUTS_FOLDER_NAME);
        
        XmlFileAccess.toFile(myTwitterConf.createErrorsLayout(), new File(layoutFolder, ModuleConfigurationDataProvider.ERRORS_FILE_NAME));
        XmlFileAccess.toFile(myTwitterConf.createModuleConf(), new File(confModuleFolder, FrontEndConfigurationDataProvider.MODULE_CONF_FILE_NAME));
        XmlFileAccess.toFile(myTwitterConf.createApiKeys(), new File(confModuleFolder, FrontEndConfigurationDataProvider.API_KEY_FILE_NAME));
    }

    /* Console */
    public static void createConsoleFrontEndFolder(File frontendsFolder) throws IOException, XmlFileAccessException {
        ConsoleConf myConsoleConf = new ConsoleConf();
        
        File consoleFolder = createFolder(frontendsFolder, ConsoleConf.MODULE_NAME);
        
        File confModuleFolder = createFolder(consoleFolder, FrontEndConfigurationDataProvider.CONF_FOLDER_NAME);
        File layoutFolder = createFolder(consoleFolder, FrontEndConfigurationDataProvider.LAYOUTS_FOLDER_NAME);
        
        XmlFileAccess.toFile(myConsoleConf.createErrorsLayout(), new File(layoutFolder, ModuleConfigurationDataProvider.ERRORS_FILE_NAME));
        XmlFileAccess.toFile(myConsoleConf.createModuleConf(), new File(confModuleFolder, FrontEndConfigurationDataProvider.MODULE_CONF_FILE_NAME));
        XmlFileAccess.toFile(myConsoleConf.createApiKeys(), new File(confModuleFolder, FrontEndConfigurationDataProvider.API_KEY_FILE_NAME));
    }
    
    public static void createUsersModuleFolder(File modulesFolder) throws IOException, XmlFileAccessException {
        UsersConf myUsersConf = new UsersConf();
        File moduleFolder = createFolder(modulesFolder, UsersConf.MODULE_NAME);
        File confModuleFolder = createFolder(moduleFolder, ModuleConfigurationDataProvider.CONF_FOLDER_NAME);
        File layoutFolder = createFolder(moduleFolder, ModuleConfigurationDataProvider.LAYOUTS_FOLDER_NAME);
        File dataFolder = createFolder(moduleFolder, ModuleConfigurationDataProvider.DATA_FOLDER_NAME);
        
        XmlFileAccess.toFile(myUsersConf.createErrorsLayout(), new File(layoutFolder, ModuleConfigurationDataProvider.ERRORS_FILE_NAME));
        XmlFileAccess.toFile(myUsersConf.createModuleConf(), new File(confModuleFolder, ModuleConfigurationDataProvider.MODULE_CONF_FILE_NAME));
        XmlFileAccess.toFile(myUsersConf.createApiKeys(), new File(confModuleFolder, ModuleConfigurationDataProvider.API_KEY_FILE_NAME));
        XmlFileAccess.toFile(myUsersConf.createUsersData(), new File(dataFolder, UsersConf.USERS_DATA_PATH));

    }


    /* Answers */
    public static void createAnswersModuleFolder(File modulesFolder) throws IOException, XmlFileAccessException  {
        AnswersConf myAnswersConf = new AnswersConf();
        
        File moduleFolder = createFolder(modulesFolder, AnswersConf.MODULE_NAME);
        
        File confModuleFolder = createFolder(moduleFolder, ModuleConfigurationDataProvider.CONF_FOLDER_NAME);
        File layoutFolder = createFolder(moduleFolder, ModuleConfigurationDataProvider.LAYOUTS_FOLDER_NAME);
        File dataFolder = createFolder(moduleFolder, ModuleConfigurationDataProvider.DATA_FOLDER_NAME);
        
        XmlFileAccess.toFile(myAnswersConf.createErrorsLayout(), new File(layoutFolder, ModuleConfigurationDataProvider.ERRORS_FILE_NAME));
        XmlFileAccess.toFile(myAnswersConf.createAnswersLayout(), new File(layoutFolder, ModuleConfigurationDataProvider.ANSWERS_FILE_NAME));
        XmlFileAccess.toFile(myAnswersConf.createModuleConf(), new File(confModuleFolder, ModuleConfigurationDataProvider.MODULE_CONF_FILE_NAME));
        XmlFileAccess.toFile(myAnswersConf.createApiKeys(), new File(confModuleFolder, ModuleConfigurationDataProvider.API_KEY_FILE_NAME));
    }
    
    /* Status */
    public static void createStatusModuleFolder(File modulesFolder) throws Exception {
        HardwareStatusConf myHardwareStatusConf = new HardwareStatusConf();
        
        File moduleFolder = createFolder(modulesFolder, myHardwareStatusConf.MODULE_NAME);
        
        File confModuleFolder = createFolder(moduleFolder, ModuleConfigurationDataProvider.CONF_FOLDER_NAME);
        File layoutFolder = createFolder(moduleFolder, ModuleConfigurationDataProvider.LAYOUTS_FOLDER_NAME);
        File dataFolder = createFolder(moduleFolder, ModuleConfigurationDataProvider.DATA_FOLDER_NAME);
        
        XmlFileAccess.toFile(myHardwareStatusConf.createErrorsLayout(), new File(layoutFolder, ModuleConfigurationDataProvider.ERRORS_FILE_NAME));
        XmlFileAccess.toFile(myHardwareStatusConf.createAnswersLayout(), new File(layoutFolder, ModuleConfigurationDataProvider.ANSWERS_FILE_NAME));
        XmlFileAccess.toFile(myHardwareStatusConf.createModuleConf(), new File(confModuleFolder, ModuleConfigurationDataProvider.MODULE_CONF_FILE_NAME));
        XmlFileAccess.toFile(myHardwareStatusConf.createApiKeys(), new File(confModuleFolder, ModuleConfigurationDataProvider.API_KEY_FILE_NAME));
    }
    
    /* Routes */
    public static void createRoutesModuleFolder(File modulesFolder) throws IOException, XmlFileAccessException  {
        RoutesConf myRoutesConf = new RoutesConf();
        
        File moduleFolder = createFolder(modulesFolder, RoutesConf.MODULE_NAME);
        
        File confModuleFolder = createFolder(moduleFolder, ModuleConfigurationDataProvider.CONF_FOLDER_NAME);
        File layoutFolder = createFolder(moduleFolder, ModuleConfigurationDataProvider.LAYOUTS_FOLDER_NAME);
        File dataFolder = createFolder(moduleFolder, ModuleConfigurationDataProvider.DATA_FOLDER_NAME);
        
        XmlFileAccess.toFile(myRoutesConf.createErrorsLayout(), new File(layoutFolder, ModuleConfigurationDataProvider.ERRORS_FILE_NAME));
        XmlFileAccess.toFile(myRoutesConf.createModuleConf(), new File(confModuleFolder, ModuleConfigurationDataProvider.MODULE_CONF_FILE_NAME));
        XmlFileAccess.toFile(myRoutesConf.createApiKeys(), new File(confModuleFolder, ModuleConfigurationDataProvider.API_KEY_FILE_NAME));
        XmlFileAccess.toFile(myRoutesConf.createRouteNode(), new File(dataFolder, RoutesConf.ROUTES_DATA_PATH));
    }

    /* Dictionary */
    public static void createDictionaryModuleFolder(File modulesFolder) throws IOException, XmlFileAccessException  {
        DictionaryConf myDictionaryConf = new DictionaryConf();
        
        File moduleFolder       = createFolder(modulesFolder, DictionaryConf.MODULE_NAME);
        
        File confModuleFolder   = createFolder(moduleFolder, ModuleConfigurationDataProvider.CONF_FOLDER_NAME);
        File layoutFolder       = createFolder(moduleFolder, ModuleConfigurationDataProvider.LAYOUTS_FOLDER_NAME);
        File dataFolder         = createFolder(moduleFolder, ModuleConfigurationDataProvider.DATA_FOLDER_NAME);
        
        XmlFileAccess.toFile(myDictionaryConf.createErrorsLayout(), new File(layoutFolder, ModuleConfigurationDataProvider.ERRORS_FILE_NAME));
        XmlFileAccess.toFile(myDictionaryConf.createModuleConf(), new File(confModuleFolder, ModuleConfigurationDataProvider.MODULE_CONF_FILE_NAME));
        XmlFileAccess.toFile(myDictionaryConf.createApiKeys(), new File(confModuleFolder, ModuleConfigurationDataProvider.API_KEY_FILE_NAME));
        XmlFileAccess.toFile(myDictionaryConf.createDictionary(), new File(dataFolder, DictionaryConf.DICTIONARY_DATA_PATH));
    }

    /* Weather */
    public static void createWeatherModuleFolder(File modulesFolder) throws IOException, XmlFileAccessException  {
        WeatherConf myWeatherConf = new WeatherConf();
        
        File moduleFolder       = createFolder(modulesFolder, WeatherConf.MODULE_NAME);
        
        File confModuleFolder   = createFolder(moduleFolder, ModuleConfigurationDataProvider.CONF_FOLDER_NAME);
        File layoutFolder       = createFolder(moduleFolder, ModuleConfigurationDataProvider.LAYOUTS_FOLDER_NAME);
        File dataFolder         = createFolder(moduleFolder, ModuleConfigurationDataProvider.DATA_FOLDER_NAME);
        
        XmlFileAccess.toFile(myWeatherConf.createErrorsLayout(), new File(layoutFolder, ModuleConfigurationDataProvider.ERRORS_FILE_NAME));
        XmlFileAccess.toFile(myWeatherConf.createAnswerLayout(), new File(layoutFolder, ModuleConfigurationDataProvider.ANSWERS_FILE_NAME));
        XmlFileAccess.toFile(myWeatherConf.createModuleConf(), new File(confModuleFolder, ModuleConfigurationDataProvider.MODULE_CONF_FILE_NAME));
        XmlFileAccess.toFile(myWeatherConf.createApiKeys(), new File(confModuleFolder, ModuleConfigurationDataProvider.API_KEY_FILE_NAME));
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
}

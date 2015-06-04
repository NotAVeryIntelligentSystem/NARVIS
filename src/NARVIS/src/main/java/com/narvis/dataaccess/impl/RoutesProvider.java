/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.impl;

import com.narvis.common.tools.serialization.XmlFileAccess;
import com.narvis.common.generics.NarvisLogger;
import com.narvis.dataaccess.interfaces.IDataModelProvider;
import com.narvis.dataaccess.models.route.RouteNode;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Zack
 */
public class RoutesProvider implements IDataModelProvider<RouteNode> {
    //private final static String ROUTESPATH = "src\\test\\java\\com\\narvis\\test\\dataaccess\\models\\route\\routes.xml"; // Le chemin d'accès au fichier XML contenant les routes
    private final RouteNode routes;
    private final ModuleConfigurationDataProvider conf;
    
    public RoutesProvider(ModuleConfigurationDataProvider conf) throws ParserConfigurationException, SAXException, IOException, Exception{
        this.conf = conf;
        this.routes = XmlFileAccess.fromFile(RouteNode.class, new File(this.conf.getDataFolder(), this.getRoutesDataPath()));
        
    }
    
    private String getRoutesDataPath() {
        return this.conf.getData("RoutesDataPath");
    }

    @Override
    public void persist() {
        try {
            XmlFileAccess.toFile(this.routes, new File(this.conf.getDataFolder(), this.getRoutesDataPath()));
        } catch (Exception ex) {
            NarvisLogger.getInstance().log(Level.SEVERE, ex.toString());
        }
    }

    @Override
    public String getData(String... keywords) {
        return this.routes.toString();
    }

    @Override
    public RouteNode getModel(String... keywords) {
        return this.routes;
    }

}

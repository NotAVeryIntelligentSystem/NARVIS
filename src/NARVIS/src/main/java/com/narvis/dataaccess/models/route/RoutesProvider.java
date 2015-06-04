/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.models.route;

import com.narvis.common.functions.serialization.XmlSerializer;
import com.narvis.common.generics.NarvisLogger;
import com.narvis.dataaccess.impl.ModuleConfigurationDataProvider;
import com.narvis.dataaccess.interfaces.IDataModelProvider;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import javax.xml.parsers.ParserConfigurationException;
import org.simpleframework.xml.Root;
import org.xml.sax.SAXException;

/**
 *
 * @author Zack
 */
@Root(name = "RoutesProvider")
public class RoutesProvider implements IDataModelProvider<RouteNode> {
    //private final static String ROUTESPATH = "src\\test\\java\\com\\narvis\\test\\dataaccess\\models\\route\\routes.xml"; // Le chemin d'accès au fichier XML contenant les routes
    private final RouteNode routes;
    private final ModuleConfigurationDataProvider conf;
    
    public RoutesProvider(ModuleConfigurationDataProvider conf) throws ParserConfigurationException, SAXException, IOException, Exception{
        this.conf = conf;
        this.routes = XmlSerializer.fromFile(RouteNode.class, this.getRoutesDataPath());
        
    }
    
    private String getRoutesDataPath() {
        return this.conf.getData("RoutesDataPath");
    }

    @Override
    public void persist() {
        try {
            XmlSerializer.toFile(this.routes, this.getRoutesDataPath());
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

    @Override
    public String getData(Map<String, String> details, String... keywords) {
        throw new UnsupportedOperationException("Not supported by this class"); //To change body of generated methods, choose Tools | Templates.
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.models.route;

import com.narvis.dataaccess.interfaces.models.route.IRouteNode;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import com.narvis.dataaccess.interfaces.models.route.IRoutesProvider;
import org.simpleframework.xml.core.Persister;
import org.xml.sax.SAXException;

/**
 *
 * @author Zack
 */
public class RoutesProvider implements IRoutesProvider {
    private final static String ROUTESPATH = "src\\test\\java\\com\\narvis\\test\\dataaccess\\models\\route\\routes.xml"; // Le chemin d'accès au fichier XML contenant les routes

    Persister persister;
    File file;
    
    private IRouteNode route;
    
    public RoutesProvider() throws ParserConfigurationException, SAXException, IOException{
        persister = new Persister();
        file = new File(ROUTESPATH);
        
        route = new RouteNode();
    }
    
    @Override
    public IRouteNode getRouteNode() {
        try {
            RouteNode routeNode = persister.read(RouteNode.class, file);
            
            route = routeNode;
        } catch (Exception ex) {
            Logger.getLogger(RoutesProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return route;
    }
    
    @Override
    public void setRouteNode(IRouteNode route)
    {
        this.route = route;
    }

    @Override
    public void persist() {
        try {
            persister.write(route, file);
        } catch (Exception ex) {
            Logger.getLogger(RoutesProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Object getModel(String... keywords) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getData(String... keywords) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

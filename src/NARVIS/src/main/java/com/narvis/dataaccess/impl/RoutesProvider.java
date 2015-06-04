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
package com.narvis.dataaccess.impl;

import com.narvis.common.tools.serialization.XmlFileAccess;
import com.narvis.common.generics.NarvisLogger;
import com.narvis.dataaccess.interfaces.IDataModelProvider;
import com.narvis.dataaccess.models.route.RouteNode;
import java.io.File;
import java.io.IOException;
import java.util.Map;
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

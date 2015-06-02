/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.conf;

import com.narvis.dataaccess.interfaces.IDataProvider;
import java.io.File;
import java.util.Locale;

/**
 * Implements the IDataProvider interface for API Keys
 * The supported KeyWords are 
 * WeatherApi
 * 
 * 
 * @author puma
 */
public class ApiKeyProvider implements IDataProvider{

    @Override
    public String getData(String... keyWords) {
    
        String keyword = keyWords[0].toLowerCase(Locale.FRENCH);
        
        
        switch( keyword )
        {
            case "weatherapi":
                return getWeatherApiKey();
            default:
                return "null";
        }
    }
    
    /**
     * Return the API stored in the conf file stored in the root of narvis
     * @return 
     */
    private String getWeatherApiKey()
    {
        
    }
    
    
    
    
}

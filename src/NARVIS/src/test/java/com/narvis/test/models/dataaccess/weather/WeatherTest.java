/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.test.models.dataaccess.weather;


import com.narvis.dataaccess.impl.ModuleConfigurationDataProvider;
import com.narvis.dataaccess.models.conf.ApiKeys;
import com.narvis.dataaccess.weather.OpenWeatherMapPortal;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


import org.junit.Test;
/**
 *
 * @author puma
 */
public class WeatherTest {
    
    public WeatherTest() {
    }

    
    
    
    @Test( expected = UnsupportedOperationException.class)
    public void testCallUnsupportedGetData() {
        
        ApiKeys api = new ApiKeys();
        OpenWeatherMapPortal weatherPortal = new OpenWeatherMapPortal(api);
        
        weatherPortal.getData("weather", "city");
    }

    
    @Test 
    public void testGetData() throws Exception {
        
        
        //Change this
        ModuleConfigurationDataProvider conf = new ModuleConfigurationDataProvider(new File("../../modules/Weather/"));
        OpenWeatherMapPortal weatherPortal = new OpenWeatherMapPortal(conf);
        
        Map<String,String> details = new HashMap<>();
        
        details.put("city", "nimes");
        
        String result = weatherPortal.getDataDetails(details, "weather");
        
        
    }
    

}

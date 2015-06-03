/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.test.models.dataaccess.weather;


import com.narvis.common.functions.serialization.XmlSerializer;
import com.narvis.dataaccess.models.conf.ApiKeys;
import com.narvis.dataaccess.weather.OpenWeatherMapPortal;
import org.junit.Test;
/**
 *
 * @author puma
 */
public class WeatherTest {
    
    public WeatherTest() {
    }

    
    @Test
    public void GetInfoMeteo() throws Exception
    {
        String apiKeysFilePath = "path_to_file";
        ApiKeys api = XmlSerializer.fromFile(ApiKeys.class, apiKeysFilePath);
        OpenWeatherMapPortal weatherPortal = new OpenWeatherMapPortal(api);
        //String resultat = weatherPortal.getData("fghgfhfghfghfghfgn", "temperature");
        //String resString = weatherPortal.getData("Nimes", "fdfngidf");
        String resString1 = weatherPortal.getData("Nimes", "temperature", "cloud");
        

    }


}

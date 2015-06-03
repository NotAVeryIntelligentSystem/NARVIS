/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.test.models.dataaccess.weather;


import com.narvis.common.functions.serialization.XmlSerializer;
import com.narvis.dataaccess.models.conf.ApiKeys;
import com.narvis.dataaccess.weather.OpenWeatherMapPortal;
import java.io.File;


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
        try {
            String apiKeysFilePath = "../../conf/modules/Weather/conf/api.key";

            File f = new File(apiKeysFilePath);
            ApiKeys api = XmlSerializer.fromFile(ApiKeys.class, f);

            OpenWeatherMapPortal weatherPortal = new OpenWeatherMapPortal(api);
            String resString1 = weatherPortal.getData("Nimes", "temperature", "cloud");
            
            
        }catch (Exception e ) {
            
            System.out.println(e);
            
        }
        

    }


}

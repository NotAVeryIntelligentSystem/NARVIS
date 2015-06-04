/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.test.models.dataaccess.weather;


import com.narvis.common.tools.serialization.XmlFileAccess;
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
        String apiKeysFilePath = "../../tests/weather/api_weather.key";

        File f = new File(apiKeysFilePath).getAbsoluteFile();
        ApiKeys api = XmlFileAccess.fromFile(ApiKeys.class, f);

    }


}

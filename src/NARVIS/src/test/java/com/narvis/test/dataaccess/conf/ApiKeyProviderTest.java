/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.test.dataaccess.conf;

import com.narvis.dataaccess.conf.ApiKeyProvider;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author puma
 */
public class ApiKeyProviderTest {
    
    public ApiKeyProviderTest() {
    }

    
    @Test
    public void TestFile()
    {
        ApiKeyProvider provider = new ApiKeyProvider();
        
        String apiKey = provider.getData("WeatherApi","OpenWeatherMap");
        assertEquals("01b5f54b9605d5bbae6cf9f831560fb5", apiKey);
    }
}

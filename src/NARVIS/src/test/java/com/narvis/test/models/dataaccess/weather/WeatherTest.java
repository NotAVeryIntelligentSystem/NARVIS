/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.test.models.dataaccess.weather;

import com.narvis.models.dataaccess.weather.OpenWeatherMapPortal;

import org.junit.Test;
/**
 *
 * @author puma
 */
public class WeatherTest {
    
    public WeatherTest() {
    }

    
    @Test
    public void GetInfoMeteo()
    {
        OpenWeatherMapPortal weatherPortal = new OpenWeatherMapPortal();
        //String resultat = weatherPortal.getData("fghgfhfghfghfghfgn", "temperature");
        //String resString = weatherPortal.getData("Nimes", "fdfngidf");
        String resString1 = weatherPortal.getData("Nimes", "temperature", "cloud");
        

    }


}

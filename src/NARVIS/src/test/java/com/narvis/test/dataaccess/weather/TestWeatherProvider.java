/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.test.dataaccess.weather;

import com.narvis.dataaccess.exception.NoDataException;
import com.narvis.dataaccess.exception.NoValueException;
import com.narvis.dataaccess.exception.ProviderException;
import com.narvis.dataaccess.impl.ModuleConfigurationDataProvider;
import com.narvis.dataaccess.models.conf.ApiKeys;
import com.narvis.dataaccess.weather.OpenWeatherMapPortal;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.junit.Assert;

import org.junit.Test;

/**
 *
 * @author puma
 */
public class TestWeatherProvider {

    public TestWeatherProvider() {
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCallUnsupportedGetData() {

        ApiKeys api = new ApiKeys();
        OpenWeatherMapPortal weatherPortal = new OpenWeatherMapPortal(api);

        weatherPortal.getData("weather", "city");
    }

    @Test(expected = NoValueException.class)
    public void testGetDataWithShittyData() throws Exception {

        ModuleConfigurationDataProvider conf = new ModuleConfigurationDataProvider(new File("../../tests/weather"));
        OpenWeatherMapPortal weatherPortal = new OpenWeatherMapPortal(conf);

        Map<String, String> details = new HashMap<>();

        details.put("location", "fdhdfhgf");

        String result = weatherPortal.getDataDetails(details, "");

        Assert.assertEquals("Sorry guy I can't help you", result);

    }

    @Test
    public void testGetDataWithDefaultCommand() throws Exception {

        ModuleConfigurationDataProvider conf = new ModuleConfigurationDataProvider(new File("../../tests/weather"));
        OpenWeatherMapPortal weatherPortal = new OpenWeatherMapPortal(conf);

        Map<String, String> details = new HashMap<>();
        details.put("location", "nimes");

        String result = weatherPortal.getDataDetails(details, "");

        Pattern p = Pattern.compile("The temperature in (([A-Z]*)|([a-z]*))* is ([0-9]*\\.[0-9]*)°C and the cloud percentage is ([0-9]*\\.[0-9])%");

        Assert.assertTrue(result.matches(p.pattern()));

    }

    @Test(expected = NoValueException.class)
    public void testGetDataWithShittyCommand() throws Exception {

        ModuleConfigurationDataProvider conf = new ModuleConfigurationDataProvider(new File("../../tests/weather"));
        OpenWeatherMapPortal weatherPortal = new OpenWeatherMapPortal(conf);

        Map<String, String> details = new HashMap<>();
        details.put("location", "nimes");

        String result = weatherPortal.getDataDetails(details, "gfdghdfhg");

        Assert.assertEquals("Sorry guy I can't help you", result);
    }

    @Test( expected = NoValueException.class )
    public void testWithNewZealandCity() throws ProviderException {
        
        ModuleConfigurationDataProvider conf = new ModuleConfigurationDataProvider(new File("../../tests/weather"));
        OpenWeatherMapPortal weatherPortal = new OpenWeatherMapPortal(conf);

        Map<String, String> details = new HashMap<>();
        details.put("Taumatawhakatangihangakoauauotamateaturipukakapikimaungahoronukupokaiwhenuakitanatahu", "location");
        
        String result = weatherPortal.getDataDetails(details, "");

        Pattern p = Pattern.compile("The temperature in (([A-Z]*)|([a-z]*))* is ([0-9]*\\.[0-9]*)°C and the cloud percentage is ([0-9]*\\.[0-9])%");
        
        
        Assert.assertTrue(result.matches(p.pattern()));
        
    }
}

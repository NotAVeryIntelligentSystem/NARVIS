/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.models.dataaccess.weather;

import java.io.IOException;
import java.util.NoSuchElementException;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import org.json.JSONException;


/**
 *
 * @author puma
 */
public class OpenWeatherMapPortal implements IWeather {

    private CurrentWeather _currentWeather;
    
    public OpenWeatherMapPortal() {
        
        
    }

    
    @Override
    public String getInfoMeteo(String city) {
        
        try {
            OpenWeatherMap owm = new OpenWeatherMap("01b5f54b9605d5bbae6cf9f831560fb5");
            _currentWeather = owm.currentWeatherByCityName(city);
            
            float celsiusTemperature = getTemperatureInCelsius();
            float percentageOfClouds = GetPercentageOfCloud();
            
            return buildResponse(city,celsiusTemperature, percentageOfClouds);
       
            
        } catch (IOException | JSONException | NoSuchElementException  ex ) {
            return "I can't find the meteo sorry guy !";
        }
        
        
    }
    
    /**
     * Return the temperature in the city in celsius 
     * @return 
     */
    private float getTemperatureInCelsius() {
     
        //Early out
        if( this._currentWeather == null || !this._currentWeather.hasMainInstance()  ||  !this._currentWeather.getMainInstance().hasMaxTemperature() )
            throw new NoSuchElementException("Can not find temperature");
        
        float farenheitTemperature = this._currentWeather.getMainInstance().getTemperature();
        return (farenheitTemperature - 32.0f)* 5.0f/9.0f;
    }
    
    /**
     * Return the percentage of clouds 
     * @return 
     */
    private float GetPercentageOfCloud() {
        
        //Early out
        if( this._currentWeather == null || !this._currentWeather.hasCloudsInstance() || !this._currentWeather.getCloudsInstance().hasPercentageOfClouds() )
            throw new NoSuchElementException("Can not find clouds percentage");
        
        return this._currentWeather.getCloudsInstance().getPercentageOfClouds();
        
    }
    
    /**
     * Build the responsse using the data 
     * @return 
     */
    private String buildResponse(String city, float celsiusTemperature, float percentageOfClouds) {
        
        return "The temperature in "+ city + " is " + celsiusTemperature + "°C" + " The clouds percentage is " + percentageOfClouds + "%";
        
    }
    
    
    
}

    
    
    
   
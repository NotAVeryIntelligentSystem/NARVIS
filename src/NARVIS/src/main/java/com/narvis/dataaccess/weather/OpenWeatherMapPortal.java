/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.weather;

import com.narvis.common.tools.serialization.XmlFileAccess;
import com.narvis.dataaccess.impl.ModuleConfigurationDataProvider;
import com.narvis.dataaccess.interfaces.IDataProvider;
import com.narvis.dataaccess.models.conf.ApiKeys;
import com.narvis.dataaccess.models.layouts.weather.WeatherAnswers;
import com.narvis.dataaccess.weather.annotations.Command;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.NoSuchElementException;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import org.json.JSONException;


/**
 *
 * Get Data implementation for the OpenWeatherMapPortal
 * The first Parameter must be the name of the city the other parameters could be (Not case sensitive)
 * @author puma
 */
public class OpenWeatherMapPortal implements IDataProvider {

    private final ApiKeys weatherApiKeys;
    private CurrentWeather _currentWeather;
    private String _answer = "";
    
    private final String ANSWER_FILE_LOCATION = "";
    private final String KEY_FOLDER = "WeatherApi";
    private final String KEY_TAG = "OpenWeatherMap";
    
    public OpenWeatherMapPortal(ApiKeys api) {
        this.weatherApiKeys = api;
    }
    
    public OpenWeatherMapPortal(ModuleConfigurationDataProvider moduleConf) {
        this.weatherApiKeys = moduleConf.getApiKeys();
        
    }

    @Override
    public String getData(String... keyWords) {
        
        try {
            
            String key = this.weatherApiKeys.getData(KEY_TAG);
            
            if( key == null )
                return null;

            OpenWeatherMap owm = new OpenWeatherMap(key);
            this._currentWeather = owm.currentWeatherByCityName(keyWords[0]);

            //For each command call the method and format the answer
            for( int i = 1; i < keyWords.length; i++ ) {
                String result = CallMethodByCommand(keyWords[i].toLowerCase(Locale.FRENCH));
                AppendToAnswer(keyWords[i], result);
            }

            return this._answer;

       
            
        } catch (IOException | JSONException | NoSuchElementException  ex ) {
            return "I can't find the meteo sorry guy !";
        }
        
        
    }
    
    /**
     * Return the temperature in the city in celsius 
     * @return 
     */
    @Command(CommandName = "temperature")
    public String getTemperatureInCelsius() {
     
        //Early out
        if( this._currentWeather == null || !this._currentWeather.hasMainInstance()  ||  !this._currentWeather.getMainInstance().hasMaxTemperature() )
            throw new NoSuchElementException("Can not find temperature");
        
        float farenheitTemperature = this._currentWeather.getMainInstance().getTemperature();
        float celsiusTemp = (farenheitTemperature - 32.0f)* 5.0f/9.0f;
        return Float.toString(celsiusTemp);
    }
    
    /**
     * Return the percentage of clouds 
     * @return 
     */
    @Command(CommandName = "cloud")
    public String GetPercentageOfCloud() {
        
        //Early out
        if( this._currentWeather == null || !this._currentWeather.hasCloudsInstance() || !this._currentWeather.getCloudsInstance().hasPercentageOfClouds() )
            throw new NoSuchElementException("Can not find clouds percentage");
        
        float cloudPercentage = this._currentWeather.getCloudsInstance().getPercentageOfClouds();
        
        return Float.toString(cloudPercentage);
        
    }

    
    /**
     * Call the method wich provide the answer for the given method
     * @param command
     * @return the return value of the called method
     */
    private String CallMethodByCommand(String command) {
        
        if( command == null )
            return null;
        
        Method[] methods = this.getClass().getMethods();

        
        //Heizenberg is behind you...
        for( Method meth : methods ) {
            
            Command[] comAnnotations = meth.getAnnotationsByType(Command.class);
            for( Command comAnnot : comAnnotations ) {
                if(comAnnot.CommandName().equals(command) ) {
                    try {
                        //Method found we call it
                        return (String)meth.invoke(this);
                        
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        //The user came to the wrong methodhood, we quit
                        return null;
                    }
                }
            }
            
                    
        }
        
        return null;
        
    }
    
    /**
     * The answer builder, add to the answer the result to the command
     */
    private void AppendToAnswer(String command, String result){
        
        try {
            File f = new File(this.ANSWER_FILE_LOCATION);
            WeatherAnswers answer = XmlFileAccess.fromFile(WeatherAnswers.class, f);
            
            
            String finalAnswer = answer.getData(command);
            
            this._answer += String.format(finalAnswer, result);
            this._answer += "\n";
        } catch (Exception ex) {
            
            this._answer += "Can not get answer\n";
        }
        
        
    }    
}

    
    
    
   
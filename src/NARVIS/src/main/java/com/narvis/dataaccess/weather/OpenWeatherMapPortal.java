/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.weather;

import com.narvis.dataaccess.impl.AnswerBuilder;
import com.narvis.dataaccess.impl.ModuleConfigurationDataProvider;
import com.narvis.dataaccess.interfaces.IAnswerProvider;
import com.narvis.dataaccess.interfaces.IAnswserBuilder;
import com.narvis.dataaccess.interfaces.IDataProviderDetails;
import com.narvis.dataaccess.models.conf.ApiKeys;
import com.narvis.dataaccess.weather.annotations.Command;
import com.narvis.dataaccess.conf.exception.CanNotFindValueForParamException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import org.json.JSONException;


/**
 *
 * Get Data implementation for the OpenWeatherMapPortal
 * The first Parameter must be the name of the city the other parameters could be (Not case sensitive)*
 * The supported command are :
 * Empty string : Return Temperature and cloud percentage
 * Temperature : Return temperature in celsius
 * Cloud : return cloud percentage
 * 
 * 
 * 
 * @author puma
 */
public class OpenWeatherMapPortal implements IDataProviderDetails, IAnswerProvider {

    private final ApiKeys weatherApiKeys;
    private CurrentWeather _currentWeather;
    private ModuleConfigurationDataProvider _confProvider;
    private Map<String,String> _tempDetails;
    
    private final String DEFAULT_ANSWER = "weather";
    private final String ERROR_ANSWER = "error";

    private final String KEY_TAG = "OpenWeatherMap";
    
    public OpenWeatherMapPortal(ApiKeys api) {
        this.weatherApiKeys = api;
    }
    
    public OpenWeatherMapPortal(ModuleConfigurationDataProvider moduleConf) {
        this.weatherApiKeys = moduleConf.getApiKeys();
        this._confProvider = moduleConf;
        
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
     * Get weather data
     * @param detailsToValue The details : City, Date...
     * @param keywords the command which represent the data asked, Only the first parameters will be used
     * @return 
     */
    @Override
    public String getDataDetails(Map<String, String> detailsToValue, String... keywords) {


        try {
            
            //Problem we quit
            if( keywords.length < 1 || !detailsToValue.containsKey("city") || this._confProvider == null )
                return null;
            
            String key = this.weatherApiKeys.getData(KEY_TAG);
            
            if( key == null )
                return null;
            
            //If the user asked for nothing special, we pick the default answer
            if( keywords[0].isEmpty() ) {
                keywords[0] = DEFAULT_ANSWER;
            }
            
            this._tempDetails = detailsToValue;


            OpenWeatherMap owm = new OpenWeatherMap(key);
            this._currentWeather = owm.currentWeatherByCityName(this._tempDetails.get("city"));
            
            IAnswserBuilder answerBuilder = new AnswerBuilder();
            String answerFromXml = answerBuilder.readAnswerForCommand(this._confProvider, keywords[0] );
            List<String> listOfParams = answerBuilder.getListOfRequiredParams(answerFromXml);
            Map<String,String> paramsToValues = buildParamsToValueMap(listOfParams);
            
            return answerBuilder.buildAnswer(paramsToValues, answerFromXml);
            
            //return buildResponse(city,celsiusTemperature, percentageOfClouds);
       
            
        } catch ( CanNotFindValueForParamException | IOException | JSONException | NoSuchElementException  ex ) {
            
            //Because it failed we return the default error message
            //The builder don't throw any exception
            AnswerBuilder builder = new AnswerBuilder();
            String errorMessage = builder.buildAnswer(new HashMap<String, String>(), ERROR_ANSWER);
            
            return errorMessage;
            
        } 

    
    }

    @Override
    public Map<String, String> buildParamsToValueMap(List<String> listOfParams) throws CanNotFindValueForParamException {
     
        Map<String, String> paramsToValue = new HashMap<>();
        
        //To gain time we get all the details and their value
        paramsToValue.putAll(this._tempDetails);
        
        for( String param : listOfParams ) {
            
            //Make sur we don't already have the value of the params
            if( !paramsToValue.containsKey( param ) ) {
                
                param = removeBracketFromParam(param);
                String value = CallMethodByCommand(param);
                if( value == null ) {
                    //We don't have the answer whatever the reason we use the default answer
                    throw new CanNotFindValueForParamException(param);
                    
                } else {
                    
                    paramsToValue.put(param, value);
                    
                }
            }
            
        }
        
        return paramsToValue;
    }
    
    
    @Override
    public String getData(String... keywords) {
        throw new UnsupportedOperationException("Not supported"); 
    }
    
    /**
     * Remove the bracket from the param if it exist
     * @param paramName the name of the param
     * @return return the param name without bracket
     */
    private String removeBracketFromParam(String paramName) {
        
        paramName = paramName.replace("[", "");
        paramName = paramName.replace("]", "");
        
        return paramName;
    }

 
}
    
    


    
    
    
   
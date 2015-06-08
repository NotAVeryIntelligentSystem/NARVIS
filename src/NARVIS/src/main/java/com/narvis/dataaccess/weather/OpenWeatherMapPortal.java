/*
 * The MIT License
 *
 * Copyright 2015 puma.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.narvis.dataaccess.weather;

import com.narvis.common.debug.NarvisLogger;
import com.narvis.dataaccess.exception.IllegalKeywordException;
import com.narvis.dataaccess.exception.NoAccessDataException;
import com.narvis.engine.AnswerBuilder;
import com.narvis.dataaccess.impl.ModuleConfigurationDataProvider;
import com.narvis.dataaccess.interfaces.IAnswerProvider;
import com.narvis.engine.interfaces.IAnswerBuilder;
import com.narvis.dataaccess.interfaces.dataproviders.IDataProviderDetails;
import com.narvis.dataaccess.models.conf.ApiKeys;
import com.narvis.dataaccess.weather.annotations.Command;
import com.narvis.dataaccess.exception.NoValueException;
import com.narvis.dataaccess.exception.ProviderException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import org.json.JSONException;

/**
 *
 * Get Data implementation for the OpenWeatherMapPortal The first Parameter must
 * be the name of the city the other parameters could be (Not case sensitive)*
 * The supported command are : Empty string : Return Temperature and cloud
 * percentage Temperature : Return temperature in celsius Cloud : return cloud
 * percentage
 *
 *
 *
 * @author puma
 */
public class OpenWeatherMapPortal implements IDataProviderDetails, IAnswerProvider {

    private final ApiKeys weatherApiKeys;
    private CurrentWeather _currentWeather;
    private ModuleConfigurationDataProvider _confProvider;

    private final String DEFAULT_ANSWER = "weather";
    private final String ERROR_ANSWER = "error";
    private final String LOCATION_STRING = "location";

    private String _city;
    private final String KEY_TAG = "key";

    //tests purpose only
    public OpenWeatherMapPortal(ApiKeys api) {
        this.weatherApiKeys = api;
    }

    public OpenWeatherMapPortal(ModuleConfigurationDataProvider moduleConf) {
        this.weatherApiKeys = moduleConf.getApiKeys();
        this._confProvider = moduleConf;

    }

    /**
     * Return the temperature in the city in celsius
     *
     * @return
     * @throws com.narvis.dataaccess.exception.NoAccessDataException in case the
     * data is not accessible
     */
    @Command(CommandName = "temperature")
    public String getTemperatureInCelsius() throws NoAccessDataException {

        //Early out
        if (this._currentWeather == null || !this._currentWeather.hasMainInstance() || !this._currentWeather.getMainInstance().hasMaxTemperature()) {
            throw new NoAccessDataException(OpenWeatherMapPortal.class, "Can not find temperature", this._confProvider.getErrorsLayout().getData("temperature"));
        }

        float farenheitTemperature = this._currentWeather.getMainInstance().getTemperature();
        float celsiusTemp = (farenheitTemperature - 32.0f) * 5.0f / 9.0f;
        return Float.toString(celsiusTemp);
    }

    /**
     * Return the percentage of clouds
     *
     * @return
     * @throws com.narvis.dataaccess.exception.NoAccessDataException in case the
     * data is not accessible
     */
    @Command(CommandName = "cloud")
    public String GetPercentageOfCloud() throws NoAccessDataException {

        //Early out
        if (this._currentWeather == null || !this._currentWeather.hasCloudsInstance() || !this._currentWeather.getCloudsInstance().hasPercentageOfClouds()) {
            throw new NoAccessDataException(OpenWeatherMapPortal.class, "Can not find clouds percentage", this._confProvider.getErrorsLayout().getData("cloud"));
        }

        float cloudPercentage = this._currentWeather.getCloudsInstance().getPercentageOfClouds();

        return Float.toString(cloudPercentage);

    }

    @Command(CommandName = "city")
    public String getCity() {

        return _city;

    }

    /**
     * Call the method wich provide the answer for the given method
     *
     * @param command
     * @return the return value of the called method
     */
    private String CallMethodByCommand(String command) {

        if (command == null) {
            return null;
        }

        Method[] methods = this.getClass().getMethods();

        //Heizenberg is behind you...
        for (Method meth : methods) {

            Command[] comAnnotations = meth.getAnnotationsByType(Command.class);
            for (Command comAnnot : comAnnotations) {
                if (comAnnot.CommandName().equals(command)) {
                    try {
                        //Method found we call it
                        return (String) meth.invoke(this);

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
     *
     * @param details The details : City, Date...
     * @param keywords the command which represent the data asked, Only the
     * first parameters will be used
     * @return
     * @throws com.narvis.dataaccess.exception.NoValueException
     */
    @Override
    public String getDataDetails(Map<String, String> details, String... keywords) throws NoValueException, ProviderException {
       
         
        //Problem we give weather in nimesquit
        if ( !details.containsKey(LOCATION_STRING) ) {
            
            _city = lookForValueLocation(details);
            
            if( _city == null )
                throw new IllegalKeywordException(OpenWeatherMapPortal.class, keywords, "No keyword correspondance", this._confProvider.getErrorsLayout().getData("wrongkeywords"));
        }else {
            
            _city = details.get(this.LOCATION_STRING);
            
        }
        
        if( this._confProvider == null || this._confProvider.getAnswersLayout() == null) {
            
            throw new IllegalKeywordException(OpenWeatherMapPortal.class, keywords, "No configuration or answer layout found", this._confProvider.getErrorsLayout().getData("engine"));
        }
        
        
        
        String key = this.weatherApiKeys.getData(KEY_TAG);

        if (key == null) {
            return null;
        }

        String wantedAnswer = "";
        //If the user asked for nothing special, we pick the default answer
        if (keywords.length == 0) {
            
            wantedAnswer = DEFAULT_ANSWER;
        }else if( keywords[0] != null ) {
            
            wantedAnswer = keywords[0];
            
        }

        OpenWeatherMap owm = new OpenWeatherMap(key);
        try {
            this._currentWeather = owm.currentWeatherByCityName(_city);
        } catch (IOException | JSONException ex) {
            NarvisLogger.logException(ex);
            throw new ProviderException(OpenWeatherMapPortal.class, "Woops, this is (really really) bad.", this._confProvider.getData("general"));
        }

        IAnswerBuilder answerBuilder = new AnswerBuilder();
        String answerFromXml = this._confProvider.getAnswersLayout().getData(wantedAnswer);

        //Can not find the answer from the xml something went wrong we quit
        if (answerFromXml == null) {
            throw new NoValueException(OpenWeatherMapPortal.class, "Command not supported", this._confProvider.getErrorsLayout().getData("noanswers"));
        }

        List<String> listOfParams = answerBuilder.getListOfRequiredParams(answerFromXml);
        Map<String, String> paramsToValues = buildParamsToValueMap(details, listOfParams);

        return answerBuilder.buildAnswer(paramsToValues, answerFromXml);

    }
    
    @Override
    public Map<String, String> buildParamsToValueMap(Map<String,String> details, List<String> listOfParams) throws NoValueException {

        Map<String, String> paramsToValue = new HashMap<>();

        //To gain time we get all the details and their value
        paramsToValue.putAll(details);

        for (String param : listOfParams) {

            //Make sur we don't already have the value of the params
            if (!paramsToValue.containsKey(param)) {

                String value = CallMethodByCommand(param);
                if (value == null) {
                    throw new NoValueException(OpenWeatherMapPortal.class, param, this._confProvider.getErrorsLayout().getData("general"));

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

    private String lookForValueLocation(Map<String, String> details) {

        for (Map.Entry<String, String> entry : details.entrySet()) {

            if (entry.getValue().equals(LOCATION_STRING)) {
                return entry.getKey();
            }
        }

        return null;

    }

}

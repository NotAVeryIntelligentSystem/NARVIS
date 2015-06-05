/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.weather;

import com.narvis.common.debug.NarvisLogger;
import com.narvis.dataaccess.exception.NoAccessDataException;
import com.narvis.engine.AnswerBuilder;
import com.narvis.dataaccess.impl.ModuleConfigurationDataProvider;
import com.narvis.dataaccess.interfaces.IAnswerProvider;
import com.narvis.engine.interfaces.IAnswserBuilder;
import com.narvis.dataaccess.interfaces.IDataProviderDetails;
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
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private Map<String, String> _tempDetails;

    private final String DEFAULT_ANSWER = "weather";
    private final String ERROR_ANSWER = "error";

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
            throw new NoAccessDataException(OpenWeatherMapPortal.class, "Can not find temperature");
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
            throw new NoAccessDataException(OpenWeatherMapPortal.class, "Can not find clouds percentage");
        }

        float cloudPercentage = this._currentWeather.getCloudsInstance().getPercentageOfClouds();

        return Float.toString(cloudPercentage);

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
     * @param detailsToValue The details : City, Date...
     * @param keywords the command which represent the data asked, Only the
     * first parameters will be used
     * @return
     * @throws com.narvis.dataaccess.exception.NoValueException
     */
    @Override
    public String getDataDetails(Map<String, String> detailsToValue, String... keywords) throws NoValueException, ProviderException {
        //Problem we quit
        if (keywords.length < 1 || !detailsToValue.containsKey("city") || this._confProvider == null || this._confProvider.getAnswersLayout() == null) {
            return null;
        }

        String key = this.weatherApiKeys.getData(KEY_TAG);

        if (key == null) {
            return null;
        }

        //If the user asked for nothing special, we pick the default answer
        if (keywords[0].isEmpty()) {
            keywords[0] = DEFAULT_ANSWER;
        }

        this._tempDetails = detailsToValue;

        OpenWeatherMap owm = new OpenWeatherMap(key);
        try {
            this._currentWeather = owm.currentWeatherByCityName(this._tempDetails.get("city"));
        } catch (IOException | JSONException ex) {
            NarvisLogger.logException(ex);
            throw new ProviderException(OpenWeatherMapPortal.class, "Woops, this is (really really) bad.");
        }

        IAnswserBuilder answerBuilder = new AnswerBuilder();
        String answerFromXml = answerBuilder.readAnswerForCommand(this._confProvider.getAnswersLayout(), keywords[0]);

        //Can not find the answer from the xml something went wrong we quit
        if (answerFromXml == null) {
            throw new NoValueException(OpenWeatherMapPortal.class, "Command not supported");
        }

        List<String> listOfParams = answerBuilder.getListOfRequiredParams(answerFromXml);
        Map<String, String> paramsToValues = buildParamsToValueMap(listOfParams);

        return answerBuilder.buildAnswer(paramsToValues, answerFromXml);

    }

    @Override
    public Map<String, String> buildParamsToValueMap(List<String> listOfParams) throws NoValueException {

        Map<String, String> paramsToValue = new HashMap<>();

        //To gain time we get all the details and their value
        paramsToValue.putAll(this._tempDetails);

        for (String param : listOfParams) {

            //Make sur we don't already have the value of the params
            if (!paramsToValue.containsKey(param)) {

                String value = CallMethodByCommand(param);
                if (value == null) {
                    throw new NoValueException(OpenWeatherMapPortal.class, param);

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

}

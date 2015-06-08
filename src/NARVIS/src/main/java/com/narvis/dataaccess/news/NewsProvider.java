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
package com.narvis.dataaccess.news;

import com.narvis.common.debug.NarvisLogger;
import com.narvis.dataaccess.exception.IllegalKeywordException;
import com.narvis.dataaccess.exception.NoDataException;
import com.narvis.dataaccess.exception.NoValueException;
import com.narvis.dataaccess.exception.ProviderException;
import com.narvis.common.annotations.Command;
import com.narvis.dataaccess.impl.ModuleConfigurationDataProvider;
import com.narvis.dataaccess.interfaces.IAnswerProvider;
import com.narvis.dataaccess.interfaces.dataproviders.IDataProviderDetails;
import com.narvis.engine.AnswerBuilder;
import com.narvis.engine.interfaces.IAnswerBuilder;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * News provider, To get the Data, call GetDataDetails() with the city name in
 * the details
 *
 * @author puma
 */
public class NewsProvider implements IDataProviderDetails, IAnswerProvider {

    private SyndFeed _inputFeed;
    private SyndEntry _news;
    private ModuleConfigurationDataProvider _confProvider;
    private String LOCATION_STRING = "location";
    private String RSS_NEWS_URL = "http://feeds.reuters.com/Reuters/worldNews";
    private String DEFAULT_COMMAND = "news";

    public NewsProvider(ModuleConfigurationDataProvider confProvider) throws ProviderException {

        try {

            URL newsUrl = new URL(RSS_NEWS_URL);

            this._confProvider = confProvider;
            SyndFeedInput input = new SyndFeedInput();
            this._inputFeed = input.build(new XmlReader(newsUrl));

        } catch (MalformedURLException | IllegalArgumentException | FeedException ex) {

            NarvisLogger.logException(ex);
            throw new ProviderException(NewsProvider.class, "Inner error : This is bad, I mean really really bad", ex, "Really bad !");

        } catch (IOException ex) {
            NarvisLogger.logException(ex);
            throw new ProviderException(NewsProvider.class, "Inner error : This is bad, I mean really really bad", ex, "Really bad !");
        }

    }

    /**
     * Get the city in the details provided by the caller
     *
     * @param details This param could be null but it fail if it is catch a
     * NullPointerException
     * @return
     */
    public String findCityInDetails(Map<String, String> details) {

        if (details == null) {
            return null;
        }

        //First check if the location key is present
        if (details.containsKey(this.LOCATION_STRING)) {
            return details.get(this.LOCATION_STRING);
        } else {

            //It's not, check if the location is present as a value not a key
            //Historical reason, the parser create the map as : [cityname] -> location
            //It's the opposite of us, we expect : location -> [cityname]
            for (Map.Entry<String, String> entry : details.entrySet()) {

                if (entry.getValue().equals(this.LOCATION_STRING)) {

                    //Found it return the key, (because it is the opposite of what we expect)
                    return entry.getKey();
                }
            }

        }

        //We did not find it return null the exception will be thrown later
        return null;
    }

    /**
     * Use it to extract the city name from a news
     *
     * @param news the current news to analyse
     * @return the name of the city
     */
    private String extractCityNameFromnews(SyndEntry news) {

        String description = news.getDescription().getValue();

        String pattern = "(,|\\.| )";
        Pattern splitter = Pattern.compile(pattern);

        String[] splitted;

        //On applique une limite, on ne veut a la fin qu'un tableau avec deux entrées
        splitted = splitter.split(description, 2);

        return splitted[0].toLowerCase();

    }

    @Override
    public Map<String, String> buildParamsToValueMap(Map<String, String> details, List<String> listOfParams) throws NoValueException {

        Map<String, String> paramsToValue = new HashMap<>();

        //To gain time we get all the details and their value
        paramsToValue.putAll(details);

        for (String param : listOfParams) {

            //Make sur we don't already have the value of the params
            if (!paramsToValue.containsKey(param)) {

                String value = CallMethodByCommand(param);
                if (value == null) {
                    throw new NoValueException(NewsProvider.class, param, this._confProvider.getErrorsLayout().getData("general"));

                } else {

                    paramsToValue.put(param, value);

                }
            }

        }

        //Every details we need should be in the details provided by the caller !
        return paramsToValue;

    }

    @Override
    public String getDataDetails(Map<String, String> detailsToValue, String... keywords) throws ProviderException, NoValueException {

        String askedCity = findCityInDetails(detailsToValue);

        if (detailsToValue == null || askedCity == null) {
            throw new IllegalKeywordException("Incorrect parameters for getDetails methods", "engine");
        }
        askedCity = askedCity.toLowerCase();

        String wantedAnswer;
        if (keywords.length == 0) {
            wantedAnswer = DEFAULT_COMMAND;
        } else {
            wantedAnswer = keywords[0];
        }

        this._news = findNewsForCity(askedCity);

        if (this._news == null) {
            return this._confProvider.getErrorsLayout().getData("error");
        }

        IAnswerBuilder answerBuilder = new AnswerBuilder();
        String answerFromXml = this._confProvider.getAnswersLayout().getData(wantedAnswer);

        //Can not find the answer from the xml something went wrong we quit
        if (answerFromXml == null) {
            throw new NoValueException(NewsProvider.class, "Command not supported", this._confProvider.getErrorsLayout().getData("error"));
        }

        List<String> listOfParams = answerBuilder.getListOfRequiredParams(answerFromXml);
        Map<String, String> paramsToValues = buildParamsToValueMap(detailsToValue, listOfParams);

        return answerBuilder.buildAnswer(paramsToValues, answerFromXml);

    }

    private SyndEntry findNewsForCity(String city) {

        //Try to find a news for the asked city
        for (SyndEntry news : this._inputFeed.getEntries()) {

            String currentCity = extractCityNameFromnews(news).toLowerCase(Locale.FRENCH);

            if (currentCity.equals(city)) {

                return news;

            }
        }

        //No news found
        return null;

    }

    @Command(CommandName = "link")
    public String getLink() {

        return this._news.getLink();

    }

    @Command(CommandName = "title")
    public String getTitle() {

        return this._news.getTitle();
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

    @Override
    public String getData(String... keywords) throws NoDataException, IllegalKeywordException {
        throw new UnsupportedOperationException("Not supported ");
    }

}

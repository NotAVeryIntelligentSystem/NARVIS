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
import com.narvis.dataaccess.impl.ModuleConfigurationDataProvider;
import com.narvis.dataaccess.interfaces.IAnswerProvider;
import com.narvis.dataaccess.interfaces.IDataProviderDetails;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
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
    private ModuleConfigurationDataProvider _confProvider;
    private URL NEWS_FEED_URL;
    private String LOCATION_STRING = "location";

    public NewsProvider(ModuleConfigurationDataProvider confProvider) throws ProviderException {

        try {
            this.NEWS_FEED_URL = new URL("http://feeds.reuters.com/Reuters/worldNews");

            this._confProvider = confProvider;
            SyndFeedInput input = new SyndFeedInput();
            this._inputFeed = input.build(new XmlReader(NEWS_FEED_URL));

        } catch (MalformedURLException | IllegalArgumentException | FeedException ex) {

            NarvisLogger.logException(ex);
            throw new ProviderException(NewsProvider.class, "Inner error : This is bad, I mean really really bad", ex, "Really bad !");

        } catch (IOException ex) {
            NarvisLogger.logException(ex);
            throw new ProviderException(NewsProvider.class, "Inner error : This is bad, I mean really really bad", ex, "Really bad !");
        }

    }

    @Override
    public Map<String, String> buildParamsToValueMap(List<String> listOfParams) throws NoValueException {

        
        throw new UnsupportedOperationException("Not implemented yet");
        
        
    }

    @Override
    public String getDataDetails(Map<String, String> detailsToValue, String... keywords) throws ProviderException, NoValueException {

        
        String askedCity = findCityInDetails(detailsToValue);
        
        if( detailsToValue == null || askedCity == null ) {
            throw new NoValueException(NewsProvider.class ,"No value found for location", "engine");
        }
        
        for( SyndEntry news : this._inputFeed.getEntries() ) {
            
            String currentCity = extractCityNameFromnews(news);
            
            if( currentCity.equals(askedCity) ) {
                //Found a news for the asked city
                return formatNews(news);
            }
        }
        
        //Found nothing, we'll check for it laterr
        throw new NoValueException(NewsProvider.class, "No news for city", "data");
    }
    
    

    @Override
    public String getData(String... keywords) throws NoDataException, IllegalKeywordException {
        throw new UnsupportedOperationException("Not supported"); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Get the city in the details provided by the caller
     *
     * @param details This param could be null but it fail if it is
     * catch a NullPointerException
     * @return
     */
    public String findCityInDetails(Map<String, String> details) {

        
        if( details == null )
            return null;
        
        //First check if the location key is present
        if (details.containsKey(this.LOCATION_STRING)) {
            return details.get(this.LOCATION_STRING);
        } else {
            
            //It's not, check if the location is present as a value not a key
            //Historical reason, the parser create the map as : [cityname] -> location
            //It's the opposite of us, we expect : location -> [cityname]
            
            for( Map.Entry<String,String> entry : details.entrySet() ) {
                
                if( entry.getValue().equals(this.LOCATION_STRING) ) {
                
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
     * @param news the current news to analyse
     * @return the name of the city
     */
    private String extractCityNameFromnews(SyndEntry news) {
        
        String description = news.getDescription().getValue();

        String pattern = "(,|\\.| )";
        Pattern splitter = Pattern.compile(pattern);
        
        String[] splitted;
        
        //On applique une limite, on ne veut a la fin qu'un tableau avec deux entrées
        splitted = splitter.split(description,2);
        
        return splitted[0].toLowerCase();
        
    }
    
    /**
     * Format the news the construct a good old string
     * @param news the news to format
     * @return the formated string
     */
    private String formatNews(SyndEntry news) {
        
        return "Titre : " + news.getTitle() + "\nLien : " + news.getLink();
         
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package narvis.modules.news;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


/***
 *  Ajouter cette dépendance au fichier pom
 * 
 *       <dependency>
 *            <groupId>com.rometools</groupId>
 *           <artifactId>rome</artifactId>
 *           <version>1.5.0</version>
 *       </dependency>
 *
 * 
 */


/**
 *
 * @author Puma
 */
public class TestReuters {
    
    private SyndFeed _inputFeed;
    
    public static void main(String[] args)
    {
        TestReuters reuters;
        reuters = new TestReuters();
        
        //On récpupere la derniere news a paris. 
        System.out.println(reuters.getLastNewsByCity("Paris"));
        
        
        try
        {
            SimpleDateFormat dateformat;
            dateformat = new SimpleDateFormat("dd/MM/yyyy");
            Date date;
            date = dateformat.parse("19/04/2015");
            System.out.println(reuters.getNewsByDate(date));
        }
        catch( ParseException e)
        {
            System.err.println(e);
        }
        
        //On récupère la derniere news
        System.out.println(reuters.getLastWorldNews());
    }

    /**
     * Construit un lecteur de news reuters
     */
    public TestReuters() {
        
        try {
            URL feedUrl = new URL("http://feeds.reuters.com/Reuters/worldNews");
            
            SyndFeedInput input = new SyndFeedInput();
            this._inputFeed = input.build(new XmlReader(feedUrl));
            
        } catch (MalformedURLException ex) {
            System.err.println("Erreur interne");
        } catch (IllegalArgumentException | FeedException | IOException ex) {
            System.err.println("Erreur interne");
        }
    }
    
    
    
    /**
     * Récupère la derniere news qui s'est produite dans la ville donnée
     * @param city 
     * @return Une string décrivant la news
     */
    public String getLastNewsByCity(String city)
    {
        city = city.toLowerCase();
        for( SyndEntry news : this._inputFeed.getEntries() )
        {
            String currentCityOfNews = getCityFromNews(news);
            //On verifie que la news actuelle s'est produite dans la ville demandée
            if( currentCityOfNews == null ? city == null : currentCityOfNews.equals(city) )
            {
                return formatNewsString(news);
            }
        }
        
        return "rien a dire";
    }
    
    /**
     * Recupere la derniere news publiée à la date donnée
     * @param date
     * @return 
     */
    public String getNewsByDate( Date date )
    {
        for( SyndEntry news : this._inputFeed.getEntries() )
        {
            if( date.compareTo( news.getPublishedDate() ) == 0 )
            {
                return formatNewsString(news);
            }
        }
        
        return "No News";
    }
    
    /**
     * Retourne les derniere news 
     * @return 
     */
    public String getLastWorldNews()
    {
        //On formate le message et on le retourne
        return this.formatNewsString( this._inputFeed.getEntries().get(0) );
            
    }
    
    /**
     * Formate un message contenant des informations contenu dans l'entrée donnée
     * @param entry l'entrée dont on doit récupérer les info
     * @return un message formaté contenant 
     */
    private String formatNewsString(SyndEntry news)
    {
        return "Titre : " + news.getTitle() + "\nLien : " + news.getLink();
    }
    
    /**
     * Récupère le nom de la ville concernée par la news
     * @param news news à analyser
     * @return La ville ou la news s'est produite
     */
    private String getCityFromNews(SyndEntry news)
    {
        //Gros bricolage on utilise 
        
        String description = news.getDescription().getValue();

        String pattern = "(,|\\.| )";
        Pattern splitter = Pattern.compile(pattern);
        
        String[] splitted;
        
        //On applique une limite, on ne veut a la fin qu'un tableau avec deux entrées
        splitted = splitter.split(description,2);
        
        return splitted[0].toLowerCase();
    }
    
}

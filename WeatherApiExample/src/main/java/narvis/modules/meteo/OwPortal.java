/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package narvis.module.meteo;


import java.io.IOException;
import java.net.MalformedURLException;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import org.json.JSONException;
import sun.management.MemoryUsageCompositeData;


/**
 *
 * @author Puma
 */
public class OwPortal {
    
    private final CurrentWeather _currentWeather;
    
    public OwPortal(String city) throws IOException, JSONException
    {
		/*01b5f54b9605d5bbae6cf9f831560fb5 : my Api key*/
        OpenWeatherMap owm = new OpenWeatherMap("01b5f54b9605d5bbae6cf9f831560fb5");
        
		/*Warning : The weather API could mistake the given city with another.*/
		/*For example : London, if you just put london : The result will be....*/
		/*....*/
		/*drum roll*/
		/*....*/
		/*The LONDON from CANADA !!! */
		/* which is not the London from UK ! Greets from Captain Obvious*/
        this._currentWeather = owm.currentWeatherByCityName(city);
        
    }

    public double getCountry()
    {
        return this._currentWeather.getCityCode();
    }
    
    public double getMaxTemperatureInFarenheit()
    {
        return this._currentWeather.getMainInstance().getMaxTemperature();
    }
    
    public double getMaxTemperatureInCelsius()
    {
        
        double farenheitTemperature = getMaxTemperatureInFarenheit();
        return (farenheitTemperature  -  32.0) *5.0/9.0;
    }
 
    public double getWindSpeed() throws NoSuchFieldException
    {
        if( this._currentWeather.getWindInstance().hasWindSpeed() )
            return this._currentWeather.getWindInstance().getWindSpeed();
        
        throw new NoSuchFieldException("No wind speed found");
    }
    
    
    public double getPercentageOfCloud() throws NoSuchFieldException
    {
        if( this._currentWeather.getCloudsInstance().hasPercentageOfClouds() )
            return this._currentWeather.getCloudsInstance().getPercentageOfClouds();
        
        throw new NoSuchFieldException("No cloud percentage found");
    }
    
    
    /***
     * Quick and rough way to know if the weather is good or not
     * @return 
     */
    public boolean isSkyClear()
    {
       double pressure = this._currentWeather.getMainInstance().getPressure();
       
       if( pressure > 1010)
           return true;
       else 
           return false;
    }
	
	
	    
    public static void main(String[] args)throws IOException, MalformedURLException, JSONException 
    {
        OwPortal p = new OwPortal("London");
        
        System.out.print(p.getCountry());
        System.out.println(p.getMaxTemperatureInCelsius());
        System.out.println(p.getMaxTemperatureInFarenheit());
        System.out.println(p.isSkyClear());
    
    }
	
	
	
	
}

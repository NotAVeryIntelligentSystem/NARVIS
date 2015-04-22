/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package narvis.modules.weather;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.NoSuchElementException;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import org.json.JSONException;
import sun.management.MemoryUsageCompositeData;


/**
 *
 * @author Puma
 */
public class OwPortal implements IWeatherApi {
    
    private final CurrentWeather _currentWeather;
    
    public OwPortal(String city) throws IOException, JSONException
    {
		/*01b5f54b9605d5bbae6cf9f831560fb5 : My Key API*/
        OpenWeatherMap owm = new OpenWeatherMap("01b5f54b9605d5bbae6cf9f831560fb5");
        
		/***
			Warning : The API could mistake the given city with another one !
			For example : London
			
			If you put london as argument the result will be....
			.
			.
			.
			.
			.
			.
			.
			drum roll	
			.
			.
			.
			.
			.
			.
			.
			The LONDON FROM CANADA !!!
			
			Which is definitly not the london from uk
			respectfully yours
																		Captain Obvious
			
		 */
        this._currentWeather = owm.currentWeatherByCityName(city);
        
    }
    
    public static void main(String[] args)throws IOException, MalformedURLException, JSONException 
    {
        OwPortal p = new OwPortal("London");
        
        System.out.print(p.getCountry());
        System.out.println(p.getMaxTemperatureInCelsius());
        System.out.println(p.getMaxTemperatureInFarenheit());
        System.out.println(p.isSkyClear());
    
    }
    
    public double getCountry()
    {
        return this._currentWeather.getCityCode();
    }
    
    public double getMaxTemperatureInFarenheit()
    {
        return this._currentWeather.getMainInstance().getMaxTemperature();
    }
    
    @Override
    public double getMaxTemperatureInCelsius()
    {
        
        double farenheitTemperature = getMaxTemperatureInFarenheit();
        return (farenheitTemperature  -  32.0) *5.0/9.0;
    }
 
    @Override
    public double getWindSpeed() throws NoSuchElementException
    {
        if( this._currentWeather.getWindInstance().hasWindSpeed() )
            return this._currentWeather.getWindInstance().getWindSpeed();
       
        
        throw new NoSuchElementException("No wind speed found");
    }
    
    
    @Override
    public double getPercentageOfCloud() throws NoSuchElementException
    {
        if( this._currentWeather.getCloudsInstance().hasPercentageOfClouds() )
            return this._currentWeather.getCloudsInstance().getPercentageOfClouds();
        
        throw new NoSuchElementException("No cloud percentage found");
    }
    
    
    /***
     * Quick and rough way to know if the weather is good or not
     * @return 
     */
    @Override
    public boolean isSkyClear()
    {
       double pressure = this._currentWeather.getMainInstance().getPressure();
       
       if( pressure > 1010)
           return true;
       else 
           return false;
    }
   
}

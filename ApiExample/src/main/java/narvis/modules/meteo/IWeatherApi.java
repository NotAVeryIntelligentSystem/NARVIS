/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package narvis.modules.meteo;

import java.util.NoSuchElementException;

/**
 *
 * @author Puma
 */
public interface IWeatherApi {

    double getMaxTemperatureInCelsius() throws NoSuchElementException;
    double getPercentageOfCloud() throws NoSuchElementException;
    double getWindSpeed() throws NoSuchElementException;
    boolean isSkyClear();
    
}

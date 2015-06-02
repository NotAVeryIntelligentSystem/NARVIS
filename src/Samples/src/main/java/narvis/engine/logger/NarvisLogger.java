/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package narvis.engine.logger;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import narvis.engine.parser.Parser;

/**
 *
 * @author Zack
 */
public class NarvisLogger extends Logger {

    private static NarvisLogger INSTANCE = null;
    
    
    /** Constructeur privé */
    private NarvisLogger() 
    {
        super(Parser.class.getName(), null);
        
        INSTANCE.addHandler(new ConsoleHandler());
        
        try {
            INSTANCE.addHandler(new FileHandler("%t\narvis-log.%u.txt"));
        } catch (IOException | SecurityException ex) {
            INSTANCE.severe(ex.getMessage());
        }
    }
 
    /**
     * Retourne l'instance unique du logger
     * @return L'instance du logger
     */
    public static synchronized NarvisLogger getInstance()
    {			
    	if (INSTANCE == null)
	{ 	
            INSTANCE = new NarvisLogger();	
	}
	return INSTANCE;
    }
}

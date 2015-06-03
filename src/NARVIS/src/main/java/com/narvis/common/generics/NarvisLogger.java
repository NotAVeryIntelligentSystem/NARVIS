/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.common.generics;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;

/**
 *
 * @author Zack
 */
public class NarvisLogger extends Logger {
    private final static String PATH = "src\\main\\java\\narvis\\engine\\logger\\logs\\";
    private static NarvisLogger INSTANCE = null;
    
    
    /** Constructeur privé */
    private NarvisLogger() 
    {
        super(NarvisLogger.class.getName(), null);
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
            
            INSTANCE.addHandler(new ConsoleHandler());
        
            try {
                Date date= new Date();
                System.out.println("Date : "+date.getTime());
                INSTANCE.addHandler(new FileHandler(PATH+"narvis-log."+date.getTime()+".txt"));
            } catch (IOException | SecurityException ex) {
                INSTANCE.severe(ex.getMessage());
            }
	}
	return INSTANCE;
    }
}

/*
 * The MIT License
 *
 * Copyright 2015 Zack.
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
package com.narvis.common.debug;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.*;

/**
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class NarvisLogger {

    private final static boolean DEBUG_MOD = false; // True : use console + file log, False : use only file log
    private static NarvisLogger INSTANCE = null;

    /**
     * Private constructor
     */
    private final Logger logger;
    
    private NarvisLogger() {
        this.logger = Logger.getGlobal();

    }
    
    /**
     * Return the logger
     * @return 
     */
    public Logger getLogger() {
        return this.logger;
    }

    /**
     * Log an "Info"
     * @param msg The information you want to log
     */
    public static void logInfo(String msg) {
        getInstance().getLogger().log(Level.INFO, msg);
    }

    /**
     * Log an "Exception"
     * @param ex The exception you want to log
     */
    public static void logException(Throwable ex) {
        getInstance().getLogger().log(Level.SEVERE, "Nothing provided", ex);
    }

    /**
     * Log an "Exception" and attach a particular level
     * @param ex The exception you want to log
     * @param level the Level of the log
     */
    public static void logException(Level level, Throwable ex) {
        getInstance().getLogger().log(level, "Nothing provided", ex);
    }

    /**
     * Log an "Exception" 
     * @param ex The exception you want to log
     * @param msg 
     * @param level the Level of the log
     */
    public static void logException(Level level, String msg, Throwable ex) {
        getInstance().getLogger().log(level, msg, ex);
    }

    /**
     * Retourne l'instance unique du logger
     *
     * @return L'instance du logger
     */
    public static synchronized NarvisLogger getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NarvisLogger();

            try {
                String logDirectoryPath = System.getProperty("user.home");

                File logDirectory = new File(logDirectoryPath, "narvis-logs");

                if (!logDirectory.isDirectory() && !logDirectory.mkdirs()) {
                    INSTANCE.getLogger().warning("Can't create logs directory...");
                } else {
                    Date date = new Date();
                    File logPath = new File(logDirectory.getAbsolutePath(), "narvis-log." + date.getTime() + ".txt");

                    INSTANCE.getLogger().addHandler(new FileHandler(logPath.getAbsolutePath()));

                    /* If the log file is correctly initialized, we disable parents logs that could be console, etc.*/
                    INSTANCE.getLogger().setUseParentHandlers(DEBUG_MOD);
                }

            } catch (IOException | SecurityException ex) {
                INSTANCE.getLogger().warning(ex.getMessage());
            }
        }
        return INSTANCE;
    }
}

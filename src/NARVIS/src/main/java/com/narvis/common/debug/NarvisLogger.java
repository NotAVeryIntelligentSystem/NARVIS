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
 * @author Zack
 */
public class NarvisLogger {

    private static NarvisLogger INSTANCE = null;

    /**
     * Constructeur privé
     */
    private final Logger logger;
    private NarvisLogger() {
        this.logger = Logger.getGlobal();

    }
    
    public Logger getLogger() {
        return this.logger;
    }

    public static void logInfo(String msg) {
        getInstance().getLogger().log(Level.INFO, msg);
    }

    public static void logException(Throwable ex) {
        getInstance().getLogger().log(Level.SEVERE, "Nothing provided", ex);
    }

    public static void logException(Level level, Throwable ex) {
        getInstance().getLogger().log(level, "Nothing provided", ex);
    }

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
            INSTANCE.getLogger().addHandler(new ConsoleHandler());

            try {
                String logDirectoryPath = System.getProperty("user.home");

                File logDirectory = new File(logDirectoryPath, "narvis-logs");

                if (!logDirectory.isDirectory() && !logDirectory.mkdirs()) {
                    INSTANCE.getLogger().warning("Can't create logs directory...");
                } else {
                    Date date = new Date();
                    File logPath = new File(logDirectory.getAbsolutePath(), "narvis-log." + date.getTime() + ".txt");

                    INSTANCE.getLogger().addHandler(new FileHandler(logPath.getAbsolutePath()));
                }

            } catch (IOException | SecurityException ex) {
                INSTANCE.getLogger().warning(ex.getMessage());
            }
        }
        return INSTANCE;
    }
}

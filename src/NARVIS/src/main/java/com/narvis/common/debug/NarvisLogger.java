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
import java.nio.file.Files;
import java.util.Date;
import java.util.logging.*;

/**
 *
 * @author Zack
 */
public class NarvisLogger extends Logger {

    private static NarvisLogger INSTANCE = null;

    /**
     * Constructeur privé
     */
    private NarvisLogger() {
        super(NarvisLogger.class.getName(), null);
    }

    public static void logInfo(String msg) {
        getInstance().log(Level.INFO, msg);
    }

    public static void logException(Throwable ex) {
        getInstance().log(Level.SEVERE, "Nothing provided", ex);
    }

    public static void logException(Level level, Throwable ex) {
        getInstance().log(level, "Nothing provided", ex);
    }

    public static void logException(Level level, String msg, Throwable ex) {
        getInstance().log(level, msg, ex);
    }

    /**
     * Retourne l'instance unique du logger
     *
     * @return L'instance du logger
     */
    public static synchronized NarvisLogger getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NarvisLogger();

            INSTANCE.addHandler(new ConsoleHandler());

            try {
                String logDirectoryPath = System.getProperty("user.home");

                File logDirectory = new File(logDirectoryPath, "narvis-logs");

                if (!logDirectory.isDirectory() && !logDirectory.mkdirs()) {
                    INSTANCE.warning("Can't create logs directory...");
                } else {
                    Date date = new Date();
                    File logPath = new File(logDirectory.getAbsolutePath(), "narvis-log." + date.getTime() + ".txt");

                    INSTANCE.addHandler(new FileHandler(logPath.getAbsolutePath()));
                }

            } catch (IOException | SecurityException ex) {
                INSTANCE.warning(ex.getMessage());
            }
        }
        return INSTANCE;
    }
}

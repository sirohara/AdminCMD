/*
 * This file is part of AdminCMD
 * Copyright (C) 2014 AdminCMD Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.admincmd.admincmd.utils;

import com.admincmd.admincmd.Main;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ACLogger {

    private static final Logger logger = Logger.getLogger("AdminCMD");
    private static final String PREFIX = "[AdminCMD] ";

    /**
     * Logs information to console
     *
     * @param message The message to log
     */
    public static void info(final String message) {
        logger.log(Level.INFO, PREFIX + message);
    }

    /**
     * Logs warnings to console
     *
     * @param message The warning to log
     */
    public static void warn(final String message) {
        logger.log(Level.WARNING, PREFIX + message);
    }

    /**
     * Logs errors to console
     *
     * @param message The error to log
     */
    public static void severe(final String message) {
        logger.log(Level.SEVERE, PREFIX + message);
    }

    /**
     * Logs errors and exceptions to console
     *
     * @param message The error to log
     * @param ex The exception to log
     */
    public static void severe(final String message, final Throwable ex) {
        logger.log(Level.SEVERE, PREFIX + message, ex);
        printError(message, ex);
    }

    /**
     * Logs debug messages to console
     *
     * @param message The debug message to log
     */
    public static void debug(final String message) {
        if (!Config.DEBUG.getBoolean()) {
            return;
        }
        logger.log(Level.INFO, PREFIX + message);
        writeToDebug(message);
    }

    /**
     * Logs debug messages and exceptions to console
     *
     * @param message The debug message to log
     * @param ex The exception to log
     */
    public static void debug(final String message, final Throwable ex) {
        if (!Config.DEBUG.getBoolean()) {
            return;
        }
        logger.log(Level.INFO, PREFIX + message, ex);
        writeToDebug(message, ex);
    }

    private static String prefix() {
        DateFormat date = new SimpleDateFormat("[dd-MM-yyyy HH:mm:ss] ");
        Calendar cal = Calendar.getInstance();
        return date.format(cal.getTime());
    }

    private static void writeToDebug(String message) {
        BufferedWriter bw = null;
        File file = new File(Main.getInstance().getDataFolder(), "logs");
        file.mkdirs();
        try {
            bw = new BufferedWriter(new FileWriter(file + File.separator + "debug.log", true));
            bw.write(prefix() + ":" + message);
            bw.newLine();
        } catch (Exception ex) {
        } finally {
            try {
                if (bw != null) {
                    bw.flush();
                    bw.close();
                }
            } catch (Exception ex) {
            }
        }
    }

    private static void writeToDebug(String message, Throwable t) {
        BufferedWriter bw = null;
        File file = new File(Main.getInstance().getDataFolder(), "logs");
        file.mkdirs();
        try {
            bw = new BufferedWriter(new FileWriter(file + File.separator + "debug.log", true));
            bw.newLine();
            bw.newLine();
            bw.write("///////////////////////////////////////////////////////////////////////////////");
            bw.newLine();
            bw.newLine();
            bw.write(prefix() + ": An Exception happened!");
            bw.newLine();
            bw.write(prefix() + message);
            bw.newLine();
            bw.write(getStackTrace(t));
            bw.newLine();
            bw.newLine();
            bw.write("///////////////////////////////////////////////////////////////////////////////");
            bw.newLine();
            bw.newLine();
        } catch (Exception ex) {
        } finally {
            try {
                if (bw != null) {
                    bw.flush();
                    bw.close();
                }
            } catch (Exception ex) {
            }
        }
    }

    private static void printError(String message, Throwable t) {
        BufferedWriter bw = null;
        File file = new File(Main.getInstance().getDataFolder(), "logs" + File.separator + "errors");
        file.mkdirs();
        try {

            DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
            Calendar cal = Calendar.getInstance();
            String d = date.format(cal.getTime());

            bw = new BufferedWriter(new FileWriter(file + File.separator + d + ".log", true));
            bw.write(prefix() + ": An Exception happened!");
            bw.newLine();
            bw.write(prefix() + message);
            bw.newLine();
            bw.write(getStackTrace(t));
            bw.newLine();
            bw.newLine();
            bw.write("///////////////////////////////////////////////////////////////////////////////");
            bw.newLine();
            bw.newLine();
        } catch (Exception ex) {
        } finally {
            try {
                if (bw != null) {
                    bw.flush();
                    bw.close();
                }
            } catch (Exception ex) {
            }
        }
    }

    private static String getStackTrace(Throwable t) {
        String ret = prefix() + ": " + t + ": " + t.getMessage();

        StackTraceElement[] elements = t.getStackTrace();

        for (StackTraceElement element : elements) {
            ret += "\n" + prefix() + ": " + element;
        }

        return ret;
    }

}

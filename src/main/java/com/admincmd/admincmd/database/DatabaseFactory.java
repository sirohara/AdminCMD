/*
 * This file is part of AdminCMD
 * Copyright (C) 2015 AdminCMD Team
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
package com.admincmd.admincmd.database;

import com.admincmd.admincmd.utils.Config;
import com.admincmd.admincmd.Main;
import com.admincmd.admincmd.utils.ACLogger;
import java.io.File;
import java.sql.SQLException;

public class DatabaseFactory {

    private static Database db = null; 

    public static void init() {
        if (Config.MYSQL_USE.getBoolean()) {
            db = new MySQL(Config.MYSQL_IP.getString(), Config.MYSQL_USER.getString(), Config.MYSQL_PASSWORD.getString(), Config.MYSQL_DATABASE.getString(), Config.MYSQL_PORT.getInteger());
        } else {
            db = new SQLite(new File(Main.getInstance().getDataFolder(), "Database.db"));
        }

        if (db.testConnection()) {
            ACLogger.info("The connection was successful!");
            createTables();
        } else {
            ACLogger.severe("Could not connect to the Database!");
        }

    }

    private static void createTables() {
        try {
            String PLAYER_TABLE;
            String HOME_TABLE;
            String SPAWN_TABLE;
            String WORLD_TABLE;
            if (db instanceof SQLite) {
                PLAYER_TABLE = "CREATE TABLE IF NOT EXISTS `ac_player` ("
                        + "`ID` INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "`uuid` varchar(64) NOT NULL,"
                        + "`god` BOOLEAN,"
                        + "`invisible` BOOLEAN,"
                        + "`commandwatcher` BOOLEAN,"
                        + "`spy` BOOLEAN,"
                        + "`fly` BOOLEAN,"
                        + "`muted` BOOLEAN"
                        + ");";
                HOME_TABLE = "CREATE TABLE IF NOT EXISTS `ac_homes` ("
                        + "`id` INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "`playerid` INTEGER NOT NULL,"
                        + "`location` varchar(320) NOT NULL,"
                        + "`name` varchar(64) NOT NULL"
                        + ");";
                SPAWN_TABLE = "CREATE TABLE IF NOT EXISTS `ac_spawns` ("
                        + "`type` INTEGER NOT NULL,"
                        + "`group` varchar(64) NOT NULL,"
                        + "`location` varchar(320) NOT NULL"
                        + ");";
                
                WORLD_TABLE = "CREATE TABLE IF NOT EXISTS `ac_worlds` ("
                        + "`name` varchar(64) PRIMARY KEY NOT NULL,"
                        + "`paused` BOOLEAN NOT NULL,"
                        + "`time` varchar(128) NOT NULL"
                        + ");";
            } else {
                PLAYER_TABLE = "CREATE TABLE IF NOT EXISTS `ac_player` ("
                        + "`ID` INTEGER PRIMARY KEY AUTO_INCREMENT,"
                        + "`uuid` varchar(64) NOT NULL,"
                        + "`god` BOOLEAN,"
                        + "`invisible` BOOLEAN,"
                        + "`commandwatcher` BOOLEAN,"
                        + "`spy` BOOLEAN,"
                        + "`fly` BOOLEAN,"
                        + "`muted` BOOLEAN"
                        + ");";
                HOME_TABLE = "CREATE TABLE IF NOT EXISTS `ac_homes` ("
                        + "`id` INTEGER PRIMARY KEY AUTO_INCREMENT,"
                        + "`playerid` INTEGER NOT NULL,"
                        + "`location` varchar(320) NOT NULL,"
                        + "`name` varchar(64) NOT NULL"
                        + ");";
                SPAWN_TABLE = "CREATE TABLE IF NOT EXISTS `ac_spawns` ("
                        + "`type` INTEGER NOT NULL,"
                        + "`group` varchar(64) NOT NULL,"
                        + "`location` varchar(320) NOT NULL"
                        + ");";
                
                WORLD_TABLE = "CREATE TABLE IF NOT EXISTS `ac_worlds` ("
                        + "`name` varchar(64) PRIMARY KEY NOT NULL,"
                        + "`paused` BOOLEAN NOT NULL,"
                        + "`time` varchar(128) NOT NULL"
                        + ");";
            }
            db.executeStatement(PLAYER_TABLE);
            db.executeStatement(HOME_TABLE);
            db.executeStatement(SPAWN_TABLE);
            db.executeStatement(WORLD_TABLE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static Database getDatabase() {
        return db;
    }

}

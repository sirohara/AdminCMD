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
package com.admincmd.admincmd.world;

import com.admincmd.admincmd.utils.ACLogger;
import com.admincmd.admincmd.database.Database;
import com.admincmd.admincmd.database.DatabaseFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class WorldManager {

    private static final HashMap<String, ACWorld> worlds = new HashMap<>();
    private static final Database conn = DatabaseFactory.getDatabase();

    public static void init() {
        worlds.clear();        
        try {
            PreparedStatement st = conn.getPreparedStatement("SELECT * FROM `ac_worlds`;");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                boolean paused = rs.getBoolean("paused");
                String time = rs.getString("time");
                if (Bukkit.getWorld(name) == null) {
                    PreparedStatement str = conn.getPreparedStatement("DELETE FROM `ac_worlds` WHERE `name` = ?;");
                    str.setString(1, name);
                    str.executeUpdate();
                    conn.closeStatement(str);
                } else {
                    ACWorld vcw = new ACWorld(Bukkit.getWorld(name), paused, time);
                    worlds.put(name, vcw);
                }
            }

            conn.closeResultSet(rs);
            conn.closeStatement(st);

            for (World w : Bukkit.getWorlds()) {
                if (getWorld(w) == null) {
                    createWorld(new ACWorld(w, false, String.valueOf(w.getTime())));
                }
            }
            
            ACLogger.info("Loaded " + worlds.size() + " worlds!");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static ACWorld getWorld(Location loc) {
        ACWorld ret = worlds.get(loc.getWorld().getName());
        return ret;
    }
    
    public static ACWorld getWorld(String name) {
        World w = Bukkit.getWorld(name);
        return getWorld(w);
    }

    public static ACWorld getWorld(World w) {
        return worlds.get(w.getName());
    }

    public static void createWorld(ACWorld w) {
        try {
            PreparedStatement s = conn.getPreparedStatement("INSERT INTO `ac_worlds` (`name`, `paused`, `time`) VALUES (?, ?, ?);");
            s.setString(1, w.getWorld().getName());
            s.setBoolean(2, w.isTimePaused());
            s.setString(3, String.valueOf(w.getTimePauseMoment()));
            s.executeUpdate();
            conn.closeStatement(s);
            worlds.put(w.getWorld().getName(), w);
            ACLogger.info("World " + w.getWorld().getName() + " was put into the database.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void save() {
        int saved = worlds.size();
        for (ACWorld w : worlds.values()) {
            update(w);
        }
        worlds.clear();
        ACLogger.info("Saved " + saved + " worlds!");
    }

    public static void unloadWorld(ACWorld vcw) {
        worlds.remove(vcw.getWorld().getName());
    }

    private static void update(ACWorld w) {
        try {
            PreparedStatement st = conn.getPreparedStatement("UPDATE `ac_worlds` SET `paused` = ?, `time` = ? WHERE `name` = ?;");
            st.setBoolean(1, w.isTimePaused());
            st.setString(2, String.valueOf(w.getTimePauseMoment()));
            st.setString(3, w.getWorld().getName());
            st.executeUpdate();
            conn.closeStatement(st);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}

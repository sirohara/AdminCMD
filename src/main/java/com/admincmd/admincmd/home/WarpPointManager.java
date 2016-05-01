/* copied from HomeManager by CRAFTER_JUN */

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
package com.admincmd.admincmd.home;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.admincmd.admincmd.database.DatabaseFactory;
import com.admincmd.admincmd.player.BukkitPlayer;
import com.admincmd.admincmd.player.PlayerManager;
import com.admincmd.admincmd.utils.ACLogger;

public class WarpPointManager {

    private static final HashMap<String, Home> wps = new HashMap<>();
    private static final int homeid = -1; // home id is constantly -1 to share the warp point within all players
    private static int saved = 0;

    public static Home getWP(String name) {
        if (!wps.containsKey(name)) return null;
        return wps.get(name);
    }

    public static void deleteWP(String name) {
        wps.put(name, null);

        // save deletion immediately
        saved++;
        try {
            PreparedStatement s = DatabaseFactory.getDatabase().getPreparedStatement("DELETE FROM `ac_wps` WHERE `name` = ?;");
            s.setString(1, name);
            s.executeUpdate();
            DatabaseFactory.getDatabase().closeStatement(s);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static HashMap<String, Home> getWPs() {
        return wps;
    }

    public static void createWP(Player owner, String name) {
    	Location target = owner.getLocation();
        BukkitPlayer player = PlayerManager.getPlayer(owner);
        if (player == null) return;
        Home wp = new Home(target, player, name);
        wps.put(name, wp);

        // save creation immediately
        saved++;
        try {
            PreparedStatement s = DatabaseFactory.getDatabase().getPreparedStatement("SELECT `id` FROM `ac_wps` WHERE `id` = ? LIMIT 1;");
            s.setInt(1, homeid);
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                PreparedStatement sta = DatabaseFactory.getDatabase().getPreparedStatement("UPDATE `ac_wps` SET `location` = ? WHERE `id` = ?;");
                sta.setString(1, wp.getSerializedLocation());
                sta.setInt(2, homeid);
                sta.executeUpdate();
                DatabaseFactory.getDatabase().closeStatement(sta);
            } else {
                PreparedStatement sta = DatabaseFactory.getDatabase().getPreparedStatement("INSERT INTO `ac_wps` (`playerid`, `location`, `name`) VALUES (?, ?, ?);");
                sta.setInt(1, wp.getOwner().getId());
                sta.setString(2, wp.getSerializedLocation());
                sta.setString(3, wp.getName());
                sta.executeUpdate();
                DatabaseFactory.getDatabase().closeStatement(sta);
            }

            DatabaseFactory.getDatabase().closeResultSet(rs);
            DatabaseFactory.getDatabase().closeStatement(s);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void init() {
        try {
            Statement s = DatabaseFactory.getDatabase().getStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM `ac_wps`");
            int loaded = 0;

            while (rs.next()) {
                BukkitPlayer player = PlayerManager.getPlayer(rs.getInt("playerid"));
                if (player == null) continue;
                String name = rs.getString("name");
                Home wp = new Home(rs.getString("location"), player, name, rs.getInt("id"));
                if (!wps.containsKey(name)) wps.put(name, wp);
                loaded++;
            }

            DatabaseFactory.getDatabase().closeStatement(s);
            DatabaseFactory.getDatabase().closeResultSet(rs);
            ACLogger.info("Loaded " + loaded + " homes!");
        } catch (SQLException ex) {
            ACLogger.severe("Error loading the homes", ex);
        }
    }

    public static void save() {
        if (wps.isEmpty()) return;
        wps.clear();
        ACLogger.info("Saved " + saved + " homes!");
    }

}

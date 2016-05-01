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

public class HomeManager {

    private static final HashMap<Integer, HashMap<String, Home>> homes = new HashMap<>();
    private static int saved = 0;

    public static Home getHome(Player p, String name) {
        BukkitPlayer player = PlayerManager.getPlayer(p);
        if (player == null) return null;
        if (!homes.containsKey(player.getId())) return null;
        return homes.get(player.getId()).get(name);
    }

    public static void deleteHome(Home h) {
    	int id = h.getOwner().getId();
        HashMap<String, Home> home = homes.get(id);
        String name = h.getName();
        if (!home.containsKey(name) || home.get(name) == null) return;
        home.remove(name);
        home.put(name, null);

        // save deletion immediately
        saved++;
        try {
            PreparedStatement s = DatabaseFactory.getDatabase().getPreparedStatement("DELETE FROM `ac_homes` WHERE `playerid` = ? AND `name` = ?;");
            s.setInt(1, id);
            s.setString(2, name);
            s.executeUpdate();
            DatabaseFactory.getDatabase().closeStatement(s);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static HashMap<String, Home> getHomes(Player p) {
        BukkitPlayer player = PlayerManager.getPlayer(p);
        if (player == null) return new HashMap<>();
        if (!homes.containsKey(player.getId())) return new HashMap<>();
        HashMap<String, Home> ret = new HashMap<>();
        for (String name : homes.get(player.getId()).keySet()) {
            Home home = homes.get(player.getId()).get(name);
            if (home == null) continue;
            ret.put(name, home);
        }
        return ret;
    }

    public static void createHome(Player owner, String name) {
        Location target = owner.getLocation();
        BukkitPlayer player = PlayerManager.getPlayer(owner);
        if (player == null) return;
        Home h = new Home(target, player, name);
        int id = player.getId();
        if (!homes.containsKey(id)) homes.put(id, new HashMap<String, Home>());
        homes.get(id).put(name, h);

        // save creation immediately
        saved++;
        try {
            PreparedStatement s = DatabaseFactory.getDatabase().getPreparedStatement("SELECT `id` FROM `ac_homes` WHERE `id` = ? LIMIT 1;");
            s.setInt(1, h.getID());
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                PreparedStatement sta = DatabaseFactory.getDatabase().getPreparedStatement("UPDATE `ac_homes` SET `location` = ? WHERE `id` = ?;");
                sta.setString(1, h.getSerializedLocation());
                sta.setInt(2, id);
                sta.executeUpdate();
                DatabaseFactory.getDatabase().closeStatement(sta);
            } else {
                PreparedStatement sta = DatabaseFactory.getDatabase().getPreparedStatement("INSERT INTO `ac_homes` (`playerid`, `location`, `name`) VALUES (?, ?, ?);");
                sta.setInt(1, id);
                sta.setString(2, h.getSerializedLocation());
                sta.setString(3, name);
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
            ResultSet rs = s.executeQuery("SELECT * FROM `ac_homes`");
            int loaded = 0;

            while (rs.next()) {
                BukkitPlayer player = PlayerManager.getPlayer(rs.getInt("playerid"));
                if (player == null) continue;
                Home h = new Home(rs.getString("location"), player, rs.getString("name"), rs.getInt("id"));
                if (!homes.containsKey(player.getId())) homes.put(player.getId(), new HashMap<String, Home>());
                homes.get(player.getId()).put(h.getName(), h);
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
        if (homes.isEmpty()) return;
        homes.clear();
        ACLogger.info("Saved " + saved + " homes!");
    }

}

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
package com.admincmd.admincmd.player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.admincmd.admincmd.database.Database;
import com.admincmd.admincmd.database.DatabaseFactory;
import com.admincmd.admincmd.utils.ACLogger;


public class PlayerManager {

    private static final HashMap<UUID, BukkitPlayer> players = new HashMap<>();
    private static final Database conn = DatabaseFactory.getDatabase();

    public static HashMap<UUID, BukkitPlayer> getPlayers() {
        return players;
    }

    public static void init() {
        players.clear();
        try {
            PreparedStatement s = conn.getPreparedStatement("SELECT `uuid` FROM `ac_player`");
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                BukkitPlayer p = new BukkitPlayer(rs.getString("uuid"), conn);
                players.put(p.getUuid(), p);
            }
            ACLogger.info("Loaded " + players.size() + " players!");
        } catch (SQLException ex) {
            ACLogger.severe("Error loading the players!", ex);
        }
    }

    public static void save() {
        int saved = players.size();
        for (UUID uuid : players.keySet()) {
            BukkitPlayer p = players.get(uuid);
            p.update();
        }
        players.clear();
        ACLogger.info("Saved " + saved + " players!");
    }

    public static BukkitPlayer getPlayer(OfflinePlayer p) {
        return players.get(p.getUniqueId());
    }

    public static BukkitPlayer getPlayer(int id) {
        for (UUID u : players.keySet()) {
            BukkitPlayer p = players.get(u);
            if (p.getId() == id) return p;
        }
        return null;
    }

    public static void unload(BukkitPlayer p) {
        p.update();
        if (players.containsKey(p.getUuid()))
            players.remove(p.getUuid());
    }

    public static void insert(Player p) {

        try {
            PreparedStatement s = conn.getPreparedStatement("INSERT INTO `ac_player` (`uuid`, `god`, `invisible`, `commandwatcher`, `spy`, `fly`, `muted`, `lastMsg`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
            s.setString(1, p.getUniqueId().toString());
            s.setBoolean(2, false);
            s.setBoolean(3, false);
            s.setBoolean(4, false);
            s.setBoolean(5, false);
            s.setBoolean(6, false);
            s.setBoolean(7, false);
            s.setBoolean(8, false);
            s.executeUpdate();
            conn.closeStatement(s);

            BukkitPlayer bp = new BukkitPlayer(p, conn);
            players.put(bp.getUuid(), bp);
        } catch (SQLException ex) {
            ACLogger.severe("Error creating the player!", ex);
        }
    }

    public static boolean hasPlayedBefore(OfflinePlayer p) {
        return getPlayer(p) != null;
    }

}

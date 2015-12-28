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
package com.admincmd.admincmd.spawn;

import com.admincmd.admincmd.Main;
import com.admincmd.admincmd.database.Database;
import com.admincmd.admincmd.database.DatabaseFactory;
import com.admincmd.admincmd.utils.ACLogger;
import com.admincmd.admincmd.utils.Config;
import com.admincmd.admincmd.utils.LocationSerialization;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SpawnManager {

    private static Location spawn = null;
    private static final Database db = DatabaseFactory.getDatabase();

    public static void init() {
        if (!Config.GLOBAL_SPAWNS.getBoolean()) {
            try {
                PreparedStatement st = db.getPreparedStatement("SELECT * FROM `ac_spawn`");
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    Location loc = LocationSerialization.deserialLocation(rs.getString("location"));
                    spawn = loc;
                }
                db.closeResultSet(rs);
                db.closeStatement(st);

                ACLogger.info("Loaded spawn!");

            } catch (SQLException ex) {
                ACLogger.severe("Error loading spawn", ex);
            }
        }
    }

    public static Location getSpawn(Player p) {
        if (!Config.GLOBAL_SPAWNS.getBoolean()) {
            if (spawn != null) {
                return spawn;
            } else {
                return Main.getInstance().getServer().getWorlds().get(0).getSpawnLocation();
            }
        } else {
            return p.getWorld().getSpawnLocation();
        }

    }

    public static void setSpawn(Player p) {
        if (!Config.GLOBAL_SPAWNS.getBoolean()) {
            if (spawn == null) {
                createSpawn(p.getLocation());
            } else {
                try {
                    PreparedStatement st = db.getPreparedStatement("UPDATE `ac_spawn` SET `location` = ?;");
                    st.setString(1, LocationSerialization.serialLocation(p.getLocation()));
                    st.executeUpdate();
                    db.closeStatement(st);
                    spawn = p.getLocation();
                } catch (SQLException ex) {
                    ACLogger.severe("Error saving spawn!", ex);
                }
            }
        } else {
            p.getWorld().setSpawnLocation(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
        }
    }

    private static void createSpawn(Location s) {
        try {
            PreparedStatement pst = db.getPreparedStatement("INSERT INTO `ac_spawn` (`location`) VALUES (?);");
            pst.setString(1, LocationSerialization.serialLocation(s));
            pst.executeUpdate();
            db.closeStatement(pst);
            spawn = s;
        } catch (SQLException ex) {
            ACLogger.severe("Error creating spawn!", ex);
        }
    }

}

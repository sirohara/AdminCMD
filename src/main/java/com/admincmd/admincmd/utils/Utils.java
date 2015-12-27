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
package com.admincmd.admincmd.utils;

import com.admincmd.admincmd.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Utils {
    
    public static String replaceColors(String string) {
        return string.replaceAll("&((?i)[0-9a-fk-or])", "§$1");
    }
    
    public static void respawn(final Player p) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
                    Object packet = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".PacketPlayInClientCommand").newInstance();
                    Class<?> enumClass = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".EnumClientCommand");
                    for (Object o : enumClass.getEnumConstants()) {
                        if (o.toString().equals("PERFORM_RESPAWN")) {
                            packet = packet.getClass().getConstructor(enumClass).newInstance(o);
                        }
                    }
                    Object con = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
                    con.getClass().getMethod("a", packet.getClass()).invoke(con, packet);
                } catch (Throwable t) {
                    ACLogger.severe("Error sending respawn packet!", t);
                }
            }
        });
    }
    
}

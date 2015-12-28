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
package com.admincmd.admincmd.events;

import com.admincmd.admincmd.player.BukkitPlayer;
import com.admincmd.admincmd.player.PlayerManager;
import com.admincmd.admincmd.utils.BukkitListener;
import com.admincmd.admincmd.utils.Config;
import com.admincmd.admincmd.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerJoinListener extends BukkitListener {
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLogin(PlayerLoginEvent event) {
        if (!Config.MAINTENANCE_ENABLED.getBoolean()) {
            return;
        }
        
        if (!event.getPlayer().hasPermission("admincmd.maintenance.bypass")) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Utils.replaceColors(Config.MAINTENANCE_KICKMESSAGE.getString()));
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!PlayerManager.hasPlayedBefore(e.getPlayer())) {
            PlayerManager.insert(e.getPlayer());
        }
        
        if (Config.MAINTENANCE_ENABLED.getBoolean()) {
            if (!e.getPlayer().hasPermission("admincmd.maintenance.bypass")) {
                e.getPlayer().kickPlayer(Utils.replaceColors(Config.MAINTENANCE_KICKMESSAGE.getString()));
                return;
            }
        }
        
        final BukkitPlayer bp = PlayerManager.getPlayer(e.getPlayer());
        if (!bp.getNickname().equalsIgnoreCase("none")) {
            e.getPlayer().setDisplayName(bp.getNickname());
        }
        
        if (bp.isInvisible()) {
            for (Player op : Bukkit.getOnlinePlayers()) {
                op.hidePlayer(e.getPlayer());
            }
            e.setJoinMessage(null);
        }
        
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            e.getPlayer().setAllowFlight(bp.isFly());
            if (bp.isFly()) {
                e.getPlayer().setFlying(bp.isFly());
            }
        }
        
        for (Player op : Bukkit.getOnlinePlayers()) {
            BukkitPlayer ocp = PlayerManager.getPlayer(op);
            if (ocp == null) {
                continue;
            }
            if (ocp.isInvisible()) {
                e.getPlayer().hidePlayer(op);
            }
        }
    }
    
}

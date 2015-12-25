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
import com.admincmd.admincmd.utils.Locales;
import com.admincmd.admincmd.utils.Messager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandListener extends BukkitListener {

    @EventHandler(ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        BukkitPlayer p = PlayerManager.getPlayer(e.getPlayer());
        for (Player bp : Bukkit.getOnlinePlayers()) {
            BukkitPlayer bpc = PlayerManager.getPlayer(bp);
            if (!bpc.isCmdwatcher()) {
                continue;
            }
            if (p.getId() == bpc.getId()) {
                continue;
            }
            
            String message = Locales.PLAYER_CW_RAN.getString();
            message = message.replaceAll("%player%", e.getPlayer().getDisplayName());
            message = message.replaceAll("%command%", e.getMessage());
            
            Messager.sendMessage(bp, message, Messager.MessageType.INFO);
        }
    }

}

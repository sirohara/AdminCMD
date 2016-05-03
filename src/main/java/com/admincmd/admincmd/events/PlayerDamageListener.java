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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerDamageListener extends BukkitListener {

	@EventHandler(ignoreCancelled = true)
	public void onDamage(final EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		BukkitPlayer bp = PlayerManager.getPlayer(p);

		if (bp.isGod()) {
			e.setCancelled(true);
			e.setDamage(0.0);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onHunger(final FoodLevelChangeEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}

		Player p = (Player) e.getEntity();

		BukkitPlayer bp = PlayerManager.getPlayer(p);
		if (bp.isGod()) {
			e.setCancelled(true);
		}
	}

}

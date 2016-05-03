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
package com.admincmd.admincmd.update;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateListener implements Listener {

	private final Updater u;

	public UpdateListener(Updater u) {
		this.u = u;
	}

	/**
	 * EventHandler for Bukkit to inform ops when they join.
	 *
	 * @param e
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(final PlayerJoinEvent e) {
		if (!u.isEnabled())
			return;
		final Player p = e.getPlayer();
		if (!p.isOp())
			return;
		u.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(u.getPlugin(), new Runnable() {
			@Override
			public void run() {
				String[] data = u.read();
				if (u.checkForNewVersions(data)) {
					String prefix = "§a[" + u.getPlugin().getDescription().getName() + "]§7 ";
					p.sendMessage(prefix + "A new update is available! §e(" + data[1] + ")§7 current: §e"
							+ u.getPlugin().getDescription().getVersion());
					if (u.getUpdateType() == UpdateType.ANNOUNCE) {
						p.sendMessage(prefix + "You can get it at: §a" + u.getLink());
					} else {
						p.sendMessage(prefix
								+ "It will be downloaded for you and will be installed automatically when the server restarts.");
					}
				}
			}
		});
	}

}

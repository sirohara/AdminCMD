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

import com.admincmd.admincmd.utils.BukkitListener;
import com.admincmd.admincmd.utils.Locales;
import com.admincmd.admincmd.utils.Messager;
import com.admincmd.admincmd.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener extends BukkitListener {

	@EventHandler(ignoreCancelled = true)
	public void onSignChange(final SignChangeEvent e) {
		for (int i = 0; i < e.getLines().length; i++) {
			String line = e.getLines()[i];
			line = Utils.replaceColors(line);
			e.setLine(i, line);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onSignEdit(final SignChangeEvent e) {
		Player p = e.getPlayer();
		String[] lines = e.getLines();

		if (lines.length < 2) {
			return;
		}
		if (!lines[0].equalsIgnoreCase("[command]")) {
			return;
		}
		if (!p.hasPermission("admincmd.commandsign.create")) {
			Messager.sendMessage(p, Locales.COMMAND_MESSAGES_NO_PERMISSION.getString().replaceAll("%perm%",
					"admincmd.commandsign.create"), Messager.MessageType.NONE);
			return;
		}

		for (int i = 0; i < lines.length; i++) {
			ChatColor color = null;
			if (i == 0) {
				color = ChatColor.BLUE;
			} else if (i == 1) {
				color = ChatColor.GOLD;
			}

			String line;
			if (color != null) {
				line = color + lines[i];
			} else {
				line = lines[i];
			}

			if (line.length() > 16) {
				e.getBlock().breakNaturally();
				Messager.sendMessage(p, "The line is too long to fit on the sign!", Messager.MessageType.ERROR);
				break;
			}

			e.setLine(i, line);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onSignCLick(final PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		if (!(e.getClickedBlock().getState() instanceof Sign)) {
			return;
		}
		Sign s = (Sign) e.getClickedBlock().getState();
		List<String> lines = new ArrayList<>();

		for (String string : s.getLines()) {
			lines.add(Utils.removeColors(string));
		}

		if (lines.size() < 2) {
			return;
		}
		if (!e.getPlayer().hasPermission("admincmd.commandsign.use")) {
			Messager.sendMessage(e.getPlayer(),
					Locales.COMMAND_MESSAGES_NO_PERMISSION.getString().replaceAll("%perm%", "admincmd.commandsign.use"),
					Messager.MessageType.NONE);
			return;
		}

		if (!lines.get(0).equalsIgnoreCase("[command]")) {
			return;
		}

		String command = lines.get(1);
		e.getPlayer().performCommand(command.replaceFirst("/", ""));
	}

}

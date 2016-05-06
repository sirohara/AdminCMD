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
package com.admincmd.admincmd.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.admincmd.admincmd.commandapi.BaseCommand;
import com.admincmd.admincmd.commandapi.CommandArgs;
import com.admincmd.admincmd.commandapi.CommandHandler;
import com.admincmd.admincmd.commandapi.CommandResult;
import com.admincmd.admincmd.commandapi.HelpPage;
import com.admincmd.admincmd.home.Home;
import com.admincmd.admincmd.home.WarpPointManager;
import com.admincmd.admincmd.utils.Locales;
import com.admincmd.admincmd.utils.Messager;
import com.admincmd.admincmd.utils.Messager.MessageType;
import com.google.common.base.Joiner;

@CommandHandler
public class TeleportCommands {

	private final Map<String, HelpPage> helpPages = new HashMap<>();

	public TeleportCommands() {
		helpPages.put("twp", new HelpPage("twp", "<name>"));
		helpPages.put("wp", new HelpPage("wp", "<name>"));
		helpPages.put("rwp", new HelpPage("rwp", "<name>"));
		helpPages.put("lwp", new HelpPage("lwp", "<>"));
	}

	@BaseCommand(command = "twp", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.warp.tp", aliases = "twarp")
	public CommandResult executeTWP(Player p, CommandArgs args) {
		if (helpPages.get("twp").sendHelp(p, args)) {
			return CommandResult.SUCCESS;
		}

		if (args.getLength() > 1) {
			return CommandResult.ERROR;
		}

		if (args.isEmpty()) {
			String wps = Locales.WARP_LISTWPS.getString() + " (" + WarpPointManager.getWPs().size() + "): §6"
					+ Joiner.on(", ").join(WarpPointManager.getWPs().keySet());
			return Messager.sendMessage(p, wps, MessageType.INFO);
		} else {
			if (args.getString(0).contains(":")) {
				return Messager.sendMessage(p, "world specifier ':' is not supported.", MessageType.INFO);
			}

			Home wp = WarpPointManager.getWP(args.getString(0));
			if (wp != null) {
				wp.teleport(p);
				String msg = Locales.WARP_TP.getString().replaceAll("%wp%", wp.getName());
				Messager.sendMessage(p, msg, MessageType.INFO);
				return CommandResult.SUCCESS;
			} else {
				return Messager.sendMessage(p, Locales.WARP_NOWP, MessageType.ERROR);
			}
		}
	}

	@BaseCommand(command = "wp", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.warp.create", aliases = "cwarp")
	public CommandResult executeWP(Player p, CommandArgs args) {
		if (helpPages.get("wp").sendHelp(p, args)) {
			return CommandResult.SUCCESS;
		}

		if (args.getLength() != 1) {
			return CommandResult.ERROR;
		}

		String name = args.getString(0);
		Home wp = WarpPointManager.getWP(name);
		if (wp != null) {
			return Messager.sendMessage(p, Locales.WARP_ALREADY_EXISTING, MessageType.ERROR);
		}

		WarpPointManager.createWP(p, name);
		return Messager.sendMessage(p, Locales.WARP_SET, MessageType.INFO);
	}

	@BaseCommand(command = "rwp", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.warp.create", aliases = "rwarp")
	public CommandResult executeRWP(Player p, CommandArgs args) {
		if (helpPages.get("rwp").sendHelp(p, args)) {
			return CommandResult.SUCCESS;
		}

		if (args.getLength() != 1) {
			return CommandResult.ERROR;
		}

		String name = args.getString(0);
		Home wp = WarpPointManager.getWP(name);
		if (wp == null) {
			return Messager.sendMessage(p, Locales.WARP_NOWP, MessageType.ERROR);
		}

		WarpPointManager.deleteWP(name);
		String msg = Locales.WARP_DELETED.getString().replaceAll("%wp%", name);
		return Messager.sendMessage(p, msg, MessageType.INFO);
	}

	@BaseCommand(command = "lwp", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.warp.tp", aliases = "lwarp")
	public CommandResult executeLWP(Player p, CommandArgs args) {
		if (helpPages.get("lwp").sendHelp(p, args)) {
			return CommandResult.SUCCESS;
		}

		if (args.getLength() > 1) {
			return CommandResult.ERROR;
		}

		if (!args.isEmpty()) {
			if (args.getString(0).equals("-a")) {
				Messager.sendMessage(p, "-a option is not support (constantly specified -a).", MessageType.INFO);
			} else {
				return CommandResult.ERROR;
			}
		}

		HashMap<String, Home> wps = WarpPointManager.getWPs();
		String validwps = "";
		int cnt = 0;
		for (final String name : wps.keySet()) {
			if (wps.get(name) != null) {
				validwps += name + " ";
				cnt++;
			}
		}

		String msg = Locales.WARP_LISTWPS.getString() + " (" + cnt + "): §6" + validwps;
		return Messager.sendMessage(p, msg, MessageType.INFO);
	}

}

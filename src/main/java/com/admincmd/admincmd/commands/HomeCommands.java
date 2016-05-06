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
import com.admincmd.admincmd.home.HomeManager;
import com.admincmd.admincmd.utils.Config;
import com.admincmd.admincmd.utils.Locales;
import com.admincmd.admincmd.utils.Messager;
import com.admincmd.admincmd.utils.Messager.MessageType;
import com.google.common.base.Joiner;

@CommandHandler
public class HomeCommands {

	private final Map<String, HelpPage> helpPages = new HashMap<>();

	public HomeCommands() {
		helpPages.put("home", new HelpPage("home", "", "<name>"));
		helpPages.put("sethome", new HelpPage("sethome", "<name>"));
		helpPages.put("delhome", new HelpPage("delhome", "<name>"));
		helpPages.put("edithome", new HelpPage("edithome", "<name>"));
		helpPages.put("listhome", new HelpPage("listhome", "<name>"));
	}

	@BaseCommand(command = "home", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.home.home", aliases = "h")
	public CommandResult executeHome(Player p, CommandArgs args) {
		if (helpPages.get("home").sendHelp(p, args)) {
			return CommandResult.SUCCESS;
		}

		if (args.getLength() > 1) {
			return CommandResult.ERROR;
		}

		if (args.isEmpty()) {
			String homes = Locales.HOME_HOME.getString() + " (" + HomeManager.getHomes(p).size() + "): §6"
					+ Joiner.on(", ").join(HomeManager.getHomes(p).keySet());
			return Messager.sendMessage(p, homes, MessageType.INFO);
		} else {
			Home h = HomeManager.getHome(p, args.getString(0));
			if (h != null) {
				p.teleport(h.getLocation());
				String msg = Locales.HOME_MULTI.getString().replaceAll("%home%", h.getName());
				Messager.sendMessage(p, msg, MessageType.INFO);
				return CommandResult.SUCCESS;
			} else {
				return Messager.sendMessage(p, Locales.HOME_NOHOME, MessageType.ERROR);
			}
		}
	}

	@BaseCommand(command = "sethome", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.home.create", aliases = "sh")
	public CommandResult executeSethome(Player sender, CommandArgs args) {
		if (helpPages.get("sethome").sendHelp(sender, args)) {
			return CommandResult.SUCCESS;
		}

		if (args.getLength() != 1) {
			return CommandResult.ERROR;
		}

		Home h = HomeManager.getHome(sender, args.getString(0));
		if (h != null) {
			return Messager.sendMessage(sender, Locales.HOME_ALREADY_EXISTING, MessageType.ERROR);
		}

		if (Config.HOME_LIMIT.getInteger() <= HomeManager.getHomes(sender).size()){
			return Messager.sendMessage(sender, Locales.HOME_LIMIT, MessageType.ERROR);
		}

		HomeManager.createHome(sender, args.getString(0));
		String msg = Locales.HOME_SET.getString().replaceAll("%home%", args.getString(0));
		return Messager.sendMessage(sender, msg, MessageType.INFO);
	}

	@BaseCommand(command = "delhome", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.home.create", aliases = "rh")
	public CommandResult executeRemovehome(Player sender, CommandArgs args) {
		if (helpPages.get("delhome").sendHelp(sender, args)) {
			return CommandResult.SUCCESS;
		}

		if (args.getLength() != 1) {
			return CommandResult.ERROR;
		}

		Home h = HomeManager.getHome(sender, args.getString(0));
		if (h == null) {
			return Messager.sendMessage(sender, Locales.HOME_NOHOME, MessageType.ERROR);
		}

		HomeManager.deleteHome(h);
		String msg = Locales.HOME_DELETED.getString().replaceAll("%home%", h.getName());
		return Messager.sendMessage(sender, msg, MessageType.INFO);
	}

	@BaseCommand(command = "edithome", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.home.create", aliases = "eh")
	public CommandResult executeEdithome(Player sender, CommandArgs args) {
		if (helpPages.get("edithome").sendHelp(sender, args)) {
			return CommandResult.SUCCESS;
		}

		if (args.getLength() != 1) {
			return CommandResult.ERROR;
		}

		Home h = HomeManager.getHome(sender, args.getString(0));

		if (h == null) {
			return Messager.sendMessage(sender, Locales.HOME_NOHOME, MessageType.ERROR);
		}

		h.updateLocation(sender.getLocation());
		return Messager.sendMessage(sender, Locales.HOME_UPDATED, MessageType.INFO);
	}

	@BaseCommand(command = "listhome", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.home.home", aliases = "lh")
	public CommandResult executeListHome(Player p, CommandArgs args) {
		if (helpPages.get("listhome").sendHelp(p, args)) {
			return CommandResult.SUCCESS;
		}

		if (args.getLength() != 0) {
			return CommandResult.ERROR;
		}

		String homes = Locales.HOME_LISTHOME.getString() + " (" + HomeManager.getHomes(p).size() + "): §6"
				+ Joiner.on(" ").join(HomeManager.getHomes(p).keySet());
		return Messager.sendMessage(p, homes, MessageType.INFO);
	}
}

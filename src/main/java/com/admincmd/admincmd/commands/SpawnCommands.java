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

import com.admincmd.admincmd.commandapi.BaseCommand;
import com.admincmd.admincmd.commandapi.CommandArgs;
import com.admincmd.admincmd.commandapi.CommandArgs.Flag;
import com.admincmd.admincmd.commandapi.CommandHandler;
import com.admincmd.admincmd.commandapi.CommandResult;
import com.admincmd.admincmd.commandapi.HelpPage;
import com.admincmd.admincmd.spawn.SpawnManager;
import com.admincmd.admincmd.utils.Locales;
import com.admincmd.admincmd.utils.Messager;
import com.admincmd.admincmd.utils.Utils;
import org.bukkit.entity.Player;

@CommandHandler
public class SpawnCommands {

	private final HelpPage setspawn = new HelpPage("setspawn", "");
	private final HelpPage spawn = new HelpPage("spawn", "", "<-p player>");

	@BaseCommand(command = "setspawn", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.spawn.setspawn")
	public CommandResult executeSetspawn(Player sender, CommandArgs args) {
		if (setspawn.sendHelp(sender, args)) {
			return CommandResult.SUCCESS;
		}

		if (!args.isEmpty()) {
			return CommandResult.ERROR;
		}

		SpawnManager.setSpawn(sender);
		return Messager.sendMessage(sender, Locales.SPAWN_SET, Messager.MessageType.INFO);
	}

	@BaseCommand(command = "spawn", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.spawn.spawn")
	public CommandResult executeSpawn(Player sender, CommandArgs args) {
		if (spawn.sendHelp(sender, args)) {
			return CommandResult.SUCCESS;
		}

		if (args.isEmpty()) {
			sender.teleport(SpawnManager.getSpawn(sender));
			return Messager.sendMessage(sender, Locales.SPAWN_TP, Messager.MessageType.INFO);
		} else if (args.getLength() == 1) {
			if (!args.hasFlag("p")) {
				return CommandResult.ERROR;
			}

			if (!sender.hasPermission("admincmd.spawn.spawn.other")) {
				return CommandResult.NO_PERMISSION_OTHER;
			}

			Flag f = args.getFlag("p");
			if (!f.isPlayer()) {
				return CommandResult.NOT_ONLINE;
			}
			Player t = f.getPlayer();
			t.teleport(SpawnManager.getSpawn(t));
			Messager.sendMessage(t, Locales.SPAWN_TP, Messager.MessageType.INFO);
			Messager.sendMessage(sender,
					Locales.SPAWN_TP_OTHER.getString().replaceAll("%player%", Utils.replacePlayerPlaceholders(t)),
					Messager.MessageType.INFO);
			return CommandResult.SUCCESS;
		} else {
			return CommandResult.ERROR;
		}
	}

}

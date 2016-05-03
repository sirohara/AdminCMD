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
import com.admincmd.admincmd.utils.Locales;
import com.admincmd.admincmd.utils.Messager;
import com.admincmd.admincmd.world.ACWorld;
import com.admincmd.admincmd.world.WorldManager;
import org.bukkit.World;
import org.bukkit.entity.Player;

@CommandHandler
public class WorldCommands {

	private final HelpPage day = new HelpPage("day", "", "<-w world>");
	private final HelpPage night = new HelpPage("night", "", "<-w world>");
	private final HelpPage time = new HelpPage("time", "day <-w world>", "night <-w world>", "time <-w world>",
			"pause <-w world>", "unpause <-w world>");
	private final HelpPage sun = new HelpPage("sun", "", "<-w world>");

	@BaseCommand(command = "sun", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.world.sun")
	public CommandResult executeSun(Player sender, CommandArgs args) {
		if (sun.sendHelp(sender, args)) {
			return CommandResult.SUCCESS;
		}

		if (args.isEmpty()) {
			sender.getWorld().setStorm(false);
			sender.getWorld().setThundering(false);
			String msg = Locales.WORLD_WEATHER_CLEAR.getString().replaceAll("%world%", sender.getWorld().getName());
			return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
		}

		if (args.hasFlag("w")) {
			Flag flag = args.getFlag("w");
			if (!flag.isWorld()) {
				return CommandResult.NOT_A_WORLD;
			}

			if (!sender.hasPermission("admincmd.world.sun.other")) {
				return CommandResult.NO_PERMISSION_OTHER;
			}

			ACWorld world = flag.getWorld();
			world.getWorld().setStorm(false);
			world.getWorld().setThundering(false);
			String msg = Locales.WORLD_WEATHER_CLEAR.getString().replaceAll("%world%", world.getWorld().getName());
			return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
		}

		return CommandResult.ERROR;
	}

	@BaseCommand(command = "time", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.world.time")
	public CommandResult executeTime(Player sender, CommandArgs args) {
		if (time.sendHelp(sender, args)) {
			return CommandResult.SUCCESS;
		}

		if (args.getLength() < 1) {
			return CommandResult.ERROR;
		}

		World target = sender.getWorld();
		if (args.hasFlag("w")) {
			if (!args.getFlag("w").isWorld()) {
				return CommandResult.NOT_A_WORLD;
			}
			if (!sender.hasPermission("admincmd.world.time.other")) {
				return CommandResult.NO_PERMISSION_OTHER;
			}
			target = args.getFlag("w").getWorld().getWorld();
		}

		if (args.getString(0).equalsIgnoreCase("day")) {
			long time = 0;
			target.setTime(time);
			String msg = Locales.WORLD_DAY_SET.getString().replaceAll("%world%", target.getName());
			return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
		} else if (args.getString(0).equalsIgnoreCase("night")) {
			long time = 13100;
			target.setTime(time);
			String msg = Locales.WORLD_NIGHT_SET.getString().replaceAll("%world%", target.getName());
			return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
		} else if (args.isInteger(0)) {
			long time = args.getInt(0);
			target.setTime(time);
			String msg = Locales.WORLD_TIME_SET.getString().replaceAll("%world%", target.getName()).replaceAll("%time%",
					time + "");
			return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
		} else if (args.getString(0).equalsIgnoreCase("pause")) {
			ACWorld w = WorldManager.getWorld(target);
			w.pauseTime();
			String msg = Locales.WORLD_TIME_PAUSED.getString().replaceAll("%world%", target.getName());
			return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
		} else if (args.getString(0).equalsIgnoreCase("unpause")) {
			ACWorld w = WorldManager.getWorld(target);
			w.unPauseTime();
			String msg = Locales.WORLD_TIME_UNPAUSED.getString().replaceAll("%world%", target.getName());
			return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
		}

		return CommandResult.ERROR;
	}

	@BaseCommand(command = "day", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.world.day")
	public CommandResult executeDay(Player sender, CommandArgs args) {
		if (day.sendHelp(sender, args)) {
			return CommandResult.SUCCESS;
		}

		if (args.isEmpty()) {
			long time = 0;
			sender.getWorld().setTime(time);
			String msg = Locales.WORLD_DAY_SET.getString().replaceAll("%world%", sender.getWorld().getName());
			return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
		}

		if (args.hasFlag("w")) {
			Flag flag = args.getFlag("w");
			if (!flag.isWorld()) {
				return CommandResult.NOT_A_WORLD;
			}

			if (!sender.hasPermission("admincmd.world.day.other")) {
				return CommandResult.NO_PERMISSION_OTHER;
			}

			ACWorld world = flag.getWorld();
			long time = 0;
			world.getWorld().setTime(time);
			String msg = Locales.WORLD_DAY_SET.getString().replaceAll("%world%", world.getWorld().getName());
			return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
		}

		return CommandResult.ERROR;
	}

	@BaseCommand(command = "night", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.world.night")
	public CommandResult executeNight(Player sender, CommandArgs args) {
		if (night.sendHelp(sender, args)) {
			return CommandResult.SUCCESS;
		}

		if (args.isEmpty()) {
			long time = 13100;
			sender.getWorld().setTime(time);
			String msg = Locales.WORLD_NIGHT_SET.getString().replaceAll("%world%", sender.getWorld().getName());
			return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
		}

		if (args.hasFlag("w")) {
			Flag flag = args.getFlag("w");
			if (!flag.isWorld()) {
				return CommandResult.NOT_A_WORLD;
			}

			if (!sender.hasPermission("admincmd.world.night.other")) {
				return CommandResult.NO_PERMISSION_OTHER;
			}

			ACWorld world = flag.getWorld();
			long time = 13100;
			world.getWorld().setTime(time);
			String msg = Locales.WORLD_NIGHT_SET.getString().replaceAll("%world%", world.getWorld().getName());
			return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
		}

		return CommandResult.ERROR;
	}

}

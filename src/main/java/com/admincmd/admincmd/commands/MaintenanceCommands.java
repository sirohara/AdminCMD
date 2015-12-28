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

import com.admincmd.admincmd.Main;
import com.admincmd.admincmd.commandapi.BaseCommand;
import com.admincmd.admincmd.commandapi.CommandArgs;
import com.admincmd.admincmd.commandapi.CommandHandler;
import com.admincmd.admincmd.commandapi.CommandResult;
import com.admincmd.admincmd.commandapi.HelpPage;
import com.admincmd.admincmd.utils.Config;
import com.admincmd.admincmd.utils.Locales;
import com.admincmd.admincmd.utils.Messager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandHandler
public class MaintenanceCommands {

    private final HelpPage maintenance = new HelpPage("maintenance", "on", "off");

    @BaseCommand(command = "maintenance", sender = BaseCommand.Sender.CONSOLE, permission = "admincmd.maintenance.enable")
    public CommandResult executeConsole(CommandSender sender, CommandArgs args) {
        return executePlayer(sender, args);
    }

    @BaseCommand(command = "maintenance", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.maintenance.enable")
    public CommandResult executePlayer(CommandSender sender, CommandArgs args) {
        if (maintenance.sendHelp(sender, args)) {
            return CommandResult.SUCCESS;
        }

        if (args.getLength() != 1) {
            return CommandResult.ERROR;
        }

        if (!Main.getInstance().checkForProtocolLib()) {
            return Messager.sendMessage(sender, "ProtocolLib is not installed. Maintenance feature is not available.", Messager.MessageType.ERROR);
        }

        if (args.getString(0).equalsIgnoreCase("on")) {
            Config.MAINTENANCE_ENABLED.set(true, true);
            if (Config.MAINTENANCE_KICK.getBoolean()) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("admincmd.maintenance.bypass")) {
                        continue;
                    }
                    p.kickPlayer(Config.MAINTENANCE_KICKMESSAGE.getString());
                }
            }
            return Messager.sendMessage(sender, Locales.MAINTENANCE_TOGGLED.getString().replaceAll("%status%", "enabled"), Messager.MessageType.INFO);
        } else if (args.getString(0).equalsIgnoreCase("off")) {
            Config.MAINTENANCE_ENABLED.set(false, true);
            return Messager.sendMessage(sender, Locales.MAINTENANCE_TOGGLED.getString().replaceAll("%status%", "disabled"), Messager.MessageType.INFO);
        }

        return CommandResult.ERROR;
    }

}

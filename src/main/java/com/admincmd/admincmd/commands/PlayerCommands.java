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
import com.admincmd.admincmd.player.BukkitPlayer;
import com.admincmd.admincmd.player.PlayerManager;
import com.admincmd.admincmd.utils.Locales;
import com.admincmd.admincmd.utils.Messager;
import com.admincmd.admincmd.utils.Utils;
import com.comphenix.net.sf.cglib.core.Local;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandHandler
public class PlayerCommands {

    private final HelpPage cw = new HelpPage("cw", "", "<-p player>");
    private final HelpPage fly = new HelpPage("fly", "", "<-p player>");
    private final HelpPage god = new HelpPage("god", "", "<-p player>");
    private final HelpPage enderchest = new HelpPage("enderchest", "", "<-p player>");
    private final HelpPage gm = new HelpPage("gamemode", "", "<-p player>", "<0|1|2|3>", "<-p player> <0|1|2|3>");
    private final HelpPage heal = new HelpPage("heal", "", "<-p player>");
    private final HelpPage ip = new HelpPage("ip", "", "<-p player>");

    //TODO: Console execution
    @BaseCommand(command = "gamemode", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.player.gamemode", aliases = "gm")
    public CommandResult executeGamemode(Player sender, CommandArgs args) {
        if (gm.sendHelp(sender, args)) {
            return CommandResult.SUCCESS;
        }

        if (args.isEmpty()) {
            GameMode gm = sender.getGameMode() == GameMode.SURVIVAL ? GameMode.CREATIVE : GameMode.SURVIVAL;
            sender.setGameMode(gm);
            String msg = Locales.PLAYER_GAMEMODE_CHANGED.getString().replaceAll("%status%", gm.toString());
            return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
        }

        if (args.hasFlag("p")) {
            if (!sender.hasPermission("admincmd.player.gamemode.other")) {
                return CommandResult.NO_PERMISSION_OTHER;
            }
            Flag flag = args.getFlag("p");
            if (!flag.isPlayer()) {
                return CommandResult.NOT_ONLINE;
            }

            Player target = flag.getPlayer();
            if (args.getLength() == 2) {
                GameMode gm = target.getGameMode() == GameMode.SURVIVAL ? GameMode.CREATIVE : GameMode.SURVIVAL;
                target.setGameMode(gm);
                String msg = Locales.PLAYER_GAMEMODE_CHANGED.getString().replaceAll("%status%", gm.toString());
                Messager.sendMessage(target, msg, Messager.MessageType.INFO);

                String msg2 = Locales.PLAYER_GAMEMODE_CHANGED_OTHER.getString().replaceAll("%player%", Utils.replacePlayerPlaceholders(target)).replaceAll("%status%", gm.toString());
                Messager.sendMessage(sender, msg2, Messager.MessageType.INFO);
                return CommandResult.SUCCESS;
            } else if (args.getLength() == 3) {
                if (!args.isInteger(2)) {
                    return CommandResult.NOT_A_NUMBER;
                }
                int num = args.getInt(2);
                GameMode gm = GameMode.getByValue(num);
                target.setGameMode(gm);
                String msg = Locales.PLAYER_GAMEMODE_CHANGED.getString().replaceAll("%status%", gm.toString());
                Messager.sendMessage(target, msg, Messager.MessageType.INFO);

                String msg2 = Locales.PLAYER_GAMEMODE_CHANGED_OTHER.getString().replaceAll("%player%", Utils.replacePlayerPlaceholders(target)).replaceAll("%status%", gm.toString());
                Messager.sendMessage(sender, msg2, Messager.MessageType.INFO);
                return CommandResult.SUCCESS;
            } else {
                return CommandResult.ERROR;
            }
        } else {
            if (args.getLength() == 1) {
                if (!args.isInteger(0)) {
                    return CommandResult.NOT_A_NUMBER;
                }
                int num = args.getInt(0);
                GameMode gm = GameMode.getByValue(num);
                sender.setGameMode(gm);
                String msg = Locales.PLAYER_GAMEMODE_CHANGED.getString().replaceAll("%status%", gm.toString());
                return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
            }
        }

        return CommandResult.ERROR;
    }

    @BaseCommand(command = "enderchest", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.player.enderchest", aliases = "ec")
    public CommandResult executeEnderchest(Player sender, CommandArgs args) {
        if (enderchest.sendHelp(sender, args)) {
            return CommandResult.SUCCESS;
        }
        if (args.isEmpty()) {
            sender.openInventory(sender.getEnderChest());
            return CommandResult.SUCCESS;
        }

        if (args.hasFlag("p")) {
            if (!sender.hasPermission("admincmd.player.enderchest.other")) {
                return CommandResult.NO_PERMISSION_OTHER;
            }
            Flag flag = args.getFlag("p");
            if (!flag.isPlayer()) {
                return CommandResult.NOT_ONLINE;
            }

            Player target = flag.getPlayer();
            sender.openInventory(target.getEnderChest());
            return CommandResult.SUCCESS;
        }

        return CommandResult.ERROR;
    }

    @BaseCommand(command = "god", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.player.god", aliases = "g,gg")
    public CommandResult executeGod(Player sender, CommandArgs args) {
        if (god.sendHelp(sender, args)) {
            return CommandResult.SUCCESS;
        }
        if (args.isEmpty()) {
            BukkitPlayer p = PlayerManager.getPlayer(sender);
            p.setGod(!p.isGod());

            String s = p.isGod() ? Locales.COMMAND_MESSAGES_ENABLED.getString() : Locales.COMMAND_MESSAGES_DISABLED.getString();
            String msg = Locales.PLAYER_GOD_TOGGLED.getString().replaceAll("%status%", s);
            return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
        }

        if (args.hasFlag("p")) {
            if (!sender.hasPermission("admincmd.player.god.other")) {
                return CommandResult.NO_PERMISSION_OTHER;
            }
            Flag flag = args.getFlag("p");
            if (!flag.isPlayer()) {
                return CommandResult.NOT_ONLINE;
            }

            Player target = flag.getPlayer();
            BukkitPlayer p = PlayerManager.getPlayer(target);
            p.setGod(!p.isGod());

            String s = p.isGod() ? Locales.COMMAND_MESSAGES_ENABLED.getString() : Locales.COMMAND_MESSAGES_DISABLED.getString();
            String msg = Locales.PLAYER_GOD_TOGGLED_OTHER.getString().replaceAll("%status%", s).replaceAll("%player%", Utils.replacePlayerPlaceholders(target));
            String msg2 = Locales.PLAYER_GOD_TOGGLED.getString().replaceAll("%status%", s);
            Messager.sendMessage(target, msg2, Messager.MessageType.INFO);
            return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
        }

        return CommandResult.ERROR;
    }

    @BaseCommand(command = "fly", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.player.fly")
    public CommandResult executeFly(Player sender, CommandArgs args) {
        if (fly.sendHelp(sender, args)) {
            return CommandResult.SUCCESS;
        }
        if (args.isEmpty()) {
            BukkitPlayer p = PlayerManager.getPlayer(sender);
            p.setFly(!p.isFly());

            sender.setAllowFlight(p.isFly());
            String s = p.isFly() ? Locales.COMMAND_MESSAGES_ENABLED.getString() : Locales.COMMAND_MESSAGES_DISABLED.getString();
            String msg = Locales.PLAYER_FLY_TOGGLED.getString().replaceAll("%status%", s);
            return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
        }

        if (args.hasFlag("p")) {
            if (!sender.hasPermission("admincmd.player.fly.other")) {
                return CommandResult.NO_PERMISSION_OTHER;
            }
            Flag flag = args.getFlag("p");
            if (!flag.isPlayer()) {
                return CommandResult.NOT_ONLINE;
            }

            Player target = flag.getPlayer();
            BukkitPlayer p = PlayerManager.getPlayer(target);
            p.setFly(!p.isFly());

            target.setAllowFlight(p.isFly());
            String s = p.isFly() ? Locales.COMMAND_MESSAGES_ENABLED.getString() : Locales.COMMAND_MESSAGES_DISABLED.getString();
            String msg = Locales.PLAYER_FLY_TOGGLED_OTHER.getString().replaceAll("%status%", s).replaceAll("%player%", Utils.replacePlayerPlaceholders(target));
            String msg2 = Locales.PLAYER_FLY_TOGGLED.getString().replaceAll("%status%", s);
            Messager.sendMessage(target, msg2, Messager.MessageType.INFO);
            return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
        }

        return CommandResult.ERROR;
    }

    @BaseCommand(command = "cw", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.player.cw", aliases = "cmdwatcher,commandwatcher")
    public CommandResult executeCW(Player sender, CommandArgs args) {
        if (cw.sendHelp(sender, args)) {
            return CommandResult.SUCCESS;
        }
        if (args.isEmpty()) {
            BukkitPlayer p = PlayerManager.getPlayer(sender);
            p.setCmdwatcher(!p.isCmdwatcher());
            String s = p.isCmdwatcher() ? Locales.COMMAND_MESSAGES_ENABLED.getString() : Locales.COMMAND_MESSAGES_DISABLED.getString();
            String msg = Locales.PLAYER_CW_TOGGLED.getString().replaceAll("%status%", s);
            return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
        }

        if (args.hasFlag("p")) {
            if (!sender.hasPermission("admincmd.player.cw.other")) {
                return CommandResult.NO_PERMISSION_OTHER;
            }
            Flag flag = args.getFlag("p");
            if (!flag.isPlayer()) {
                return CommandResult.NOT_ONLINE;
            }

            Player target = flag.getPlayer();
            BukkitPlayer p = PlayerManager.getPlayer(target);
            p.setCmdwatcher(!p.isCmdwatcher());
            String s = p.isFly() ? Locales.COMMAND_MESSAGES_ENABLED.getString() : Locales.COMMAND_MESSAGES_DISABLED.getString();
            String msg = Locales.PLAYER_CW_TOGGLED_OTHER.getString().replaceAll("%status%", s).replaceAll("%player%", Utils.replacePlayerPlaceholders(target));
            String msg2 = Locales.PLAYER_CW_TOGGLED.getString().replaceAll("%status%", s);
            Messager.sendMessage(target, msg2, Messager.MessageType.INFO);
            return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
        }

        return CommandResult.ERROR;

    }

    @BaseCommand(command = "heal", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.player.heal", aliases = "pheal")
    public CommandResult executeHeal(Player sender, CommandArgs args) {
        if (heal.sendHelp(sender, args)) {
            return CommandResult.SUCCESS;
        }

        if (args.isEmpty()) {
            sender.setHealth(sender.getMaxHealth());
            return Messager.sendMessage(sender, Locales.PLAYER_HEAL_SELF, Messager.MessageType.INFO);
        }

        if (args.hasFlag("p")) {
            if (!sender.hasPermission("admincmd.player.heal.other")) {
                return CommandResult.NO_PERMISSION_OTHER;
            }

            Flag flag = args.getFlag("p");
            if (!flag.isPlayer()) {
                return CommandResult.NOT_ONLINE;
            }

            Player target = flag.getPlayer();
            target.setHealth(target.getMaxHealth());
            String msgSender = Locales.PLAYER_HEAL_OTHER.getString().replaceAll("%player%", Utils.replacePlayerPlaceholders(target));
            Messager.sendMessage(sender, msgSender, Messager.MessageType.INFO);
            return Messager.sendMessage(target, Locales.PLAYER_HEAL_SELF, Messager.MessageType.INFO);
        }

        return CommandResult.ERROR;
    }

    @BaseCommand(command = "ip", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.player.ip", aliases = "")
    public CommandResult executeIp(Player sender, CommandArgs args) {
        if (ip.sendHelp(sender, args)) {
            return CommandResult.NO_PERMISSION_OTHER;
        }

        if (args.isEmpty()) {
            String ip = sender.getAddress().getAddress().toString();
            String msg = Locales.PLAYER_IP_SELF.getString().replaceAll("%ip", ip);
            return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
        }

        if (args.hasFlag("p")) {
            if (!sender.hasPermission("admincmd.player.ip.other")) {
                return CommandResult.NO_PERMISSION_OTHER;
            }

            Flag flag = args.getFlag("p");
            if (!flag.isPlayer()) {
                return CommandResult.NOT_ONLINE;
            }

            Player target = flag.getPlayer();
            String ip = target.getAddress().getAddress().toString();
            String msg = Locales.PLAYER_IP_OTHER.getString().replaceAll("%player%", Utils.replacePlayerPlaceholders(target)).replaceAll("%ip%", ip);
            return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
        }

        return CommandResult.ERROR;

    }

}

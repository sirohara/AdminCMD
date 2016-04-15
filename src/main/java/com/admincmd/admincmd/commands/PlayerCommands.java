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
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.text.DecimalFormat;
import org.bukkit.command.CommandSender;

@CommandHandler
public class PlayerCommands {

    private final HelpPage cw = new HelpPage("cw", "", "<-p player>");
    private final HelpPage fly = new HelpPage("fly", "", "<-p player>");
    private final HelpPage god = new HelpPage("god", "", "<-p player>");
    private final HelpPage enderchest = new HelpPage("enderchest", "", "<-p player>");
    private final HelpPage gm = new HelpPage("gamemode", "", "<-p player>", "<0|1|2|3>", "<-p player> <0|1|2|3>");
    private final HelpPage heal = new HelpPage("heal", "", "<-p player>");
    private final HelpPage ip = new HelpPage("ip", "", "<-p player>");
    private final HelpPage openinv = new HelpPage("openinv", "", "<-p player>");
    private final HelpPage loc = new HelpPage("loc", "", "<-p player>");
    private final HelpPage msg = new HelpPage("msg", "", "<player> <message>");
    private final HelpPage reply = new HelpPage("reply", "", "<message>");
    private final HelpPage spy = new HelpPage("spy", "", "<-p player>");
    private final HelpPage list = new HelpPage("who", "");
    private final HelpPage vanish = new HelpPage("vanish", "", "<-p player>");
    private final HelpPage mute = new HelpPage("mute", "", "<player>");
    private final HelpPage unmute = new HelpPage("unmute", "", "<player>");

    @BaseCommand(command = "gamemode", sender = BaseCommand.Sender.CONSOLE, aliases = "gm")
    public CommandResult executeGamemodeConsole(CommandSender sender, CommandArgs args) {
        if (gm.sendHelp(sender, args)) {
            return CommandResult.SUCCESS;
        }

        if (args.isEmpty()) {
            return CommandResult.NOT_ONLINE;
        }

        if (args.hasFlag("p")) {
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
        }
        return CommandResult.ERROR;
    }

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

    @BaseCommand(command = "openinv", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.player.openinv", aliases = "invsee,oi")
    public CommandResult executeOpenInv(Player sender, CommandArgs args) {
        if (openinv.sendHelp(sender, args)) {
            return CommandResult.NO_PERMISSION_OTHER;
        }

        if (args.isEmpty()) {
            sender.openInventory(sender.getInventory());
            return CommandResult.SUCCESS;
        }

        if (args.hasFlag("p")) {
            if (!sender.hasPermission("admincmd.player.openinv.other")) {
                return CommandResult.NO_PERMISSION_OTHER;
            }

            Flag flag = args.getFlag("p");
            if (!flag.isPlayer()) {
                return CommandResult.NOT_ONLINE;
            }

            Player target = flag.getPlayer();
            sender.openInventory(target.getInventory());
            return CommandResult.SUCCESS;
        }

        return CommandResult.ERROR;

    }

    @BaseCommand(command = "loc", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.player.loc", aliases = "location,coords")
    public CommandResult executeLocation(Player sender, CommandArgs args) {
        if (loc.sendHelp(sender, args)) {
            return CommandResult.SUCCESS;
        }

        if (args.isEmpty()) {
            Location loc = sender.getLocation();
            DecimalFormat decimalFormat = new DecimalFormat("##.#");
            String msg = Locales.PLAYER_LOCATION_SELF.getString().replaceAll("%x%", decimalFormat.format(loc.getX()));
            msg = msg.replaceAll("%y%", decimalFormat.format(loc.getY()));
            msg = msg.replaceAll("%z%", decimalFormat.format(loc.getZ()));
            return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
        }

        if (args.hasFlag("p")) {
            if (!sender.hasPermission("admincmd.player.loc.other")) {
                return CommandResult.NO_PERMISSION_OTHER;
            }

            Flag flag = args.getFlag("p");
            if (!flag.isPlayer()) {
                return CommandResult.NOT_ONLINE;
            }

            Player target = flag.getPlayer();
            Location loc = target.getLocation();
            String msg = Locales.PLAYER_LOCATION_OTHER.getString().replaceAll("%player%", Utils.replacePlayerPlaceholders(target));
            DecimalFormat decimalFormat = new DecimalFormat("##.#");
            msg = msg.replaceAll("%x%", decimalFormat.format(loc.getX()));
            msg = msg.replaceAll("%y%", decimalFormat.format(loc.getY()));
            msg = msg.replaceAll("%z%", decimalFormat.format(loc.getZ()));
            return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
        }

        return CommandResult.ERROR;

    }

    @BaseCommand(command = "msg", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.player.msg", aliases = "pm,message")
    public CommandResult executeMsg(Player sender, CommandArgs args) {
        if (msg.sendHelp(sender, args)) {
            return CommandResult.SUCCESS;
        }

        if (args.getLength() >= 2) {
            if (!args.isPlayer(0)) {
                return CommandResult.NOT_ONLINE;
            }

            String message = "";
            for (int i = 1; i < args.getLength(); i++) {
                message += args.getString(i) + " ";
            }

            Player target = args.getPlayer(0);
            PlayerManager.getPlayer(target).setLastMsg(sender.getName());
            String msgSpy = Locales.PLAYER_MSG_FORMAT.getString().replaceAll("%sender%", sender.getDisplayName());
            msgSpy = msgSpy.replaceAll("%target%", target.getDisplayName());
            msgSpy = msgSpy.replaceAll("%message%", message);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (PlayerManager.getPlayer(p).isSpy()) {
                    Messager.sendMessage(p, msgSpy, Messager.MessageType.NONE);
                }
            }

            String msgSender = Locales.PLAYER_MSG_FORMAT.getString().replaceAll("%sender%", "You");
            msgSender = msgSender.replaceAll("%target%", target.getDisplayName());
            msgSender = msgSender.replaceAll("%message%", message);
            String msgTarget = Locales.PLAYER_MSG_FORMAT.getString().replaceAll("%target%", "You");
            msgTarget = msgTarget.replaceAll("%sender%", sender.getDisplayName());
            msgTarget = msgTarget.replaceAll("%message%", message);
            Messager.sendMessage(target, msgTarget, Messager.MessageType.NONE);
            return Messager.sendMessage(sender, msgSender, Messager.MessageType.NONE);
        }

        return CommandResult.ERROR;

    }

    @BaseCommand(command = "reply", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.player.reply", aliases = "")
    public CommandResult executeReply(Player sender, CommandArgs args) {
        if (reply.sendHelp(sender, args)) {
            return CommandResult.SUCCESS;
        }

        String lastMsg = PlayerManager.getPlayer(sender).getLastMsg();
        if (lastMsg != null && args.getLength() >= 1) {
            Player target = Bukkit.getPlayer(lastMsg);
            if (target == null) {
                return CommandResult.NOT_ONLINE;
            }

            String message = "";
            for (String temp : args.getArgs()) {
                message += temp + " ";
            }

            PlayerManager.getPlayer(target).setLastMsg(sender.getName());
            String msgSpy = Locales.PLAYER_MSG_FORMAT.getString().replaceAll("%sender%", sender.getDisplayName());
            msgSpy = msgSpy.replaceAll("%target%", target.getDisplayName());
            msgSpy = msgSpy.replaceAll("%message%", message);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (PlayerManager.getPlayer(p).isSpy()) {
                    Messager.sendMessage(p, msgSpy, Messager.MessageType.NONE);
                }
            }

            String msgSender = Locales.PLAYER_MSG_FORMAT.getString().replaceAll("%sender%", "You");
            msgSender = msgSender.replaceAll("%target%", target.getDisplayName());
            msgSender = msgSender.replaceAll("%message%", message);
            String msgTarget = Locales.PLAYER_MSG_FORMAT.getString().replaceAll("%target%", "You");
            msgTarget = msgTarget.replaceAll("%sender%", sender.getDisplayName());
            msgTarget = msgTarget.replaceAll("%message%", message);
            Messager.sendMessage(target, msgTarget, Messager.MessageType.NONE);
            return Messager.sendMessage(sender, msgSender, Messager.MessageType.NONE);
        }

        return CommandResult.ERROR;

    }

    @BaseCommand(command = "spy", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.player.spy", aliases = "spymsg")
    public CommandResult executeSpy(Player sender, CommandArgs args) {
        if (spy.sendHelp(sender, args)) {
            return CommandResult.SUCCESS;
        }

        if (args.isEmpty()) {
            BukkitPlayer p = PlayerManager.getPlayer(sender);
            p.setSpy(!p.isSpy());
            String s = p.isSpy() ? Locales.COMMAND_MESSAGES_ENABLED.getString() : Locales.COMMAND_MESSAGES_DISABLED.getString();
            String msg = Locales.PLAYER_SPY_TOGGLED_SELF.getString().replaceAll("%status%", s);
            return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
        }

        if (args.hasFlag("p")) {
            if (!sender.hasPermission("admincmd.player.spy.other")) {
                return CommandResult.NO_PERMISSION_OTHER;
            }

            Flag flag = args.getFlag("p");
            if (!flag.isPlayer()) {
                return CommandResult.NOT_ONLINE;
            }

            BukkitPlayer p = PlayerManager.getPlayer(flag.getPlayer());
            p.setSpy(!p.isSpy());
            String s = p.isSpy() ? Locales.COMMAND_MESSAGES_ENABLED.getString() : Locales.COMMAND_MESSAGES_DISABLED.getString();
            String msgTarget = Locales.PLAYER_SPY_TOGGLED_SELF.getString().replaceAll("%status%", s);
            String msgSender = Locales.PLAYER_SPY_TOGGLED_OTHER.getString().replaceAll("%status%", s).replaceAll("%player%", Utils.replacePlayerPlaceholders(flag.getPlayer()));
            Messager.sendMessage(sender, msgTarget, Messager.MessageType.INFO);
            return Messager.sendMessage(sender, msgSender, Messager.MessageType.INFO);
        }

        return CommandResult.ERROR;

    }

    @BaseCommand(command = "who", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.player.list", aliases = "plist,online")
    public CommandResult executeList(Player sender, CommandArgs args) {
        if (list.sendHelp(sender, args)) {
            return CommandResult.SUCCESS;
        }

        if (args.isEmpty()) {
            String playerList = "";
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!PlayerManager.getPlayer(p).isInvisible()) {
                    playerList += p.getDisplayName() + " ";
                }
            }

            playerList = playerList.trim().replaceAll(" ", ", ");
            String msg = Locales.PLAYER_LIST_FORMAT.getString().replaceAll("%playerList%", playerList);
            return Messager.sendMessage(sender, msg, Messager.MessageType.NONE);
        }
        return CommandResult.ERROR;
    }

    @BaseCommand(command = "vanish", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.player.vanish", aliases = "invisible,poof")
    public CommandResult executeVanish(Player sender, CommandArgs args) {
        if (vanish.sendHelp(sender, args)) {
            return CommandResult.SUCCESS;
        }

        if (args.isEmpty()) {
            BukkitPlayer p = PlayerManager.getPlayer(sender);
            p.setInvisible(!p.isInvisible());
            if (p.isInvisible()) {
                for (Player op : Bukkit.getOnlinePlayers()) {
                    op.hidePlayer(p.getPlayer());
                }
            } else {
                for (Player op : Bukkit.getOnlinePlayers()) {
                    op.showPlayer(p.getPlayer());
                }
            }
            String s = p.isInvisible() ? Locales.COMMAND_MESSAGES_ENABLED.getString() : Locales.COMMAND_MESSAGES_DISABLED.getString();
            String msg = Locales.PLAYER_VANISH_TOGGLED_SELF.getString().replaceAll("%status%", s);
            return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
        }

        if (args.hasFlag("p")) {
            if (!sender.hasPermission("admincmd.player.vanish.other")) {
                return CommandResult.NO_PERMISSION_OTHER;
            }

            Flag flag = args.getFlag("p");
            if (!flag.isPlayer()) {
                return CommandResult.NOT_ONLINE;
            }

            BukkitPlayer p = PlayerManager.getPlayer(flag.getPlayer());
            p.setInvisible(!p.isInvisible());
            if (p.isInvisible()) {
                for (Player op : Bukkit.getOnlinePlayers()) {
                    op.hidePlayer(p.getPlayer());
                }
            } else {
                for (Player op : Bukkit.getOnlinePlayers()) {
                    op.showPlayer(p.getPlayer());
                }
            }
            String s = p.isInvisible() ? Locales.COMMAND_MESSAGES_ENABLED.getString() : Locales.COMMAND_MESSAGES_DISABLED.getString();
            String msgTarget = Locales.PLAYER_VANISH_TOGGLED_SELF.getString().replaceAll("%status%", s);
            String msgSender = Locales.PLAYER_VANISH_TOGGLED_OTHER.getString().replaceAll("%status%", s).replaceAll("%player%", Utils.replacePlayerPlaceholders(flag.getPlayer()));
            Messager.sendMessage(sender, msgTarget, Messager.MessageType.INFO);
            return Messager.sendMessage(sender, msgSender, Messager.MessageType.INFO);
        }

        return CommandResult.ERROR;
    }

    @BaseCommand(command = "mute", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.player.mute", aliases = "unmute")
    public CommandResult executeMute(Player sender, CommandArgs args) {
        if (mute.sendHelp(sender, args)) {
            return CommandResult.SUCCESS;
        }

        if (args.getLength() == 1) {
            if (!args.isPlayer(0)) {
                return CommandResult.NOT_ONLINE;
            }

            BukkitPlayer p = PlayerManager.getPlayer(args.getPlayer(0));
            p.setMuted(!p.isMuted());

            String s = p.isMuted() ? Locales.COMMAND_MESSAGES_ENABLED.getString() : Locales.COMMAND_MESSAGES_DISABLED.getString();
            String msgTarget = Locales.PLAYER_MUTE_TOGGLED_SELF.getString().replaceAll("%status%", s);
            String msgSender = Locales.PLAYER_MUTE_TOGGLED_OTHER.getString().replaceAll("%status%", s).replaceAll("%player%", Utils.replacePlayerPlaceholders(args.getPlayer(0)));
            Messager.sendMessage(p.getPlayer(), msgTarget, Messager.MessageType.INFO);
            return Messager.sendMessage(sender, msgSender, Messager.MessageType.INFO);
        }

        return CommandResult.ERROR;
    }
}

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
import com.admincmd.admincmd.utils.Locator;
import com.admincmd.admincmd.utils.Messager;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@CommandHandler
public class MobCommands {

    private final HelpPage killall = new HelpPage("killall", "<-w world>");
    private final HelpPage spawnmob = new HelpPage("spawnmob", "mobtype amount");

    @BaseCommand(command = "spawnmob", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.mob.spawnmob")
    public CommandResult executeSpawnmob(Player sender, CommandArgs args) {
        if (spawnmob.sendHelp(sender, args)) {
            return CommandResult.SUCCESS;
        }

        if (args.getLength() != 2 || !args.isInteger(1)) {
            return CommandResult.ERROR;
        }

        World target = sender.getWorld();
        int amount = args.getInt(1);

        EntityType type;
        try {
            type = EntityType.valueOf(args.getString(0).toUpperCase());
        } catch (IllegalArgumentException e) {
            return CommandResult.NOT_A_MOB;
        }

        if (!type.isSpawnable() || !type.isAlive()) {
            return CommandResult.NOT_SPAWNABLE;
        }

        for (int i = 0; i < amount; i++) {
            target.spawnEntity(Locator.getLocationLooking(sender, 10), type);
        }

        String msg = Locales.MOB_SPAWNED.getString().replaceAll("%num%", spawnmob + "");
        return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
    }

    @BaseCommand(command = "killall", sender = BaseCommand.Sender.PLAYER, permission = "admincmd.mob.killall")
    public CommandResult executeKillall(Player sender, CommandArgs args) {
        if (killall.sendHelp(sender, args)) {
            return CommandResult.SUCCESS;
        }

        if (args.getLength() > 2) {
            return CommandResult.ERROR;
        }

        World target = sender.getWorld();
        if (args.hasFlag("w")) {
            Flag f = args.getFlag("w");
            if (!f.isWorld()) {
                return CommandResult.NOT_A_WORLD;
            }

            if (!sender.hasPermission("admincmd.mob.kill.other")) {
                return CommandResult.NO_PERMISSION_OTHER;
            }

            target = f.getWorld().getWorld();
        }

        int killed = 0;
        for (Entity e : target.getEntities()) {
            if (e instanceof LivingEntity) {
                if (e instanceof Player) {
                    continue;
                }
                LivingEntity l = (LivingEntity) e;
                l.setHealth(0.0);
                killed++;
            }
        }

        String msg = Locales.MOB_KILLALL.getString().replaceAll("%num%", killed + "").replaceAll("%world%", target.getName());
        return Messager.sendMessage(sender, msg, Messager.MessageType.INFO);
    }

}

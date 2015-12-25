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
package com.admincmd.admincmd.commandapi;

import com.admincmd.admincmd.world.ACWorld;
import com.admincmd.admincmd.world.WorldManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandArgs {

    private final Map<String, Flag> flags = new HashMap<>();
    private final List<String> args;

    public CommandArgs(String[] args) {
        this.args = parseArgs(args);
    }

    public CommandArgs(String[] args, int start) {
        String newArgs = "";
        for (int i = start; i < args.length; i++) {
            newArgs += args[i] + " ";
        }
        this.args = parseArgs(newArgs.split(" "));
    }

    private List<String> parseArgs(final String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            list.add(args[i]);
            String arg = args[i];
            if (arg.charAt(0) == '-' && arg.length() > 1 && arg.matches("-[a-zA-Z]")) {
                String character = arg.replaceFirst("-", "");
                String value = args[i + 1];
                Flag flag = new Flag(value, character);
                flags.put(character, flag);
            }
        }

        return list;
    }

    public Flag getFlag(final String flag) {
        return flags.get(flag);
    }

    public boolean hasFlag(final String flag) {
        return flags.containsKey(flag);
    }

    public String getString(int index) {
        return args.get(index);
    }

    public int getInt(int index) {
        return Integer.valueOf(args.get(index));
    }

    public double getDouble(int index) {
        return Double.valueOf(args.get(index));
    }

    public Player getPlayer(int index) {
        return Bukkit.getPlayer(args.get(index));
    }

    public boolean isPlayer(int index) {
        return Bukkit.getPlayer(args.get(index)) != null;
    }

    public boolean isInteger(int index) {
        try {
            Integer.valueOf(args.get(index));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isDouble(int index) {
        try {
            Double.valueOf(args.get(index));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isWorld(int index) {
        return WorldManager.getWorld(args.get(index)) != null;
    }

    public ACWorld getWorld(int index) {
        return WorldManager.getWorld(args.get(index));
    }

    public boolean isEmpty() {
        return args.isEmpty();
    }

    public int getLength() {
        return args.size();
    }

    public List<String> getArgs() {
        return args;
    }

    public class Flag {

        private final String value;
        private final String flag;

        public Flag(String value, String flag) {
            this.value = value;
            this.flag = flag;
        }

        public String getFlag() {
            return flag;
        }

        public String getString() {
            return value;
        }

        public int getInt() {
            return Integer.valueOf(value);
        }

        public double getDouble() {
            return Double.valueOf(value);
        }

        public boolean isInteger() {
            try {
                Integer.valueOf(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        public boolean isDouble() {
            try {
                Double.valueOf(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        public Player getPlayer() {
            return Bukkit.getPlayer(value);
        }

        public boolean isPlayer() {
            return Bukkit.getPlayer(value) != null;
        }

        public boolean isWorld() {
            return WorldManager.getWorld(value) != null;
        }

        public ACWorld getWorld() {
            return WorldManager.getWorld(value);
        }

    }

}

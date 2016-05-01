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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.admincmd.admincmd.world.ACWorld;
import com.admincmd.admincmd.world.WorldManager;

public class CommandArgs {

    private final Map<String, Flag> flags = new HashMap<>();
    private final List<String> args;

    protected CommandArgs(String[] args) {
        this.args = parseArgs(args);
    }

    protected CommandArgs(String[] args, int start) {
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
                String value;
                if (args.length == i + 1) {
                	value = "";
                }
                else {
                	value = args[i + 1];
                }
                Flag flag = new Flag(value, character);
                flags.put(character, flag);
            }
        }

        return list;
    }

    /**
     * Returns the flag by the given param
     *
     * @param flag the param
     * @return {@link com.admincmd.admincmd.commandapi.CommandArgs.Flag}
     */
    public Flag getFlag(final String flag) {
        return flags.get(flag);
    }

    /**
     * Checks if a flag was specified
     *
     * @param flag the flag to search for
     * @return true if flag was found
     */
    public boolean hasFlag(final String flag) {
        return flags.containsKey(flag);
    }

    /**
     * Parses an argument to a String
     *
     * @param index the position of the argument
     * @return the argument
     */
    public String getString(int index) {
        return args.get(index);
    }

    /**
     * Parses an argument to an integer
     *
     * @param index the position of the argument
     * @return the argument as int
     */
    public int getInt(int index) {
        return Integer.valueOf(args.get(index));
    }

    /**
     * parses an argument to a double
     *
     * @param index the position of the argument
     * @return the argument as double
     */
    public double getDouble(int index) {
        return Double.valueOf(args.get(index));
    }

    /**
     * Parses an argument to a Player on the server. Throws a
     * NullPointerException if the player is not online.
     *
     * @param index the position of the argument
     * @return the argument as Player
     */
    public Player getPlayer(int index) {
        return Bukkit.getPlayer(args.get(index));
    }

    /**
     * Checks if the argument at the index is a Player on the server.
     *
     * @param index the position of the argument
     * @return true if player is found
     */
    public boolean isPlayer(int index) {
        return Bukkit.getPlayer(args.get(index)) != null;
    }

    /**
     * Checks if the argument at the index is an integer
     *
     * @param index the position of the argument
     * @return true if it is a number
     */
    public boolean isInteger(int index) {
        try {
            Integer.valueOf(args.get(index));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the argument at the index is a double
     *
     * @param index the position of the argument
     * @return true if it is a number
     */
    public boolean isDouble(int index) {
        try {
            Double.valueOf(args.get(index));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the argument at the index is a loaded World
     *
     * @param index the position of the argument
     * @return true if it is a world name
     */
    public boolean isWorld(int index) {
        return WorldManager.getWorld(args.get(index)) != null;
    }

    /**
     * Gets a world by name at the given index
     *
     * @param index the position of the argument
     * @return {@link com.admincmd.admincmd.world.ACWorld}
     */
    public ACWorld getWorld(int index) {
        return WorldManager.getWorld(args.get(index));
    }

    /**
     * Checks if there are any arguments
     *
     * @return true if there are no arguments given
     */
    public boolean isEmpty() {
        return args.isEmpty();
    }

    /**
     * @return the size of the arguments
     */
    public int getLength() {
        return args.size();
    }

    /**
     * @return a List containing all arguments
     */
    public List<String> getArgs() {
        return args;
    }

    public class Flag {

        private final String value;
        private final String flag;

        protected Flag(String value, String flag) {
            this.value = value;
            this.flag = flag;
        }

        /**
         * @return the param of the flag.
         */
        public String getFlag() {
            return flag;
        }

        /**
         * @return the value of the flag
         */
        public String getString() {
            return value;
        }

        /**
         * parses the value to an Integer
         *
         * @return the value as Integer
         */
        public int getInt() {
            return Integer.valueOf(value);
        }

        /**
         * parses the value to a double
         *
         * @return the value as Double
         */
        public double getDouble() {
            return Double.valueOf(value);
        }

        /**
         * Checks if the value is an Integer
         *
         * @return true if it is an Integer
         */
        public boolean isInteger() {
            try {
                Integer.valueOf(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        /**
         * Checks if the value is a double
         *
         * @return true if it is a double
         */
        public boolean isDouble() {
            try {
                Double.valueOf(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        /**
         * Parses the value to a Player on the server.
         *
         * @return {@link org.bukkit.entity.Player}
         */
        public Player getPlayer() {
            return Bukkit.getPlayer(value);
        }

        /**
         * Checks if there is a Player online with the given name.
         *
         * @return true if a player is online with the name.
         */
        public boolean isPlayer() {
            return Bukkit.getPlayer(value) != null;
        }

        /**
         * Checks if there is a world loaded with the value of the flag.
         *
         * @return true if a world is loaded.
         */
        public boolean isWorld() {
            return WorldManager.getWorld(value) != null;
        }

        /**
         * Parses the value to a world.
         *
         * @return {@link com.admincmd.admincmd.world.ACWorld}
         */
        public ACWorld getWorld() {
            return WorldManager.getWorld(value);
        }

    }

}

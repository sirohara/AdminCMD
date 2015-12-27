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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BaseCommand {

    /**
     * Used to determine the type of the command sender
     */
    public enum Sender {

        PLAYER,
        CONSOLE;
    }

    /**
     * Determines the command sender this method is for.
     *
     * @return {@link com.admincmd.admincmd.commandapi.BaseCommand.Sender}
     */
    Sender sender();

    /**
     * Determines the command name.
     *
     * @return
     */
    String command();

    /**
     * Determines the permission which is needed to execute this command.
     *
     * @return
     */
    String permission() default "";

    /**
     * Used to determine a certain subcommand this method is for.
     *
     * @return
     */
    String subCommand() default "";

    /**
     * Used to add any aliases for this command.
     *
     * @return
     */
    String aliases() default "";

}

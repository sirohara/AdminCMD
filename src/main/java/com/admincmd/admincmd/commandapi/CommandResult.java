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

import com.admincmd.admincmd.utils.Locales;


public enum CommandResult {

    SUCCESS(null),
    NO_PERMISSION(Locales.COMMAND_MESSAGES_NO_PERMISSION.getString()),
    NO_PERMISSION_OTHER(Locales.COMMAND_MESSAGES_NO_PERMISSION_OTHER.getString()),
    ERROR(Locales.COMMAND_MESSAGES_WRONG_USAGE.getString()),
    NOT_ONLINE(Locales.COMMAND_MESSAGES_NOT_ONLINE.getString()),
    NOT_A_NUMBER(Locales.COMMAND_MESSAGES_NOT_A_NUMBER.getString()),
    NOT_A_WORLD(Locales.COMMAND_MESSAGES_NOT_A_WORLD.getString()),
    NOT_A_MOB(Locales.COMMAND_MESSAGES_NOT_A_MOB.getString()),
    NOT_SPAWNABLE(Locales.COMMAND_MESSAGES_NOT_SPAWNABLE.getString());

    private final String msg;

    CommandResult(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return this.msg;
    }
}

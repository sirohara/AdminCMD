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
package com.admincmd.admincmd.utils;

import com.admincmd.admincmd.commandapi.CommandResult;
import org.bukkit.command.CommandSender;

public class Messager {

    public enum MessageType {
        INFO(Locales.MESSAGE_PREFIX_INFO.getString()),
        ERROR(Locales.MESSAGE_PREFIX_ERROR.getString());
            
        private final String prefix;

        private MessageType(String prefix) {
            this.prefix = prefix;
        }
        
        public String getPrefix() {
            return prefix;
        }
    }
    
    public static CommandResult sendMessage(CommandSender s, String message, MessageType type) {
        s.sendMessage(type.getPrefix() + message);
        return CommandResult.SUCCESS;
    }
    
    public static CommandResult sendMessage(CommandSender s, Locales message, MessageType type) {
        s.sendMessage(type.getPrefix() + message.getString());
        return CommandResult.SUCCESS;
    }

}

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
package com.admincmd.admincmd.events;

import com.admincmd.admincmd.Main;
import com.admincmd.admincmd.utils.ACLogger;
import com.admincmd.admincmd.utils.Config;
import com.admincmd.admincmd.utils.MotD;
import java.io.File;
import java.io.FileInputStream;
import com.comphenix.protocol.PacketType.Status.Server;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.GamePhase;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import com.comphenix.protocol.wrappers.WrappedServerPing.CompressedImage;

public class PingListener {

    public void addPingResponsePacketListener() {
        if (!Main.getInstance().checkForProtocolLib()) {
            Config.MAINTENANCE_ENABLED.set(false, true);
            return;
        }

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        try {
            protocolManager.addPacketListener(new PacketAdapter(PacketAdapter.params(Main.getInstance(), Server.OUT_SERVER_INFO).serverSide().gamePhase(GamePhase.BOTH).listenerPriority(ListenerPriority.HIGHEST).optionAsync()) {
                @Override
                public void onPacketSending(PacketEvent event) {
                    try {
                        if (!Config.MAINTENANCE_ENABLED.getBoolean()) {
                            return;
                        }
                        WrappedServerPing ping = event.getPacket().getServerPings().getValues().get(0);
                        String pingMessage = Config.MAINTENANCE_VERSION.getString();
                        ping.setVersionProtocol(-1);
                        ping.setVersionName(pingMessage);
                        MotD motd = new MotD(Config.MAINTENANCE_MOTD_LINE_1.getString(), Config.MAINTENANCE_MOTD_LINE_2.getString());
                        ping.setMotD(motd.getMotd());
                        File iconfile = new File(Config.MAINTENANCE_ICON.getString());
                        if (iconfile.exists()) {
                            CompressedImage favicon = CompressedImage.fromPng(new FileInputStream(iconfile));
                            ping.setFavicon(favicon);
                        }
                        event.getPacket().getServerPings().getValues().set(0, ping);
                    } catch (Exception e) {
                        ACLogger.severe("Error setting Maintenance values!", e);
                    }
                }
            }
            );
        } catch (Exception e) {
            ACLogger.severe("Error setting Maintenance values!", e);
        }
    }
}

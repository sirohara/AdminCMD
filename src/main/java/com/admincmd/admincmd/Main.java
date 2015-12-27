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
package com.admincmd.admincmd;

import com.admincmd.admincmd.addon.AddonManager;
import com.admincmd.admincmd.commandapi.CommandManager;
import com.admincmd.admincmd.commands.HomeCommands;
import com.admincmd.admincmd.commands.MaintenanceCommands;
import com.admincmd.admincmd.commands.MobCommands;
import com.admincmd.admincmd.commands.PlayerCommands;
import com.admincmd.admincmd.commands.ServerCommands;
import com.admincmd.admincmd.commands.SpawnCommands;
import com.admincmd.admincmd.commands.WorldCommands;
import com.admincmd.admincmd.utils.Config;
import com.admincmd.admincmd.utils.Locales;
import com.admincmd.admincmd.database.DatabaseFactory;
import com.admincmd.admincmd.events.PingListener;
import com.admincmd.admincmd.events.PlayerCommandListener;
import com.admincmd.admincmd.events.PlayerDamageListener;
import com.admincmd.admincmd.events.PlayerDeathListener;
import com.admincmd.admincmd.events.PlayerJoinListener;
import com.admincmd.admincmd.events.SignListener;
import com.admincmd.admincmd.events.WorldListener;
import com.admincmd.admincmd.home.HomeManager;
import com.admincmd.admincmd.player.PlayerManager;
import com.admincmd.admincmd.spawn.SpawnManager;
import com.admincmd.admincmd.utils.ACLogger;
import com.admincmd.admincmd.utils.EventManager;
import com.admincmd.admincmd.world.WorldManager;
import java.sql.SQLException;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    
    private static Main INSTANCE;
    private final CommandManager manager = new CommandManager(this);
    
    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        
        INSTANCE = this;
        
        Config.load();
        Locales.load();
        
        DatabaseFactory.init();
        
        PlayerManager.init();
        SpawnManager.init();
        WorldManager.init();
        HomeManager.init();
        
        registerCommands();
        registerEvents();
        
        if (checkForProtocolLib()) {
            ACLogger.info("ProtocolLib was found. Maintenance feature is available.");
        }
        
        AddonManager.loadAddons();
        
        long timeTook = System.currentTimeMillis() - start;
        ACLogger.info("Plugin start took " + timeTook + " miliseconds");
    }
    
    @Override
    public void onDisable() {
        AddonManager.disableAddons();
        
        PlayerManager.save();
        WorldManager.save();
        HomeManager.save();
        
        try {
            DatabaseFactory.getDatabase().closeConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        System.gc();
    }

    /**
     * Returns an instance of this class.
     *
     * @return {@link com.admincmd.admincmd.Main}
     */
    public static Main getInstance() {
        return INSTANCE;
    }
    
    public boolean checkForProtocolLib() {
        return getServer().getPluginManager().getPlugin("ProtocolLib") != null;
    }
    
    private void registerCommands() {
        manager.registerClass(ServerCommands.class);
        manager.registerClass(PlayerCommands.class);
        manager.registerClass(HomeCommands.class);
        manager.registerClass(WorldCommands.class);
        manager.registerClass(MobCommands.class);
        manager.registerClass(SpawnCommands.class);
        manager.registerClass(MaintenanceCommands.class);
    }
    
    private void registerEvents() {
        EventManager.registerEvent(PlayerJoinListener.class);
        EventManager.registerEvent(PlayerCommandListener.class);
        EventManager.registerEvent(WorldListener.class);
        EventManager.registerEvent(PlayerDamageListener.class);
        EventManager.registerEvent(PlayerDeathListener.class);
        EventManager.registerEvent(SignListener.class);
        new PingListener().addPingResponsePacketListener();
    }
    
}

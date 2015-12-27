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

import com.admincmd.admincmd.Main;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Config {

    MYSQL_USE("MySQL.Use", false, "true=Use MySQL, false=use SQLite"),
    MYSQL_IP("MySQL.IP", "127.0.0.1", "The ip of the mysql server"),
    MYSQL_PORT("MySQL.Port", 3306, "The port of the MySQL server"),
    MYSQL_DATABASE("MySQL.Database", "minecraft", "The name of the dataabse"),
    MYSQL_USER("MySQL.User", "root", "The user of the database"),
    MYSQL_PASSWORD("MySQL.Password", "password", "The password of the user"),
    GLOBAL_SPAWNS("Options.WorldSpawns", false, "Set to true if you want one spawn for every world. false means one spawn for the whole server."),
    DIRECT_RESPAWN("Options.DirectRespawn", true, "Set to false if you want players to manually click the respawn button."),
    DEBUG("Options.Debug", false, "Enables debugging chat."),
    MAINTENANCE_ENABLED("Maintenance.Enabled", false, "Enables Maintenance."),
    MAINTENANCE_MOTD_LINE_1("Maintenance.Motd.Line1", "&c&nServer is in Maintenance", "Maintenance motd line 1"),
    MAINTENANCE_MOTD_LINE_2("Maintenance.Motd.Line2", "&eWe are actually working on the server!", "Maintenance motd line 2"),
    MAINTENANCE_KICKMESSAGE("Maintenance.KickMessages", "&c&nServer is in Maintenance", "Maintenance kickmessage"),
    MAINTENANCE_ICON("Maintenance.Favicon", Main.getInstance().getDataFolder().getAbsolutePath() + File.separator + "maintenance.png", "Maintenance favicon"),
    MAINTENANCE_VERSION("Maintenance.Version", "Maintenance", "Maintenance version number"),
    MAINTENANCE_KICK("Maintenance.KickOnMaintenance", true, "Kick if maintenance gets activated");

    private Config(String path, Object val, String description) {
        this.path = path;
        this.value = val;
        this.description = description;
    }

    private final Object value;
    private final String path;
    private final String description;
    private static YamlConfiguration cfg;
    private static final File f = new File(Main.getInstance().getDataFolder(), "config.yml");

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }

    public Object getDefaultValue() {
        return value;
    }

    public boolean getBoolean() {
        return cfg.getBoolean(path);
    }

    public int getInteger() {
        return cfg.getInt(path);
    }

    public double getDouble() {
        return cfg.getDouble(path);
    }

    public String getString() {
        return cfg.getString(path);
    }

    public List<String> getStringList() {
        return cfg.getStringList(path);
    }

    public static void load() {
        Main.getInstance().getDataFolder().mkdirs();
        reload(false);
        String header = "";
        for (Config c : values()) {
            header += c.getPath() + ": " + c.getDescription() + System.lineSeparator();
            if (!cfg.contains(c.getPath())) {
                c.set(c.getDefaultValue(), false);
            }
        }
        cfg.options().header(header);
        try {
            cfg.save(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void set(Object value, boolean save) {
        cfg.set(path, value);
        if (save) {
            try {
                cfg.save(f);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            reload(false);
        }
    }

    public static void reload(boolean complete) {
        if (!complete) {
            cfg = YamlConfiguration.loadConfiguration(f);
            return;
        }
        load();
    }

}

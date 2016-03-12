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
package com.admincmd.admincmd.update;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {

    public enum Values {

        /**
         * Represents the Enabled boolean
         */
        ENABLED("Enabled", true, "Is the updater enabled?"),
        /**
         * Represents the Update-Type boolean
         */
        UPDATE_TYPE("Update-Type", UpdateType.DOWNLOAD.toString(), "DOWNLOAD: Download the new version. ANNOUNCE: Only say that a new version was released."),
        /**
         * Represents the Release-Type boolean
         */
        RELEASE_TYPE("Release-Type", ReleaseType.RELEASE.toString(), "ALPHA: Only download alpha files. BETA: Only download files. RELEASE: Only download release files. ALL: Download all files."),
        /**
         * Represents the debug boolean.
         */
        DEBUG("Debug", false, "Toggles the debug logging");

        private final Object value;
        private final String path;
        private final String comment;

        private Values(String path, Object val, String comment) {
            this.path = path;
            this.value = val;
            this.comment = comment;
        }

        public Object getDefaultValue() {
            return value;
        }

        public String getPath() {
            return path;
        }

        public String getComment() {
            return comment;
        }

        public boolean getBoolean(Config c) {
            return c.getConfig().getBoolean(path);
        }

        public UpdateType getUpdateType(Config c) {
            return UpdateType.valueOf(c.getConfig().getString(path, UpdateType.DOWNLOAD.toString()).toUpperCase());
        }

        public ReleaseType getReleaseType(Config c) {
            return ReleaseType.valueOf(c.getConfig().getString(path, ReleaseType.ALL.toString()).toUpperCase());
        }
    }

    private final YamlConfiguration cfg;
    private final File f;

    public Config(JavaPlugin pl, String cfgFile) {
        pl.getLogger().info("Loading updater-config...");
        pl.getDataFolder().mkdirs();
        f = new File(pl.getDataFolder(), cfgFile);
        cfg = YamlConfiguration.loadConfiguration(f);
        String header = "Updater config for the plugin " + pl.getName() + ". (Automaticaly generated)\n";
        for (Values c : Values.values()) {
            header += c.getPath() + ": " + c.getComment() + "\n";
            if (!cfg.contains(c.getPath())) {
                cfg.set(c.getPath(), c.getDefaultValue());
            }
        }
        cfg.options().header(header);
        try {
            cfg.save(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        pl.getLogger().info("Done!");
    }

    public YamlConfiguration getConfig() {
        return cfg;
    }

    public File getFile() {
        return f;
    }

}

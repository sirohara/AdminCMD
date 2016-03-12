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

import com.admincmd.admincmd.Main;
import com.admincmd.admincmd.utils.ACLogger;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

public class Updater {

    private final int id;
    private URL url;
    private final JavaPlugin main;
    private final String link;
    private final boolean enabled;
    private final UpdateType type;
    private final ReleaseType rType;
    private final Config config;

    /**
     * Inits a new updater Notice: Automatically generates a update-config.yml
     * file to en- / disable the updater. Bukkit has the rule that every updater
     * needs to be able to get disabled.
     *
     * @param main: Your plugin's main file
     * @param id: Your project id. Can be found using
     * (http://wiki.bukkit.org/ServerMods_API#Searching_for_project_IDs)
     * @param link: Your project slug. http://dev.bukkit.org/bukkit-plugins/ is
     * added automatically.
     */
    public Updater(int id, String link) {
        main = Main.getInstance();
        main.getLogger().info("Loading updater by TheJeterLP. Project id: " + id);
        config = new Config(main, "update-config.yml");
        this.type = Config.Values.UPDATE_TYPE.getUpdateType(config);
        this.rType = Config.Values.RELEASE_TYPE.getReleaseType(config);
        this.id = id;
        this.link = "http://dev.bukkit.org/bukkit-plugins/" + link;
        boolean enable = (id == -1 ? false : Config.Values.ENABLED.getBoolean(config));

        try {
            this.url = new URL("https://api.curseforge.com/servermods/files?projectIds=" + this.id);
        } catch (MalformedURLException ex) {
            enable = false;
            ex.printStackTrace();
        }

        if (enable) {
            main.getServer().getPluginManager().registerEvents(new UpdateListener(this), main);
        }
        this.enabled = enable;
        main.getLogger().info("Updatechecker is " + (this.enabled ? "enabled" : "disabled") + ".");
    }

    /**
     * Search for an update.
     */
    public void search() {
        debug("Method: search()");
        if (!enabled) {
            return;
        }
        main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {
                String[] data = read();
                if (checkForNewVersions(data)) {
                    main.getLogger().info("A new update is available! (" + data[1] + ") current: " + main.getDescription().getVersion());
                    if (type == UpdateType.ANNOUNCE) {
                        main.getLogger().info("You can get it at: " + link);
                    } else {
                        main.getLogger().info("It will be downloaded for you and will be installed automatically when the server restarts.");
                    }
                }
            }
        }, 3 * 20);
    }

    /**
     * @return true: If the updater is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @return the plugin this updater belongs to
     */
    public JavaPlugin getPlugin() {
        return main;
    }

    /**
     * @return updateType
     */
    public UpdateType getUpdateType() {
        return type;
    }

    /**
     * @return releaseType
     */
    public ReleaseType getReleaseType() {
        return rType;
    }

    /**
     * @return dev.bukkit.org link
     */
    public String getLink() {
        return link;
    }

    /**
     * Perform a complete update-check and download new file
     *
     * @param data
     * @return
     */
    protected boolean checkForNewVersions(String[] data) {
        debug("Method: checkForNewVersions(String[])");
        if (data == null || !versionCheck(data)) {
            return false;
        }
        if (type == UpdateType.DOWNLOAD) {
            downloadFile(data);
        }
        return true;
    }

    /**
     * Check if the remote version is higher than the current version.
     *
     * @param data
     * @return
     */
    protected boolean versionCheck(String[] data) {
        debug("Method: versionCheck(String[])");
        String title = data[1];
        if (this.rType != ReleaseType.ALL) {
            ReleaseType releaseType = ReleaseType.valueOf(data[2].toUpperCase());
            if (releaseType != this.rType) {
                debug("Releasetype of the new file does not match the one which we search for! Ignoring the file...");
                return false;
            }
        }
        String remote;
        if (title.contains(" v")) {
            remote = title.split(" v")[1];
        } else if (title.contains("v")) {
            remote = title.replace("v", "");
        } else {
            remote = title;
        }
        ArrayList<String> rNumbers = new ArrayList<>();
        ArrayList<String> numbers = new ArrayList<>();
        rNumbers.addAll(Arrays.asList(remote.split("\\.")));

        if (main.getDescription().getVersion().contains("-")) {
            numbers.addAll(Arrays.asList(main.getDescription().getVersion().split("-")[0].split("\\.")));
            debug("numbers: " + Arrays.toString(numbers.toArray()));
        } else {
            numbers.addAll(Arrays.asList(main.getDescription().getVersion().split("\\.")));
            debug("numbers: " + Arrays.toString(numbers.toArray()));
        }

        if (rNumbers.size() > numbers.size()) {
            int missing = rNumbers.size() - numbers.size();
            for (int i = 0; i < missing; i++) {
                numbers.add("0");
            }
            debug("numbers: " + Arrays.toString(numbers.toArray()));
        } else if (numbers.size() > rNumbers.size()) {
            int missing = numbers.size() - rNumbers.size();
            for (int i = 0; i < missing; i++) {
                rNumbers.add("0");
            }
            debug("rNumbers: " + Arrays.toString(rNumbers.toArray()));
        }

        for (int i = 0; i < rNumbers.size(); i++) {
            int rNumber = Integer.valueOf(rNumbers.get(i));
            int number = Integer.valueOf(numbers.get(i));
            if (rNumber > number) {
                debug(rNumber + " is bigger than " + number + ". This means there is a new version. Returnign true...");
                return true;
            } else if (number > rNumber) {
                main.getLogger().info("It seems that your version is newer than the one on BukkitDev. Maybe you are using a development build?");
                return false;
            }
        }
        main.getLogger().info("There is no new version available. You are up-to-date!");
        return false;
    }

    /**
     * Index: 0: downloadUrl 1: name 2: releaseType
     *
     * @return
     */
    protected String[] read() {
        debug("Method: read()");
        try {
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.readLine();
            JSONArray array = (JSONArray) JSONValue.parse(response);
            if (array.size() == 0) {
                return null;
            }
            Map<?, ?> map = (Map) array.get(array.size() - 1);

            int index = 1;
            String releaseType = (String) map.get("releaseType");

            if (this.rType != ReleaseType.ALL) {
                ReleaseType release = ReleaseType.valueOf(releaseType.toUpperCase());
                while (release != this.rType) {
                    debug("Releasetype of the new file does not match the one which we search for! Ignoring the file...");
                    debug("Getting next file...");
                    index++;

                    if ((array.size() - index) < 0) {
                        return null;
                    }

                    map = (Map) array.get(array.size() - index);
                    if (map == null) {
                        return null;
                    }
                    releaseType = (String) map.get("releaseType");
                    release = ReleaseType.valueOf(releaseType.toUpperCase());
                }
            }
            String downloadUrl = (String) map.get("downloadUrl");
            String name = (String) map.get("name");

            return new String[]{downloadUrl, name, releaseType};
        } catch (Exception e) {
            ACLogger.severe("Error on trying to check remote versions. Error: " + e, e);
            return null;
        }
    }

    /**
     * Downloads a new file from BukkitDev
     *
     * @param data
     */
    protected void downloadFile(final String[] data) {
        debug("Method: downlaoadFile(String[])");
        main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {
                main.getLogger().info("Downloading update from " + link);
                File update = main.getServer().getUpdateFolderFile();
                if (!update.exists()) {
                    update.mkdirs();
                }
                BufferedInputStream in = null;
                FileOutputStream fout = null;
                try {
                    final URL url = new URL(data[0]);
                    final int fileLength = url.openConnection().getContentLength();
                    final String fileName = new File(main.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getName();
                    File target = new File(update, fileName);

                    in = new BufferedInputStream(url.openStream());
                    fout = new FileOutputStream(target);
                    final byte[] data = new byte[1024];
                    int count;
                    long downloaded = 0;
                    while ((count = in.read(data, 0, 1024)) != -1) {
                        downloaded += count;
                        fout.write(data, 0, count);
                        final int percent = (int) ((downloaded * 100) / fileLength);
                        if ((percent % 10) == 0) {
                            main.getLogger().info("Downloaded " + percent + "% of " + fileLength + " bytes.");
                        }
                    }
                    main.getLogger().info("Download done!");
                } catch (final Exception ex) {
                    main.getLogger().severe("Error on trying to download update. Error: " + ex);
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                        if (fout != null) {
                            fout.close();
                        }
                    } catch (final Exception ex) {
                        main.getLogger().severe("Error on trying to close Streams. Error: " + ex);
                    }
                }
            }
        });
    }

    protected void debug(String message) {
        if (!Config.Values.DEBUG.getBoolean(config)) {
            return;
        }
        main.getLogger().info("[Debug] " + message);
    }
}

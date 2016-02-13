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
package com.admincmd.admincmd.player;

import com.admincmd.admincmd.database.Database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public abstract class SQLPlayer {

    private final UUID uuid;
    private boolean fly, god, invisible, cmdwatcher, spy, muted;
    private String nickname, lastMsg;
    private int id;
    private Database db;

    public SQLPlayer(UUID uuid, Database db) {
        this.db = db;
        this.uuid = uuid;
        try {
            PreparedStatement s = db.getPreparedStatement("SELECT * FROM `ac_player` WHERE `uuid` = ?;");
            s.setString(1, uuid.toString());
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                boolean god = rs.getBoolean("god");
                boolean fly = rs.getBoolean("fly");
                boolean inv = rs.getBoolean("invisible");
                boolean cw = rs.getBoolean("commandwatcher");
                boolean spy = rs.getBoolean("spy");
                boolean muted = rs.getBoolean("muted");
                String nickname = rs.getString("nickname");
                int id = rs.getInt("ID");
                String lastMsg = rs.getString("lastMsg");

                this.god = god;
                this.fly = fly;
                this.invisible = inv;
                this.cmdwatcher = cw;
                this.spy = spy;
                this.muted = muted;
                this.nickname = nickname;
                this.id = id;
                this.lastMsg = lastMsg;
            }
            
            db.closeResultSet(rs);
            db.closeStatement(s);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public boolean isFly() {
        return fly;
    }

    public void setFly(boolean fly) {
        this.fly = fly;
    }

    public boolean isGod() {
        return god;
    }

    public void setGod(boolean god) {
        this.god = god;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean isCmdwatcher() {
        return cmdwatcher;
    }

    public void setCmdwatcher(boolean cmdwatcher) {
        this.cmdwatcher = cmdwatcher;
    }

    public boolean isSpy() {
        return spy;
    }

    public void setSpy(boolean spy) {
        this.spy = spy;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public void update() {
        try {
            PreparedStatement st = db.getPreparedStatement("UPDATE `ac_player` SET `god` = ?, `invisible` = ?, `commandwatcher` = ?, `spy` = ?, `fly` = ?, `muted` = ?, `nickname` = ?, 'lastMsg' = ? WHERE `id` = ?;");
            st.setBoolean(1, this.god);
            st.setBoolean(2, this.invisible);
            st.setBoolean(3, this.cmdwatcher);
            st.setBoolean(4, this.spy);
            st.setBoolean(5, this.fly);
            st.setBoolean(6, this.muted);
            st.setString(7, this.nickname);
            st.setInt(8, this.id);
            st.setString(9, this.lastMsg);
            st.executeUpdate();
            db.closeStatement(st);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public UUID getUuid() {
        return uuid;
    }

}

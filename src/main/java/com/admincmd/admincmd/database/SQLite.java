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
package com.admincmd.admincmd.database;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite extends Database {

    private final File dbFile;

    /**
     * Creates a new instance for SQLite databases.
     *
     * @param dbFile Database file
     */
    public SQLite(File dbFile) { 
        super("org.sqlite.JDBC");
        dbFile.getParentFile().mkdirs();
        this.dbFile = dbFile;
    }

    @Override
    public void reactivateConnection() throws SQLException {
        setConnection(DriverManager.getConnection("jdbc:sqlite://" + dbFile.getAbsolutePath()));
    }

}

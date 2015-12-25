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

import com.admincmd.admincmd.utils.ACLogger;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {

    private Connection conn = null;
    
    public Database(String driver) {
        try {
            Class d = Class.forName(driver);
            Object o = d.newInstance();
            if(!(o instanceof Driver)) {
                ACLogger.severe("Driver is not an instance of the Driver class!");
            } else {
                Driver dr = (Driver) o;
                DriverManager.registerDriver(dr);
            }
        } catch (Exception ex) {
            ACLogger.severe("Driver not found! " + driver, ex);
        } 
    }
    
    public final boolean testConnection() {
        try {
            getConnection();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    /**
     * Gets the connection
     *
     * @return
     * @throws SQLException
     */
    public final Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed())
            reactivateConnection();
        return conn;
    }

    /**
     * Used to reactivate the connection
     *
     * @param conn
     */
    public final void setConnection(Connection conn) {
        this.conn = conn;
    }

    /**
     * closes the actual connection to the database
     *
     * @throws SQLException
     */
    public final void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed())
            conn.close();
    }

    /**
     * closes a statement
     *
     * @param s
     * @throws SQLException
     */
    public final void closeStatement(Statement s) throws SQLException {
        if (s != null)
            s.close();

    }

    /**
     * closes a resultset
     *
     * @param rs
     * @throws SQLException
     */
    public final void closeResultSet(ResultSet rs) throws SQLException {
        if (rs != null)
            rs.close();
    }

    /**
     * creates a new statement
     *
     * @return
     * @throws SQLException
     */
    public final Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }

    /**
     * creates a new PreparedStatement
     *
     * @param query
     * @return
     * @throws SQLException
     */
    public final PreparedStatement getPreparedStatement(String query) throws SQLException {
        return getConnection().prepareStatement(query);
    }

    /**
     * Creates a new Statement and executes it
     *
     * @param query
     * @throws SQLException
     */
    public final void executeStatement(String query) throws SQLException {
        Statement s = getStatement();
        s.execute(query);
        closeStatement(s);
    }

    public abstract void reactivateConnection() throws SQLException;

}

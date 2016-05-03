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

import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class LocationSerialization {

	public static String serialLocation(Location l) {
		int pitch = Integer.valueOf(String.valueOf(l.getPitch()).split("\\.")[0]);
		int yaw = Integer.valueOf(String.valueOf(l.getYaw()).split("\\.")[0]);
		return l.getX() + ";" + l.getY() + ";" + l.getZ() + ";" + l.getWorld().getName() + ";" + yaw + ";" + pitch;
	}

	public static Location deserialLocation(String s) {
		String[] a = s.split(";");
		World w = Bukkit.getWorld(a[3]);
		if (w == null) {
			w = Bukkit.getWorlds().get(0);
		}
		double x = Double.parseDouble(a[0]);
		double y = Double.parseDouble(a[1]);
		double z = Double.parseDouble(a[2]);
		int yaw = Integer.parseInt(a[4]);
		int pitch = Integer.parseInt(a[5]);
		Location l = new Location(w, x, y, z, yaw, pitch);
		return l;
	}

	public static Block getBlockLooking(Player player, int range) {
		Block b = player.getTargetBlock((Set<Material>) null, range);
		return b;

	}

	public static Location getLocationLooking(Player player, int range) {
		Block b = getBlockLooking(player, range);
		return b.getLocation();
	}

}

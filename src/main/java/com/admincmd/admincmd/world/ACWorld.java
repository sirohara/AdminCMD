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
package com.admincmd.admincmd.world;

import com.admincmd.admincmd.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class ACWorld {

	private final World w;
	private long moment;
	private boolean timePaused;

	public ACWorld(World world, boolean timePaused, String moment) {
		this.w = world;
		this.timePaused = timePaused;
		this.moment = Long.valueOf(moment);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new ResetTime(this), 20 * 3, 20 * 3);
	}

	public World getWorld() {
		return w;
	}

	public void pauseTime() {
		this.timePaused = true;
		this.moment = w.getTime();
	}

	public void unPauseTime() {
		this.timePaused = false;
	}

	public boolean isTimePaused() {
		return timePaused;
	}

	public long getTimePauseMoment() {
		return moment;
	}
}

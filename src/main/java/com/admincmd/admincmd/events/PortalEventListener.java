package com.admincmd.admincmd.events;
/**
 * SiroharaServerCore (PortalEventListener.java)
 **/
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import com.admincmd.admincmd.utils.BukkitListener;

public class PortalEventListener extends BukkitListener{
	@EventHandler
	public void onEntityEnterGate(EntityPortalEvent event) {
		// プレイヤー以外のエンティティのワープを禁止
		if (event.getEntityType() != EntityType.PLAYER) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onPlayerPortal(PlayerPortalEvent event) {
		if (event.getTo().getWorld().getEnvironment() == World.Environment.THE_END) {
			return;
		}
		//TravelAgent キャンセル
		event.useTravelAgent(false);
		//メインワールド・ネザー間で飛び先は互いの初期スポーンとする
		event.setTo(event.getTo().getWorld().getSpawnLocation());
	}
}

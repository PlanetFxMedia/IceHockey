package de.SebastianMikolai.PlanetFx.IceHockey.API.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.Arena;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.HockeyPlayer;

public class PlayerJoinArenaEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private HockeyPlayer player;
	private Arena arena;
	private boolean cancel = false;
	
	public PlayerJoinArenaEvent(HockeyPlayer player, Arena arena) {
		this.player = player;
		this.arena = arena;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public HockeyPlayer getPlayer() {
		return this.player;
	}
	
	public Arena getArena() {
		return this.arena;
	}
	
	public boolean isCancelled() {
		return this.cancel;
	}
	
	public void setCancelled(boolean flag) {
		this.cancel = flag;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
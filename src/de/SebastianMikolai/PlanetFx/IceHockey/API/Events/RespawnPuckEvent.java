package de.SebastianMikolai.PlanetFx.IceHockey.API.Events;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.Arena;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.Puck;

public class RespawnPuckEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel = false;
	private Puck puck;
	private Location loc;
	private Arena arena;
	
	public RespawnPuckEvent(Arena arena, Puck puck, Location loc) {
		this.arena = arena;
		this.puck = puck;
		this.loc = loc;
	}
	
	public Arena getArena() {
		return this.arena;
	}
	
	public Location getLocation() {
		return this.loc;
	}
	
	public Puck getPuck() {
		return this.puck;
	}
	
	public void setLocation(Location loc) {
		this.loc = loc;
	}
	
	public void setPuck(Puck puck) {
		this.puck = puck;
	}
	
	public boolean isCancelled() {
		return this.cancel;
	}
	
	public void setCancelled(boolean flag) {
		this.cancel = flag;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
package de.SebastianMikolai.PlanetFx.IceHockey.API.Events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.Arena;

public class StartPlayMusicEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel = false;
	private Material record;
	private Location loc;
	private Arena arena;
	
	public StartPlayMusicEvent(Arena arena, Location loc, Material record) {
		this.arena = arena;
		this.loc = loc;
		setRecord(record);
	}
	
	public Arena getArena() {
		return this.arena;
	}
	
	public Location getLocation() {
		return this.loc;
	}
	
	public Material getRecord() {
		return this.record;
	}
	
	public void setRecord(Material record) {
		this.record = record;
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
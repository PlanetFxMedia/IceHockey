package de.SebastianMikolai.PlanetFx.IceHockey.API.Signs;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract interface SignType {
	
	public abstract void handleCreateSign(SignChangeEvent paramSignChangeEvent);
	
	public abstract void handleClickSign(PlayerInteractEvent paramPlayerInteractEvent);
	
	public abstract void handleDestroy(BlockBreakEvent paramBlockBreakEvent);
}
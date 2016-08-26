package de.SebastianMikolai.PlanetFx.IceHockey.API.Classes;

import org.bukkit.inventory.ItemStack;

public abstract interface ClassType {
	
	public abstract String getName();
	
	public abstract ItemStack getHelmet();
	
	public abstract ItemStack getChestplate();
	
	public abstract ItemStack getLeggings();
	
	public abstract ItemStack getBoots();
	
	public abstract ItemStack getHockeyStick();
}
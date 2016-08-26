package de.SebastianMikolai.PlanetFx.IceHockey.API.Classes.Types;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.SebastianMikolai.PlanetFx.IceHockey.API.Classes.ClassType;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Utils.Lang;

public class Winger implements ClassType {

	private String name = "Winger";
	
	public String getName() {
		return this.name;
	}
	
	public ItemStack getHelmet() {
		ItemStack item = new ItemStack(Material.LEATHER_HELMET);
		return item;
	}
	
	public ItemStack getChestplate() {
		ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
		return item;
	}
	
	public ItemStack getLeggings() {
		ItemStack item = new ItemStack(Material.LEATHER_LEGGINGS);
		return item;
	}
	
	public ItemStack getBoots() {
		ItemStack item = new ItemStack(Material.LEATHER_BOOTS);
		return item;
	}
	
	public ItemStack getHockeyStick() {
		ItemStack item = new ItemStack(Material.GOLD_HOE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Lang.HOCKEY_STICK.toString());
		item.setItemMeta(meta);
		return item;
	}
}
package de.SebastianMikolai.PlanetFx.IceHockey.API.Addons;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;

public class AddonManager {
	
	private List<Addon> addons = new ArrayList<Addon>();
	
	public void loadAddon(Addon addon) {
		enableAddon(addon);
		this.addons.add(addon);
	}
	
	public void enableAddon(Addon addon) {
		addon.setEnabled(true);
		addon.onEnable();
		if (!addon.isEnabled()) {
			return;
		}
		addAddonListeners(addon);
		registerPermission(addon); 
		String authors = "";
		if (addon.getAuthors().size() >= 2) {
			for (String at : addon.getAuthors()) {
				authors = authors + ", " + at;
			}
		} else if (addon.getAuthors().size() <= 1) {
			authors = (String)addon.getAuthors().get(0);
		}
		authors.replaceFirst(", ", "");
		HGAPI.getPlugin().getLogger().info("Enabled Addon: " + addon.getName() + " made by " + authors);
	}
	
	public void loadAddonAll() {
		File dir = new File("plugins/PlanetFxIceHockey/addons/");
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}
		for (Addon addon : AddonLoader.load(dir.getAbsolutePath())) {
			loadAddon(addon);
		}
	}
	
	public List<Addon> getAddons() {
		return this.addons;
	}
	
	public Addon getAddon(String name) {
		for (Addon addon : getAddons()) {
			if (addon.getName().equals(name)) {
				return addon;
			}
		}
		return null;
	}
	
	public void removeAddon(Addon addon) {
		disableAddon(addon);
		this.addons.remove(addon);
	}
	
	public void disableAddon(Addon addon) {
		removeAddonListeners(addon);
		unregisterPermissions(addon);
		addon.onDisable();
		addon.setEnabled(false); 
		HGAPI.getPlugin().getLogger().info("Disabled Addon: " + addon.getName());
	}
	
	public void addAddonListeners(Addon addon) {
		for (Listener listener : addon.getListeners()) {
			Bukkit.getPluginManager().registerEvents(listener, HGAPI.getPlugin());
		}
	}
	
	public void removeAddonListeners(Addon addon) {
		if (addon.getListeners() == null) {
			return;
		}
		for (Iterator<Listener> it = addon.getListeners().iterator(); it.hasNext();) {
			Listener listener = (Listener)it.next();
			it.remove();
			HandlerList.unregisterAll(listener);
			addon.getListeners().remove(listener);
		}
	}
	
	public void registerPermission(Addon addon) {
		for (Permission permission : addon.getPermissions()) {
			Bukkit.getPluginManager().addPermission(permission);
		}
	}
	
	public void unregisterPermission(Permission permission) {
		if (permission == null) {
			return;
		}
		Bukkit.getPluginManager().removePermission(permission);
	}
	
	public void unregisterPermissions(Addon addon) {
		if (addon.getPermissions() == null) {
			return;
		}
		for (Iterator<Permission> it = addon.getPermissions().iterator(); it.hasNext();) {
			Permission permission = (Permission)it.next();
			it.remove();
			unregisterPermission(permission);
			addon.getPermissions().remove(permission);
		}
	}
}
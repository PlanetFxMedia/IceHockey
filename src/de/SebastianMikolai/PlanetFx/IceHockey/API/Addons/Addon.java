package de.SebastianMikolai.PlanetFx.IceHockey.API.Addons;

import com.google.common.base.Charsets;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;

public abstract class Addon {
	
	private boolean enable = true;
	private AddonLogger logger = new AddonLogger(this);
	private List<Listener> listeners = new ArrayList<Listener>();
	private List<Permission> permissions = new ArrayList<Permission>();
	private FileConfiguration newConfig = null;
	private File configFile = new File(getDataFolder(), "config.yml");
	
	public abstract String getName();
	
	public abstract String getVersion();
	
	public abstract List<String> getAuthors();
	
	public abstract void onEnable();
	
	public abstract void onDisable();
	
	public AddonLogger getLogger() {
		return logger;
	}
	
	public boolean isEnabled() {
		return enable;
	}
	
	public void setEnabled(boolean flag) {
		enable = flag;
	}
	
	public List<Listener> getListeners() {
		return listeners;
	}
	
	public List<Permission> getPermissions() {
		return permissions;
	}
	
	public void registerListener(Listener listener) {
		getListeners().add(listener);
	}
	
	public void registerPermission(Permission permission) {
		getPermissions().add(permission);
	}
	
	public FileConfiguration getConfig() {
		if (newConfig == null) {
			reloadConfig();
		}
		return newConfig;
	}
	
	public void reloadConfig() {
		newConfig = YamlConfiguration.loadConfiguration(configFile);
		InputStream defConfigStream = getResource("config.yml");
		if (defConfigStream == null) {
			return;
		}
		if (HGAPI.checkOldMCVersion()) {
			@SuppressWarnings("deprecation")
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);  
			newConfig.setDefaults(defConfig);
		} else if (!HGAPI.checkOldMCVersion()) {
			YamlConfiguration defConfig;  
			defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)); 
			newConfig.setDefaults(defConfig);
		}
	}
	
	public InputStream getResource(String filename) {
		if (filename == null) {
			throw new IllegalArgumentException("Filename cannot be null");
		}
		try {
			URL url = AddonLoader.getClassLoader().getResource(filename);
			if (url == null) {
				return null;
			}
			URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			return connection.getInputStream();
		} catch (IOException ex) {}
		return null;
	}
	
	public void saveConfig() {
		try {
			getConfig().save(configFile);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Could not save config to " + configFile, ex);
		}
	}
	
	public File getDataFolder() {
		return new File(HGAPI.getPlugin().getDataFolder() + "/addons/" + getName());
	}
}
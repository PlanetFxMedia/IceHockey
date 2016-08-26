package de.SebastianMikolai.PlanetFx.IceHockey;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.Arena;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.Team;
import de.SebastianMikolai.PlanetFx.IceHockey.Commands.HockeyCommands;
import de.SebastianMikolai.PlanetFx.IceHockey.Listeners.GUIListener;
import de.SebastianMikolai.PlanetFx.IceHockey.Listeners.PlayerListener;
import de.SebastianMikolai.PlanetFx.IceHockey.Listeners.SignListener;

public class HG extends JavaPlugin {
	
	private HockeyCommands hockey;
	private HashMap<String, Arena> arenas = new HashMap<String, Arena>();
	private HashMap<String, Team> teams = new HashMap<String, Team>();
	
	public void onEnable() {
		saveDefaultConfig();
		File teams = new File("plugins/PlanetFxIceHockey/teams/");
		if (!teams.exists()) {
			teams.mkdirs();
		}
		File arenas = new File("plugins/PlanetFxIceHockey/arenas/");
		if (!arenas.exists()) {
			arenas.mkdirs();
		}
		new HGAPI(this);
		this.hockey = new HockeyCommands();
		Bukkit.getPluginManager().registerEvents(new SignListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
		getCommand("hockey").setExecutor(this.hockey);
	}
	
	public void onDisable() {
		for (Arena arena : HGAPI.getArenaManager().getArenas().values()) {
			arena.stopArena();
		}
		HandlerList.unregisterAll(this);
		hockey = null;
    	arenas.clear();
    	teams.clear();
	}
	
	public void reloadPlugin() {
		onDisable();
		onEnable();
	}
	
	public HockeyCommands getHockeyCommands() {
		return this.hockey;
	}
	
	public HashMap<String, Arena> getDevArenas() {
		return this.arenas;
	}
	
	public HashMap<String, Team> getDevTeams() {
		return this.teams;
	}
}
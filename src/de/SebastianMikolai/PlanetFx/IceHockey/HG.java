package de.SebastianMikolai.PlanetFx.IceHockey;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Addons.Addon;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.Arena;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.Team;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Utils.Lang;
import de.SebastianMikolai.PlanetFx.IceHockey.Commands.HockeyCommands;
import de.SebastianMikolai.PlanetFx.IceHockey.Listeners.GUIListener;
import de.SebastianMikolai.PlanetFx.IceHockey.Listeners.PlayerListener;
import de.SebastianMikolai.PlanetFx.IceHockey.Listeners.SignListener;

public class HG extends JavaPlugin {
	
	private static YamlConfiguration LANG;
	private static File LANG_FILE;
	private String lang;
	private HockeyCommands hockey;
	private HashMap<String, Arena> arenas = new HashMap<String, Arena>();
	private List<Player> teams_creators = new ArrayList<Player>();
	private List<Player> arena_creators = new ArrayList<Player>();
	private HashMap<String, Team> teams = new HashMap<String, Team>();
	
	public void onEnable() {
		createConfig(false);
		checkConfig();
		File teams = new File("plugins/PlanetFxIceHockey/teams/");
		if (!teams.exists()) {
			teams.mkdirs();
		}
		File arenas = new File("plugins/PlanetFxIceHockey/arenas/");
		if (!arenas.exists()) {
			arenas.mkdirs();
		}
		initLangDir(); 
		this.lang = getConfig().getString("Lang");  
		loadLang(); 
		new HGAPI(this);
		this.hockey = new HockeyCommands();
		Bukkit.getPluginManager().registerEvents(new SignListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
		getCommand("hockey").setExecutor(this.hockey);
	}
  
	private void createConfig(boolean recreate) {
		try {
			FileConfiguration fc = getConfig();
			if (!new File(getDataFolder(), "config.yml").exists()) {
				fc.addDefault("Lang", "German");
				fc.addDefault("Game.AutoBalance", Boolean.valueOf(false));
				fc.addDefault("Game.MatchTimer", Integer.valueOf(200));
				fc.addDefault("Game.CountToStart", Integer.valueOf(30));
				fc.addDefault("Game.MinPlayers", Integer.valueOf(2));
				fc.addDefault("Game.MaxPlayers", Integer.valueOf(12));
				fc.addDefault("Game.MaxWingers", Integer.valueOf(3));
				fc.addDefault("Game.MaxDefenders", Integer.valueOf(2));
				fc.addDefault("Game.MusicMatch", Boolean.valueOf(true));
				fc.addDefault("Game.puck.material", "RECORD_7");
				fc.addDefault("Game.PowerBeat.Winger", Double.valueOf(0.6D));
				fc.addDefault("Game.PowerBeat.Defender", Double.valueOf(0.4D));
				fc.addDefault("Game.PowerBeat.Goalkeeper", Double.valueOf(0.3D));
				List<String> win = new ArrayList<String>();
				win.add("266:3");
				win.add("264:1"); 
				List<String> loss = new ArrayList<String>();
				loss.add("1:1");
				fc.addDefault("Game.Rewards.Winners", win);
				fc.addDefault("Game.Rewards.Losers", loss);
				fc.options().copyDefaults(true);
				saveConfig();
				fc.options().copyDefaults(false);
				lang = "German";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void checkConfig() {
		reloadConfig();
		boolean save = false;
		if (getConfig().getString("Lang") == null) {
			getConfig().set("Lang", "German");
			save = true;
		}
		if (getConfig().get("Game.AutoBalance") == null) {
			getConfig().set("Game.AutoBalance", Boolean.valueOf(false));
			save = true;
		}
		if (getConfig().get("Game.MatchTimer") == null) {
			getConfig().set("Game.MatchTimer", Integer.valueOf(200));
			save = true;
		}
		if (getConfig().get("Game.CountToStart") == null) {
			getConfig().set("Game.CountToStart", Integer.valueOf(30));
			save = true;
		}
		if (getConfig().get("Game.MinPlayers") == null) {
			getConfig().set("Game.MinPlayers", Integer.valueOf(2));
			save = true;
		}
		if (getConfig().get("Game.MaxPlayers") == null) {
			getConfig().set("Game.MaxPlayers", Integer.valueOf(12));
			save = true;
		}
		if (getConfig().get("Game.MaxWingers") == null) {
			getConfig().set("Game.MaxWingers", Integer.valueOf(3));
			save = true;
		}
		if (getConfig().get("Game.MaxDefenders") == null) {
			getConfig().set("Game.MaxDefenders", Integer.valueOf(3));
			save = true;
		}
		if (!getConfig().getBoolean("Game.MusicMatch")) {
			getConfig().set("Game.MusicMatch", Boolean.valueOf(true));
			save = true;
		}
		if (getConfig().get("Game.puck.material") == null) {
			getConfig().set("Game.puck.material", "RECORD_7");
			save = true;
		}
		if (getConfig().get("Game.PowerBeat.Winger") == null) {
			getConfig().set("Game.PowerBeat.Winger", Double.valueOf(0.6D));
			save = true;
		}
		if (getConfig().get("Game.PowerBeat.Defender") == null) {
			getConfig().set("Game.PowerBeat.Defender", Double.valueOf(0.4D));
			save = true;
		}
		if (getConfig().get("Game.PowerBeat.Goalkeeper") == null) {
			getConfig().set("Game.PowerBeat.Goalkeeper", Double.valueOf(0.3D));
			save = true;
		}
		if (getConfig().get("Game.Rewards.Winners") == null) {
			List<String> win = new ArrayList<String>();
			win.add("266:3");
			win.add("264:1");
			getConfig().set("Game.Rewards.Winners", win);
			save = true;
		}
		if (getConfig().get("Game.Rewards.Losers") == null) {
			List<String> loss = new ArrayList<String>();
			loss.add("1:1");
			getConfig().set("Game.Rewards.Losers", loss);
			save = true;
		}
		if (save) {
			getConfig().options().copyDefaults(true);
			saveConfig();
			getConfig().options().copyDefaults(false);
		}
	}
	
	private void initLangDir() {
		File dataFolder = new File("plugins/PlanetFxIceHockey/lang/");
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}
		ArrayList<String> langFiles = new ArrayList<String>();
		langFiles.add("German.yml");
		for (String fName : langFiles) {
			File file = new File("plugins/PlanetFxIceHockey/lang/" + fName);
			if (!file.exists()) {
				try {
					InputStream in = getResource("lang/" + fName);
					if (in != null) {
						@SuppressWarnings("deprecation")
						YamlConfiguration config = YamlConfiguration.loadConfiguration(in);
						config.save(file);
					}
				} catch (IOException ex) {
					getLogger().info(ex.getMessage());
				}
			}
		}
	}
	
	public void onDisable() {
		Addon addon;
		for (Iterator<Addon> it = HGAPI.getAddonManager().getAddons().iterator(); it.hasNext();) {
			addon = (Addon)it.next();
			it.remove();
			HGAPI.getAddonManager().removeAddon(addon);
		}
		for (Arena arena : HGAPI.getArenaManager().getArenas().values()) {
			arena.stopArena();
		}
		HandlerList.unregisterAll(this);
		hockey = null;
		lang = null;
		LANG = null;
		LANG_FILE = null;
    	arena_creators.clear();
    	arenas.clear();
    	teams.clear();
    	teams_creators.clear();
	}
	
	public void reloadLang() {
		this.lang = getConfig().getString("Lang");
		loadLang();
	}
	
	public void reloadPlugin() {
		onDisable();
		onEnable();
	}
	
	public List<String> getWhitelistCommands() {
		return getConfig().getStringList("Whitelist-commands");
	}
	
	public List<String> getWinnersRewards() {
		return getConfig().getStringList("Game.Rewards.Winners");
	}
	
	public List<String> getLosersRewards() {
		return getConfig().getStringList("Game.Rewards.Losers");
	}
	
	public HockeyCommands getHockeyCommands() {
		return this.hockey;
	}
	
	public List<Player> getArenaCreators() {
		return this.arena_creators;
	}
	
	public List<Player> getTeamCreators() {
		return this.teams_creators;
	}
	
	public HashMap<String, Arena> getDevArenas() {
		return this.arenas;
	}
	
	public HashMap<String, Team> getDevTeams() {
		return this.teams;
	}
	
	public void loadLang() {
		File lang = new File("plugins/PlanetFxIceHockey/lang/", this.lang + ".yml");
		File dir = new File("plugins/PlanetFxIceHockey/lang/");
		if (!lang.exists()) {
			try {
				getDataFolder().mkdir();
				dir.mkdir();
				lang.createNewFile();
				InputStream defConfigStream = getResource("lang/" + this.lang + ".yml");
				if (defConfigStream != null) {
					@SuppressWarnings("deprecation")
					YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
					defConfig.save(lang);
					Lang.setFile(defConfig);
				}
			} catch (IOException e) {
				e.printStackTrace();
				getLogger().severe("[PlanetFxIceHockey] Couldn't create language file.");
				getLogger().severe("[PlanetFxIceHockey] This is a fatal error. Now reloading...");
				dir.mkdir();
				loadLang();
			}
		}
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
		Lang[] arrayOfLang;
		int j = (arrayOfLang = Lang.values()).length;
		for (int i = 0; i < j; i++) {
			Lang item = arrayOfLang[i];
			if (conf.getString(item.getPath()) == null) {
				conf.set(item.getPath(), item.getDefault());
			}
		}
		Lang.setFile(conf);
		LANG = conf;
		LANG_FILE = lang;
		try {
			conf.save(getLangFile());
		} catch (IOException e) {
			getLogger().log(Level.WARNING, "PlanetFxIceHockey: Failed to save " + this.lang + ".yml.");
			getLogger().log(Level.WARNING, "PlanetFxIceHockey: Report this stack trace to BeYkeRYkt.");
			e.printStackTrace();
		}
	}
	
	public YamlConfiguration getLang() {
		return LANG;
	}
	
	public File getLangFile() {
		return LANG_FILE;
	}
}
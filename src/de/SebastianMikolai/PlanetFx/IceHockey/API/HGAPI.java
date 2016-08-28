package de.SebastianMikolai.PlanetFx.IceHockey.API;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import de.SebastianMikolai.PlanetFx.IceHockey.HG;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.Arena;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.ArenaManager;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Classes.ClassManager;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Signs.SignManager;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.PlayerManager;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.TeamManager;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Utils.GameState;

public class HGAPI {
	
	private static ArenaManager arena;
	private static SignManager signs;
	private static HG plugin;
	private static ClassManager classes;
	private static PlayerManager players;
	private static TeamManager teams;
	private static List<Color> colors;
	private static String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "IceHockey" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD;
	
	public HGAPI(HG _plugin) {
		plugin = _plugin;
		init();
	}
	
	public void init() {
		classes = new ClassManager();
		teams = new TeamManager();
		players = new PlayerManager();
    	signs = new SignManager();
    	arena = new ArenaManager();
    	colors = new ArrayList<Color>();
    	colors.add(Color.AQUA);
    	colors.add(Color.BLACK);
    	colors.add(Color.BLUE);
    	colors.add(Color.FUCHSIA);
    	colors.add(Color.GRAY);
    	colors.add(Color.GREEN);
    	colors.add(Color.LIME);
    	colors.add(Color.MAROON);
    	colors.add(Color.NAVY);
    	colors.add(Color.OLIVE);
    	colors.add(Color.ORANGE);
    	colors.add(Color.PURPLE);
    	colors.add(Color.RED);
    	colors.add(Color.SILVER);
    	colors.add(Color.TEAL);
    	colors.add(Color.WHITE);
    	colors.add(Color.YELLOW);
    	getTeamManager().loadAllTeams();
    	getArenaManager().loadAllArenas();
	}
	
	public static GameState getGameState(String ArenaName) {
		Arena arena = HGAPI.getArenaManager().getArena(ArenaName);
		if (arena != null) {
			return arena.getGameState();
		} else {
			return null;
		}
	}
	
	public static ArenaManager getArenaManager() {
		return arena;
	}
	
	public static SignManager getSignManager() {
		return signs;
	}
	
	public static ClassManager getClassManager() {
		return classes;
	}
	
	public static PlayerManager getPlayerManager() {
		return players;
	}
	
	public static TeamManager getTeamManager() {
		return teams;
	}
	
	public static HG getPlugin() {
		return plugin;
	}
		
	public static List<Color> getColors() {
		return colors;
	}
	
	public static void sendMessage(Player p, String msg, boolean sound) {
		p.sendMessage(prefix + msg);
		if (sound) {
			playSound(p, p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
		}
	}
	
	public static void sendMessageAll(String message, boolean sound) {
		@SuppressWarnings("unchecked")
		List<Player> arrayOfPlayer = (List<Player>) Bukkit.getOnlinePlayers();
		int j = arrayOfPlayer.size();
		for (int i = 0; i < j; i++) {
			Player players = arrayOfPlayer.get(i);
			sendMessage(players, message, sound);
		}
	}
	
	public static void playSound(Player player, Location loc, Sound sound, int Volume, int Pitch) {
		player.playSound(loc, sound, Volume, Pitch);
	}
	
	public static void playEffect(World world, Location loc, Effect effect, int data) {
		world.playEffect(loc, effect, data);
	}
	
	public static void spawnRandomFirework(World world, Location loc) {
		Firework fw = (Firework)world.spawnEntity(loc, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();
		Random random = new Random(); 
		int rt = random.nextInt(5) + 1;
		FireworkEffect.Type type = FireworkEffect.Type.BALL;
		if (rt == 1) {
			type = FireworkEffect.Type.BALL;
		}
		if (rt == 2) {
			type = FireworkEffect.Type.BALL_LARGE;
		}
		if (rt == 3) {
			type = FireworkEffect.Type.BURST;
		}
		if (rt == 4) {
			type = FireworkEffect.Type.CREEPER;
		}
		if (rt == 5) {
			type = FireworkEffect.Type.STAR;
		}
		int r = random.nextInt(256);
		int b = random.nextInt(256);
		int g = random.nextInt(256);
		Color c1 = Color.fromRGB(r, g, b);
		r = random.nextInt(256);
		b = random.nextInt(256);
		g = random.nextInt(256);
		Color c2 = Color.fromRGB(r, g, b);
		FireworkEffect effect = FireworkEffect.builder().flicker(random.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(random.nextBoolean()).build();
		fwm.addEffect(effect);  
		int rp = random.nextInt(1) + 1;
		fwm.setPower(rp); 
		fw.setFireworkMeta(fwm);
	}
	
	public static void checkAndSave(Player player, Arena arena, Location loc) {
		if (arena == null) {
			return;
		}
		if (arena.getFirstTeamLobbyLocation() == null) {
			arena.setFirstTeamLobbyLocation(loc);
			sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.second-team-set-lobby")), true);
			return;
		}
		if (arena.getSecondTeamLobbyLocation() == null) {
			arena.setSecondTeamLobbyLocation(loc);
			sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.first-team-set-spawn")), true);
			return;
		}
		if (arena.getFirstTeamSpawnLocation() == null) {
			arena.setFirstTeamSpawnLocation(loc);
			sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.second-team-set-spawn")), true);
			return;
		}
		if (arena.getSecondTeamSpawnLocation() == null) {
			arena.setSecondTeamSpawnLocation(loc);
			sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.puck-set-spawn")), true);
			return;
		}
		if (arena.getPuckLocation() == null) {
			arena.setPuckLocation(loc);
			sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.set-first-gates")), true);
			return;
		}
		if (!arena.isFirstGatesFulled()) {
			arena.addFirstTeamGate(loc);
			sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.gate-stored")), true);
			return;
		}
		if (!arena.isSecondGatesFulled()) {
			arena.addSecondTeamGate(loc);
			sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.gate-stored")), true);
			return;
		}
		getArenaManager().save(arena);
		getArenaManager().addArena(arena);
		getPlugin().getDevArenas().remove(player.getName());
		sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.arena-saved")), true);
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack parseString(String itemId) {
		String[] parts = itemId.split(":");
		int matId = Integer.parseInt(parts[0]);
		if (parts.length == 2) {
			int amount = Integer.parseInt(parts[1]);
			return new ItemStack(Material.getMaterial(matId), amount);
		}
		if (parts.length == 3) {
			int amount = Integer.parseInt(parts[1]);
			short data = Short.parseShort(parts[2]);
			return new ItemStack(Material.getMaterial(matId), amount, data);
		}
		return new ItemStack(Material.getMaterial(matId));
	}
}
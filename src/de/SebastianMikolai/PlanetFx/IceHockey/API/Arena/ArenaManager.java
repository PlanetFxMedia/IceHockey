package de.SebastianMikolai.PlanetFx.IceHockey.API.Arena;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

import org.bukkit.Location;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.Team;

public class ArenaManager{
	
	private HashMap<String, Arena> arenas = new HashMap<String, Arena>();
	
	public void startMatch(Arena arena) {
		arena.startArena();
	}
  
	public HashMap<String, Arena> getArenas() {
		return this.arenas;
	}
  
	public void addArena(Arena arena) {
		getArenas().put(arena.getName(), arena);
	}
	
	public void removeArena(Arena arena) {
		getArenas().remove(arena.getName());
	}
	
	public boolean isRunning(Arena arena) {
		return arena.isRunning();
	}
	
	public Arena getArena(String name) {
		return (Arena)getArenas().get(name);
	}
	
	public void loadAllArenas() {
		String path = "plugins/PlanetFxIceHockey/arenas";
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String files = listOfFiles[i].getName();
				if (files.endsWith(".yml")) {
					HGAPI.getPlugin().getLogger().info("load " + files.toString());
					LoadArena(files);
				}
			}
		}
	}
  
	public void LoadArena(String fn) {
		try {
			File f = new File("plugins/PlanetFxIceHockey/arenas/" + fn);
			if (!f.exists()) {
				HGAPI.getPlugin().getLogger().info("[PlanetFxIceHockey] plugins/PlanetFxIceHockey/arenas/" + fn + " not found; Creating one.");
				return;
			}
			String name = fn.replace(".yml", "");
			Arena arena = new Arena(name); 
			Scanner snr = new Scanner(f);
			String txt = "";
			String an = snr.nextLine().trim();
			HGAPI.getPlugin().getLogger().info("Arena name: " + an);
			String wn = snr.nextLine().trim(); 
			HGAPI.getPlugin().getLogger().info("World name: " + wn);
			arena.setWorld(wn);
			int amount = Integer.parseInt(snr.nextLine().trim());
			HGAPI.getPlugin().getLogger().info("MaxPlayers: " + amount);
			arena.setMaxPlayers(amount);
			String team1name = snr.nextLine().trim();
			HGAPI.getPlugin().getLogger().info("FirstTeam Name: " + team1name);
			if (HGAPI.getTeamManager().getTeam(team1name) != null) {
				Team team1 = HGAPI.getTeamManager().getTeam(team1name);
				Team teamNew = new Team(team1name);
				teamNew.setColor(team1.getColor());
				arena.setFirstTeam(teamNew);
			}
			String team2name = snr.nextLine().trim();
			HGAPI.getPlugin().getLogger().info("SecondTeam Name: " + team2name);
			if (HGAPI.getTeamManager().getTeam(team2name) != null) {
				Team team2 = HGAPI.getTeamManager().getTeam(team2name);
				Team teamNew2 = new Team(team2name);
				teamNew2.setColor(team2.getColor());	
				arena.setSecondTeam(teamNew2);
			}
			txt = snr.nextLine().trim();
			double locX = Double.parseDouble(txt.split("%")[0]);
			double locY = Double.parseDouble(txt.split("%")[1]);
			double locZ = Double.parseDouble(txt.split("%")[2]);
			Location locA = new Location(HGAPI.getPlugin().getServer().getWorld(wn), locX, locY, locZ);
			HGAPI.getPlugin().getLogger().info("Lobby for first team: " + txt);
			arena.setFirstTeamLobbyLocation(locA);
			txt = snr.nextLine().trim();
			locX = Double.parseDouble(txt.split("%")[0]);
			locY = Double.parseDouble(txt.split("%")[1]);
			locZ = Double.parseDouble(txt.split("%")[2]);
			Location locB = new Location(HGAPI.getPlugin().getServer().getWorld(wn), locX, locY, locZ);
			HGAPI.getPlugin().getLogger().info("Lobby for second team: " + txt);
			arena.setSecondTeamLobbyLocation(locB);   
			txt = snr.nextLine().trim();
			locX = Double.parseDouble(txt.split("%")[0]);
			locY = Double.parseDouble(txt.split("%")[1]);
			locZ = Double.parseDouble(txt.split("%")[2]);
			Location locASpawn = new Location(HGAPI.getPlugin().getServer().getWorld(wn), locX, locY, locZ);     
			HGAPI.getPlugin().getLogger().info("Spawnpoint for first team: " + txt);
			arena.setFirstTeamSpawnLocation(locASpawn);      
			txt = snr.nextLine().trim();
			locX = Double.parseDouble(txt.split("%")[0]);
			locY = Double.parseDouble(txt.split("%")[1]);
			locZ = Double.parseDouble(txt.split("%")[2]);
			Location locBSpawn = new Location(HGAPI.getPlugin().getServer().getWorld(wn), locX, locY, locZ);      
			HGAPI.getPlugin().getLogger().info("Spawnpoint for second team: " + txt);
			arena.setSecondTeamSpawnLocation(locBSpawn);      
			HGAPI.getPlugin().getLogger().info("-- Goal Locations A--");
			for (;;) {
				txt = snr.nextLine().trim();
				HGAPI.getPlugin().getLogger().info("A goal:" + txt);
				if (txt.contains("---")) {
					break;
				}
				locX = Double.parseDouble(txt.split("%")[0]);
				locY = Double.parseDouble(txt.split("%")[1]);
				locZ = Double.parseDouble(txt.split("%")[2]);
				Location loc = new Location(HGAPI.getPlugin().getServer().getWorld(wn), locX, locY, locZ);
				arena.addFirstTeamGate(loc);
			}
			HGAPI.getPlugin().getLogger().info("-- Goal Locations B--");
			for (;;) {
				txt = snr.nextLine().trim();
				HGAPI.getPlugin().getLogger().info("B goal:" + txt);
				if (txt.contains("---")) {
					break;
				}
				locX = Double.parseDouble(txt.split("%")[0]);
				locY = Double.parseDouble(txt.split("%")[1]);
				locZ = Double.parseDouble(txt.split("%")[2]);
				Location loc = new Location(HGAPI.getPlugin().getServer().getWorld(wn), locX, locY, locZ);
				arena.addSecondTeamGate(loc);
			}
			txt = snr.nextLine().trim();
			locX = Double.parseDouble(txt.split("%")[0]);
			locY = Double.parseDouble(txt.split("%")[1]);
			locZ = Double.parseDouble(txt.split("%")[2]);
			Location mloc = new Location(HGAPI.getPlugin().getServer().getWorld(wn), locX, locY, locZ);
			HGAPI.getPlugin().getLogger().info("Puck position:" + txt);    
			arena.setPuckLocation(mloc);  
			snr.close();    
			HGAPI.getPlugin().getLogger().info("Arena loaded " + an);
			HGAPI.getArenaManager().addArena(arena);
		} catch (Exception e) {
			HGAPI.getPlugin().getLogger().warning("[PlanetFxIceHockey] Error while loading plugins/PlanetFxIceHockey/arenas/" + fn);
			HGAPI.getPlugin().getLogger().warning("Error: " + e);
			HGAPI.getPlugin().getLogger().warning("Full Error: ");
			e.printStackTrace();
		}
	}
  
	public void save(Arena arena) {
		String fn = "plugins/PlanetFxIceHockey/arenas/" + arena.getName() + ".yml";
		try {
			File oldFile = new File(fn);
			if (!oldFile.getParentFile().isDirectory()) {
				oldFile.mkdirs();
			}
			oldFile.delete();
			File newFile = new File(fn);
			newFile.createNewFile();      
			String toWrite = "";      
			PrintWriter pw = new PrintWriter(new File(fn));      
			pw.write(arena.getName() + "\r\n");      
			pw.write(arena.getWorld().getName().toString() + "\r\n");      
			pw.write(arena.getMaxPlayers() + "\r\n");      
			pw.write(arena.getFirstTeam().getName() + "\r\n");      
			pw.write(arena.getSecondTeam().getName() + "\r\n");      
			toWrite = arena.getFirstTeamLobbyLocation().getX() + "%" + arena.getFirstTeamLobbyLocation().getY() + "%" + arena.getFirstTeamLobbyLocation().getZ();
			pw.write(toWrite + "\r\n");      
			toWrite = arena.getSecondTeamLobbyLocation().getX() + "%" + arena.getSecondTeamLobbyLocation().getY() + "%" + arena.getSecondTeamLobbyLocation().getZ();
			pw.write(toWrite + "\r\n");      
			toWrite = arena.getFirstTeamSpawnLocation().getX() + "%" + arena.getFirstTeamSpawnLocation().getY() + "%" + arena.getFirstTeamSpawnLocation().getZ();
			pw.write(toWrite + "\r\n");      
			toWrite = arena.getSecondTeamSpawnLocation().getX() + "%" + arena.getSecondTeamSpawnLocation().getY() + "%" + arena.getSecondTeamSpawnLocation().getZ();
			pw.write(toWrite + "\r\n");      
			toWrite = "";
			for (Location item : arena.getFirstTeamGates()) {
				pw.write(item.getX() + "%" + item.getY() + "%" + item.getZ() + "\r\n");
			}
			pw.write("---\r\n");      
			toWrite = "";
			for (Location item : arena.getSecondTeamGates()) {
				pw.write(item.getX() + "%" + item.getY() + "%" + item.getZ() + "\r\n");
			}
			pw.write("---\r\n");      
			toWrite = arena.getPuckLocation().getX() + "%" + arena.getPuckLocation().getY() + "%" + arena.getPuckLocation().getZ();
			pw.write(toWrite + "\r\n");      
			pw.close();
		} catch (Exception e) {
			HGAPI.getPlugin().getLogger().warning("[PlanetFxIceHockey] Error while writing new " + fn);
			e.printStackTrace();
		}
	}
	
	public void deleteArena(Arena arena) {
		removeArena(arena);
		arena.getFile().delete();
	}
}
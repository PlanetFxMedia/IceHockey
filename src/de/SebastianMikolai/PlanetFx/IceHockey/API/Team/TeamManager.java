package de.SebastianMikolai.PlanetFx.IceHockey.API.Team;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

import org.bukkit.Color;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;

public class TeamManager {
	
	private HashMap<String, Team> teams = new HashMap<String, Team>();
			
	public HashMap<String, Team> getTeams() {
		return this.teams;
	}
	
	public void addTeam(Team team) {
		getTeams().put(team.getName(), team);
	}
	
	public void removeTeam(Team team) {
		getTeams().remove(team.getName());
	}
	
	public Team getTeam(String name) {
		return (Team)getTeams().get(name);
	}
	
	public void deleteTeam(Team team) {
	    removeTeam(team);
	    team.getFile().delete();
	}
	
	public void loadAllTeams() {
		String path = "plugins/PlanetFxIceHockey/teams"; 
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String files = listOfFiles[i].getName();
				if (files.endsWith(".yml")) {
					HGAPI.getPlugin().getLogger().info("load " + files.toString());
					LoadTeam(files);
				}
			}
		}
	}
	
	public void LoadTeam(String fn) {
		try {
			File f = new File("plugins/PlanetFxIceHockey/teams/" + fn);
			if (!f.exists()) {
				HGAPI.getPlugin().getLogger().info("[PlanetFxIceHockey] plugins/PlanetFxIceHockey/teams/" + fn + " not found; Creating one.");
				return;
			}
			String name = fn.replace(".yml", "");
			Team team = new Team(name);
			@SuppressWarnings("resource")
			Scanner snr = new Scanner(f); 
			String color_string = snr.nextLine().trim();
			HGAPI.getPlugin().getLogger().info("Team Color: " + color_string);
			Color color = Color.fromRGB(Integer.parseInt(color_string));  
			team.setColor(color);   
			HGAPI.getPlugin().getLogger().info("Team loaded " + team.getName());
			HGAPI.getTeamManager().addTeam(team);
		} catch (Exception e) {
			HGAPI.getPlugin().getLogger().warning("[PlanetFxIceHockey] Error while loading plugins/PlanetFxIceHockey/teams/" + fn);
			HGAPI.getPlugin().getLogger().warning("Error: " + e);
			HGAPI.getPlugin().getLogger().warning("Full Error: ");
			e.printStackTrace();
		}
	}
	
	public void save(Team team) {
		String fn = "plugins/PlanetFxIceHockey/teams/" + team.getName() + ".yml";
		try {
			File oldFile = new File(fn);
			if (!oldFile.getParentFile().isDirectory()) {
				oldFile.mkdirs();
			}
			oldFile.delete();
			File newFile = new File(fn);
			newFile.createNewFile();   
			PrintWriter pw = new PrintWriter(new File(fn));      
			pw.write(team.getColor().asRGB() + "\r\n");      
			pw.close();
		} catch (Exception e) {
			HGAPI.getPlugin().getLogger().warning("[PlanetFxIceHockey] Error while writing new " + fn);
			e.printStackTrace();
		}
	}
}
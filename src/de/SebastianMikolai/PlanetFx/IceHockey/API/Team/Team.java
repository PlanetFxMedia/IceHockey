package de.SebastianMikolai.PlanetFx.IceHockey.API.Team;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;

public class Team {
	
	private String name;
	private List<HockeyPlayer> players = new ArrayList<HockeyPlayer>();
	private List<HockeyPlayer> wingers = new ArrayList<HockeyPlayer>();
	private List<HockeyPlayer> defend = new ArrayList<HockeyPlayer>();
	private HockeyPlayer goalkeeper;
	private Color color;
	
	public Team(String name) {
		this.name = name;
	}
	
	public int getMaxMembers() {
		return HGAPI.getPlugin().getConfig().getInt("Game.MaxPlayers") / 2;
	}
	
	public File getFile() {
		File file = new File("plugins/PlanetFxIceHockey/teams/", this.name + ".yml");
		return file;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void MassTeleportToLocation(Location location) {
		for (HockeyPlayer players : getMembers()) {
			location.setPitch(players.getBukkitPlayer().getLocation().getPitch());
			location.setYaw(players.getBukkitPlayer().getLocation().getYaw());
			players.getBukkitPlayer().teleport(location);
		}
	}
	
	public List<HockeyPlayer> getMembers() {
		return this.players;
	}
	
	public List<HockeyPlayer> getWingers() {
		return this.wingers;
	}
	
	public List<HockeyPlayer> getDefends() {
		return this.defend;
	}
	
	public HockeyPlayer getGoalKeeper() {
		return this.goalkeeper;
	}
	
	public void addWinger(HockeyPlayer player) {
		getWingers().add(player);
	}
	
	public void addDefend(HockeyPlayer player) {
		getDefends().add(player);
	}
	
	public void setGoalkeeper(HockeyPlayer player) {
		this.goalkeeper = player;
	}
	
	public void removeWinger(HockeyPlayer player) {
		getWingers().remove(player);
	}
	
	public void removeDefend(HockeyPlayer player) {
		getDefends().remove(player);
  	}
  
  	public void removeGoalkeeper() {
	  this.goalkeeper = null;
  	}
}
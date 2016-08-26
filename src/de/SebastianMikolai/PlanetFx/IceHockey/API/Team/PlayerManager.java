package de.SebastianMikolai.PlanetFx.IceHockey.API.Team;

import java.util.HashMap;

public class PlayerManager {
	
	private HashMap<String, HockeyPlayer> players = new HashMap<String, HockeyPlayer>();
	
	public HashMap<String, HockeyPlayer> getPlayers() {
		return this.players;
	}
	
	public void addPlayer(String line, HockeyPlayer player) {
		getPlayers().put(line, player);
	}
	
	public void removePlayer(String line) {
		getPlayers().remove(line);
	}
	
	public HockeyPlayer getHockeyPlayer(String name) {
		return (HockeyPlayer)getPlayers().get(name);
	}
}
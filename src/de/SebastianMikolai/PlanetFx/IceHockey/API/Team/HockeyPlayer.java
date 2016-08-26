package de.SebastianMikolai.PlanetFx.IceHockey.API.Team;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.Arena;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Classes.ClassType;

public class HockeyPlayer {
	
	private String name;
	private ClassType type = null;
	private Team team = null;
	private Arena arena = null;
	private boolean ready = false;
	private boolean teleport = false;
	
	public HockeyPlayer(Player player) {
		this.name = player.getName();
	}
	
	public String getName() {
		return this.name;
	}
	
	public Arena getArena() {
		return this.arena;
	}
 	
	public boolean getAllowTeleport() {
		return this.teleport;
	}
	
	public void setAllowTeleport(boolean flag) {
		this.teleport = flag;
	}
	
	public void setArena(Arena arena) {
		this.arena = arena;
	}
	
	public ClassType getType() {
		return this.type;
	}
	
	public void setType(ClassType type) {
		this.type = type;
	}
	
	public Team getTeam() {
		return this.team;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}
	
	public Player getBukkitPlayer() {
		return Bukkit.getPlayer(this.name);
	}
	
	public boolean isReady() {
		return this.ready;
	}
	
	public void setReady(boolean flag) {
		this.ready = flag;
	}
}
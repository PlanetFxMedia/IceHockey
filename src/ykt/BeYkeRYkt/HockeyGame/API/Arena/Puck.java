package ykt.BeYkeRYkt.HockeyGame.API.Arena;

import org.bukkit.entity.Item;

import ykt.BeYkeRYkt.HockeyGame.API.Team.HockeyPlayer;

public class Puck {
	
	private HockeyPlayer lastPlayer;
	private Item item;
	
	public Puck(Item item) {
		this.item = item;
	}
	
	public HockeyPlayer getLastPlayer() {
		return this.lastPlayer;
	}
	
	public void setLastPlayer(HockeyPlayer player) {
		this.lastPlayer = player;
	}
	
	public void clearPlayer() {
		this.lastPlayer = null;
	}
	
	public Item getItem() {
		return this.item;
	}
	
	public void clearItem() {
		this.item = null;
	}
}
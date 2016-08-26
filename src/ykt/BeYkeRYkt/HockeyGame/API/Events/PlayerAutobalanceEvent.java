package ykt.BeYkeRYkt.HockeyGame.API.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ykt.BeYkeRYkt.HockeyGame.API.Team.HockeyPlayer;
import ykt.BeYkeRYkt.HockeyGame.API.Team.Team;

public class PlayerAutobalanceEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private HockeyPlayer player;
	private Team old;
	private Team new_;
	private boolean cancel = false;
	
	public PlayerAutobalanceEvent(HockeyPlayer player, Team old, Team new_) {
		this.player = player;
		this.old = old;
		this.new_ = new_;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public HockeyPlayer getPlayer() {
		return this.player;
	}
	
	public Team getOldTeam() {
		return this.old;
	}
	
	public Team getNewTeam() {
		return this.new_;
	}
	
	public boolean isCancelled() {
		return this.cancel;
	}
	
	public void setCancelled(boolean flag) {
		this.cancel = flag;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
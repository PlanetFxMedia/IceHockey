package ykt.BeYkeRYkt.HockeyGame.Runnables;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import ykt.BeYkeRYkt.HockeyGame.API.Arena.Arena;
import ykt.BeYkeRYkt.HockeyGame.API.HGAPI;
import ykt.BeYkeRYkt.HockeyGame.API.Team.HockeyPlayer;
import ykt.BeYkeRYkt.HockeyGame.API.Utils.Lang;

public class CountToStartRunnable extends BukkitRunnable {
	
	private Arena arena;
	private int seconds = HGAPI.getPlugin().getConfig().getInt("Game.CountToStart");
	
	public CountToStartRunnable(Arena arena) {
		this.arena = arena;
	}
	
	public int getSeconds() {
		return this.seconds;
	}
	
	public void run() {
		for (HockeyPlayer players : this.arena.getPlayers()) {
			if (!players.isReady()) {
				this.arena.getCountToStartRunnable().cancel();
			}
		}
		if (this.arena.getPlayers().size() < HGAPI.getPlugin().getConfig().getInt("Game.MinPlayers")) {
			this.arena.stopArena();
		}
		if (this.seconds == 30) {
			for (HockeyPlayer players : this.arena.getPlayers()) {
				HGAPI.sendMessage(players.getBukkitPlayer(), ChatColor.YELLOW + String.valueOf(this.seconds) + ChatColor.GRAY + "...", false);
				HGAPI.playSound(players.getBukkitPlayer(), players.getBukkitPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
			}
		} else if (this.seconds == 25) {
			for (HockeyPlayer players : this.arena.getPlayers()) {
				HGAPI.sendMessage(players.getBukkitPlayer(), ChatColor.YELLOW + String.valueOf(this.seconds) + ChatColor.GRAY + "...", false);
				HGAPI.playSound(players.getBukkitPlayer(), players.getBukkitPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
			}
		} else if (this.seconds == 20) {
			for (HockeyPlayer players : this.arena.getPlayers()) {
				HGAPI.sendMessage(players.getBukkitPlayer(), ChatColor.YELLOW + String.valueOf(this.seconds) + ChatColor.GRAY + "...", false);
				HGAPI.playSound(players.getBukkitPlayer(), players.getBukkitPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
			}
		} else if (this.seconds == 15) {
			for (HockeyPlayer players : this.arena.getPlayers()) {
				HGAPI.sendMessage(players.getBukkitPlayer(), ChatColor.YELLOW + String.valueOf(this.seconds) + ChatColor.GRAY + "...", false);
				HGAPI.playSound(players.getBukkitPlayer(), players.getBukkitPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
			}
		}
		if ((this.seconds < 11) && (this.seconds > 0)) {
			for (HockeyPlayer players : this.arena.getPlayers()) {
				HGAPI.sendMessage(players.getBukkitPlayer(), ChatColor.YELLOW + String.valueOf(this.seconds) + ChatColor.GRAY + "...", false);
				HGAPI.playSound(players.getBukkitPlayer(), players.getBukkitPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
			}
		}
		if (this.seconds == 0) {
			this.arena.broadcastMessage(Lang.GAME_STARTED.toString());
			this.arena.startArena();
		}
		this.seconds -= 1;
	}
}
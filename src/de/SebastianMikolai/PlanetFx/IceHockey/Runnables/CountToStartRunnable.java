package de.SebastianMikolai.PlanetFx.IceHockey.Runnables;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.Arena;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.HockeyPlayer;

public class CountToStartRunnable extends BukkitRunnable {
	
	private Arena arena;
	private int seconds = HGAPI.getPlugin().getConfig().getInt("GameSettings.CountToStart");
	
	public CountToStartRunnable(Arena arena) {
		this.arena = arena;
	}
	
	public int getSeconds() {
		return this.seconds;
	}
	
	public void run() {
		for (HockeyPlayer hp : this.arena.getPlayers()) {
			hp.getBukkitPlayer().setLevel(getSeconds());
			hp.getBukkitPlayer().setExp(getSeconds() * 0.03F);
		}
		for (HockeyPlayer players : this.arena.getPlayers()) {
			if (!players.isReady()) {
				this.arena.getCountToStartRunnable().cancel();
			}
		}
		if (this.arena.getPlayers().size() < HGAPI.getPlugin().getConfig().getInt("GameSettings.MinPlayers")) {
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
			this.arena.broadcastMessage(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.game-started")));
			this.arena.startArena();
		}
		this.seconds -= 1;
	}
}
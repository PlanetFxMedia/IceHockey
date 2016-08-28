package de.SebastianMikolai.PlanetFx.IceHockey.Runnables;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.Arena;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.Puck;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Events.GoalEvent;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.HockeyPlayer;

public class ArenaRunnable extends BukkitRunnable {
	
	private Arena arena;
	private int seconds;
	private int lastgoalsec = -2;
	private Puck puck;
	
	public ArenaRunnable(Arena arena, Puck item, int seconds) {
		this.arena = arena;
		this.puck = item;
		this.seconds = seconds;
	}
	
	public void setPuck(Puck puck) {
		this.puck = puck;
	}
	
	public int getSeconds() {
		return this.seconds;
	}
	
	public void run() {
		for (Location loc : this.arena.getFirstTeamGates()) {
			if ((this.puck.getItem() != null) && (loc.getWorld().getName().equals(this.puck.getItem().getWorld().getName())) && (loc.getBlockX() == this.puck.getItem().getLocation().getBlockX()) && (loc.getBlockY() == this.puck.getItem().getLocation().getBlockY()) && (loc.getBlockZ() == this.puck.getItem().getLocation().getBlockZ())) {
				GoalEvent event = new GoalEvent(this.puck.getLastPlayer(), this.arena, this.puck);
				Bukkit.getPluginManager().callEvent(event); 
				this.arena.broadcastMessage(ChatColor.YELLOW + this.puck.getLastPlayer().getName() + ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.scored-goal")) + ChatColor.GOLD + this.arena.getFirstTeam().getName());
				HGAPI.spawnRandomFirework(this.arena.getWorld(), this.puck.getLastPlayer().getBukkitPlayer().getLocation());
				this.arena.getWorld().createExplosion(loc, 0.0F);
				this.puck.getItem().getItemStack().setAmount(0);
				this.puck.getItem().remove();
				this.puck.clearItem();
				this.puck.clearPlayer();
				this.arena.addSecondTeamScore(1);  
				this.lastgoalsec = this.seconds;
				this.seconds += 5;
			}
		}
		for (Location loc : this.arena.getSecondTeamGates()) {
			if ((this.puck.getItem() != null) && (loc.getWorld().getName().equals(this.puck.getItem().getWorld().getName())) && (loc.getBlockX() == this.puck.getItem().getLocation().getBlockX()) && (loc.getBlockY() == this.puck.getItem().getLocation().getBlockY()) && (loc.getBlockZ() == this.puck.getItem().getLocation().getBlockZ())) {
				GoalEvent event = new GoalEvent(this.puck.getLastPlayer(), this.arena, this.puck);
				Bukkit.getPluginManager().callEvent(event);
				this.arena.broadcastMessage(ChatColor.YELLOW + this.puck.getLastPlayer().getName() + ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.scored-goal")) + ChatColor.GOLD + this.arena.getSecondTeam().getName());
				HGAPI.spawnRandomFirework(this.arena.getWorld(), this.puck.getLastPlayer().getBukkitPlayer().getLocation()); 
				this.arena.getWorld().createExplosion(loc, 0.0F); 
				this.puck.getItem().getItemStack().setAmount(0);
				this.puck.getItem().remove();
				this.puck.clearItem();
				this.puck.clearPlayer();
				this.arena.addFirstTeamScore(1); 
				this.lastgoalsec = this.seconds;
				this.seconds += 5;
			}
		}
		if (HGAPI.getPlugin().getConfig().getBoolean("GameSettings.AutoBalance")) {
			this.arena.autobalance();
		}
		if (this.arena.getPlayers().size() < HGAPI.getPlugin().getConfig().getInt("GameSettings.MinPlayers")) {
			this.arena.stopArena();
		}
		if ((this.seconds == this.lastgoalsec) && (this.seconds > 5)) {
			this.arena.respawnPuck();
		}
		if ((this.seconds < 6) && (this.seconds > 0)) {
			for (HockeyPlayer players : this.arena.getPlayers()) {
				HGAPI.sendMessage(players.getBukkitPlayer(), ChatColor.YELLOW + String.valueOf(this.seconds) + ChatColor.GRAY + "...", false);
				HGAPI.playSound(players.getBukkitPlayer(), players.getBukkitPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
			}
		}
		if (this.seconds == 0) {
			this.arena.startRewards();
			this.arena.stopArena();
		}
		for (HockeyPlayer hp : this.arena.getPlayers()) {
			ScoreboardManager m = Bukkit.getScoreboardManager();
			Scoreboard b = m.getNewScoreboard();
			Objective o = b.registerNewObjective("IceHockey", "dummy");	
			int i = 6;
			o.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lIceHockey"));
			o.setDisplaySlot(DisplaySlot.SIDEBAR);
			o.getScore(" ").setScore(i--);
			o.getScore(ChatColor.translateAlternateColorCodes('&', "&4Rot &6" + arena.getFirstTeamScores() + ":" + arena.getSecondTeamScores() + " &2Grün")).setScore(i--);
			o.getScore("  ").setScore(i--);
			if (HGAPI.getArenaManager().isRunning(arena)) {
				o.getScore(ChatColor.translateAlternateColorCodes('&', "&dSpielzeit: " + arena.getMainRunnable().getSeconds())).setScore(i--);
				o.getScore("   ").setScore(i--);
			}
			hp.getBukkitPlayer().setScoreboard(b);	
			hp.getBukkitPlayer().setLevel(getSeconds());
		}
		this.seconds -= 1;
	}
}
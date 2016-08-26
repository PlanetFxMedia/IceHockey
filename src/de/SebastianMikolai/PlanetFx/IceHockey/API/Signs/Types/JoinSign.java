package de.SebastianMikolai.PlanetFx.IceHockey.API.Signs.Types;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.Arena;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Signs.SignType;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.HockeyPlayer;

public class JoinSign implements SignType {

	public void handleCreateSign(SignChangeEvent event) {
		String arenaName = event.getLine(2);
		String teamName = event.getLine(3);
		if (HGAPI.getArenaManager().getArena(arenaName) != null) {
			Arena arena = HGAPI.getArenaManager().getArena(arenaName);
			if (arena.getTeam(teamName) != null) {
				event.setLine(0, ChatColor.DARK_RED + "[IceHockey]");
				HGAPI.sendMessage(event.getPlayer(), ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.success-sign-create")), false);
				event.getBlock().getState().update(true);
			} else if (arena.getTeam(teamName) == null) {
				HGAPI.sendMessage(event.getPlayer(), ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.team-does-not-exist")), false);
				event.setCancelled(true);
				event.getBlock().breakNaturally();
			}
		} else if (HGAPI.getArenaManager().getArena(arenaName) == null) {
			HGAPI.sendMessage(event.getPlayer(), ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.arena-does-not-exist")), false);
			event.setCancelled(true);
			event.getBlock().breakNaturally();
		}
	}
	
	public void handleClickSign(PlayerInteractEvent event) {
		String arenaName = ((Sign)event.getClickedBlock().getState()).getLine(2);
		String teamName = ((Sign)event.getClickedBlock().getState()).getLine(3);
		Arena arena = HGAPI.getArenaManager().getArena(arenaName);
		Player player = event.getPlayer();
		if (arena != null) {
			if (HGAPI.getPlayerManager().getHockeyPlayer(player.getName()) != null) {
				return;
			}
			boolean run = arena.isRunning();
			if (!run) {
				if (teamName.equals(arena.getFirstTeam().getName())) {
					HockeyPlayer hplayer = new HockeyPlayer(player);
					if (arena.getFirstTeam().getMembers().size() < arena.getFirstTeam().getMaxMembers()) {
						arena.joinPlayer(hplayer, arena.getFirstTeam());
						HGAPI.playSound(player, player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
					} else {
						HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.arena-full")), run);
					}
				} else if (teamName.equals(arena.getSecondTeam().getName())) {
					HockeyPlayer hplayer = new HockeyPlayer(player);
					if (arena.getSecondTeam().getMembers().size() < arena.getSecondTeam().getMaxMembers()) {
						arena.joinPlayer(hplayer, arena.getSecondTeam());
						HGAPI.playSound(player, player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
					} else {
						HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.arena-full")), run);
					}
				}
			} else {
				HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.game-running")), run);
			}
		} else {
			HGAPI.sendMessage(event.getPlayer(), ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.arena-does-not-exist")), false);
		}
	}
	
	public void handleDestroy(BlockBreakEvent event) {
		HGAPI.sendMessage(event.getPlayer(), ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.success-sign-remove")), false);
	}
}
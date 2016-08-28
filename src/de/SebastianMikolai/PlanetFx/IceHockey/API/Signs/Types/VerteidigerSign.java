package de.SebastianMikolai.PlanetFx.IceHockey.API.Signs.Types;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Classes.ClassType;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Signs.SignType;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.HockeyPlayer;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Utils.ItemGiver;

public class VerteidigerSign implements SignType {
	
	public void handleCreateSign(SignChangeEvent event) {
		String className = event.getLine(1);
		if (HGAPI.getClassManager().getClass(className) != null) {
			event.setLine(0, ChatColor.DARK_RED + "[IceHockey]");  
			HGAPI.sendMessage(event.getPlayer(), ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.success-sign-create")), false);
			event.getBlock().getState().update(true);
		} else {
			HGAPI.sendMessage(event.getPlayer(), ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.class-does-not-exist")), false);
			event.setCancelled(true);
			event.getBlock().breakNaturally();
		}
	}
	
	public void handleClickSign(PlayerInteractEvent event) {
		String className = ((Sign)event.getClickedBlock().getState()).getLine(1);
		ClassType type = HGAPI.getClassManager().getClass(className);
		if (type != null) {
			HockeyPlayer player = HGAPI.getPlayerManager().getHockeyPlayer(event.getPlayer().getName());
			if (player != null) {
				if (player.getTeam().getDefends().size() < HGAPI.getPlugin().getConfig().getInt("GameSettings.MaxVerteidiger")) {
					if ((player.getType() != null) && (!player.getType().getName().equals(type.getName()))) {
						HGAPI.sendMessage(player.getBukkitPlayer(), ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.change-class")), false);
						HGAPI.playSound(player.getBukkitPlayer(), player.getBukkitPlayer().getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1);
						player.getBukkitPlayer().getInventory().clear();
						player.getBukkitPlayer().updateInventory();
						if (player.getTeam().getWingers().contains(player)) {
							player.getTeam().removeWinger(player);
						} else if ((player.getTeam().getGoalKeeper() != null) && (player.getTeam().getGoalKeeper().equals(player))) {
							player.getTeam().removeGoalkeeper();
						}
						player.setType(type);
						ItemGiver.setItems(player, player.getTeam().getColor());
						player.getTeam().addDefend(player);
					} else if (player.getType() == null) {
						HGAPI.sendMessage(player.getBukkitPlayer(), ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.change-class")), false);
						HGAPI.playSound(player.getBukkitPlayer(), player.getBukkitPlayer().getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1);
						player.getBukkitPlayer().getInventory().clear();
						player.getBukkitPlayer().updateInventory();
						player.setType(type);
						ItemGiver.setItems(player, player.getTeam().getColor());
						player.getTeam().addDefend(player);
					}
					if (!player.isReady()) {
						player.setReady(true);
						player.getArena().broadcastMessage(ChatColor.YELLOW + player.getName() + ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.player-is-ready")));
						player.getArena().startCountToStartRunnable();
					}
				} else {
					HGAPI.sendMessage(player.getBukkitPlayer(), ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.class-full")), false);
				}
			}
		} else {
			HGAPI.sendMessage(event.getPlayer(), ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.class-does-not-exist")), false);
		}
	}
	
	public void handleDestroy(BlockBreakEvent event) {
		HGAPI.sendMessage(event.getPlayer(), ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.success-sign-remove")), false);
	}
}
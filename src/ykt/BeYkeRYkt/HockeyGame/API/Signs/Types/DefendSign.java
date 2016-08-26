package ykt.BeYkeRYkt.HockeyGame.API.Signs.Types;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import ykt.BeYkeRYkt.HockeyGame.API.Classes.ClassType;
import ykt.BeYkeRYkt.HockeyGame.API.HGAPI;
import ykt.BeYkeRYkt.HockeyGame.API.Signs.SignType;
import ykt.BeYkeRYkt.HockeyGame.API.Team.HockeyPlayer;
import ykt.BeYkeRYkt.HockeyGame.API.Utils.ItemGiver;
import ykt.BeYkeRYkt.HockeyGame.API.Utils.Lang;

public class DefendSign implements SignType {
	
	public void handleCreateSign(SignChangeEvent event) {
		String className = event.getLine(1);
		if (HGAPI.getClassManager().getClass(className) != null) {
			event.setLine(0, ChatColor.RED + "[PlanetFxIceHockey]");  
			HGAPI.sendMessage(event.getPlayer(), Lang.SUCCESS_SIGN_CREATE.toString(), false);
			event.getBlock().getState().update(true);
		} else {
			HGAPI.sendMessage(event.getPlayer(), Lang.CLASS_DOES_NOT_EXIT.toString(), false);
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
				if (player.getTeam().getDefends().size() < HGAPI.getPlugin().getConfig().getInt("Game.MaxDefenders")) {
					if ((player.getType() != null) && (!player.getType().getName().equals(type.getName()))) {
						HGAPI.sendMessage(player.getBukkitPlayer(), Lang.CHANGE_CLASS.toString(), false);
						if (!HGAPI.checkOldMCVersion()) {
							HGAPI.playSound(player.getBukkitPlayer(), player.getBukkitPlayer().getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1);
						}
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
						HGAPI.sendMessage(player.getBukkitPlayer(), Lang.CHANGE_CLASS.toString(), false);
						if (!HGAPI.checkOldMCVersion()) {
							HGAPI.playSound(player.getBukkitPlayer(), player.getBukkitPlayer().getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1);
						}
						player.getBukkitPlayer().getInventory().clear();
						player.getBukkitPlayer().updateInventory();
						player.setType(type);
						ItemGiver.setItems(player, player.getTeam().getColor());
						player.getTeam().addDefend(player);
					}
					if (!player.isReady()) {
						player.setReady(true);
						player.getArena().broadcastMessage(ChatColor.YELLOW + player.getName() + Lang.PLAYER_READY.toString());
						player.getArena().startCountToStartRunnable();
					}
				} else {
					HGAPI.sendMessage(player.getBukkitPlayer(), Lang.CLASS_FULL.toString(), false);
				}
			}
		} else {
			HGAPI.sendMessage(event.getPlayer(), Lang.CLASS_DOES_NOT_EXIT.toString(), false);
		}
	}
	
	public void handleDestroy(BlockBreakEvent event) {
		HGAPI.sendMessage(event.getPlayer(), Lang.SUCCESS_SIGN_REMOVE.toString(), false);
	}
}
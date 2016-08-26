package de.SebastianMikolai.PlanetFx.IceHockey.Listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.Arena;
import de.SebastianMikolai.PlanetFx.IceHockey.API.GUI.CustomGUIMenu;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.HockeyPlayer;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.Team;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Utils.Lang;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if (HGAPI.getPlayerManager().getPlayers().containsKey(player.getName())) {
			String msg = event.getMessage();
			String[] split = msg.split(" ");
			if ((split.length > 0) && (split[0].startsWith("/"))) {
				String command = split[0].substring(1);
				if (!HGAPI.getPlugin().getWhitelistCommands().contains(command)) {
					HGAPI.sendMessage(player, Lang.NO_COMMANDS.toString() + Lang.ICON_ARENA_LEAVE.toString(), true);
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoinInv(PlayerJoinEvent event) {
//		Player player = event.getPlayer();
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if (HGAPI.getPlayerManager().getPlayers().containsKey(player.getName())) {
			Bukkit.getScheduler().runTaskLaterAsynchronously(HGAPI.getPlugin(), new Runnable() {
				HockeyPlayer hp;
				
				public void run() {
					this.hp.getArena().leavePlayer(this.hp);
				}
			}, 10L);
		}
	}
	
	@EventHandler
	public void onPlayerGameMode(PlayerGameModeChangeEvent event) {
		Player player = event.getPlayer();
		if (HGAPI.getPlayerManager().getPlayers().containsKey(player.getName())) {
			HockeyPlayer hp = HGAPI.getPlayerManager().getHockeyPlayer(player.getName());
			if (hp.getArena().isRunning()) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		if (HGAPI.getPlayerManager().getPlayers().containsKey(name)) {
			HockeyPlayer hplayer = HGAPI.getPlayerManager().getHockeyPlayer(name);
			hplayer.getArena().leavePlayer(hplayer);
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (HGAPI.getPlugin().getArenaCreators().contains(player)) {
				Arena arena = (Arena)HGAPI.getPlugin().getDevArenas().get(player.getName());
				Location loc = new Location(block.getWorld(), block.getLocation().getBlockX(), block.getLocation().getY() + 1.0D, block.getLocation().getBlockZ());
				HGAPI.checkAndSave(player, arena, loc);
			}
		}
		if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)){
			if (HGAPI.getPlayerManager().getPlayers().containsKey(player.getName())) {
				HockeyPlayer hp = HGAPI.getPlayerManager().getHockeyPlayer(player.getName());
				if (player.getInventory().getItemInMainHand() != null) {
					if (!player.getInventory().getItemInMainHand().hasItemMeta()) {
						return;
					}
					if (!player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
						return;
					}
					if (!player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Lang.HOCKEY_STICK.toString())) {
						return;
					}
					for (Entity entity : player.getNearbyEntities(0.6D, 0.6D, 0.6D)) {
						if ((entity instanceof Item)) {
							Item i = (Item)entity;
							if (i.getEntityId() == hp.getArena().getPuckEntity().getItem().getEntityId()) {
								double speedbonus = 0.3D;
								double beat = 0.0D;
								Vector vi = player.getEyeLocation().getDirection();
								hp.getArena().getPuckEntity().setLastPlayer(hp);
								if (hp.getType().getName().equals("Winger")) {
									beat = HGAPI.getPlugin().getConfig().getDouble("Game.PowerBeat.Winger");
									if (player.isSprinting()) {
										beat = speedbonus + beat;
									}
									if (player.isSneaking()) {
										vi.multiply(0.8D);
									}
								} else if (hp.getType().getName().equals("Defender")) {
									beat = HGAPI.getPlugin().getConfig().getDouble("Game.PowerBeat.Defender");
									if (player.isSprinting()) {
										beat = speedbonus + beat;
									}
									if (player.isSneaking()) {
										vi.multiply(0.8D);
									}
								} else if (hp.getType().getName().equals("Goalkeeper")) {
									beat = HGAPI.getPlugin().getConfig().getDouble("Game.PowerBeat.Goalkeeper");
									if (player.isSprinting()) {
										beat = speedbonus + beat;
									}
									if (player.isSneaking()) {
										vi.multiply(0.8D);
									}
								}
								vi.multiply(beat); 
								entity.setVelocity(vi);
							}
						}
					}
				}
			}
		} else if (event.getAction() != Action.RIGHT_CLICK_AIR) {
			event.getAction();
		}
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();
		int i;
		if (HGAPI.getPlugin().getArenaCreators().contains(player)) {
			Arena arena = new Arena(message, player.getWorld());
			if ((!HGAPI.getPlugin().getDevArenas().containsKey(player.getName())) && (!HGAPI.getArenaManager().getArenas().containsKey(message))) {
				HGAPI.getPlugin().getDevArenas().put(player.getName(), arena);
				event.setCancelled(true); 
				HGAPI.sendMessage(player, Lang.SELECT_THE_FIRST_TEAM.toString(), true); 
				int size = 36;
				CustomGUIMenu menu = new CustomGUIMenu(ChatColor.stripColor(Lang.SELECT_THE_FIRST_TEAM.toString()), size);
				List<String> keys = new ArrayList<String>(HGAPI.getTeamManager().getTeams().keySet());
				for (i = 0; i < keys.size(); i++) {
					String obj = (String)keys.get(i);
					ItemStack item = new ItemStack(Material.LEATHER_HELMET);
					LeatherArmorMeta meta = (LeatherArmorMeta)item.getItemMeta();
					meta.setDisplayName(HGAPI.getTeamManager().getTeam(obj).getName());
					meta.setColor(HGAPI.getTeamManager().getTeam(obj).getColor());
					item.setItemMeta(meta); 
					menu.addItem(item, keys.indexOf(obj));
				}
				player.openInventory(menu.getInventory());
			} else if ((!HGAPI.getPlugin().getDevArenas().containsKey(player.getName())) && (HGAPI.getArenaManager().getArenas().containsKey(message))) {
				HGAPI.sendMessage(player, Lang.ARENA_NAME_IS_TAKEN.toString(), true);
				event.setCancelled(true);
			}
		}
		if (HGAPI.getPlugin().getTeamCreators().contains(player)) {
			Team team = new Team(message);
			if ((!HGAPI.getPlugin().getDevTeams().containsKey(player.getName())) && (!HGAPI.getTeamManager().getTeams().containsKey(message))) {
				HGAPI.getPlugin().getDevTeams().put(player.getName(), team);
				event.setCancelled(true);
				HGAPI.sendMessage(player, Lang.SELECT_TEAM_COLOR.toString(), true);  
				int size = 36;
				CustomGUIMenu menu = new CustomGUIMenu(ChatColor.stripColor(Lang.SELECT_TEAM_COLOR.toString()), size);
				for (Color color : HGAPI.getColors()) {
					ItemStack item = new ItemStack(Material.LEATHER_HELMET);
					LeatherArmorMeta meta = (LeatherArmorMeta)item.getItemMeta();
					meta.setColor(color);
					item.setItemMeta(meta);
					menu.addItem(item, HGAPI.getColors().indexOf(color));
				}
				player.openInventory(menu.getInventory());
			} else if ((!HGAPI.getPlugin().getDevTeams().containsKey(player.getName())) && (HGAPI.getTeamManager().getTeams().containsKey(message))) {
				HGAPI.sendMessage(player, Lang.TEAM_NAME_IS_TAKEN.toString(), true);
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerPickup(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if (HGAPI.getPlayerManager().getPlayers().containsKey(player.getName())) {
			event.setCancelled(true);
		}
		for (String name : HGAPI.getArenaManager().getArenas().keySet()) {
			Arena arena = HGAPI.getArenaManager().getArena(name);
			if ((arena.getPuckEntity() != null) &&  (event.getItem().getEntityId() == arena.getPuckEntity().getItem().getEntityId())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (!HGAPI.checkOldMCVersion()) {
			Entity target = event.getEntity();
			if ((target instanceof Player)) {
				Player player = (Player)target;
				String name = player.getName();
				if ((HGAPI.getPlayerManager().getPlayers().containsKey(name)) && (player.getHealth() < 4.0D)) {
					player.setHealth(4.0D);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (HGAPI.getPlayerManager().getPlayers().containsKey(player.getName())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		if (HGAPI.getPlayerManager().getPlayers().containsKey(player.getName())) {
			HockeyPlayer hp = HGAPI.getPlayerManager().getHockeyPlayer(player.getName());
			if ((hp.getArena().isRunning()) &&  (!hp.getAllowTeleport())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBlockDestroyEvent(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (HGAPI.getPlayerManager().getPlayers().containsKey(player.getName())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (HGAPI.getPlayerManager().getPlayers().containsKey(player.getName())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		if (HGAPI.getPlayerManager().getPlayers().containsKey(name)) {
			HockeyPlayer hplayer = HGAPI.getPlayerManager().getHockeyPlayer(name); 
			hplayer.getArena().leavePlayer(hplayer);
		}
	}
	
 	@EventHandler
 	public void onDamagePlayers(EntityDamageByEntityEvent event) {
 		if (!HGAPI.checkOldMCVersion()) {
 			Entity target = event.getEntity();
 			if ((target instanceof Player)) {
 				Player player = (Player)target;
 				String name = player.getName();
 				if ((HGAPI.getPlayerManager().getPlayers().containsKey(name)) && (player.getHealth() < 4.0D)) {
 					player.setHealth(4.0D);
 				}
 			}
 		}
 	}
}
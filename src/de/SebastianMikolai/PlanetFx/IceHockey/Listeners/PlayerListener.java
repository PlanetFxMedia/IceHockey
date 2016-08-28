package de.SebastianMikolai.PlanetFx.IceHockey.Listeners;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.Arena;
import de.SebastianMikolai.PlanetFx.IceHockey.API.GUI.CustomGUIMenu;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.HockeyPlayer;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.Team;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (!p.isOp()) {
			HGAPI.sendMessage(p, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.no-commands")) + ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.icon-arena-leave")), true);
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(HGAPI.getPlugin(), new Runnable() {

			@Override
			public void run() {
				if (!HGAPI.getArenaManager().getArenas().isEmpty()) {
					Arena arena = HGAPI.getArenaManager().getArena("IceHockey");
					if (HGAPI.getArenaManager().isRunning(arena)) {
						p.kickPlayer("Auf diesem Server wird bereits gespielt!");
					} else  {
						Random rnd = new Random();
						Boolean bool = rnd.nextBoolean();
						if (bool) {
							Team team = arena.getFirstTeam();
							HockeyPlayer hplayer = new HockeyPlayer(p);		
							arena.joinPlayer(hplayer, team);
							HGAPI.playSound(p, p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
						} else {
							Team team = arena.getSecondTeam();
							HockeyPlayer hplayer = new HockeyPlayer(p);		
							arena.joinPlayer(hplayer, team);
							HGAPI.playSound(p, p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
						}
						ScoreboardManager m = Bukkit.getScoreboardManager();
						Scoreboard b = m.getNewScoreboard();
						Objective o = b.registerNewObjective("IceHockey", "dummy");	
						int i = 6;
						o.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lIceHockey"));
						o.setDisplaySlot(DisplaySlot.SIDEBAR);
						o.getScore(" ").setScore(i--);
						o.getScore(ChatColor.translateAlternateColorCodes('&', "&4Rot &6" + arena.getFirstTeamScores() + ":" + arena.getSecondTeamScores() + " &2Grün")).setScore(i--);
						o.getScore("  ").setScore(i--);
						p.setScoreboard(b);				
					}
				}
			}	
		}, 10L);
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if (HGAPI.getPlayerManager().getPlayers().containsKey(p.getName())) {
			Bukkit.getScheduler().runTaskLaterAsynchronously(HGAPI.getPlugin(), new Runnable() {
				HockeyPlayer hp;
				
				public void run() {
					this.hp.getArena().leavePlayer(this.hp);
				}
			}, 10L);
		}
	}
	
	@EventHandler
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent e) {
		Player p = e.getPlayer();
		if (!p.isOp()) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent e) {
		Player p = e.getPlayer();
		String name = p.getName();
		if (HGAPI.getPlayerManager().getPlayers().containsKey(name)) {
			HockeyPlayer hp = HGAPI.getPlayerManager().getHockeyPlayer(name);
			hp.getArena().leavePlayer(hp);
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Block block = e.getClickedBlock();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (HGAPI.getPlugin().getArenaCreators().contains(p)) {
				Arena arena = (Arena)HGAPI.getPlugin().getDevArenas().get(p.getName());
				Location loc = new Location(block.getWorld(), block.getLocation().getBlockX(), block.getLocation().getY() + 1.0D, block.getLocation().getBlockZ());
				HGAPI.checkAndSave(p, arena, loc);
			}
		}
		if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)){
			if (HGAPI.getPlayerManager().getPlayers().containsKey(p.getName())) {
				HockeyPlayer hp = HGAPI.getPlayerManager().getHockeyPlayer(p.getName());
				if (p.getInventory().getItemInMainHand() != null) {
					if (!p.getInventory().getItemInMainHand().hasItemMeta()) {
						return;
					}
					if (!p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
						return;
					}
					if (!p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.hockey-stick")))) {
						return;
					}
					for (Entity entity : p.getNearbyEntities(1.6D, 1.6D, 1.6D)) {
						if (entity instanceof Item) {
							Item i = (Item)entity;
							if (i.getEntityId() == hp.getArena().getPuckEntity().getItem().getEntityId()) {
								double speedbonus = 0.3D;
								double beat = 0.0D;
								Vector vi = p.getEyeLocation().getDirection();
								hp.getArena().getPuckEntity().setLastPlayer(hp);
								if (hp.getType().getName().equals("Angreifer")) {
									beat = HGAPI.getPlugin().getConfig().getDouble("GameSettings.Schlagkraft.Angreifer");
									if (p.isSprinting()) {
										beat = speedbonus + beat;
									}
									if (p.isSneaking()) {
										vi.multiply(0.8D);
									}
								} else if (hp.getType().getName().equals("Verteidiger")) {
									beat = HGAPI.getPlugin().getConfig().getDouble("GameSettings.Schlagkraft.Verteidiger");
									if (p.isSprinting()) {
										beat = speedbonus + beat;
									}
									if (p.isSneaking()) {
										vi.multiply(0.8D);
									}
								} else if (hp.getType().getName().equals("Torwart")) {
									beat = HGAPI.getPlugin().getConfig().getDouble("GameSettings.Schlagkraft.Torwart");
									if (p.isSprinting()) {
										beat = speedbonus + beat;
									}
									if (p.isSneaking()) {
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
		} else if (e.getAction() != Action.RIGHT_CLICK_AIR) {
			e.getAction();
		}
	}
	
	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
 		String message = event.getMessage();
 		int i;
 		if (HGAPI.getPlugin().getArenaCreators().contains(player)) {
 			Arena arena = new Arena(message, player.getWorld());
  			if ((!HGAPI.getPlugin().getDevArenas().containsKey(player.getName())) && (!HGAPI.getArenaManager().getArenas().containsKey(message))) {
  				HGAPI.getPlugin().getDevArenas().put(player.getName(), arena);
  				event.setCancelled(true); 
 				HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.select-the-first-team")), true); 
  				int size = 36;
 				CustomGUIMenu menu = new CustomGUIMenu(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.select-the-first-team"))), size);
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
 				HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.arena-name-is-taken")), true);
  				event.setCancelled(true);
  			}
  		}
 		if (HGAPI.getPlugin().getTeamCreators().contains(player)) {
 			Team team = new Team(message);
  			if ((!HGAPI.getPlugin().getDevTeams().containsKey(player.getName())) && (!HGAPI.getTeamManager().getTeams().containsKey(message))) {
  				HGAPI.getPlugin().getDevTeams().put(player.getName(), team);
  				event.setCancelled(true);
 				HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.select-team-color")), true);  
  				int size = 36;
				CustomGUIMenu menu = new CustomGUIMenu(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.select-team-color"))), size);
  				for (Color color : HGAPI.getColors()) {
  					ItemStack item = new ItemStack(Material.LEATHER_HELMET);
  					LeatherArmorMeta meta = (LeatherArmorMeta)item.getItemMeta();
 					meta.setColor(color);
 					item.setItemMeta(meta);
 					menu.addItem(item, HGAPI.getColors().indexOf(color));
  				}
  				player.openInventory(menu.getInventory());
  			} else if ((!HGAPI.getPlugin().getDevTeams().containsKey(player.getName())) && (HGAPI.getTeamManager().getTeams().containsKey(message))) {
 				HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("team-name-is-taken")), true);
				HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.team-name-is-taken")), true);
  				event.setCancelled(true);
  			}
  		}	
		if (HGAPI.getPlayerManager().getHockeyPlayer(player.getName()).getTeam().getName().equals("Rot")) {
			event.setFormat(ChatColor.GOLD + "[" + ChatColor.DARK_RED + HGAPI.getPlayerManager().getHockeyPlayer(player.getName()).getTeam().getName() + ChatColor.GOLD + "] " + ChatColor.WHITE + "%1$s: %2$s");
		} else if (HGAPI.getPlayerManager().getHockeyPlayer(player.getName()).getTeam().getName().equals("Grün")) {
			event.setFormat(ChatColor.GOLD + "[" + ChatColor.DARK_GREEN + HGAPI.getPlayerManager().getHockeyPlayer(player.getName()).getTeam().getName() + ChatColor.GOLD + "] " + ChatColor.WHITE + "%1$s: %2$s");	
		}
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
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
	public void onEntityDamage(EntityDamageEvent event) {
		Entity target = event.getEntity();
		if ((target instanceof Player)) {
			Player player = (Player)target;
			String name = player.getName();
			if ((HGAPI.getPlayerManager().getPlayers().containsKey(name)) && (player.getHealth() < 4.0D)) {
				player.setHealth(4.0D);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
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
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (HGAPI.getPlayerManager().getPlayers().containsKey(player.getName())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
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
 	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
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
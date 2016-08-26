package de.SebastianMikolai.PlanetFx.IceHockey.Listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.Arena;
import de.SebastianMikolai.PlanetFx.IceHockey.API.GUI.CustomGUIMenu;
import de.SebastianMikolai.PlanetFx.IceHockey.API.GUI.Menus;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.HockeyPlayer;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.Team;
import de.SebastianMikolai.PlanetFx.IceHockey.Commands.Icons;

public class GUIListener implements Listener {
	
	@EventHandler
	public void onStandartClick(InventoryClickEvent event) {
		Player player = (Player)event.getWhoClicked();
		ItemStack clicked = event.getCurrentItem();
		Inventory inventory = event.getInventory();
		String name = inventory.getTitle();
		if ((clicked != null) && (clicked.getType() != Material.AIR)) {
			if (ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.select-the-first-team"))).equals(name)) {
				event.setCancelled(true);
				for (Team teams : HGAPI.getTeamManager().getTeams().values()) {
					if (teams.getName().equals(clicked.getItemMeta().getDisplayName())) {
						Team team = HGAPI.getTeamManager().getTeam(clicked.getItemMeta().getDisplayName());
						Arena arena = (Arena)HGAPI.getPlugin().getDevArenas().get(player.getName());
						arena.setFirstTeam(team); 
						HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.select-the-second-team")), true); 
						player.closeInventory();
						int size = 36;
						CustomGUIMenu menu = new CustomGUIMenu(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.select-the-second-team"))), size);
						List<String> keys = new ArrayList<String>(HGAPI.getTeamManager().getTeams().keySet());  
						keys.remove(arena.getFirstTeam().getName());
						for (int i = 0; i < keys.size(); i++) {
							String obj = (String)keys.get(i);
							ItemStack item = new ItemStack(Material.LEATHER_HELMET);
							LeatherArmorMeta meta = (LeatherArmorMeta)item.getItemMeta();
							meta.setDisplayName(HGAPI.getTeamManager().getTeam(obj).getName());
							meta.setColor(HGAPI.getTeamManager().getTeam(obj).getColor());
							item.setItemMeta(meta);
							menu.addItem(item, keys.indexOf(obj));
						}
						player.openInventory(menu.getInventory());
					}
				}
			} else if (ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.select-the-second-team"))).equals(name)) {
				event.setCancelled(true);
				for (Team teams : HGAPI.getTeamManager().getTeams().values()) {
					if (teams.getName().equals(clicked.getItemMeta().getDisplayName())) {
						Team team = HGAPI.getTeamManager().getTeam(clicked.getItemMeta().getDisplayName());
						Arena arena = (Arena)HGAPI.getPlugin().getDevArenas().get(player.getName());
						arena.setSecondTeam(team); 
						HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.first-team-set-lobby")), true);
						player.closeInventory();
					}
				}
			} else if ((ChatColor.DARK_AQUA + "[PlanetFxIceHockey]").equals(name)) {
				event.setCancelled(true);
				if (Icons.getArenas().getItemMeta().getDisplayName().equals(clicked.getItemMeta().getDisplayName())) {
					player.closeInventory();  
					Menus.openArenasMenu(player);
				} else if (Icons.getReload().getItemMeta().getDisplayName().equals(clicked.getItemMeta().getDisplayName())) {
					player.closeInventory();
					HGAPI.getPlugin().reloadPlugin();
					HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.plugin-restarted")), true);
				} else if (Icons.getArenaLeave(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.icon-arena-leave"))).getItemMeta().getDisplayName().equals(clicked.getItemMeta().getDisplayName())) {
					player.closeInventory();
					HockeyPlayer hplayer = HGAPI.getPlayerManager().getHockeyPlayer(player.getName());
					hplayer.getArena().leavePlayer(hplayer);
				} else if (Icons.getArenaManager().getItemMeta().getDisplayName().equals(clicked.getItemMeta().getDisplayName())) {
					player.closeInventory();   
					Menus.openArenaManagerMenu(player);
				} else if (Icons.getTeamManager().getItemMeta().getDisplayName().equals(clicked.getItemMeta().getDisplayName())) {
					player.closeInventory();  
					Menus.openTeamManagerMenu(player);
				} else if (Icons.getNextStage().getItemMeta().getDisplayName().equals(clicked.getItemMeta().getDisplayName())) {
					player.closeInventory();
					Arena arena = (Arena)HGAPI.getPlugin().getDevArenas().get(player.getName());
					if (!arena.isFirstGatesFulled()) {
						if (arena.getFirstTeamGates().isEmpty()) {
							HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.first-team-empty-gates")), true);
							return;
						}
						arena.setFirstGatesFulled(true);
						HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.set-second-gates")) + ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.icon-next-stage")), true);
					} else if (arena.isFirstGatesFulled()) {
						if (arena.getSecondTeamGates().isEmpty()) {
							HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.second-team-empty-gates")), true);
							return;
						}
						arena.setSecondGatesFulled(true);
						HGAPI.checkAndSave(player, arena, null);
					}
				} else if (Icons.getCancel().getItemMeta().getDisplayName().equals(clicked.getItemMeta().getDisplayName())) {
					player.closeInventory();
					if ((HGAPI.getPlugin().getArenaCreators().contains(player)) && (HGAPI.getPlugin().getDevArenas().containsKey(player.getName()))) {
						HGAPI.getPlugin().getDevArenas().remove(player.getName());
						HGAPI.getPlugin().getArenaCreators().remove(player);
						HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.create-arena-cancelled")), true);
					} else if ((HGAPI.getPlugin().getTeamCreators().contains(player)) && (HGAPI.getPlugin().getDevTeams().containsKey(player.getName()))) {
						HGAPI.getPlugin().getDevTeams().remove(player.getName());
						HGAPI.getPlugin().getTeamCreators().remove(player);
						HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.create-team-cancelled")), true);
					}
				}
			} else if (Icons.getArenas().getItemMeta().getDisplayName().equals(name)) {
				event.setCancelled(true);
				if (HGAPI.getArenaManager().getArenas().keySet().contains(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))) {
					Arena arena = HGAPI.getArenaManager().getArena(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
					if (arena.isRunning()) {
						player.closeInventory();
						HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.game-running")), true);
					} else if (arena.getPlayers().size() == arena.getMaxPlayers()) {
						player.closeInventory();
						HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.arena-full")), true);
					} else {
						Menus.openTeamArenaMenu(arena, player, clicked.getItemMeta().getDisplayName());
					}
				}
			} else if (HGAPI.getArenaManager().getArenas().containsKey(ChatColor.stripColor(name))) {
				event.setCancelled(true);
				if (HGAPI.getTeamManager().getTeams().containsKey(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))) {
					if (HGAPI.getPlayerManager().getHockeyPlayer(player.getName()) != null) {
						return;
					}
					Arena arena = HGAPI.getArenaManager().getArena(ChatColor.stripColor(name));
					Team team = arena.getTeam(ChatColor.stripColor(clicked.getItemMeta().getDisplayName())); 
					HockeyPlayer hplayer = new HockeyPlayer(player);		
					player.closeInventory();
					arena.joinPlayer(hplayer, team);
					HGAPI.playSound(player, player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
				}
			} else if (ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.select-team-color"))).equals(name)) {
				event.setCancelled(true);
				if (clicked.getType() == Material.LEATHER_HELMET) {
					ItemStack item = clicked;
					LeatherArmorMeta meta = (LeatherArmorMeta)item.getItemMeta(); 
					Team team = (Team)HGAPI.getPlugin().getDevTeams().get(player.getName());
					team.setColor(meta.getColor()); 
					HGAPI.getTeamManager().save(team);
					HGAPI.getTeamManager().addTeam(team);  
					HGAPI.getPlugin().getDevTeams().remove(player.getName());
					HGAPI.getPlugin().getTeamCreators().remove(player);    
					player.closeInventory();   
					HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.team-saved")), true);
				}
			} else if (Icons.getArenaManager().getItemMeta().getDisplayName().equals(name)) {
				event.setCancelled(true);
				if (Icons.getCreateArena().getItemMeta().getDisplayName().equals(clicked.getItemMeta().getDisplayName())) {
					if (!HGAPI.getPlugin().getArenaCreators().contains(player)) {
						HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.start-create-arena")) + ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.icon-cancel")), true);
						HGAPI.getPlugin().getArenaCreators().add(player); 
						player.closeInventory();
					}
				} else if (Icons.getDeleteArena().getItemMeta().getDisplayName().equals(clicked.getItemMeta().getDisplayName())) {
					player.closeInventory();
					Menus.openDeleterArenasMenu(player);
				} else if (Icons.getStopArena().getItemMeta().getDisplayName().equals(clicked.getItemMeta().getDisplayName())) {
					player.closeInventory();  
					Menus.openStoperArenaMenu(player);
				}
			} else if (Icons.getTeamManager().getItemMeta().getDisplayName().equals(name)) {
				event.setCancelled(true);
				if (Icons.getCreateTeam().getItemMeta().getDisplayName().equals(clicked.getItemMeta().getDisplayName())) {
					if (!HGAPI.getPlugin().getTeamCreators().contains(player)) {
						HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.start-create-team")), true);
						HGAPI.getPlugin().getTeamCreators().add(player);
						player.closeInventory();
					}
				} else if (Icons.getDeleteTeam().getItemMeta().getDisplayName().equals(clicked.getItemMeta().getDisplayName())) {
					player.closeInventory();  
					Menus.openDeleterTeamMenu(player);
				}
			} else if (Icons.getDeleteArena().getItemMeta().getDisplayName().equals(name)) {
				event.setCancelled(true);
				if (HGAPI.getArenaManager().getArenas().keySet().contains(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))) {
					Arena arena = HGAPI.getArenaManager().getArena(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
					HGAPI.getArenaManager().deleteArena(arena);
					HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.arena-deleted")), true); 
					Menus.openDeleterArenasMenu(player);
				}
			} else if (Icons.getDeleteTeam().getItemMeta().getDisplayName().equals(name)) {
				event.setCancelled(true);
				if (HGAPI.getTeamManager().getTeams().keySet().contains(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))) {
					Team team = HGAPI.getTeamManager().getTeam(ChatColor.stripColor(clicked.getItemMeta().getDisplayName())); 
					HGAPI.getTeamManager().deleteTeam(team); 
					HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.team-deleted")), true); 
					Menus.openDeleterTeamMenu(player);
				}
			} else if (Icons.getStopArena().getItemMeta().getDisplayName().equals(name)) {
				event.setCancelled(true);
				if (HGAPI.getArenaManager().getArenas().keySet().contains(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))) {
					Arena arena = HGAPI.getArenaManager().getArena(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));  
					player.closeInventory();
					if (arena.isRunning()) {
						arena.startRewards();
						arena.stopArena();
					} else if (!arena.getPlayers().isEmpty()) {
						arena.stopArena();
					}
				}
			}
		}
	}
}
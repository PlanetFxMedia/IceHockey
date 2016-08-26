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
import de.SebastianMikolai.PlanetFx.IceHockey.API.Addons.Addon;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.Arena;
import de.SebastianMikolai.PlanetFx.IceHockey.API.GUI.CustomGUIMenu;
import de.SebastianMikolai.PlanetFx.IceHockey.API.GUI.Menus;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.HockeyPlayer;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.Team;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Utils.Lang;
import de.SebastianMikolai.PlanetFx.IceHockey.Commands.Icons;

public class GUIListener implements Listener {
	
	@EventHandler
	public void onStandartClick(InventoryClickEvent event) {
		Player player = (Player)event.getWhoClicked();
		ItemStack clicked = event.getCurrentItem();
		Inventory inventory = event.getInventory();
		String name = inventory.getTitle();
		if ((clicked != null) && (clicked.getType() != Material.AIR)) {
			if (ChatColor.stripColor(Lang.SELECT_THE_FIRST_TEAM.toString()).equals(name)) {
				event.setCancelled(true);
				for (Team teams : HGAPI.getTeamManager().getTeams().values()) {
					if (teams.getName().equals(clicked.getItemMeta().getDisplayName())) {
						Team team = HGAPI.getTeamManager().getTeam(clicked.getItemMeta().getDisplayName());
						Arena arena = (Arena)HGAPI.getPlugin().getDevArenas().get(player.getName());
						arena.setFirstTeam(team); 
						HGAPI.sendMessage(player, Lang.SELECT_THE_SECOND_TEAM.toString(), true); 
						player.closeInventory();
						int size = 36;
						CustomGUIMenu menu = new CustomGUIMenu(ChatColor.stripColor(Lang.SELECT_THE_SECOND_TEAM.toString()), size);
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
			} else if (ChatColor.stripColor(Lang.SELECT_THE_SECOND_TEAM.toString()).equals(name)) {
				event.setCancelled(true);
				for (Team teams : HGAPI.getTeamManager().getTeams().values()) {
					if (teams.getName().equals(clicked.getItemMeta().getDisplayName())) {
						Team team = HGAPI.getTeamManager().getTeam(clicked.getItemMeta().getDisplayName());
						Arena arena = (Arena)HGAPI.getPlugin().getDevArenas().get(player.getName());
						arena.setSecondTeam(team); 
						HGAPI.sendMessage(player, Lang.FIRST_TEAM_SET_LOBBY.toString(), true);
						player.closeInventory();
					}
				}
			} else if ((ChatColor.DARK_AQUA + "[PlanetFxIceHockey]").equals(name)) {
				event.setCancelled(true);
				if (Icons.getChangeLang().getItemMeta().getDisplayName().equals(clicked.getItemMeta().getDisplayName())) {
					player.closeInventory();  
					Menus.openChangeLangMenu(player);
				} else if (Icons.getArenas().getItemMeta().getDisplayName().equals(clicked.getItemMeta().getDisplayName())) {
					player.closeInventory();  
					Menus.openArenasMenu(player);
				} else if (Icons.getReload().getItemMeta().getDisplayName().equals(clicked.getItemMeta().getDisplayName())) {
					player.closeInventory();
					HGAPI.getPlugin().reloadPlugin();
					HGAPI.sendMessage(player, Lang.PLUGIN_RESTARTED.toString(), true);
				} else if (Icons.getArenaLeave(Lang.ICON_ARENA_LEAVE.toString()).getItemMeta().getDisplayName().equals(clicked.getItemMeta().getDisplayName())) {
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
							HGAPI.sendMessage(player, Lang.FIRST_TEAM_EMPTY_GATES.toString(), true);
							return;
						}
						arena.setFirstGatesFulled(true);
						HGAPI.sendMessage(player, Lang.SET_SECOND_GATES.toString() + Lang.ICON_NEXT_STAGE.toString(), true);
					} else if (arena.isFirstGatesFulled()) {
						if (arena.getSecondTeamGates().isEmpty()) {
							HGAPI.sendMessage(player, Lang.SECOND_TEAM_EMPTY_GATES.toString(), true);
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
						HGAPI.sendMessage(player, Lang.CREATE_ARENA_CANCELLED.toString(), true);
					} else if ((HGAPI.getPlugin().getTeamCreators().contains(player)) && (HGAPI.getPlugin().getDevTeams().containsKey(player.getName()))) {
						HGAPI.getPlugin().getDevTeams().remove(player.getName());
						HGAPI.getPlugin().getTeamCreators().remove(player);
						HGAPI.sendMessage(player, Lang.CREATE_TEAM_CANCELLED.toString(), true);
					}
				} else if (Icons.getAddons().getItemMeta().getDisplayName().equals(clicked.getItemMeta().getDisplayName())) {
					player.closeInventory();
					Menus.openAddonsMenu(player);
				}
			} else if (Icons.getChangeLang().getItemMeta().getDisplayName().equals(name)) {
				event.setCancelled(true);
				if (Icons.getLangList().contains(clicked.getItemMeta().getDisplayName())) {
					player.closeInventory();  
					HGAPI.getPlugin().getConfig().set("Lang", ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
					HGAPI.getPlugin().getConfig().options().copyDefaults(true);
					HGAPI.getPlugin().saveConfig();
					HGAPI.getPlugin().getConfig().options().copyDefaults(false);
					HGAPI.getPlugin().reloadLang();
					HGAPI.sendMessage(player, Lang.PLUGIN_RESTARTED.toString(), true);
				}
			} else if (Icons.getArenas().getItemMeta().getDisplayName().equals(name)) {
				event.setCancelled(true);
				if (HGAPI.getArenaManager().getArenas().keySet().contains(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))) {
					Arena arena = HGAPI.getArenaManager().getArena(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
					if (arena.isRunning()) {
						player.closeInventory();
						HGAPI.sendMessage(player, Lang.GAME_RUNNING.toString(), true);
					} else if (arena.getPlayers().size() == arena.getMaxPlayers()) {
						player.closeInventory();
						HGAPI.sendMessage(player, Lang.ARENA_FULL.toString(), true);
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
			} else if (ChatColor.stripColor(Lang.SELECT_TEAM_COLOR.toString()).equals(name)) {
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
					HGAPI.sendMessage(player, Lang.TEAM_SAVED.toString(), true);
				}
			} else if (Icons.getArenaManager().getItemMeta().getDisplayName().equals(name)) {
				event.setCancelled(true);
				if (Icons.getCreateArena().getItemMeta().getDisplayName().equals(clicked.getItemMeta().getDisplayName())) {
					if (!HGAPI.getPlugin().getArenaCreators().contains(player)) {
						HGAPI.sendMessage(player, Lang.START_CREATE_ARENA.toString() + Lang.ICON_CANCEL.toString(), true);
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
						HGAPI.sendMessage(player, Lang.START_CREATE_TEAM.toString(), true);
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
					HGAPI.sendMessage(player, Lang.ARENA_DELETED.toString(), true); 
					Menus.openDeleterArenasMenu(player);
				}
			} else if (Icons.getDeleteTeam().getItemMeta().getDisplayName().equals(name)) {
				event.setCancelled(true);
				if (HGAPI.getTeamManager().getTeams().keySet().contains(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))) {
					Team team = HGAPI.getTeamManager().getTeam(ChatColor.stripColor(clicked.getItemMeta().getDisplayName())); 
					HGAPI.getTeamManager().deleteTeam(team); 
					HGAPI.sendMessage(player, Lang.TEAM_DELETED.toString(), true); 
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
			} else if (Icons.getAddons().getItemMeta().getDisplayName().equals(name)) {
				event.setCancelled(true);
				if (HGAPI.getAddonManager().getAddon(ChatColor.stripColor(clicked.getItemMeta().getDisplayName())) != null) {
					Addon addon = HGAPI.getAddonManager().getAddon(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
					if (addon.isEnabled()) {
						HGAPI.getAddonManager().disableAddon(addon);
						HGAPI.sendMessage(player, Lang.ADDON_DISABLED.toString(), true);
					} else if (!addon.isEnabled()) {
						HGAPI.getAddonManager().enableAddon(addon);
						HGAPI.sendMessage(player, Lang.ADDON_ENABLED.toString(), true);
					}
					Menus.openAddonsMenu(player);
				}
			}
		}
	}
}
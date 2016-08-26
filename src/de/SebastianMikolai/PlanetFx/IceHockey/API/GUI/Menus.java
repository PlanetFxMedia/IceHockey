package de.SebastianMikolai.PlanetFx.IceHockey.API.GUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Addons.Addon;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.Arena;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Utils.Lang;
import de.SebastianMikolai.PlanetFx.IceHockey.Commands.Icons;

public class Menus {
	
	public static void openMainMenu(Player player) {
		CustomGUIMenu menu = new CustomGUIMenu(ChatColor.DARK_AQUA + "[PlanetFxIceHockey]", 9);
		menu.addItem(Icons.getArenas(), 0);
		if (HGAPI.getPlayerManager().getHockeyPlayer(player.getName()) != null) {
			menu.addItem(Icons.getArenaLeave(Lang.ICON_ARENA_LEAVE.toString()), 1);
		}
		if ((player.hasPermission("hg.admin")) || (player.isOp())) {
			menu.addItem(Icons.getReload(), 8);
			menu.addItem(Icons.getChangeLang(), 2); 
			menu.addItem(Icons.getArenaManager(), 3);
			menu.addItem(Icons.getTeamManager(), 4);
			menu.addItem(Icons.getAddons(), 5);
			if (HGAPI.getPlugin().getArenaCreators().contains(player)) {
				menu.addItem(Icons.getCancel(), 7);
				if (HGAPI.getPlugin().getDevArenas().containsKey(player.getName())) {
					Arena arena = (Arena)HGAPI.getPlugin().getDevArenas().get(player.getName());
					if (arena.getPuckLocation() != null) {
						menu.addItem(Icons.getNextStage(), 6);
					}
				}
			} else if (HGAPI.getPlugin().getTeamCreators().contains(player)) {
				menu.addItem(Icons.getCancel(), 7);
			}
		}
		player.openInventory(menu.getInventory());
	}
	
	public static void openChangeLangMenu(Player player) {
		CustomGUIMenu menu = new CustomGUIMenu(Icons.getChangeLang().getItemMeta().getDisplayName(), 45);
		for (String lang : Icons.getLangList()) {
			menu.addItem(Icons.getLang(lang), Icons.getLangList().indexOf(lang));
		}
		player.openInventory(menu.getInventory());
	}
	
	public static void openArenasMenu(Player player) {
		CustomGUIMenu menu = new CustomGUIMenu(Icons.getArenas().getItemMeta().getDisplayName(), 45); 
		List<String> musor = new ArrayList<String>();
		for (String arenas : HGAPI.getArenaManager().getArenas().keySet()) {
			musor.add(arenas);
		}
		for (String arena : musor) {
			menu.addItem(Icons.getArena(arena), musor.indexOf(arena));
		}
		player.openInventory(menu.getInventory());
	}
	
	public static void openArenaManagerMenu(Player player) {
		CustomGUIMenu menu = new CustomGUIMenu(Icons.getArenaManager().getItemMeta().getDisplayName(), 9); 
		menu.addItem(Icons.getCreateArena(), 0);
		menu.addItem(Icons.getDeleteArena(), 1);
		menu.addItem(Icons.getStopArena(), 2);  
		player.openInventory(menu.getInventory());
	}
	
	public static void openTeamManagerMenu(Player player) {
		CustomGUIMenu menu = new CustomGUIMenu(Icons.getTeamManager().getItemMeta().getDisplayName(), 9);  
		menu.addItem(Icons.getCreateTeam(), 0);
		menu.addItem(Icons.getDeleteTeam(), 1);  
		player.openInventory(menu.getInventory());
	}
	
	public static void openTeamArenaMenu(Arena arena, Player player, String arenaname) {
		CustomGUIMenu menu = new CustomGUIMenu(arenaname, 9); 
		menu.addItem(Icons.getTeam(arena, arena.getFirstTeam().getName()), 0);
		menu.addItem(Icons.getTeam(arena, arena.getSecondTeam().getName()), 1); 
		player.openInventory(menu.getInventory());
	}
	
	public static void openDeleterArenasMenu(Player player) {
		CustomGUIMenu menu = new CustomGUIMenu(Lang.ICON_DELETE_ARENA.toString(), 45);  
		List<String> musor = new ArrayList<String>();
		for (String arenas : HGAPI.getArenaManager().getArenas().keySet()) {
			musor.add(arenas);
		}
		for (String arena : musor) {
			menu.addItem(Icons.getArena(arena), musor.indexOf(arena));
		}
		player.openInventory(menu.getInventory());
	}
	
	public static void openStoperArenaMenu(Player player) {
		CustomGUIMenu menu = new CustomGUIMenu(Lang.ICON_STOP_ARENA.toString(), 45); 
		List<String> musor = new ArrayList<String>();
		for (String arenas : HGAPI.getArenaManager().getArenas().keySet()) {
			musor.add(arenas);
		}
		for (String arena : musor) {
			menu.addItem(Icons.getArena(arena), musor.indexOf(arena));
		}
		player.openInventory(menu.getInventory());
	}
	
	public static void openDeleterTeamMenu(Player player) {
		CustomGUIMenu menu = new CustomGUIMenu(Lang.ICON_DELETE_TEAM.toString(), 45);  
		List<String> musor = new ArrayList<String>();
		for (String arenas : HGAPI.getTeamManager().getTeams().keySet()) {
			musor.add(arenas);
		}
		for (String arena : musor) {
			menu.addItem(Icons.getTeam(arena), musor.indexOf(arena));
		}
		player.openInventory(menu.getInventory());
	}
	
	public static void openAddonsMenu(Player player) {
		CustomGUIMenu menu = new CustomGUIMenu(Lang.ICON_ADDONS.toString(), 45);
		for (Addon addon : HGAPI.getAddonManager().getAddons()) {
			menu.addItem(Icons.getAddon(addon.getName()), HGAPI.getAddonManager().getAddons().indexOf(addon));
		}
		player.openInventory(menu.getInventory());
	}
}
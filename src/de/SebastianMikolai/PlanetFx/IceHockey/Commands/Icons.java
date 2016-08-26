package de.SebastianMikolai.PlanetFx.IceHockey.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Arena.Arena;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.HockeyPlayer;

public class Icons {
	
	public static ItemStack getArenas() {
		ItemStack item = new ItemStack(Material.SLIME_BALL);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(0, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.available-arenas")));
		for (String arena : HGAPI.getArenaManager().getArenas().keySet()) {
			List<String> arenas = new ArrayList<String>();
			arenas.add(arena);
			lore.add(arenas.indexOf(arena) + 1, ChatColor.GOLD + arena);
		}
		meta.setLore(lore);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.icon-join-click")));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getArena(String name) {
		ItemStack item = new ItemStack(Material.SLIME_BALL);
		ItemMeta meta = item.getItemMeta();
		List<String> list = new ArrayList<String>();
		list.add(0, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.icon-join-click")));
		list.add(1, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.teams"))); 
		Arena arena = HGAPI.getArenaManager().getArena(name);
		list.add(2, ChatColor.WHITE + arena.getFirstTeam().getName());
		list.add(3, ChatColor.WHITE + arena.getSecondTeam().getName());
		list.add(4, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.players")));
		if (arena.getPlayers().isEmpty()) {
			list.add(5, ChatColor.YELLOW + "-");
		} else {
			for (HockeyPlayer players : arena.getPlayers()) {
				list.add(arena.getPlayers().indexOf(players) + 5, ChatColor.YELLOW + players.getName());
			}
		}
		meta.setLore(list);
		meta.setDisplayName(ChatColor.YELLOW + name);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getTeam(Arena arena, String name) {
		ItemStack item = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta meta = (LeatherArmorMeta)item.getItemMeta();
		List<String> list = new ArrayList<String>();
		list.add(0, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.players")));
		if (arena.getTeam(name).getMembers().isEmpty()) {
			list.add(1, ChatColor.YELLOW + "-");
		} else {
			for (HockeyPlayer players : arena.getTeam(name).getMembers()) {
				list.add(arena.getPlayers().indexOf(players) + 1, ChatColor.YELLOW + players.getName());
			}
		}
		meta.setColor(arena.getTeam(name).getColor());
		meta.setLore(list);
		meta.setDisplayName(ChatColor.YELLOW + name);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getArenaLeave(String name) {
		ItemStack item = new ItemStack(Material.GRASS);
		ItemMeta meta = item.getItemMeta();
		List<String> list = new ArrayList<String>();
		list.add(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.icon-leave-click")));  
		meta.setLore(list);
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getReload() {
		ItemStack item = new ItemStack(Material.SLIME_BALL);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.icon-reload")));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getArenaManager() {
		ItemStack item = new ItemStack(Material.WORKBENCH);
		ItemMeta meta = item.getItemMeta(); 
		List<String> lore = new ArrayList<String>();
		lore.add(0, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.available-arenas")));
		for (String arena : HGAPI.getArenaManager().getArenas().keySet()) {
			List<String> arenas = new ArrayList<String>();  
			arenas.add(arena);
			lore.add(arenas.indexOf(arena) + 1, ChatColor.GOLD + arena);
		}
		meta.setLore(lore);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.icon-arenas")));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getTeamManager() {
		ItemStack item = new ItemStack(Material.BED);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(0, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.teams")));
		for (String arena : HGAPI.getTeamManager().getTeams().keySet()) {
			List<String> arenas = new ArrayList<String>(); 
			arenas.add(arena);
			lore.add(arenas.indexOf(arena) + 1, ChatColor.GOLD + arena);
		}
		meta.setLore(lore);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.team-manager")));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getCreateArena() {
		ItemStack item = new ItemStack(Material.WATER_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.icon-create-arena")));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getDeleteArena() {
		ItemStack item = new ItemStack(Material.LAVA_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.icon-delete-arena")));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getStopArena() {
		ItemStack item = new ItemStack(Material.WEB);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.icon-stop-arena")));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getCreateTeam() {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.icon-create-team")));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getDeleteTeam() {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.icon-delete-team")));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getTeam(String name) {
		ItemStack item = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta meta = (LeatherArmorMeta)item.getItemMeta(); 
		meta.setColor(HGAPI.getTeamManager().getTeam(name).getColor());
		meta.setDisplayName(ChatColor.YELLOW + name);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getNextStage() {
		ItemStack item = new ItemStack(Material.DIAMOND_BLOCK);
		ItemMeta meta = item.getItemMeta(); 
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.icon-next-stage")));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getCancel() {
		ItemStack item = new ItemStack(Material.BLAZE_POWDER);
		ItemMeta meta = item.getItemMeta(); 
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.icon-cancel")));
		item.setItemMeta(meta);
		return item;
	}
}
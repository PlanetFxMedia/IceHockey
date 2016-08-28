package de.SebastianMikolai.PlanetFx.IceHockey.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;
import de.SebastianMikolai.PlanetFx.IceHockey.API.GUI.Menus;

public class HockeyCommands implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player)sender;
			if (p.isOp()) {
				if (cmd.getName().equalsIgnoreCase("hockey")) {
					Menus.openMainMenu(p);
				}
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.no-permission")));
			}
		}
		return true;
	}
}
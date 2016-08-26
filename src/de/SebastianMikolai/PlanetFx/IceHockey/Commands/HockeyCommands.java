package de.SebastianMikolai.PlanetFx.IceHockey.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.SebastianMikolai.PlanetFx.IceHockey.API.GUI.Menus;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Utils.ChatUtils;

public class HockeyCommands implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player)sender;
			if (p.hasPermission("hockey.playing")) {
				if (cmd.getName().equalsIgnoreCase("hockey")) {
					Menus.openMainMenu(p);
				}
			} else {
				ChatUtils.sendMessageConfig(p, "no-permission");
			}
		}
		return true;
	}
}
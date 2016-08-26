package de.SebastianMikolai.PlanetFx.IceHockey.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;
import de.SebastianMikolai.PlanetFx.IceHockey.API.GUI.Menus;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Utils.Lang;

public class HockeyCommands implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
		if ((sender instanceof Player)) {
			Player player = (Player)sender;
			if (player.hasPermission("hg.playing")) {
				if (cmd.getName().equalsIgnoreCase("hockey")) {
					Menus.openMainMenu(player);
				}
			} else {
				HGAPI.sendMessage(player, Lang.NO_PERMISSION.toString(), true);
			}
		}
		return true;
	}
}
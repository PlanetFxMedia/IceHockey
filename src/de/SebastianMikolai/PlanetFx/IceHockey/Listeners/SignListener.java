package de.SebastianMikolai.PlanetFx.IceHockey.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Signs.SignType;

public class SignListener implements Listener {
	
	@EventHandler
	public void onSignCreate(SignChangeEvent e) {
		Player player = e.getPlayer();
		if (e.getLine(0).equalsIgnoreCase("[IceHockey]")) {
			if (!player.isOp()) {
				HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.no-permission")), false);
				e.setCancelled(true);
				e.getBlock().breakNaturally();
				return;
			}
			String line = e.getLine(1);
			if (HGAPI.getSignManager().getSigns().containsKey(line)) {
				((SignType)HGAPI.getSignManager().getSigns().get(line)).handleCreateSign(e);
			}
		}
	}
	
	@EventHandler
	public void onSignClick(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		if (!(e.getClickedBlock().getState() instanceof Sign)) {
			return;
		}
		Sign sign = (Sign)e.getClickedBlock().getState();
		if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_RED + "[IceHockey]")) {
			String line = sign.getLine(1);
			if (HGAPI.getSignManager().getSigns().containsKey(line)) {
				((SignType)HGAPI.getSignManager().getSigns().get(line)).handleClickSign(e);
			}
		}
	}
	
	@EventHandler
	public void onSignDestroy(BlockBreakEvent e) {
		if (!(e.getBlock().getState() instanceof Sign)) {
			return;
		}
		Player player = e.getPlayer();
		Sign sign = (Sign)e.getBlock().getState();
		if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_RED + "[IceHockey]")) {
			if (!player.isOp()) {
				HGAPI.sendMessage(player, ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.no-permission")), false);
				e.setCancelled(true);
				return;
			}
			String line = sign.getLine(1);
			if (HGAPI.getSignManager().getSigns().containsKey(line)) {
				((SignType)HGAPI.getSignManager().getSigns().get(line)).handleDestroy(e);
			}
		}
	}
}
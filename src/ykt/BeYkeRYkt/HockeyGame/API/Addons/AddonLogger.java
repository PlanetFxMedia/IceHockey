package ykt.BeYkeRYkt.HockeyGame.API.Addons;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

import ykt.BeYkeRYkt.HockeyGame.API.HGAPI;

public class AddonLogger extends Logger {
	
	private String pluginName;
	
	public AddonLogger(Addon context) {
		super(context.getClass().getCanonicalName(), null);
		pluginName = ("[" + HGAPI.getPlugin().getName() + ": " + context.getName() + "] ");
		setParent(Bukkit.getServer().getLogger());
		setLevel(Level.ALL);
	}
	
	public void log(LogRecord logRecord) {
		logRecord.setMessage(pluginName + logRecord.getMessage());
		super.log(logRecord);
	}
}
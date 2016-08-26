package ykt.BeYkeRYkt.HockeyGame.API.Signs;

import java.util.HashMap;

import ykt.BeYkeRYkt.HockeyGame.API.Signs.Types.DefendSign;
import ykt.BeYkeRYkt.HockeyGame.API.Signs.Types.GoalKeeperSign;
import ykt.BeYkeRYkt.HockeyGame.API.Signs.Types.JoinSign;
import ykt.BeYkeRYkt.HockeyGame.API.Signs.Types.WingerSign;

public class SignManager {
	
	private HashMap<String, SignType> signs = new HashMap<String, SignType>();
	
	public SignManager() {
		addSign("[Join]", new JoinSign());
		addSign("Defender", new DefendSign());
		addSign("Winger", new WingerSign());
		addSign("Goalkeeper", new GoalKeeperSign());
	}
	
	public HashMap<String, SignType> getSigns() {
		return this.signs;
	}
	  
	public void addSign(String line, SignType type) {
		getSigns().put(line, type);
	}
	
	public void removeSign(String line) {
		getSigns().remove(line);
	}
	
	public SignType getSign(String name) {
		return (SignType)getSigns().get(name);
	}
}
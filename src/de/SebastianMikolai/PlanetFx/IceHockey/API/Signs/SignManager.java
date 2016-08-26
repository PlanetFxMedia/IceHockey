package de.SebastianMikolai.PlanetFx.IceHockey.API.Signs;

import java.util.HashMap;

import de.SebastianMikolai.PlanetFx.IceHockey.API.Signs.Types.AngreiferSign;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Signs.Types.TorwartSign;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Signs.Types.JoinSign;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Signs.Types.VerteidigerSign;

public class SignManager {
	
	private HashMap<String, SignType> signs = new HashMap<String, SignType>();
	
	public SignManager() {
		addSign("[Join]", new JoinSign());
		addSign("Verteidiger", new VerteidigerSign());
		addSign("Angreifer", new AngreiferSign());
		addSign("Torwart", new TorwartSign());
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
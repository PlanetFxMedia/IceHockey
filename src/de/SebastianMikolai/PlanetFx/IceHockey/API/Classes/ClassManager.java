package de.SebastianMikolai.PlanetFx.IceHockey.API.Classes;

import java.util.HashMap;

import de.SebastianMikolai.PlanetFx.IceHockey.API.Classes.Types.Defender;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Classes.Types.Goalkeeper;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Classes.Types.Winger;

public class ClassManager {
	
	private HashMap<String, ClassType> classes = new HashMap<String, ClassType>();
	
	public ClassManager() {
		init();
	}
	
	private void init() {
		addClass(new Defender());
		addClass(new Winger());
		addClass(new Goalkeeper());
	}
	
	public HashMap<String, ClassType> getClasses() {
		return this.classes;
	}
	
	public void addClass(ClassType type) {
		getClasses().put(type.getName(), type);
	}
	
	public void removeClass(String name) {
		getClasses().remove(name);
	}
	
	public ClassType getClass(String name) {
		return (ClassType)getClasses().get(name);
	}
}
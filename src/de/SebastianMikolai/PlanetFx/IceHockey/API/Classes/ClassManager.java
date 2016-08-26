package de.SebastianMikolai.PlanetFx.IceHockey.API.Classes;

import java.util.HashMap;

import de.SebastianMikolai.PlanetFx.IceHockey.API.Classes.Types.Angreifer;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Classes.Types.Torwart;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Classes.Types.Verteidiger;

public class ClassManager {
	
	private HashMap<String, ClassType> classes = new HashMap<String, ClassType>();
	
	public ClassManager() {
		init();
	}
	
	private void init() {
		addClass(new Verteidiger());
		addClass(new Angreifer());
		addClass(new Torwart());
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
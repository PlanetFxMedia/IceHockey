package ykt.BeYkeRYkt.HockeyGame.API.Addons;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import ykt.BeYkeRYkt.HockeyGame.API.HGAPI;

public class AddonLoader {
	
	private static ClassLoader loader;
	private static ClassLoader jarloader;
	
	public static ClassLoader getClassLoader() {
		return loader;
	}
	
	public static ClassLoader getJarClassLoader() {
		return jarloader;
	}
	
	public static List<Addon> load(String directory) {
		List<Addon> Addons = new ArrayList<Addon>();  
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
		try {
			loader = new URLClassLoader(new URL[] { dir.toURI().toURL() }, Addon.class.getClassLoader());
		} catch (MalformedURLException ex) {
			HGAPI.getPlugin().getLogger().warning("AddonLoader encountered an exception: ");
			ex.printStackTrace();
			return Addons;
		}
		File[] arrayOfFile;
		int j = (arrayOfFile = dir.listFiles()).length;
		for (int i = 0; i < j; i++) {
			File file = arrayOfFile[i]; 
			if (!file.getName().endsWith(".class")) {
				if (file.getName().endsWith(".jar")) {
					try {
						JarFile jarFile = new JarFile(file.getAbsolutePath());   
						Enumeration<JarEntry> e = jarFile.entries();   
						URL[] urls = { new URL("jar:file:" + file.getAbsolutePath() + "!/") };
						jarloader = null;
						jarloader = new URLClassLoader(urls, Addon.class.getClassLoader());
						while (e.hasMoreElements()) {
							JarEntry je = (JarEntry)e.nextElement();
							if ((!je.isDirectory()) && (je.getName().endsWith(".class"))) {
								if ((je != null) && (je.getName() != null)) {
									String className = je.getName().substring(0, je.getName().length() - 6);
									className = className.replace('/', '.');
									try {
										Class<?> aclass = jarloader.loadClass(className);
										Object object = aclass.newInstance();
										if ((object instanceof Addon)) {
											Addon a = (Addon)object;
											if ((a == null) || (a.getName() == null)) {
												HGAPI.getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.RED + je.getName() + " is invalid!");
											}
											Addons.add(a); 
											je = null;
										}
									} catch (Exception localException) {}catch (Error localError) {}
								}
							}
						}
						e = null;
						jarloader = null;
						jarFile.close();
						jarFile = null;
					} catch (IOException e) {
						System.err.println("Error: " + e.getMessage());
					}
				}
			} else {
				String name = file.getName().substring(0, 
						file.getName().lastIndexOf("."));
				try {
					Class<?> aclass = loader.loadClass(name);
					Object object = aclass.newInstance();
					if ((object instanceof Addon)) {
						Addon a = (Addon)object;  
						Addons.add(a);
					}
				} catch (Exception localException1) {}catch (Error localError1) {}
			}
		}
		if (Addons.size() == 0) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[PlanetFxIceHockey] Folder is empty.");
		}
		return Addons;
	}
}
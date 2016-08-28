package de.SebastianMikolai.PlanetFx.IceHockey.API.Arena;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.SebastianMikolai.PlanetFx.IceHockey.API.HGAPI;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Classes.ClassType;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Events.MatchStartEvent;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Events.MatchStopEvent;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Events.PlayerAutobalanceEvent;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Events.PlayerJoinArenaEvent;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Events.PlayerLeaveArenaEvent;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Events.RespawnPuckEvent;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Events.StartPlayMusicEvent;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.HockeyPlayer;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Team.Team;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Utils.GameState;
import de.SebastianMikolai.PlanetFx.IceHockey.API.Utils.ItemGiver;
import de.SebastianMikolai.PlanetFx.IceHockey.Runnables.ArenaRunnable;
import de.SebastianMikolai.PlanetFx.IceHockey.Runnables.CountToStartRunnable;
import de.SebastianMikolai.PlanetFx.IceHockey.Runnables.GameEnd;

public class Arena {
	
	private ArrayList<Location> red_gates = new ArrayList<Location>();
	private ArrayList<Location> blue_gates = new ArrayList<Location>();
	private String name;
	private ArrayList<HockeyPlayer> players = new ArrayList<HockeyPlayer>();
	private int max = HGAPI.getPlugin().getConfig().getInt("GameSettings.MaxPlayers");
	private Team team1;
	private Team team2;
	private Location puck_loc;
	private ItemStack puck;
	private boolean running = false;
  	private int red_score = 0;
  	private int blue_scores = 0;
  	private Location red_spawn;
  	private Location red_lobby;
  	private Location blue_lobby;
  	private Location blue_spawn;
  	private World world;
  	private CountToStartRunnable countrunnable;
  	private ArenaRunnable mainrun;
  	private Puck puckentity;
  	private boolean firstgatefull = false;
  	private boolean secondgatefull = false;
  	private Team loser;
  	private Team winner;
  	private GameState gamestate;
  	
  	public Arena(String arenaName) {
  		this.name = arenaName;
  		this.gamestate = GameState.Online;
  	}
  	
  	public Arena(String arenaName, World world) {
  		this.name = arenaName;
  		this.world = world;
  		this.gamestate = GameState.Online;
  	}

  	public GameState getGameState() {
  		return this.gamestate;
  	}
  	
  	public boolean isFirstGatesFulled() {
  		return this.firstgatefull;
  	}
  	
  	public boolean isSecondGatesFulled() {
  		return this.secondgatefull;
  	}
  	
  	public void setFirstGatesFulled(boolean flag) {
  		this.firstgatefull = flag;
  	}
  	
  	public void setSecondGatesFulled(boolean flag) {
  		this.secondgatefull = flag;
  	}
  	
  	public File getFile() {
  		File file = new File("plugins/PlanetFxIceHockey/arenas/", this.name + ".yml");
  		return file;
  	}
  	
  	public String getName() {
  		return this.name;
  	}
  	
  	public void setName(String arenaName) {
  		this.name = arenaName;
  	}
  	
  	public ArrayList<HockeyPlayer> getPlayers() {
  		return this.players;
  	}
  	
  	public int getMaxPlayers() {
  		return this.max;
  	}
  	
  	public void setMaxPlayers(int max) {
  		this.max = max;
  	}
  	
  	public void setPuckLocation(Location loc) {
  		this.puck_loc = loc;
  	}
  	
  	public Location getPuckLocation() {
  		return this.puck_loc;
  	}
  	
  	public void setPuck(ItemStack item) {
  		this.puck = item;
  	}
  	
  	public ItemStack getPuck() {
  		return this.puck;
  	}
  	
  	public boolean isRunning() {
  		return this.running;
  	}
  	
  	public void setRunning(boolean flag) {
  		this.running = flag;
  	}
  	
  	public World getWorld() {
  		return this.world;
  	}
  	
  	public void setWorld(String worldname) {
  		if (Bukkit.getWorld(worldname) != null) {
  			this.world = Bukkit.getWorld(worldname);
  		}
  	}
  	
  	public Location getFirstTeamLobbyLocation() {
  		return this.red_lobby;
  	}
  	
  	public Location getFirstTeamSpawnLocation() {
  		return this.red_spawn;
  	}
  	
  	public void setFirstTeamLobbyLocation(Location loc) {
  		this.red_lobby = loc;
  	}
  	
  	public void setFirstTeamSpawnLocation(Location loc) {
  		this.red_spawn = loc;
  	}
  	
  	public Location getSecondTeamLobbyLocation() {
  		return this.blue_lobby;
  	}
  	
  	public Location getSecondTeamSpawnLocation() {
  		return this.blue_spawn;
  	}
  	
  	public void setSecondTeamLobbyLocation(Location loc) {
  		this.blue_lobby = loc;
  	}
  	
  	public void setSecondTeamSpawnLocation(Location loc) {
  		this.blue_spawn = loc;
  	}
  	
  	public Team getFirstTeam() {
  		return this.team1;
  	}
  	
  	public Team getSecondTeam() {
  		return this.team2;
  	}
  	
  	public void setFirstTeam(Team team) {
  		this.team1 = team;
  	}
  	
  	public void setSecondTeam(Team team) {
  		this.team2 = team;
  	}
  	
  	public ArrayList<Location> getFirstTeamGates() {
  		return this.red_gates;
  	}
  	
  	public ArrayList<Location> getSecondTeamGates() {
  		return this.blue_gates;
  	}
  	
  	public void addFirstTeamGate(Location loc) {
  		getFirstTeamGates().add(loc);
  	}
  	
  	public void addSecondTeamGate(Location loc) {
  		getSecondTeamGates().add(loc);
  	}
  	
  	public void removeFirstTeamGate(Location loc) {
  		getFirstTeamGates().remove(loc);
  	}
  	
  	public void removeSecondTeamGate(Location loc) {
  		getSecondTeamGates().remove(loc);
  	}
  	
  	public int getFirstTeamScores() {
  		return this.red_score;
  	}
  	
  	public int getSecondTeamScores() {
  		return this.blue_scores;
  	}
  	
  	public void addFirstTeamScore(int scores) {
  		this.red_score = (getFirstTeamScores() + scores);
  	}
  	
  	public void addSecondTeamScore(int scores) {
  		this.blue_scores = (getSecondTeamScores() + scores);
  	}
  	
  	public void joinPlayer(HockeyPlayer player, Team team) {
	    PlayerJoinArenaEvent event = new PlayerJoinArenaEvent(player, this);
	    Bukkit.getPluginManager().callEvent(event);
	    if (!event.isCancelled()) {
	    	player.getBukkitPlayer().getInventory().clear();
	    	player.getBukkitPlayer().getInventory().setArmorContents(null);
	    	player.getBukkitPlayer().updateInventory();
	    	player.setArena(this);
	    	player.setTeam(team);
	      	player.setAllowTeleport(true);
	      	if (getFirstTeam().getName().equals(team.getName())) {
	      		player.getBukkitPlayer().teleport(getFirstTeamLobbyLocation()); 
	      		getFirstTeam().getMembers().add(player);
	      	} else if (getSecondTeam().getName().equals(team.getName())) {
	      		player.getBukkitPlayer().teleport(getSecondTeamLobbyLocation());  
	      		getSecondTeam().getMembers().add(player);
	      	}
	        player.getBukkitPlayer().setHealth(player.getBukkitPlayer().getMaxHealth());
	      	player.getBukkitPlayer().setFoodLevel(20);
	      	player.getBukkitPlayer().setGameMode(GameMode.SURVIVAL);
	      	getPlayers().add(player);
	      	HGAPI.getPlayerManager().addPlayer(player.getName(), player);
	    }
  	}
  	
  	public void leavePlayer(HockeyPlayer player) {
  		PlayerLeaveArenaEvent event = new PlayerLeaveArenaEvent(player, this);
  		Bukkit.getPluginManager().callEvent(event);
  		if (!event.isCancelled()) {
  			Team team = player.getTeam();
  			player.getBukkitPlayer().getInventory().clear();
  			player.getBukkitPlayer().getInventory().setArmorContents(null);
  			player.getBukkitPlayer().updateInventory();
  			if (player.getType() != null) {
  				if (team.getWingers().contains(player)) {
  					team.removeWinger(player);
  				} else if (team.getDefends().contains(player)) {
  					team.removeDefend(player);
  				} else if (team.getGoalKeeper().equals(player)) {
  					team.removeGoalkeeper();
  				}
  			}
  			getPlayers().remove(player);
  			team.getMembers().remove(player);
  			HGAPI.getPlayerManager().removePlayer(player.getName());
  		}
  	}
  	
  	private void setupPuck() {
  		String material_config = HGAPI.getPlugin().getConfig().getString("GameSettings.Puck.Material");
  		ItemStack item = new ItemStack(Material.getMaterial(material_config));
  		ItemMeta meta = item.getItemMeta();
  		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.puck-name")));
  		item.setItemMeta(meta);
  		setPuck(item);
  	}
  	
  	public void startArena() {
  		MatchStartEvent event = new MatchStartEvent(getPlayers(), this);
  		Bukkit.getPluginManager().callEvent(event);
  		getCountToStartRunnable().cancel();
    	this.countrunnable = null;
    	if (!event.isCancelled()) {
		  	this.gamestate = GameState.Running;
    		if (getPlayers().size() < HGAPI.getPlugin().getConfig().getInt("GameSettings.MinPlayers")) {
    			stopArena();
    		}
    		if (getPuck() == null) {
    			setupPuck();
    		}
    		Item item = getWorld().dropItemNaturally(getPuckLocation(), getPuck()); 
    		getFirstTeam().MassTeleportToLocation(getFirstTeamSpawnLocation());
    		getSecondTeam().MassTeleportToLocation(getSecondTeamSpawnLocation()); 
    		setRunning(true);  
    		Puck puck = new Puck(item);
    		for (HockeyPlayer players : getPlayers()) {
    			players.getBukkitPlayer().getInventory().addItem(new ItemStack[] { new ItemStack(Material.COOKED_BEEF, 10) });
    			players.setAllowTeleport(false);
    		}
    		setPuckEntity(puck);
    		startMainRunnable(puck);
    		if (HGAPI.getPlugin().getConfig().getBoolean("GameSettings.MusicMatch")) {
    			startPlayMusic();
    		}
    	}
  	}
  	
  	private void rejoinAutoBalance(HockeyPlayer player, Team team) {
  		PlayerAutobalanceEvent event = new PlayerAutobalanceEvent(player, player.getTeam(), team);
  		Bukkit.getPluginManager().callEvent(event);
  		if (!event.isCancelled()) {
  			ClassType type = player.getType();
  			player.getBukkitPlayer().getInventory().clear();
  			player.getBukkitPlayer().getInventory().setArmorContents(null);
  			player.getBukkitPlayer().updateInventory();
  			if (player.getTeam().getWingers().contains(player)) {
  				player.getTeam().removeWinger(player);
  			} else if (player.getTeam().getDefends().contains(player)) {
  				player.getTeam().removeDefend(player);
  			} else if (player.getTeam().getGoalKeeper().equals(player)) {
  				player.getTeam().removeGoalkeeper();
  			}
  			player.getTeam().getMembers().remove(player);
  			team.getMembers().add(player);
  			player.setTeam(team);
  			player.setAllowTeleport(true);
  			if (type.getName().equals("Angreifer")) {
  				player.getTeam().addWinger(player);
  			} else if (type.getName().equals("Verteidiger")) {
  				player.getTeam().addDefend(player);
  			} else if (type.getName().equals("Torwart")) {
  				if (player.getTeam().getGoalKeeper() == null) {
  					player.getTeam().setGoalkeeper(player);
  				} else {
  					player.getBukkitPlayer().getInventory().clear();
  					player.getBukkitPlayer().getInventory().setArmorContents(null);
  					player.getBukkitPlayer().updateInventory();
  					player.setType(HGAPI.getClassManager().getClass("Angreifer"));
  				}
  			}
  			ItemGiver.setItems(player, player.getTeam().getColor());
  			Location location = null;
  			if (team.getName().equals(getFirstTeam().getName())) {
  				location = getFirstTeamSpawnLocation().clone();
  			} else if (team.getName().equals(getSecondTeam().getName())) {
  				location = getSecondTeamSpawnLocation().clone();
  			}
  			location.setPitch(player.getBukkitPlayer().getLocation().getPitch());
  			location.setYaw(player.getBukkitPlayer().getLocation().getYaw());
  			player.getBukkitPlayer().teleport(location);
  			player.setAllowTeleport(false);
  		}
  	}
  	
  	public void autobalance() {
  		for (int i = 0; i < getPlayers().size(); i++) {
  			int team = -1;
  			if (getSecondTeam().getMembers().contains(getPlayers().get(i))) {
  				team = 1;
  			} else if (getFirstTeam().getMembers().contains(getPlayers().get(i))) {
  				team = 0;
  			}
  			if (team == 1) {
  				if (getFirstTeam().getMembers().size() < getSecondTeam().getMembers().size() - 1) {
  					HockeyPlayer player = (HockeyPlayer)getPlayers().get(i);
  					rejoinAutoBalance(player, getFirstTeam());
  					team = 0;
  				}
  			} else if (team == 0) {
  				if (getSecondTeam().getMembers().size() < getFirstTeam().getMembers().size() - 1) {
  					HockeyPlayer player = (HockeyPlayer)getPlayers().get(i);
  					rejoinAutoBalance(player, getSecondTeam());
  					team = 1;
  				}
  			} else if (getFirstTeam().getMembers().size() < getSecondTeam().getMembers().size() - 1) {
  				HockeyPlayer player = (HockeyPlayer)getPlayers().get(i);
  				rejoinAutoBalance(player, getFirstTeam());  
  				team = 0;
  			} else if (getSecondTeam().getMembers().size() < getFirstTeam().getMembers().size() - 1) {
  				HockeyPlayer player = (HockeyPlayer)getPlayers().get(i);
  				rejoinAutoBalance(player, getSecondTeam());  
  				team = 1;
  			}
  		}
  	}
  	
  	private void startPlayMusic() {
  		Random random = new Random();
  		int amount = random.nextInt(9);
  		if (amount == 0) {
  			StartPlayMusicEvent event = new StartPlayMusicEvent(this, getPuckLocation(), Material.RECORD_3);
  			Bukkit.getPluginManager().callEvent(event);
  			if (!event.isCancelled()) {
  				getWorld().playEffect(event.getLocation(), Effect.RECORD_PLAY, event.getRecord());
  			}
  		} else if (amount == 1) {
  			StartPlayMusicEvent event = new StartPlayMusicEvent(this, getPuckLocation(), Material.RECORD_4);
  			Bukkit.getPluginManager().callEvent(event);
  			if (!event.isCancelled()) {
  				getWorld().playEffect(event.getLocation(), Effect.RECORD_PLAY, event.getRecord());
  			}
  		} else if (amount == 2) {
  			StartPlayMusicEvent event = new StartPlayMusicEvent(this, getPuckLocation(), Material.RECORD_5);
  			Bukkit.getPluginManager().callEvent(event);
  			if (!event.isCancelled()) {
  				getWorld().playEffect(event.getLocation(), Effect.RECORD_PLAY, event.getRecord());
  			}
  		} else if (amount == 3) {
  			StartPlayMusicEvent event = new StartPlayMusicEvent(this, getPuckLocation(), Material.RECORD_6);
  			Bukkit.getPluginManager().callEvent(event);
  			if (!event.isCancelled()) {
  				getWorld().playEffect(event.getLocation(), Effect.RECORD_PLAY, event.getRecord());
  			}
  		} else if (amount == 4) {
  			StartPlayMusicEvent event = new StartPlayMusicEvent(this, getPuckLocation(), Material.RECORD_7);
  			Bukkit.getPluginManager().callEvent(event);
  			if (!event.isCancelled()) {
  				getWorld().playEffect(event.getLocation(), Effect.RECORD_PLAY, event.getRecord());
  			}
  		} else if (amount == 5) {
  			StartPlayMusicEvent event = new StartPlayMusicEvent(this, getPuckLocation(), Material.RECORD_8);
  			Bukkit.getPluginManager().callEvent(event);
  			if (!event.isCancelled()) {
  				getWorld().playEffect(event.getLocation(), Effect.RECORD_PLAY, event.getRecord());
  			}
  		} else if (amount == 6) {
  			StartPlayMusicEvent event = new StartPlayMusicEvent(this, getPuckLocation(), Material.RECORD_9);
  			Bukkit.getPluginManager().callEvent(event);
  			if (!event.isCancelled()) {
  				getWorld().playEffect(event.getLocation(), Effect.RECORD_PLAY, event.getRecord());
  			}
  		} else if (amount == 7) {
  			StartPlayMusicEvent event = new StartPlayMusicEvent(this, getPuckLocation(), Material.RECORD_10);
  			Bukkit.getPluginManager().callEvent(event);
  			if (!event.isCancelled()) {
  				getWorld().playEffect(event.getLocation(), Effect.RECORD_PLAY, event.getRecord());
  			}
  		} else if (amount == 8) {
  			StartPlayMusicEvent event = new StartPlayMusicEvent(this, getPuckLocation(), Material.RECORD_12);
  			Bukkit.getPluginManager().callEvent(event);
  			if (!event.isCancelled()) {
  				getWorld().playEffect(event.getLocation(), Effect.RECORD_PLAY, event.getRecord());
  			}
  		}
  	}
  	
  	public Team getTeam(String teamName) {
  		if (teamName.equals(getFirstTeam().getName())) {
  			return getFirstTeam();
  		}
  		if (teamName.equals(getSecondTeam().getName())) {
  			return getSecondTeam();
  		}
  		return null;
  	}
  	
  	public void broadcastMessage(String message) {
  		for (HockeyPlayer players : getPlayers()) {
  			HGAPI.sendMessage(players.getBukkitPlayer(), message, true);
  		}
  	}
  	
  	public CountToStartRunnable getCountToStartRunnable() {
  		return this.countrunnable;
  	}
  	
  	public void startCountToStartRunnable() {
  		if (getPlayers().size() < HGAPI.getPlugin().getConfig().getInt("GameSettings.MinPlayers")) {
  			return;
  		}
  		for (HockeyPlayer players : getPlayers()) {
  			if (!players.isReady()) {
  				return;
  			}
  		}
  		this.countrunnable = new CountToStartRunnable(this);
	  		this.gamestate = GameState.Waiting;
  		getCountToStartRunnable().runTaskTimer(HGAPI.getPlugin(), 0L, 20L);
  	}
  	
  	public Puck getPuckEntity() {
  		return this.puckentity;
  	}
  	
  	public void setPuckEntity(Puck puck) {
  		this.puckentity = puck;
  	}
  	
  	public ArenaRunnable getMainRunnable() {
	  return this.mainrun;
  	}
  	
  	public void startMainRunnable(Puck puck) {
  		int seconds = HGAPI.getPlugin().getConfig().getInt("GameSettings.MatchTimer");
  		this.mainrun = new ArenaRunnable(this, puck, seconds);
  		getMainRunnable().runTaskTimer(HGAPI.getPlugin(), 0L, 20L);
  	}
  	
  	public void respawnPuck() {
  		Item item = getWorld().dropItemNaturally(getPuckLocation(), getPuck());
  		Puck puck = new Puck(item);
  		RespawnPuckEvent event = new RespawnPuckEvent(this, puck, getPuckLocation());
  		Bukkit.getPluginManager().callEvent(event);
  		if (!event.isCancelled()) {
  			setPuckEntity(puck);
  			getMainRunnable().setPuck(puck);
  			HGAPI.playEffect(getWorld(), getPuckLocation(), Effect.ENDER_SIGNAL, 1);
  			HGAPI.playEffect(getWorld(), getPuckLocation(), Effect.MOBSPAWNER_FLAMES, 1);
  			HGAPI.playEffect(getWorld(), getPuckLocation(), Effect.SMOKE, 1);
  			broadcastMessage(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.match-continues")));
  		} else if (event.isCancelled()) {
  			item.remove();
  		}
  	}
  	
  	public void startRewards() {
  		if (getFirstTeamScores() > getSecondTeamScores()) {
  			broadcastMessage(ChatColor.GOLD + getFirstTeam().getName() + ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.team-win")));
  			broadcastMessage(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.result")) + ChatColor.RED + getFirstTeamScores() + ChatColor.WHITE + " : " + ChatColor.BLUE + getSecondTeamScores());
  			for (HockeyPlayer player : getFirstTeam().getMembers()) {
  				HGAPI.spawnRandomFirework(getWorld(), player.getBukkitPlayer().getLocation());
  			}
  			setWinnerTeam(getFirstTeam());
  			setLoserTeam(getSecondTeam());
  		} else if (getFirstTeamScores() < getSecondTeamScores()) {
  			broadcastMessage(ChatColor.GOLD + getSecondTeam().getName() + ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.team-win")));
  			broadcastMessage(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.result")) + ChatColor.RED + getFirstTeamScores() + ChatColor.WHITE + " : " + ChatColor.BLUE + getSecondTeamScores());
  			for (HockeyPlayer player : getSecondTeam().getMembers()) {
  				HGAPI.spawnRandomFirework(getWorld(), player.getBukkitPlayer().getLocation());
  			}
  			setWinnerTeam(getSecondTeam());
  			setLoserTeam(getFirstTeam());
  		} else if (getFirstTeamScores() == getSecondTeamScores()) {
  			broadcastMessage(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.tie")));
  			broadcastMessage(ChatColor.translateAlternateColorCodes('&', HGAPI.getPlugin().getConfig().getString("Messages.result")) + ChatColor.RED + getFirstTeamScores() + ChatColor.WHITE + " : " + ChatColor.BLUE + getSecondTeamScores());
  		}
  	}
  	
  	public void stopArena() {
  		this.gamestate = GameState.Offline;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(HGAPI.getPlugin(), new GameEnd(this.getWorld(), 10, this.getPuckLocation()), 0L, 20L);
  		MatchStopEvent event = new MatchStopEvent(getPlayers(), this);
  		Bukkit.getPluginManager().callEvent(event);
  		if (!event.isCancelled()) {
  			setRunning(false);
  			if (HGAPI.getPlugin().getConfig().getBoolean("GameSettings.MusicMatch")) {
  				getWorld().playEffect(getPuckLocation(), Effect.RECORD_PLAY, 0);
  			}
  			if (getCountToStartRunnable() != null) {
  				getCountToStartRunnable().cancel();
  			}
  			if (getMainRunnable() != null) {
  				getMainRunnable().cancel();
  			}
  			for (Iterator<HockeyPlayer> it = getPlayers().iterator(); it.hasNext();) {
  				HockeyPlayer player = (HockeyPlayer)it.next();
  				it.remove();
  				leavePlayer(player);
  			}
  			if (getPuckEntity() != null) {
  				if ((getPuckEntity().getItem() != null) && (!getPuckEntity().getItem().isDead())) {
  					getPuckEntity().getItem().remove();
  				}
  				getPuckEntity().clearItem();
  				getPuckEntity().clearPlayer();
  			}
  			this.red_score = 0;
  			this.blue_scores = 0;
  			this.countrunnable = null;
  			this.mainrun = null;
  			this.puckentity = null;
  			this.winner = null;
  			this.loser = null;
  		}
  	}
  	
  	public Team getWinnerTeam() {
  		return this.winner;
  	}
  
  	public void setWinnerTeam(Team team) {
  		this.winner = team;
  	}
  	
  	public Team getLoserTeam() {
  		return this.loser;
  	}
  	
  	public void setLoserTeam(Team team) {
  		this.loser = team;
  	}
}
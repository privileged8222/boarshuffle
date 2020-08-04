package me.privileged.colorshuffle.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.privileged.colorshuffle.Main;
import me.privileged.colorshuffle.arena.Arena;

public class ArenaManager {

	private List<Arena> arenas;
	
	public ArenaManager() {
		this.arenas = new ArrayList<>();
		this.load();
	}
	
	public boolean create(String name, Location start, Location end, Location spawn) {
		Main.getInstance().getConfigManager().reloadArenas();
		for (String arenaName : Main.getInstance().getConfigManager().getArenas().getStringList("arenas")) {
			if (arenaName.equalsIgnoreCase(name)) { return false; }
		}
		
		List<String> currentArenas = Main.getInstance().getConfigManager().getArenas().getStringList("arenas");
		currentArenas.add(name);
		Main.getInstance().getConfigManager().getArenas().set("arenas", currentArenas);
		
		Arena arena = new Arena(name, start, end, spawn);
		List<String> convertedStart = Arrays.asList(new String[] {"" + start.getX(), "" + start.getY(), "" + start.getZ()});
		Main.getInstance().getConfigManager().getArenas().set("arena." + name + ".start", convertedStart);
		List<String> convertedEnd = Arrays.asList(new String[] {"" + end.getX(), "" + end.getY(), "" + end.getZ()});
		Main.getInstance().getConfigManager().getArenas().set("arena." + name + ".end", convertedEnd);
		List<String> convertedSpawn = Arrays.asList(new String[] {"" + spawn.getX(), "" + spawn.getY(), "" + spawn.getZ()});
		Main.getInstance().getConfigManager().getArenas().set("arena." + name + ".spawn", convertedSpawn);
		String convertedWorld = spawn.getWorld().getName();
		Main.getInstance().getConfigManager().getArenas().set("arena." + name + ".world", convertedWorld);
		Main.getInstance().getConfigManager().saveArenas();
		add(arena);
		return true;
	}
	
	public void load() {
		Main.getInstance().getConfigManager().reloadArenas();
		for (String arenaName : Main.getInstance().getConfigManager().getArenas().getStringList("arenas")) {
			World world = Main.getInstance().getServer().getWorld(Main.getInstance().getConfigManager().getArenas().getString("arena." + arenaName + ".world"));
			List<String> spawnString = Main.getInstance().getConfigManager().getArenas().getStringList("arena." + arenaName + ".spawn");
			List<String> startString = Main.getInstance().getConfigManager().getArenas().getStringList("arena." + arenaName + ".start");
			List<String> endString = Main.getInstance().getConfigManager().getArenas().getStringList("arena." + arenaName + ".end");
			Location spawnLocation = new Location(world,
					Double.valueOf(spawnString.get(0)), Double.valueOf(spawnString.get(1)), Double.valueOf(spawnString.get(2)));
			Location startLocation = new Location(world,
					Double.valueOf(startString.get(0)), Double.valueOf(startString.get(1)), Double.valueOf(startString.get(2)));
			Location endLocation = new Location(world,
					Double.valueOf(endString.get(0)), Double.valueOf(endString.get(1)), Double.valueOf(endString.get(2)));
			
			add(new Arena(arenaName, startLocation, endLocation, spawnLocation));
			
		}
	}
	
	public void add(Arena arena) {
		if (!this.arenas.contains(arena)) {
			this.arenas.add(arena);
		}
	}
	
	public void remove(Arena arena) {
		if (this.arenas.contains(arena)) {
			this.arenas.remove(arena);
		}
	}
	
	public List<Arena> getArenas() {
		return this.arenas;
	}
	
	public Arena getArena() {
		if (arenas.size() >= 1)
			return this.arenas.get(0);
		
		return null;
	}
	
	public Arena get(String name) {
		for (Arena arena : this.arenas) {
			if (arena.getName().equals(name)) {
				return arena;
			}
		}
		return null;
	}
	
	public boolean setArenaStart(Location location, Arena arena) {
		Main.getInstance().getConfigManager().reloadArenas();
		if (location != null && arena != null) {
			List<String> currentArenas = Main.getInstance().getConfigManager().getArenas().getStringList("arenas");
			if (currentArenas.contains(arena.getName())) {
				List<String> convertedLocation = Arrays.asList(new String[] {"" + location.getX(), "" + location.getY(), "" + location.getZ()});
				Main.getInstance().getConfigManager().getArenas().set("arena." + arena.getName() + ".start", convertedLocation);
			}
			Main.getInstance().getConfigManager().saveArenas();
			arena.setStart(location);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean setArenaEnd(Location location, Arena arena) {
		Main.getInstance().getConfigManager().reloadArenas();
		if (location != null && arena != null) {
			List<String> currentArenas = Main.getInstance().getConfigManager().getArenas().getStringList("arenas");
			if (currentArenas.contains(arena.getName())) {
				List<String> convertedLocation = Arrays.asList(new String[] {"" + location.getX(), "" + location.getY(), "" + location.getZ()});
				Main.getInstance().getConfigManager().getArenas().set("arena." + arena.getName() + ".end", convertedLocation);
			}
			Main.getInstance().getConfigManager().saveArenas();
			arena.setEnd(location);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean setArenaSpawn(Location location, Arena arena) {
		Main.getInstance().getConfigManager().reloadArenas();
		if (location != null && arena != null) {
			List<String> currentArenas = Main.getInstance().getConfigManager().getArenas().getStringList("arenas");
			if (currentArenas.contains(arena.getName())) {
				List<String> convertedLocation = Arrays.asList(new String[] {"" + location.getX(), "" + location.getY(), "" + location.getZ()});
				Main.getInstance().getConfigManager().getArenas().set("arena." + arena.getName() + ".spawn", convertedLocation);
				Main.getInstance().getConfigManager().getArenas().set("arena." + arena.getName() + ".world", location.getWorld().getName());
			}
			Main.getInstance().getConfigManager().saveArenas();
			arena.setSpawn(location);
			return true;
		} else {
			return false;
		}
	}
	
}

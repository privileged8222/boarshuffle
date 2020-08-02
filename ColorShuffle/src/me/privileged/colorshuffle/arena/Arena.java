package me.privileged.colorshuffle.arena;

import org.bukkit.Location;

public class Arena {

	private Location start;
	private Location end;
	private Location spawn;
	private String name;
	
	public Arena(String name, Location start, Location end, Location spawn) {
		this.start = start;
		this.end = end;
		this.spawn = spawn;
		this.name = name;
	}

	public Location getStart() {
		return start;
	}

	public void setStart(Location start) {
		this.start = start;
	}

	public Location getEnd() {
		return end;
	}

	public void setEnd(Location end) {
		this.end = end;
	}

	public Location getSpawn() {
		return spawn;
	}

	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

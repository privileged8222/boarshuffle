package me.privileged.colorshuffle.manager;

import java.util.ArrayList;
import java.util.List;

import me.privileged.colorshuffle.arena.Arena;

public class ArenaManager {

	private List<Arena> arenas;
	
	public ArenaManager() {
		this.arenas = new ArrayList<>();
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
	
}

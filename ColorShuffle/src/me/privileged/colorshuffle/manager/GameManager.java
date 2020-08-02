package me.privileged.colorshuffle.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.privileged.colorshuffle.game.Game;
import net.md_5.bungee.api.ChatColor;

public class GameManager {

	private List<Game> games;
	
	public GameManager() {
		this.games = new ArrayList<>();
	}

	public void add(Game game) {
		if (!this.games.contains(game)) {
			this.games.add(game);
		}
	}
	
	public void remove(Game game) {
		if (this.games.contains(game)) {
			this.games.remove(game);
		}
	}
	
	public List<Game> getGames() {
		return this.games;
	}
	
	public void startGame(List<Player> players) {
		Game gameToStart = new Game(players);
		
		add(gameToStart);
		
		if (!gameToStart.start()) {
			players.forEach(player -> player.sendMessage(ChatColor.RED + "Game failed to start!"));
		}
	}
	
}

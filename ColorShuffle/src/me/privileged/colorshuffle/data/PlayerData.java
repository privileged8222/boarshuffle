package me.privileged.colorshuffle.data;

import org.bukkit.entity.Player;

import me.privileged.colorshuffle.enums.PlayerGameState;

public class PlayerData {

	private Player player;
	private PlayerGameState gameState;
	
	public PlayerData(Player player) {
		this.gameState = PlayerGameState.WAITING;
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public PlayerGameState getGameState() {
		return gameState;
	}

	public void setGameState(PlayerGameState gameState) {
		this.gameState = gameState;
	}
	
}

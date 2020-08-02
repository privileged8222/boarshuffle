package me.privileged.colorshuffle.manager;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.privileged.colorshuffle.data.PlayerData;

public class PlayerDataManager {

	private HashMap<Player, PlayerData> datas;
	
	public PlayerDataManager() {
		this.datas = new HashMap<>();
	}
	
	public PlayerData get(Player player) {
		if (this.datas.containsKey(player)) {
			return this.datas.get(player);
		}
		
		return null;
	}
	
	public void add(Player player) {
		if (!this.datas.containsKey(player)) {
			this.datas.put(player, new PlayerData(player));
		}
	}
	
	public void remove(Player player) {
		if (this.datas.containsKey(player)) {
			this.datas.remove(player);
		}
	}
	
}

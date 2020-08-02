package me.privileged.colorshuffle.event;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.privileged.colorshuffle.Main;
import me.privileged.colorshuffle.data.PlayerData;
import me.privileged.colorshuffle.enums.PlayerGameState;
import me.privileged.colorshuffle.util.BukkitUtil;
import net.md_5.bungee.api.ChatColor;

public class PlayerEvent implements Listener{

	private int startGameId;
	private boolean startingGame = false;
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Main.getInstance().getPlayerDataManager().add(((PlayerJoinEvent) event).getPlayer());
		PlayerData playerData = Main.getInstance().getPlayerDataManager().get(event.getPlayer());
		
		if (getWaitingPlayers().size() >= 2) {
			this.startingGame = true;
			this.startGameId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {

				int counter = 30;
				
				@Override
				public void run() {
					
					if (counter <= 0) {
						Main.getInstance().getGameManager().startGame(getWaitingPlayers());
						Bukkit.getScheduler().cancelTask(startGameId);
						return;
					}
					
					if (getWaitingPlayers().size() >= 2) {
						Bukkit.broadcastMessage(ChatColor.RED + "" + counter + " seconds left until game begins.");
					} else {
						startingGame = false; 
						Bukkit.getScheduler().cancelTask(startGameId);
						Bukkit.broadcastMessage(ChatColor.RED + "There are not enough players to start the game!");
						return;
					}
					
					
					counter--;
				}
				
			}, 0, 20);
		}
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Main.getInstance().getPlayerDataManager().remove(((PlayerQuitEvent) event).getPlayer());
		
		if (getWaitingPlayers().size() < 2) {
			if (this.startingGame) { 
				this.startingGame = false; 
				Bukkit.getScheduler().cancelTask(this.startGameId);
				Bukkit.broadcastMessage(ChatColor.RED + "There are not enough players to start the game!");
			}
		}
			
	}
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		if (BukkitUtil.buildPlayers.contains(event.getPlayer())) return;
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBreakeEvent(BlockBreakEvent event) {
		if (BukkitUtil.buildPlayers.contains(event.getPlayer())) return;
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerDamageEvent(EntityDamageEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerDamageByEntityEvent(EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}
	
	public List<Player> getWaitingPlayers() {
		List<Player> waitingPlayers = new ArrayList<>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			PlayerData playerData = Main.getInstance().getPlayerDataManager().get(player);
			if (playerData.getGameState() == PlayerGameState.WAITING) {
				waitingPlayers.add(player);
			}
		}
		
		return waitingPlayers;
		
	}
	
}

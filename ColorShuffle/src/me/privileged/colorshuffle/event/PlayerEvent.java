package me.privileged.colorshuffle.event;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.privileged.colorshuffle.Main;
import me.privileged.colorshuffle.data.PlayerData;
import me.privileged.colorshuffle.enums.PlayerGameState;
import me.privileged.colorshuffle.game.Game;
import me.privileged.colorshuffle.util.BukkitUtil;
import net.md_5.bungee.api.ChatColor;

public class PlayerEvent implements Listener{

	private int startGameId;
	private boolean startingGame = false;
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Main.getInstance().getPlayerDataManager().add(((PlayerJoinEvent) event).getPlayer());
		PlayerData playerData = Main.getInstance().getPlayerDataManager().get(event.getPlayer());
		
		event.getPlayer().setGameMode(GameMode.SURVIVAL);
		event.getPlayer().setHealth(20); event.getPlayer().setFoodLevel(20);
		Main.getInstance().getConfigManager().reloadGlobals();
		List<String> spawnString = Main.getInstance().getConfigManager().getGlobals().getStringList("spawn.location");
		String spawnWorldString = Main.getInstance().getConfigManager().getGlobals().getString("spawn.world");
		event.getPlayer().teleport(new Location(Main.getInstance().getServer().getWorld(spawnWorldString),
				Double.valueOf(spawnString.get(0)), Double.valueOf(spawnString.get(1)), Double.valueOf(spawnString.get(2))));
		
		if (startingGame || Main.getInstance().getGameManager().getGames().size() > 0) {
			Game game = Main.getInstance().getGameManager().getGames().get(0);
			event.getPlayer().teleport(game.getArena().getSpawn());
			playerData.setGameState(PlayerGameState.SPECTATING);
			event.getPlayer().setGameMode(GameMode.CREATIVE);
			event.getPlayer().getInventory().clear();
			
			for (Player gamePlayer : game.getAlivePlayers()) {
				gamePlayer.hidePlayer(event.getPlayer());
			}
		}
		
		if (getWaitingPlayers().size() >= 2) {
			if (startingGame) { return; }
			
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
		if (getWaitingPlayers().size() < 2) {
			if (this.startingGame) { 
				this.startingGame = false; 
				Bukkit.getScheduler().cancelTask(this.startGameId);
				Bukkit.broadcastMessage(ChatColor.RED + "There are not enough players to start the game!");
			}
		}
		
		Main.getInstance().getPlayerDataManager().remove(((PlayerQuitEvent) event).getPlayer());	
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
	
	@EventHandler
	public void onPlayerFoodLevelChangeEvent(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerChangeBlockEvent(EntityChangeBlockEvent event) {
		if (event.getEntity() instanceof Player) {
			if (BukkitUtil.buildPlayers.contains((Player) event.getEntity())) {
				return;
			}
		}
		
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

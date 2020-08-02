package me.privileged.colorshuffle.game;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

import me.privileged.colorshuffle.Main;
import me.privileged.colorshuffle.arena.Arena;
import me.privileged.colorshuffle.data.PlayerData;
import me.privileged.colorshuffle.enums.PlayerGameState;
import me.privileged.colorshuffle.util.BukkitUtil;
import net.md_5.bungee.api.ChatColor;

public class Game {

	private List<Player> players;
	private int gameId;
	private Player winner;
	private boolean inRound;
	private int numberOfRounds;
	private float roundTime = 5;
	
	public Game(List<Player> players) {
		this.players = new ArrayList<>();
		this.players = players;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	public List<Player> getAlivePlayers() {
		List<Player> returnList = new ArrayList<>();
		
		for (Player player : this.players) {
			PlayerData playerData = Main.getInstance().getPlayerDataManager().get(player);
			if (playerData.getGameState() == PlayerGameState.IN_GAME) {
				returnList.add(player);
			}
		}
		
		return returnList;
	}
	
	public List<Player> getSpectatingPlayers() {
		List<Player> returnList = new ArrayList<>();
		for (Player player : this.players) {
			PlayerData playerData = Main.getInstance().getPlayerDataManager().get(player);
			if (playerData.getGameState() == PlayerGameState.SPECTATING) {
				returnList.add(player);
			}
		}
		
		return returnList;
	}
	
	public Player getWinner() {
		return this.winner;
	}
	
	@SuppressWarnings("deprecation")
	public List<Block> randomiseArenaFloor(Location start, Location end) {
		List<Block> currentBlocks = BukkitUtil.blocksFromTwoPoints(start, end);
		List<Block> returnBlocks = new ArrayList<>();
		for (Block block : currentBlocks) {
			Random random = new Random();
			int randomColor = random.nextInt(DyeColor.values().length - 1);
			
			if (block.getType() == Material.WOOL) {
				block.setData(DyeColor.values()[randomColor].getData());
				returnBlocks.add(block);
			}
		}
		
		return returnBlocks;
	}
	
	@SuppressWarnings("deprecation")
	public boolean start() {
		
		Arena arenaToStart = Main.getInstance().getArenaManager().getArena();
		if (arenaToStart == null) {
			return false;
		}

		this.players.forEach(player -> player.teleport(arenaToStart.getSpawn())); 
		this.players.forEach(player -> player.getInventory().clear());
		for (Player player : this.players) {
			PlayerData playerData = Main.getInstance().getPlayerDataManager().get(player);
			playerData.setGameState(PlayerGameState.IN_GAME);
		}
		
		this.inRound = false;
		
		this.gameId = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
			
				if (!inRound) {
					if (getAlivePlayers().size() > 1) {
						
						inRound = true;
						
						numberOfRounds++;
						
						roundTime -= 0.1;
						if (roundTime <= 0.5) {
							roundTime = 0.5f;
						}
						
						getAlivePlayers().forEach(player -> player.sendMessage(ChatColor.GREEN + "Round: " + numberOfRounds));
						
						Wool randomWool = new Wool(Material.WOOL);
						
						Random random = new Random();
						int randomColor = random.nextInt(DyeColor.values().length - 1);
						
						randomWool.setColor(DyeColor.values()[randomColor]);
						
						ItemStack colorItem = randomWool.toItemStack();
						
						getAlivePlayers().forEach(player -> player.getInventory().setItem(4, colorItem));
						
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {  // cant modify blocks async :(
		
							@Override
							public void run() {
								
								List<Block> blocksInArena = BukkitUtil.blocksFromTwoPoints(arenaToStart.getStart(), arenaToStart.getEnd());
								int counter = 0;
								for (Block block : blocksInArena) {
									Material originalMaterial = block.getType();
									byte originalData = block.getData();
									if (!(block.getType() == Material.WOOL && block.getData() == DyeColor.values()[randomWool.getColor().ordinal()].getData())) {
										block.setType(Material.AIR);
										
										Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
											
											@Override
											public void run() {
																							
												block.setType(originalMaterial); block.setData(originalData);
												
												randomiseArenaFloor(arenaToStart.getStart(), arenaToStart.getEnd());
												
												for (Player player : players) {
													if (player.getLocation().getY() < arenaToStart.getSpawn().getY()) {
														player.teleport(arenaToStart.getSpawn());
														PlayerData playerData = Main.getInstance().getPlayerDataManager().get(player);
														playerData.setGameState(PlayerGameState.SPECTATING);
														player.setGameMode(GameMode.CREATIVE);
														player.getInventory().clear();
														player.sendMessage(ChatColor.RED + "You were eliminated!");
													}
												}
												
												for (Player player : getAlivePlayers()) {
													getSpectatingPlayers().forEach(specPlayer -> player.hidePlayer(specPlayer));
												}
												
												inRound = false;
											}
											
										}, (long) (15 * roundTime));
										
										counter++;
									}
								}
								
								if (counter == 0) {
									Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
		
										@Override
										public void run() {
											players.forEach(player -> player.sendMessage(ChatColor.RED + "NO BLOCKS MATCHED!"));
											inRound = false;
										}
										
									}, 20 * 3);
								}
							}
							
						}, (long) (20 * roundTime));
						
					} else {
						if (getAlivePlayers().size() > 0) {
							winner = getAlivePlayers().get(0);
							winner.getInventory().clear();
							players.forEach(player -> player.sendMessage(ChatColor.GREEN + "" + winner.getName() + " has won the game!"));
							
							Main.getInstance().getConfigManager().reloadWinners();
							
							List<String> currentWinners = Main.getInstance().getConfigManager().getWinners().getStringList("winners");			
							currentWinners.add(winner.getName());
							Main.getInstance().getConfigManager().getWinners().set("winners", currentWinners);
							
							DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
							LocalDateTime now = LocalDateTime.now();   
							
							Main.getInstance().getConfigManager().getWinners().set("winner." + winner.getName() + ".time", dtf.format(now));
							
							Main.getInstance().getConfigManager().saveWinners();
							
							Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.getInstance(), new Runnable() {

								@Override
								public void run() {
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
								}
								
							}, 20 * 15);
							
						} else {
							players.forEach(player -> player.sendMessage(ChatColor.RED + "There wasnt a winner!"));
						}
						Bukkit.getScheduler().cancelTask(gameId);
						return;
					}
				}
			}
			
		}, 0, 30L);
		
		return true;
	}
	
}

package me.privileged.colorshuffle;

import org.bukkit.plugin.java.JavaPlugin;

import me.privileged.colorshuffle.command.ColorShuffle;
import me.privileged.colorshuffle.event.PlayerEvent;
import me.privileged.colorshuffle.manager.ArenaManager;
import me.privileged.colorshuffle.manager.ConfigManager;
import me.privileged.colorshuffle.manager.GameManager;
import me.privileged.colorshuffle.manager.PlayerDataManager;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin{

	private static Main instance;
	
	private PlayerDataManager playerDataManager;
	private GameManager gameManager;
	private ArenaManager arenaManager;
	private ConfigManager configManager;
	
	public void log(String message) {
		this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[ColorShuffle] " + ChatColor.RESET + message);
	}
	
	@Override
	public void onLoad() {
		log("Loading...");
	}
	
	@Override
	public void onEnable() {
		instance = this;
		this.setupManagers();
		this.registerListeners();
		this.setupCommands();
		log("Enabled.");
	}
	
	@Override
	public void onDisable() {
		log("Disabled.");
	}
	
	private void setupManagers() {
		this.playerDataManager = new PlayerDataManager();
		this.gameManager = new GameManager();
		this.arenaManager = new ArenaManager();
		this.configManager = new ConfigManager();
	}

	private void registerListeners() {
		this.getServer().getPluginManager().registerEvents(new PlayerEvent(), this);
	}
	
	private void setupCommands() {
		this.getCommand("colorshuffle").setExecutor(new ColorShuffle());
	}
	
	public PlayerDataManager getPlayerDataManager() {
		return playerDataManager;
	}

	public static Main getInstance() {
		return instance;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public ArenaManager getArenaManager() {
		return arenaManager;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}
}

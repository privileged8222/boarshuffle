package me.privileged.colorshuffle.manager;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.privileged.colorshuffle.Main;
import net.md_5.bungee.api.ChatColor;

/*
 * Basic config manager
 */

public class ConfigManager {
	
	private File winnersFile;
	private FileConfiguration winnersCfg;
	
	private File arenasFile;
	private FileConfiguration arenasCfg;
	
	private File globalsFile;
	private FileConfiguration globalsCfg;
	
	public ConfigManager() {
		this.setup();
	}
	
	private void setup() {
		
		if (!Main.getInstance().getDataFolder().exists()) {
			Main.getInstance().getDataFolder().mkdir();
		}
		
		this.winnersFile = new File(Main.getInstance().getDataFolder(), "winners.yml");
		
		if (!this.winnersFile.exists()) {
			try {
				this.winnersFile.createNewFile();
			} catch (IOException e) {
				Main.getInstance().log(ChatColor.RED + "Failed to create winners.yml file");
			}
		}
		
		this.winnersCfg = YamlConfiguration.loadConfiguration(winnersFile);
		
		this.arenasFile = new File(Main.getInstance().getDataFolder(), "arenas.yml");
		
		if (!this.arenasFile.exists()) {
			try {
				this.arenasFile.createNewFile();
			} catch (IOException e) {
				Main.getInstance().log(ChatColor.RED + "Failed to create arenas.yml file");
			}
		}
		
		this.arenasCfg = YamlConfiguration.loadConfiguration(arenasFile);
		
		this.globalsFile = new File(Main.getInstance().getDataFolder(), "globals.yml");
		
		if (!this.globalsFile.exists()) {
			try {
				this.globalsFile.createNewFile();
			} catch (IOException e) {
				Main.getInstance().log(ChatColor.RED + "Failed to create globals.yml file");
			}
		}
		
		this.globalsCfg = YamlConfiguration.loadConfiguration(globalsFile);
	}

	public FileConfiguration getWinners() {
		return winnersCfg;
	}
	
	public FileConfiguration getArenas() {
		return arenasCfg;
	}
	
	public FileConfiguration getGlobals() {
		return globalsCfg;
	}
	
	public void reloadWinners() {
		this.winnersCfg = YamlConfiguration.loadConfiguration(winnersFile);
	}
	
	public void reloadArenas() {
		this.arenasCfg = YamlConfiguration.loadConfiguration(arenasFile);
	}
	
	public void reloadGlobals() {
		this.globalsCfg = YamlConfiguration.loadConfiguration(globalsFile);
	}
	
	public void saveWinners() {
		try {
			this.winnersCfg.save(winnersFile);
		} catch (IOException e) {
			Main.getInstance().log(ChatColor.RED + "Failed to save winners.yml file");
		}
	}
	
	public void saveArenas() {
		try {
			this.arenasCfg.save(arenasFile);
		} catch (IOException e) {
			Main.getInstance().log(ChatColor.RED + "Failed to save arenas.yml file");
		}
	}
	
	public void saveGlobals() {
		try {
			this.globalsCfg.save(globalsFile);
		} catch (IOException e) {
			Main.getInstance().log(ChatColor.RED + "Failed to save globals.yml file");
		}
	}
}
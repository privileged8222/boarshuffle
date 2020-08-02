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
	}

	public FileConfiguration getWinners() {
		return winnersCfg;
	}
	
	public void reloadWinners() {
		this.winnersCfg = YamlConfiguration.loadConfiguration(winnersFile);
	}
	
	public void saveWinners() {
		try {
			this.winnersCfg.save(winnersFile);
		} catch (IOException e) {
			Main.getInstance().log(ChatColor.RED + "Failed to save winners.yml file");
		}
	}
}

package me.privileged.colorshuffle.command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.privileged.colorshuffle.Main;
import me.privileged.colorshuffle.arena.Arena;
import me.privileged.colorshuffle.util.BukkitUtil;
import net.md_5.bungee.api.ChatColor;

public class ColorShuffle implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length > 0) {
			switch (args[0].toLowerCase()) {
			case "arena":
				if (args.length > 1) {
					Location loc = ((Player)sender).getLocation();
					switch (args[1].toLowerCase()) {
					case "list":
						for (Arena arena : Main.getInstance().getArenaManager().getArenas()) {
							sender.sendMessage(ChatColor.RED + arena.getName());
						}
						break;
						
					case "create":
						if (Main.getInstance().getArenaManager().create(args[2], loc , loc, loc)) {
							sender.sendMessage(ChatColor.GREEN + "Arena created.");
						} else {
							sender.sendMessage(ChatColor.RED + "Arena failed to be created.");
						}
						break;
					case "setspawn":
						Main.getInstance().getArenaManager().setArenaSpawn(loc, Main.getInstance().getArenaManager().get(args[2]));
						sender.sendMessage(ChatColor.GREEN + "Spawn set.");
						break;
					case "setstart":
						Main.getInstance().getArenaManager().setArenaStart(loc, Main.getInstance().getArenaManager().get(args[2]));
						sender.sendMessage(ChatColor.GREEN + "Start set.");
						break;
					case "setend":
						Main.getInstance().getArenaManager().setArenaEnd(loc, Main.getInstance().getArenaManager().get(args[2]));
						sender.sendMessage(ChatColor.GREEN + "End set.");
					}
					
				}
				break;
			case "build":
				if (BukkitUtil.buildPlayers.contains(((Player)sender))) {
					BukkitUtil.buildPlayers.remove(((Player)sender));
					sender.sendMessage(ChatColor.RED + "Build disabled.");
				} else {
					BukkitUtil.buildPlayers.add((Player)sender);
					sender.sendMessage(ChatColor.GREEN + "Build enabled.");
				}
				break;
			case "winners":
				Main.getInstance().getConfigManager().reloadWinners();
				List<String> winners = Main.getInstance().getConfigManager().getWinners().getStringList("winners");
				for (String winner: winners) {
					sender.sendMessage(ChatColor.GREEN + winner + " at time: " + Main.getInstance().getConfigManager().getWinners().getString("winner." + winner + ".time"));
				}
				break;
			case "setspawn":
				Location loc = ((Player)sender).getLocation();
				Main.getInstance().getConfigManager().reloadGlobals();
				List<String> spawnLoc = Arrays.asList("" + loc.getX(), "" + loc.getY(), "" + loc.getZ());
				String spawnWorld = loc.getWorld().getName();
				Main.getInstance().getConfigManager().getGlobals().set("spawn.location", spawnLoc);
				Main.getInstance().getConfigManager().getGlobals().set("spawn.world", spawnWorld);
				Main.getInstance().getConfigManager().saveGlobals();
			}
		}
		
		return true;
	}
	
}

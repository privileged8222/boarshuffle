package me.privileged.colorshuffle.command;

import java.util.List;

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
					switch (args[1].toLowerCase()) {
					case "list":
						for (Arena arena : Main.getInstance().getArenaManager().getArenas()) {
							sender.sendMessage(ChatColor.RED + arena.getName());
						}
						break;
						
					case "create":
						Main.getInstance().getArenaManager().getArenas().add(new Arena(args[2],  ((Player)sender).getLocation(),  ((Player)sender).getLocation(), ((Player)sender).getLocation()));
						sender.sendMessage(ChatColor.GREEN + "Arena created.");
						break;
					case "setspawn":
						Main.getInstance().getArenaManager().get(args[2]).setSpawn(((Player)sender).getLocation());
						break;
					case "setstart":
						Main.getInstance().getArenaManager().get(args[2]).setStart(((Player)sender).getLocation());
						break;
					case "setend":
						Main.getInstance().getArenaManager().get(args[2]).setEnd(((Player)sender).getLocation());
					}
					
				}
				break;
			case "build":
				if (BukkitUtil.buildPlayers.contains(((Player)sender))) {
					BukkitUtil.buildPlayers.remove(((Player)sender));
				} else {
					BukkitUtil.buildPlayers.add((Player)sender);
				}
				break;
			case "winners":
				Main.getInstance().getConfigManager().reloadWinners();
				List<String> winners = Main.getInstance().getConfigManager().getWinners().getStringList("winners");
				for (String winner: winners) {
					sender.sendMessage(ChatColor.GREEN + winner + " at time: " + Main.getInstance().getConfigManager().getWinners().getString("winner." + winner + ".time"));
				}
			}
		}
		
		return true;
	}
	
}

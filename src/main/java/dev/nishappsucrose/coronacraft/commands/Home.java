package dev.nishappsucrose.coronacraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Home implements CommandExecutor {
    private static ChatColor TEXT_COLOR = ChatColor.GOLD;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        player.setAllowFlight(false);

        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        player.sendMessage(TEXT_COLOR + "Returned to lobby");

        return true;
    }
}

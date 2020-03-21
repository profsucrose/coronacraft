package dev.nishappsucrose.coronacraft.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetView implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (player.getWorld().getName() != "world") {
            player.teleport(new Location(player.getWorld(), 0, 165, 0));
        }
        return true;
    }
}

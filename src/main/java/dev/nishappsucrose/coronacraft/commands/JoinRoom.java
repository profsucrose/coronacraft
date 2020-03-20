package dev.nishappsucrose.coronacraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinRoom implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Must specify room ID");
            return false;
        }

        String roomId = args[0];
        World room = Bukkit.getWorld(roomId);

        if (room == null) {
            sender.sendMessage(ChatColor.RED
                    + "Room with ID "
                    + ChatColor.BOLD
                    + roomId
                    + ChatColor.RESET
                    + ChatColor.RED
                    + " does not exist!"
            );
            return false;
        }

        ((Player) sender).teleport(room.getSpawnLocation());
        return true;

    }
}

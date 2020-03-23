package dev.nishappsucrose.coronacraft.commands;

import dev.nishappsucrose.coronacraft.WorldChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinRoom implements CommandExecutor {
    private static final ChatColor TEXT_COLOR = ChatColor.GOLD;

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

        Player player = (Player) sender;
        player.setAllowFlight(true);

        player.teleport(room.getSpawnLocation());
        WorldChat.sendWorldMessage(roomId, TEXT_COLOR
                + ""
                + ChatColor.BOLD
                + player.getDisplayName()
                + ChatColor.RESET
                + ChatColor.GOLD
                + " has joined the room"
        );

        player.sendMessage(TEXT_COLOR
                + "You have joined room "
                + ChatColor.BOLD
                + roomId
                + ChatColor.RESET
                + TEXT_COLOR
                + "."
        );
        player.sendMessage(TEXT_COLOR
                + "Go to "
                + ChatColor.UNDERLINE
                + "https://callcraft.co/room/"
                + roomId
                + ChatColor.RESET
                + ChatColor.GOLD
                + " to get started!"
        );

        return true;

    }
}

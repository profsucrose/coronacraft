package dev.nishappsucrose.coronacraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ToggleChannel implements CommandExecutor {

    private static final ChatColor ERROR_COLOR = ChatColor.RED;
    private static final ChatColor TEXT_COLOR = ChatColor.GREEN;

    private static void fillArea(int x, int y, int z, Material block) {
        for (int mapY = 0; mapY < 50; mapY++) {
            for (int mapX = 49; mapX > -1; mapX--)
                Bukkit.getWorld("world").getBlockAt(x + mapX, y, z + mapY).setType(block);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(ERROR_COLOR + "Must specify channel ID to block (1 - 4)");
        }

        final int channelID = Integer.valueOf(args[0]);
        final boolean isBlocked = !LoadStreams.blockedStreams[channelID - 1];

        LoadStreams.blockedStreams[channelID - 1] = isBlocked;

        int x = LoadStreams.streamCoords[channelID - 1][0];
        int y = LoadStreams.streamCoords[channelID - 1][1];
        if (isBlocked) {
            fillArea(x, 5, y, Material.BLACK_CONCRETE);
        } else {
            fillArea(x, 5, y, Material.AIR);
        }

        Bukkit.broadcastMessage(TEXT_COLOR + "Toggled channel " + channelID);

        return true;

    }
}

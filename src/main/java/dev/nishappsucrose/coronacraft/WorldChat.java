package dev.nishappsucrose.coronacraft;

import org.bukkit.Bukkit;

public class WorldChat {
    public static final void sendWorldMessage(String worldName, String message) {
        Bukkit.getWorld(worldName).getPlayers().forEach(player -> player.sendMessage(message));
    }
}

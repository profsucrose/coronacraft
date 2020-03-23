package dev.nishappsucrose.coronacraft.events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private static final ChatColor TEXT_COLOR = ChatColor.GOLD;

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        player.setGameMode(GameMode.ADVENTURE);
        player.sendMessage(TEXT_COLOR + "Welcome to CallCraft!");
        player.sendMessage(TEXT_COLOR + "CallCraft is videostreaming (both video and audio via chat) in Minecraft!");
        player.sendMessage(TEXT_COLOR + "Simply run /create to create a room");
        player.sendMessage(TEXT_COLOR + "or /join <room_id> to join an existing room");
        player.sendMessage(TEXT_COLOR + "Have fun!");

    }

}

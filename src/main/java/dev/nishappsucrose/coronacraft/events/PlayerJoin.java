package dev.nishappsucrose.coronacraft.events;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(true);

    }

}

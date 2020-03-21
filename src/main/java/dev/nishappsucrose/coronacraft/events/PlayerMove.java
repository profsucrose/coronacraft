package dev.nishappsucrose.coronacraft.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public static void onPlayerMove(PlayerMoveEvent e) {

        Player player = e.getPlayer();
        Location location = player.getLocation();

        if (isWithin(location.getX(), -22, -20)
        && isWithin(location.getZ(), 49, 51)
        && player.getWorld().getName() == "world"
        && isWithin(location.getY(), 34, 36)) {
            player.teleport(player.getWorld().getSpawnLocation());
        }


    }

    private static boolean isWithin(double x, int lower, int upper) {
        return x >= lower && x <= upper;
    }

}

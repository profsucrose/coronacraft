package dev.nishappsucrose.coronacraft.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;

public class PerWorldChat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player sender = e.getPlayer();
        Set<Player> r = e.getRecipients();
        for (Player pls : Bukkit.getServer().getOnlinePlayers()) {
            if (!pls.getWorld().getName().equals(sender.getWorld().getName())) {
                r.remove(pls);
            }
        }
    }
}

package dev.nishappsucrose.coronacraft;

import dev.nishappsucrose.coronacraft.commands.LoadImage;
import dev.nishappsucrose.coronacraft.commands.Origin;
import dev.nishappsucrose.coronacraft.events.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoronaCraft extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        LoadImage.plugin = this;
        this.getCommand("loadimage").setExecutor(new LoadImage());
        this.getCommand("origin").setExecutor(new Origin());

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
    }

}

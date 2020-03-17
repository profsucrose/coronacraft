package dev.nishappsucrose.coronacraft;

import dev.nishappsucrose.coronacraft.commands.LoadStreams;
import dev.nishappsucrose.coronacraft.commands.Origin;
import dev.nishappsucrose.coronacraft.events.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoronaCraft extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        LoadStreams.plugin = this;
        this.getCommand("loadstreams").setExecutor(new LoadStreams());
        this.getCommand("origin").setExecutor(new Origin());

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
    }

}

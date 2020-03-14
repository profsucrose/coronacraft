package dev.nishappsucrose.coronacraft;

import dev.nishappsucrose.coronacraft.commands.LoadImage;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoronaCraft extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("loadimage").setExecutor(new LoadImage());
    }

}

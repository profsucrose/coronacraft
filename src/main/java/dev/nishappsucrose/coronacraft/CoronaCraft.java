package dev.nishappsucrose.coronacraft;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import dev.nishappsucrose.coronacraft.commands.*;
import dev.nishappsucrose.coronacraft.events.PerWorldChat;
import dev.nishappsucrose.coronacraft.events.PlayerJoin;
import dev.nishappsucrose.coronacraft.events.PlayerMove;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class CoronaCraft extends JavaPlugin {

    private static void initFirebase() throws IOException {
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream("callcraftServiceAccount.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://coronacraft-0.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            initFirebase();
        } catch (IOException e) {
            System.out.println("Unable to init Firebase");
        }

        LoadStreams.plugin = this;
        HDTest.plugin = this;
        CreateRoom.plugin = this;

        //this.getCommand("loadstreams").setExecutor(new LoadStreams());
        this.getCommand("home").setExecutor(new Home());
        //this.getCommand("togglechannel").setExecutor(new ToggleChannel());
        //this.getCommand("hdtest").setExecutor(new HDTest());
        //this.getCommand("loadimage").setExecutor(new LoadImage());
        //this.getCommand("livetest").setExecutor(new LiveTest());
        this.getCommand("create").setExecutor(new CreateRoom());
        this.getCommand("join").setExecutor(new JoinRoom());
        this.getCommand("resetview").setExecutor(new ResetView());

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PerWorldChat(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMove(), this);

    }

}

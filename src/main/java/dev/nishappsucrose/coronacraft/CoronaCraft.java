package dev.nishappsucrose.coronacraft;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import dev.nishappsucrose.coronacraft.commands.*;
import dev.nishappsucrose.coronacraft.events.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class CoronaCraft extends JavaPlugin {

    private static void initFirebase() throws IOException {
        FileInputStream serviceAccount =
                null;
        try {
            serviceAccount = new FileInputStream("C:\\Users\\rolan\\Documents\\Definitely Not Stupidly Placed Super Secret Private and Public Key Files\\callcraftServiceAccount.json");
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
        this.getCommand("loadstreams").setExecutor(new LoadStreams());
        this.getCommand("origin").setExecutor(new Origin());
        this.getCommand("togglechannel").setExecutor(new ToggleChannel());
        this.getCommand("hdtest").setExecutor(new HDTest());
        this.getCommand("loadimage").setExecutor(new LoadImage());
        this.getCommand("livetest").setExecutor(new LiveTest());

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
    }

}

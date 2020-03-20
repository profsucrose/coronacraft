package dev.nishappsucrose.coronacraft.commands;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.annotations.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class AudioStream implements CommandExecutor {
    private static final Firestore db = FirestoreClient.getFirestore();
    private static final HashMap<String, ChatColor> channelColors =  new HashMap<String, ChatColor>() {{
        put("one", ChatColor.GOLD);
        put("two", ChatColor.LIGHT_PURPLE);
        put("three", ChatColor.AQUA);
        put("four", ChatColor.GREEN);
    }};

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        db.collection("chat")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirestoreException e) {
                        if (e != null) {
                            System.err.println("Listen failed: " + e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    String message = (String) dc.getDocument().get("message");
                                    String channelId = (String) dc.getDocument().get("channel");
                                    Bukkit.broadcastMessage(channelColors.get(channelId) + "Channel " + channelId + ": " + message);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
        });

        return true;


    }
}

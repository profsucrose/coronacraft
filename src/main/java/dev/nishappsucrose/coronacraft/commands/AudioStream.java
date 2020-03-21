package dev.nishappsucrose.coronacraft.commands;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.annotations.Nullable;
import dev.nishappsucrose.coronacraft.WorldChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AudioStream {
    private static final Firestore db = FirestoreClient.getFirestore();
    private static boolean didRetrieveAudio = false;
    private static final HashMap<String, ChatColor> channelColors =  new HashMap<String, ChatColor>() {{
        put("one", ChatColor.GOLD);
        put("two", ChatColor.LIGHT_PURPLE);
        put("three", ChatColor.AQUA);
        put("four", ChatColor.GREEN);
    }};

    public static boolean startAudioStream(Player player, String roomId) {
        db.collection("rooms/" + roomId + "/chat")
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
                                    if (didRetrieveAudio) {
                                        String message = (String) dc.getDocument().get("message");
                                        String channelId = (String) dc.getDocument().get("channel");
                                        WorldChat.sendWorldMessage(roomId, channelColors.get(channelId) + "Channel " + channelId + ": " + message);
                                    }
                                     break;
                                default:
                                    break;
                            }
                        }

                        if (!didRetrieveAudio) didRetrieveAudio = true;
                    }
        });

        return true;


    }
}

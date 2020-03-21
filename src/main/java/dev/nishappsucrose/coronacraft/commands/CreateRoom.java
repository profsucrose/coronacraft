package dev.nishappsucrose.coronacraft.commands;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import dev.nishappsucrose.coronacraft.EmptyChunkGenerator;
import dev.nishappsucrose.coronacraft.WorldChat;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.*;

public class CreateRoom implements CommandExecutor {

    private static final Random rand = new Random();
    private static final ChatColor TEXT_COLOR = ChatColor.GOLD;
    private static final Firestore db = FirestoreClient.getFirestore();

    public static Plugin plugin;

    private static final int[][] one = {{-59, -49}, {-58, -50}, {-57, -51}, {-57, -50}, {-57, -49}, {-57, -48}, {-57, -47}, {-56, -47}, {-55, -47}, {-58, -47}, {-59, -47}};
    private static final int[][] two = {{55, -50}, {56, -51}, {57, -51}, {58, -50}, {57, -51}, {58, -50}, {57, -49}, {56, -48}, {56, -47}, {57, -47}, {58, -47}, {55, -47}};
    private static final int[][] three = {{-58, 6}, {-57, 6}, {-56, 6}, {-55, 5}, {-56, 4}, {-57, 4}, {-55, 3}, {-56, 2}, {-57, 2}, {-58, 2}};
    private static final int[][] four = {{55, 2}, {55, 3}, {55, 4}, {56, 4}, {57, 4}, {58, 4}, {58, 3}, {58, 2}, {58, 5}, {58, 6}};
    private static final Material[] numberMaterials = {Material.ORANGE_CONCRETE, Material.MAGENTA_CONCRETE, Material.LIGHT_BLUE_CONCRETE, Material.LIME_CONCRETE};

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        sender.sendMessage(ChatColor.GOLD + "Generating room, wait a few moments...");
        String roomId = generateRoomCode();
        WorldCreator wc = new WorldCreator(roomId);
        wc.generator(new EmptyChunkGenerator());
        wc.createWorld();

        Player roomCreator = (Player) sender;
        World room = Bukkit.getWorld(roomId);

        room.setSpawnLocation(0, 165, 0);
        room.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        room.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        room.setGameRule(GameRule.FALL_DAMAGE, false);
        room.setTime(6000);

        // Create firestore room entry
        Map<String, Object> emptyChannel = new HashMap<>();
        emptyChannel.put("image", "");

        db.document("rooms/" + roomId + "/videostreams/one").set(emptyChannel);
        db.document("rooms/" + roomId + "/videostreams/two").set(emptyChannel);
        db.document("rooms/" + roomId + "/videostreams/three").set(emptyChannel);
        db.document("rooms/" + roomId + "/videostreams/four").set(emptyChannel);

        fillArea(room, -53, 80, -53, 54, 81, 54, Material.WHITE_CONCRETE);
        createStructures(room);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                LoadStreams.startVideoCall(roomCreator);
                AudioStream.startAudioStream(roomCreator);
                roomCreator.teleport(room.getSpawnLocation());

                WorldChat.sendWorldMessage(roomId, TEXT_COLOR
                        + "Welcome to your CallCraft room!");
                WorldChat.sendWorldMessage(roomId, TEXT_COLOR
                        + "Your room ID is "
                        + ChatColor.BOLD
                        + roomId
                        + ChatColor.RESET
                        + TEXT_COLOR + ".");
                WorldChat.sendWorldMessage(roomId, TEXT_COLOR
                        + "/resetview to teleport back to the viewing area.");
                WorldChat.sendWorldMessage(roomId, TEXT_COLOR
                        + "/home to teleport back to the lobby.");
                WorldChat.sendWorldMessage(roomId,TEXT_COLOR
                        + "Go to "
                        + ChatColor.UNDERLINE
                        + "https://coronacraft-0.web.app/room/"
                        + roomId
                        + ChatColor.RESET
                        + ChatColor.GOLD
                        + " to get started!"
                );
            }
        }, 40);

        return true;
    }

    private static final void fillArea(World world, int x1, int y1, int z1, int x2, int y2, int z2, Material block) {
        for (int x = x1; x < x2; x++) {
            for (int y = y1; y < y2; y++) {
                for (int z = z1; z < z2; z++) {
                    world.getBlockAt(x, y, z).setType(block);
                }
            }
        }
    }

    private static void createStructures(World world) {
        int[][][] numbers = {one, two, three, four};
        for (int i = 0; i < numbers.length; i++) {
            int[][] structure = numbers[i];
            for (int[] blockCoords : structure) {
                world.getBlockAt(blockCoords[0], 81, blockCoords[1]).setType(numberMaterials[i]);
            }
        }
        world.getBlockAt(0, 164, 0).setType(Material.BARRIER);
        world.getBlockAt(0, 167, 0).setType(Material.BLACK_CONCRETE);
    }

    private static final String generateRoomCode() {
        return returnThreeDigitCode()
                + "-"
                + returnThreeDigitCode()
                + "-"
                + returnThreeDigitCode();

    }

    private static String returnThreeDigitCode() {
        return Integer.toString(randToTen())
                + Integer.toString(randToTen())
                + Integer.toString(randToTen());
    }

    private static final int randToTen() {
        return rand.nextInt(10);
    }


}

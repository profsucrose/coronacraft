package dev.nishappsucrose.coronacraft.commands;

import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.annotations.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Base64;
import java.util.HashMap;

public class LoadStreams implements CommandExecutor {

    public static boolean[] blockedStreams = {false, false, false, false};

    private static final ChatColor TEXT_COLOR = ChatColor.GOLD;
    private static final ChatColor ERROR_COLOR = ChatColor.RED;


    public static Plugin plugin;
    public static Integer taskId;
    private static final String[] streamIds = {"one", "two", "three", "four"};
    public static final int[][] streamCoords = {{-51, -51}, {2, -51}, {-51, 2}, {2, 2}};
    private static final String peppaPlaceholderURL = "https://i.imgur.com/zrYfqjY.jpg";
    private static final int STREAM_REFRESH_SPEED = 1;

    private static final Material[] BLOCKS = {
            Material.TERRACOTTA,
            Material.BLACK_TERRACOTTA,
            Material.RED_TERRACOTTA,
            Material.GREEN_TERRACOTTA,
            Material.BROWN_TERRACOTTA,
            Material.BLUE_TERRACOTTA,
            Material.PURPLE_TERRACOTTA,
            Material.CYAN_TERRACOTTA,
            Material.LIGHT_GRAY_TERRACOTTA,
            Material.GRAY_TERRACOTTA,
            Material.PINK_TERRACOTTA,
            Material.LIME_TERRACOTTA,
            Material.YELLOW_TERRACOTTA,
            Material.LIGHT_BLUE_TERRACOTTA,
            Material.MAGENTA_TERRACOTTA,
            Material.ORANGE_TERRACOTTA,
            Material.WHITE_TERRACOTTA,
            Material.BLACK_CONCRETE,
            Material.RED_CONCRETE,
            Material.GREEN_CONCRETE,
            Material.BROWN_CONCRETE,
            Material.BLUE_CONCRETE,
            Material.PURPLE_CONCRETE,
            Material.CYAN_CONCRETE,
            Material.LIGHT_GRAY_CONCRETE,
            Material.GRAY_CONCRETE,
            Material.PINK_CONCRETE,
            Material.LIME_CONCRETE,
            Material.YELLOW_CONCRETE,
            Material.LIGHT_BLUE_CONCRETE,
            Material.PURPLE_CONCRETE,
            Material.ORANGE_CONCRETE,
            Material.WHITE_CONCRETE
    };

    // Given RGB (10, 200, 34)
    // Find RGB in array that's the closest

    private static final int[][] BLOCK_RGBS = {
            {149, 93, 68},
            {38, 23, 16},
            {139, 60, 46},
            {75, 82, 43},
            {76, 50, 35},
            {73, 58, 89},
            {116, 68, 85},
            {86, 91, 91},
            {133, 106, 97},
            {56, 42, 36},
            {157, 74, 75},
            {101, 115, 49},
            {185, 133, 37},
            {117, 109, 137},
            {150, 89, 109},
            {158, 82, 37},
            {194, 165, 149},
            {9, 11, 16},
            {140, 32, 32},
            {72, 90, 36},
            {95, 58, 31},
            {44, 46, 142},
            {100, 31, 154},
            {21, 117, 133},
            {124, 124, 114},
            {53, 56, 60},
            {211, 102, 142},
            {94, 168, 24},
            {237, 173, 21},
            {35, 135, 196},
            {167, 47, 157},
            {219, 95, 0},
            {205, 210, 211}
    };

    static private void loadStreams(CommandSender sender) {

        Player player = (Player) sender;

        // -65 4 192
        // -192 4 319

        for (int i = 0; i < 4; i++) {
            if (blockedStreams[i]) continue;

            BufferedImage image = null;
            String channel = streamIds[i];
            int x = streamCoords[i][0];
            int y = streamCoords[i][1];

            try {
                URL streamCaptureDownload = new URL("https://firestore.googleapis.com/v1/projects/coronacraft-0/databases/(default)/documents/rooms/" + player.getWorld().getName() + "/videostreams/" + channel);
                HttpURLConnection con = (HttpURLConnection) streamCaptureDownload.openConnection();
                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();
                if (content.toString().contains("data:image/jpeg;base64,")) {
                    String base64String = content.toString().split("data:image/jpeg;base64,")[1].split("\"    ")[0];
                    byte[] decodedBytes = Base64.getDecoder().decode(base64String);
                    image = ImageIO.read(new ByteArrayInputStream(decodedBytes));
                } else {
                    image = ImageIO.read(new URL(peppaPlaceholderURL));
                }

                //sender.sendMessage("Loading channel " + channel);

                for (int mapY = 0; mapY < 50; mapY++) {
                    for (int mapX = 49; mapX > -1; mapX--) {

                        int color = image.getRGB(mapX, mapY);
                        int red = (color & 0x00ff0000) >> 16;
                        int green = (color & 0x0000ff00) >> 8;
                        int blue = color & 0x000000ff;

                        int currentBlockIndex = 0;
                        double currentBlockDiff = getBlockDiff(red, green, blue, BLOCK_RGBS[0]);

                        for (int rgbI = 1; rgbI < BLOCK_RGBS.length; rgbI++) {
                            int[] rgb = BLOCK_RGBS[rgbI];
                            double difference = getBlockDiff(red, green, blue, rgb);
                            if (difference < currentBlockDiff) {
                                currentBlockDiff = difference;
                                currentBlockIndex = rgbI;
                            }
                        }

                        player.getWorld().getBlockAt(x + mapX, 81, y + mapY).setType(BLOCKS[currentBlockIndex]);
                        //System.out.println("Placed concrete at index " + currentBlockIndex + " at coordinate: " + (x + mapX) + ", " + (y + 192));

                    }
                }

                image.flush();

            } catch (IOException e) {
                sender.sendMessage(ERROR_COLOR + "Could not load image");
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 1) {
            sender.sendMessage("Must specify isToggled");
            return false;
        }

        boolean isToggled = Boolean.parseBoolean(args[0]);

        if (!isToggled) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = null;
            Bukkit.broadcastMessage(ChatColor.GREEN + "Streaming stopped successfully");
        } else {
            taskId = new BukkitRunnable() {
                @Override
                public void run() {
                    //code
                    loadStreams(sender);
                }
            }.runTaskTimer(plugin, 0, STREAM_REFRESH_SPEED).getTaskId();
            Bukkit.broadcastMessage(ChatColor.GREEN + "Streaming started successfully");
        }

        return true;

    }

    static double getBlockDiff(int red, int blue, int green, int[] block) {
        return Math.sqrt(
                Math.pow(red - block[0], 2)
                + Math.pow(blue - block[1], 2)
                + Math.pow(green - block[2], 2)
        );
    }

}

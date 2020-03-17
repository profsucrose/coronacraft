package dev.nishappsucrose.coronacraft.commands;

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
    public static final int[][] streamCoords = {{-192, 192}, {-135, 192}, {-135, 249}, {-192, 249}};
    private static final String peppaPlaceholder = "data:image/png;base64,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";

    private static final Material[] CONCRETES = {
            Material.WHITE_CONCRETE,
            Material.ORANGE_CONCRETE,
            Material.PURPLE_CONCRETE,
            Material.LIGHT_BLUE_CONCRETE,
            Material.YELLOW_CONCRETE,
            Material.LIME_CONCRETE,
            Material.PINK_CONCRETE,
            Material.GRAY_CONCRETE,
            Material.LIGHT_GRAY_CONCRETE,
            Material.CYAN_CONCRETE,
            Material.PURPLE_CONCRETE,
            Material.BLUE_CONCRETE,
            Material.BROWN_CONCRETE,
            Material.GREEN_CONCRETE,
            Material.RED_CONCRETE,
            Material.BLACK_CONCRETE
    };

    // Given RGB (10, 200, 34)
    // Find RGB in array that's the closest

    private static final int[][] CONCRETE_RGBS = {
            {255, 255, 255},
            {255, 165, 0},
            {128, 0, 128},
            {52, 204, 255},
            {255, 255, 0},
            {50, 205, 50},
            {255, 192, 203},
            {49, 51, 43},
            {134, 136, 138},
            {57, 82, 79},
            {128, 0, 128},
            {0, 0, 255},
            {255, 248, 220},
            {0, 255, 0},
            {255, 0, 0},
            {0, 0, 0}
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
                URL streamCaptureDownload = new URL("https://firestore.googleapis.com/v1/projects/coronacraft-0/databases/(default)/documents/videostreams/" + channel);
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
                byte[] decodedBytes;
                if (content.toString().contains("data:image/jpeg;base64,")) {
                    String base64String = content.toString().split("data:image/jpeg;base64,")[1].split("\"    ")[0];
                    decodedBytes = Base64.getDecoder().decode(base64String);
                } else {
                    decodedBytes = Base64.getDecoder().decode(peppaPlaceholder);
                }

                image = ImageIO.read(new ByteArrayInputStream(decodedBytes));

                //sender.sendMessage("Loading channel " + channel);

                for (int mapY = 0; mapY < 50; mapY++) {
                    for (int mapX = 49; mapX > -1; mapX--) {

                        System.out.println(mapX + ", " + mapY);

                        int color = image.getRGB(mapX, mapY);
                        int red = (color & 0x00ff0000) >> 16;
                        int green = (color & 0x0000ff00) >> 8;
                        int blue = color & 0x000000ff;

                        int currentBlockIndex = 0;
                        double currentBlockDiff = getBlockDiff(red, green, blue, CONCRETE_RGBS[0]);

                        for (int rgbI = 1; rgbI < CONCRETE_RGBS.length; rgbI++) {
                            int[] rgb = CONCRETE_RGBS[rgbI];
                            double difference = getBlockDiff(red, green, blue, rgb);
                            if (difference < currentBlockDiff) {
                                currentBlockDiff = difference;
                                currentBlockIndex = rgbI;
                            }
                        }

                        player.getWorld().getBlockAt(x + mapX, 4, y + mapY).setType(CONCRETES[currentBlockIndex]);
                        System.out.println("Placed concrete at index " + currentBlockIndex + " at coordinate: " + (x + mapX) + ", " + (y + 192));

                    }
                }

                //sender.sendMessage("Loaded channel " + channel + " successfully");

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

        if (taskId == null) {
            taskId = new BukkitRunnable() {
                @Override
                public void run() {
                    loadStreams(sender);
                }
            }.runTaskTimer(plugin, 0, 15).getTaskId();
        }

        if (!isToggled) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = null;
            sender.sendMessage(ChatColor.GREEN + "Streaming stopped successfully");
        } else {
            sender.sendMessage(ChatColor.GREEN + "Streaming started successfully");
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

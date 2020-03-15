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

public class LoadImage implements CommandExecutor {

    private static final ChatColor TEXT_COLOR = ChatColor.GOLD;
    private static final ChatColor ERROR_COLOR = ChatColor.RED;
    public static Plugin plugin;
    public static Integer taskId;
    private static final String[] streamIds = {"one", "two", "three", "four"};
    private static final int[][] streamCoords = {{-192, 192}, {-135, 192}, {-135, 249}, {-192, 249}};

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
                if (content.toString().contains("data:image/jpeg;base64,")) {
                    String base64String = content.toString().split("data:image/jpeg;base64,")[1].split("\"    ")[0];

                    byte[] decodedBytes = Base64.getDecoder().decode(base64String);

                    image = ImageIO.read(new ByteArrayInputStream(decodedBytes));
                } else {
                    image = ImageIO.read(new URL("https://i.imgur.com/zrYfqjY.jpg"));
                }

                sender.sendMessage("Loading channel " + channel);

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

                sender.sendMessage("Loaded channel " + channel + " successfully");

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

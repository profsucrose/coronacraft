package dev.nishappsucrose.coronacraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class LoadImage implements CommandExecutor {

    private static final ChatColor TEXT_COLOR = ChatColor.GOLD;
    private static final ChatColor ERROR_COLOR = ChatColor.RED;

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

    private static final int[][][] CONCRETE_RGBS = {
            {{250, 255}, {250, 255}, {250, 255}},
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 2) {
            sender.sendMessage("Must specify file path and map size");
            return false;
        }

        String imageUrl = args[0];
        int mapSize = Integer.parseInt(args[1]);

        Player player = (Player) sender;

        // -65 4 192
        // -192 4 319




        BufferedImage image = null;

        try {
            URL url = new URL(imageUrl);
            image = ImageIO.read(url);

            sender.sendMessage("Loading image to map");

            for (int mapX = 0; mapX < mapSize; mapX++) {
                for (int mapY = 0; mapY < mapSize; mapY++) {

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
                        if (difference < currentBlockDiff) currentBlockIndex = rgbI;
                    }

                    player.getWorld().getBlockAt(-192 + mapX, 4, mapY + 192).setType(CONCRETES[currentBlockIndex]);
                    System.out.println("RGB: " + red + ", " + green + ", " + blue);
                    System.out.println("Placed concrete at index: " + currentBlockIndex);

                }
            }

        } catch (IOException e) {
            sender.sendMessage(ERROR_COLOR + "Could not load image");
            e.printStackTrace();
        }

        return true;

    }

    double getBlockDiff(int red, int blue, int green, int[] block) {
        long rmean = ( (long)red + (long)block[0] ) / 2;
        long r = (long)red - (long)block[0];
        long g = (long)green - (long)block[1];
        long b = (long)blue - (long)block[2];
        return Math.sqrt((((512+rmean)*r*r)>>8) + 4*g*g + (((767-rmean)*b*b)>>8));
    }

}

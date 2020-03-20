package dev.nishappsucrose.coronacraft.commands;

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

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CreateRoom implements CommandExecutor {

    private static final Random rand = new Random();
    private static final ChatColor TEXT_COLOR = ChatColor.GOLD;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        sender.sendMessage(ChatColor.GOLD + "Generating room...");
        String roomId = generateRoomCode();
        WorldCreator wc = new WorldCreator(roomId);
        wc.generator(new EmptyChunkGenerator());
        wc.createWorld();

        Player roomCreator = (Player) sender;
        World room = Bukkit.getWorld(roomId);

        room.setSpawnLocation(0, 160, 0);
        room.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        room.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        room.setGameRule(GameRule.FALL_DAMAGE, false);
        room.setTime(6000);

        fillArea(room, -107, 80, -107, 107, 81, 107, Material.WHITE_CONCRETE);
        roomCreator.teleport(room.getSpawnLocation());
        WorldChat.sendWorldMessage(roomId, TEXT_COLOR
                + "Welcome to your CallCraft room.");
        WorldChat.sendWorldMessage(roomId, TEXT_COLOR
                + "Your room ID is "
                + ChatColor.BOLD
                + roomId
                + ChatColor.RESET
                + TEXT_COLOR + ".");
        WorldChat.sendWorldMessage(roomId, "" + TEXT_COLOR
                + ChatColor.UNDERLINE
                + "Go to https://coronacraft-0.web.app/room/"
                + roomId
                + ChatColor.RESET
                + ChatColor.GOLD
                + " to get started!"

        );

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

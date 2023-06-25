package dev.ghost.basicduels.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dev.ghost.basicduels.menusystem.PlayerMenuUtility;
import net.md_5.bungee.api.ChatColor;

public class Utils {

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendConsoleMessage(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(colorize(message));
    }

    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

    //Provide a player and return a menu system for that player
    //create one if they don't already have one
    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) { //See if the player has a playermenuutility "saved" for them

            //This player doesn't. Make one for them add add it to the hashmap
            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p); //Return the object by using the provided player
        }
    }

    public static File inputStreamToFile(InputStream input, String fileName) {
        try {
            File file = File.createTempFile(fileName, "");
            file.deleteOnExit();
    
            try (OutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            }
    
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return null;
    }


}

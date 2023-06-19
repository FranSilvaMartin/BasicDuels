package dev.ghost.basicduels.utils;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;

public class ColorUtils {

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendConsoleMessage(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(colorize(message));
    }
    
}

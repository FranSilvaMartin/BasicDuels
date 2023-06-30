package dev.ghost.basicduels;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.ChatColor;

import java.util.HashMap;

public class SpawnCommand implements CommandExecutor {

    private final Plugin plugin;
    private final HashMap<String, Long> cooldowns = new HashMap<>();

    public SpawnCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (BasicDuels.getInstance().getCoolDownManager().isPlayerInCooldown(player)) {
            player.sendMessage(ChatColor.RED + "You are in cooldown!");
            player.sendMessage(ChatColor.RED + "Time left: "
                    + BasicDuels.getInstance().getCoolDownManager().getCooldowns(player).toString());
            return true;
        }

        BasicDuels.getInstance().getCoolDownManager().addPlayerToMap(player, 10);

        player.sendMessage(ChatColor.GREEN + "Teleporting to spawn...");
        return true;
    }
}
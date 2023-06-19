package dev.ghost.basicduels.commands.kitpvp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dev.ghost.basicduels.BasicDuels;
import dev.ghost.basicduels.manager.command.CommandInfo;
import dev.ghost.basicduels.manager.command.CommandManager;

@CommandInfo(name = "finish", isSubCommand = true, desc = "Create an arena", perm = "kitpvp.commands.createarena", usage = "<name>", aliases = { "cancel" })
public class FinishArenaCommand extends CommandManager {

    public void onCommand(Player player, String[] args) {
        player.sendMessage("Duelo finalizado");
        BasicDuels.getInstance().duelManager.finishDuel(player);
    }

    public List<String> onTabComplete(Player p, String[] args) {
        if (args.length == 1) {
            Bukkit.getLogger().info("Getting suggestions");
            List<String> suggestions = new ArrayList<String>();
            return suggestions;
        }
        return null;
    }

}

package dev.ghost.basicduels.commands.kitpvp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dev.ghost.basicduels.manager.command.CommandInfo;
import dev.ghost.basicduels.manager.command.CommandManager;
import dev.ghost.basicduels.menusystem.menu.DuelMenu;
import dev.ghost.basicduels.utils.ColorUtils;

@CommandInfo(name = "send", isSubCommand = true, desc = "Create an arena", perm = "kitpvp.commands.createarena", usage = "<name>")
public class SendArenaCommand extends CommandManager {

    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            sendErrorMessage(player, "ARENA CREATE: Estas intentado");
            return;
        }

        // Check if args[0] is a player
        if (Bukkit.getPlayer(args[0]) == null) {
            sendErrorMessage(player, "Jugador no encontrado.");
            return;
        }

        new DuelMenu(ColorUtils.getPlayerMenuUtility(player), Bukkit.getPlayer(args[0])).open();
    }

    public List<String> onTabComplete(Player player, String[] args) {
        List<String> suggestions = new ArrayList<String>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            suggestions.add(p.getName());
        }
        return suggestions;
    }

}

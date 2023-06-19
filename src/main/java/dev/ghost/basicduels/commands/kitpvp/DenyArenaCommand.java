package dev.ghost.basicduels.commands.kitpvp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import dev.ghost.basicduels.BasicDuels;
import dev.ghost.basicduels.manager.command.CommandInfo;
import dev.ghost.basicduels.manager.command.CommandManager;

@CommandInfo(name = "deny", isSubCommand = true, desc = "Create an arena", perm = "kitpvp.commands.createarena", usage = "<name>")
public class DenyArenaCommand extends CommandManager {

    public void onCommand(Player player, String[] args) {
        if (args.length == 0 || !args[0].matches("[0-9]+")) {
            sendErrorMessage(player, "Duelo no encontrado o cancelado.");
            return;
        }

        Integer id = Integer.parseInt(args[0]);
        BasicDuels.getInstance().duelManager.denyDuelRequest(id);
    }

    public List<String> onTabComplete(Player player, String[] args) {
        List<String> suggestions = new ArrayList<String>();
        return suggestions;
    }

}

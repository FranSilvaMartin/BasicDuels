package dev.ghost.basicduels.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import dev.ghost.basicduels.BasicDuels;
import dev.ghost.basicduels.manager.ConfigManager;
import dev.ghost.basicduels.manager.command.CommandInfo;
import dev.ghost.basicduels.manager.command.CommandManager;

@CommandInfo(name = "accept", isSubCommand = true, desc = "Create an arena", perm = "kitpvp.commands.createarena", usage = "<name>")
public class AcceptArenaCommand extends CommandManager {

    /**
     * Acepta el duelo que le ha enviado un jugador, si el duelo no existe o ha sido
     * cancelado, se le notificará al jugador
     */
    public void onCommand(Player player, String[] args) {
        if (args.length == 0 || !args[0].matches("[0-9]+")) {
            sendErrorMessage(player, ConfigManager.getInstance().getMessage("duel_not_found", player));
            sendErrorMessage(player, ConfigManager.getInstance().getMessage("duel_test", player));
            return;
        }

        Integer id = Integer.parseInt(args[0]);
        BasicDuels.getInstance().duelManager.acceptDuelRequest(id);
    }

    /**
     * No hay sugerencias para este comando
     */
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> suggestions = new ArrayList<String>();
        return suggestions;
    }

}

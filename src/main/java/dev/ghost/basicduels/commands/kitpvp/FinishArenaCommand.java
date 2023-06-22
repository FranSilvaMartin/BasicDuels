package dev.ghost.basicduels.commands.kitpvp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import dev.ghost.basicduels.BasicDuels;
import dev.ghost.basicduels.manager.command.CommandInfo;
import dev.ghost.basicduels.manager.command.CommandManager;

@CommandInfo(name = "finish", isSubCommand = true, desc = "Create an arena", perm = "kitpvp.commands.createarena", usage = "<name>", aliases = {
        "cancel" })
public class FinishArenaCommand extends CommandManager {
    
    /*
     * Este comando se usa para finalizar un duelo
     */
    public void onCommand(Player player, String[] args) {
        sendMessage(player, "&aDuels have been finished!");
        BasicDuels.getInstance().duelManager.finishDuel(player);
    }

    /*
     * No hay sugerencias para este comando
     */
    public List<String> onTabComplete(Player p, String[] args) {
        List<String> suggestions = new ArrayList<String>();
        return suggestions;
    }

}

package dev.ghost.basicduels.commands.kitpvp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dev.ghost.basicduels.manager.command.CommandInfo;
import dev.ghost.basicduels.manager.command.CommandManager;
import dev.ghost.basicduels.menusystem.menu.DuelMenu;
import dev.ghost.basicduels.utils.Utils;

@CommandInfo(name = "send", isSubCommand = true, desc = "Create an arena", perm = "kitpvp.commands.createarena", usage = "<playername>")
public class SendArenaCommand extends CommandManager {

    /*
     * Este comando se usa para enviar un duelo a un jugador
     * Al ejecutar el comando se abre un menu para seleccionar el kit
     * y luego se envia el duelo al jugador
     */
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            sendErrorMessage(player, "/duel " + usageMessage);
            return;
        }

        // Comprueba si el jugador esta conectado
        if (Bukkit.getPlayer(args[0]) == null) {
            sendErrorMessage(player, "Jugador no encontrado.");
            return;
        }

        // Abre el menu para seleccionar el kit
        new DuelMenu(Utils.getPlayerMenuUtility(player), Bukkit.getPlayer(args[0])).open();
    }

    /*
     * Sugerencias para este comando
     * Sugerencias: Jugadores conectados
     */
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> suggestions = new ArrayList<String>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            suggestions.add(p.getName());
        }
        return suggestions;
    }

}

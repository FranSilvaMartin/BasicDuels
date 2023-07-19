package dev.ghost.basicduels.commands.kit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import dev.ghost.basicduels.manager.KitManager;
import dev.ghost.basicduels.manager.command.CommandInfo;
import dev.ghost.basicduels.manager.command.CommandManager;

@CommandInfo(name = "list", isSubCommand = true, desc = "List of kits", perm = "kitpvp.commands.createarena")
public class ListKitCommand extends CommandManager {

    /**
     * Acepta el duelo que le ha enviado un jugador, si el duelo no existe o ha sido
     * cancelado, se le notificará al jugador
     */
    public void onCommand(Player player, String[] args) {
        List<String> kitList = KitManager.getKitIds();
        if (kitList.size() == 0) {
            sendCustomMessage(player, "kits_not_found");
            return;
        }
        
        sendCustomMessage(player, "kit_list");
    }

    /**
     * No hay sugerencias para este comando
     */
    public List<String> onTabComplete(Player player, String[] args) {
        return null;
    }

}

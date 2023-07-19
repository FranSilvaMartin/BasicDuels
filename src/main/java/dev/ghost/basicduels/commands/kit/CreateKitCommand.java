package dev.ghost.basicduels.commands.kit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import dev.ghost.basicduels.manager.KitManager;
import dev.ghost.basicduels.manager.command.CommandInfo;
import dev.ghost.basicduels.manager.command.CommandManager;

@CommandInfo(name = "create", isSubCommand = true, desc = "Create kit", perm = "kitpvp.commands.createarena", usage = "<name>")
public class CreateKitCommand extends CommandManager {

    /**
     * Acepta el duelo que le ha enviado un jugador, si el duelo no existe o ha sido
     * cancelado, se le notificará al jugador
     */
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            sendErrorMessage(player, "Debes especificar un nombre para crear el kit");
            return;
        }

        String name = args[0];
        KitManager.createKit(player, name);
    }

    /**
     * No hay sugerencias para este comando
     */
    public List<String> onTabComplete(Player player, String[] args) {

        player.sendMessage("222");
        return null;
    }

}

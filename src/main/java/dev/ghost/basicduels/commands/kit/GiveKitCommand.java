package dev.ghost.basicduels.commands.kit;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.ghost.basicduels.manager.KitManager;
import dev.ghost.basicduels.manager.command.CommandInfo;
import dev.ghost.basicduels.manager.command.CommandManager;

@CommandInfo(name = "give", isSubCommand = true, desc = "Git a kit", perm = "kitpvp.commands.createarena", usage = "<name>")
public class GiveKitCommand extends CommandManager {

    /**
     * Acepta el duelo que le ha enviado un jugador, si el duelo no existe o ha sido
     * cancelado, se le notificará al jugador
     */
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            sendErrorMessage(player, "Debes especificar un nombre para obtener el kit");
            return;
        }

        String name = args[0];
        KitManager.giveKit(player, name);
    }

    /**
     * No hay sugerencias para este comando
     */
    public List<String> onTabComplete(Player player, String[] args) {
        return null;
    }

}

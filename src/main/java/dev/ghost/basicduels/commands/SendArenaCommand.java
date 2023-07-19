package dev.ghost.basicduels.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.ghost.basicduels.manager.ConfigManager;
import dev.ghost.basicduels.manager.KitManager;
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
            sendErrorMessage(player, ConfigManager.getInstance().getMessage("player_not_found", player));
            return;
        }

        Map<String, ItemStack> kits = KitManager.getKits();

        for (Map.Entry<String, ItemStack> kit : kits.entrySet()) {
            ItemStack item = kit.getValue();
            String name = kit.getKey();

            player.getInventory().addItem(item);
            player.updateInventory();

            player.sendMessage(name);
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

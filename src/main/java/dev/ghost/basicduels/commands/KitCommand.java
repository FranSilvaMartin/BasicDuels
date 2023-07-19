package dev.ghost.basicduels.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import dev.ghost.basicduels.commands.kit.CreateKitCommand;
import dev.ghost.basicduels.commands.kit.DeleteKitCommand;
import dev.ghost.basicduels.commands.kit.GiveKitCommand;
import dev.ghost.basicduels.commands.kit.ListKitCommand;
import dev.ghost.basicduels.manager.KitManager;
import dev.ghost.basicduels.manager.command.CommandInfo;
import dev.ghost.basicduels.manager.command.CommandManager;
import net.md_5.bungee.api.ChatColor;

@CommandInfo(name = "kit", isSubCommand = true, desc = "Kit manager", perm = "kitpvp.commands.createarena", usage = "<create, delete, give, list>")
public class KitCommand extends CommandManager {

    private List<CommandManager> subCommands = new ArrayList<CommandManager>();
    private CommandInfo commandInfo = getClass().getAnnotation(CommandInfo.class);

    // Constructor de la clase y añade los sub-comandos
    public KitCommand() {
        subCommands.add(new CreateKitCommand());
        subCommands.add(new DeleteKitCommand());
        subCommands.add(new GiveKitCommand());
        subCommands.add(new ListKitCommand());
    }

    // Ejecuta el comando
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("");
            for (CommandManager command : subCommands) {
                String name = ChatColor.GRAY + command.getName();
                String usage = ChatColor.GRAY + command.getUsage();
                String description = ChatColor.GRAY + command.getDescription();

                if (command.getUsage().equals("")) {
                    sendInfoMessage(player, "/duel " + commandInfo.name() + " " + name + " - " + description);
                } else {
                    sendInfoMessage(player, "/duel " + commandInfo.name() + " " + name + " " + usage + " - " + description);
                }
            }
            return;
        }

        CommandManager subCommand = null;
        for (CommandManager command : subCommands) {
            if (command.getName().equalsIgnoreCase(args[0])) {
                subCommand = command;
                break;
            }
        }

        if (subCommand == null) {
            sendMessage(player, "&cCould not find " + commandInfo.name() + " sub-command '" + args[0] + "'");
            return;
        }

        List<String> newArgs = new ArrayList<String>();
        Collections.addAll(newArgs, args);
        newArgs.remove(0);
        subCommand.onCommand(player, newArgs.toArray(new String[newArgs.size()]));
    }

    /*
     * Devuelve una lista de sugerencias para el comando
     */
    public List<String> onTabComplete(Player p, String[] args) {

        if (args.length == 2) {
            List<String> suggestions = new ArrayList<String>();
            for (CommandManager command : subCommands)
                suggestions.add(command.getName());
            return suggestions;
        }

        if (!args[1].equalsIgnoreCase("create")) {
            List<String> suggestions = KitManager.getKitIds();
            return suggestions;
        }
        return null;
    }

}

package dev.ghost.basicduels.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import dev.ghost.basicduels.manager.command.CommandInfo;
import dev.ghost.basicduels.manager.command.CommandManager;
import net.md_5.bungee.api.ChatColor;

@CommandInfo(name = "duel", desc = "All Duels commands", aliases = { "duels" }, usage = "<name>")
public class DuelCommand extends CommandManager {
    private List<CommandManager> subCommands = new ArrayList<CommandManager>();
    private CommandInfo commandInfo = getClass().getAnnotation(CommandInfo.class);

    // Constructor de la clase y añade los sub-comandos
    public DuelCommand() {
        subCommands.add(new SendArenaCommand());
        subCommands.add(new FinishArenaCommand());
        subCommands.add(new AcceptArenaCommand());
        subCommands.add(new DenyArenaCommand());
    }

    // Ejecuta el comando
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            for (CommandManager command : subCommands) {
                String name = ChatColor.GRAY + command.getName();
                String usage = ChatColor.GRAY + command.getUsage();
                String description = ChatColor.GRAY + command.getDescription();
                sendInfoMessage(player, "/" + commandInfo.name() + " " + name + " " + usage + " - " + description);
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
            sendMessage(player, "Could not find " + commandInfo.name() + " sub-command '" + args[0] + "'");
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
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<String>();
            for (CommandManager command : subCommands)
                suggestions.add(command.getName());
            return suggestions;
        }

        if (args.length > 1)
            for (CommandManager command : subCommands)
                if (command.getName().equalsIgnoreCase(args[0]))
                    return command.onTabComplete(p, args);

        return null;
    }
}
package dev.ghost.basicduels.manager.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.ghost.basicduels.manager.ConfigManager;
import dev.ghost.basicduels.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;

/**
 * Clase abstracta para manejar los comandos
 */
public abstract class CommandManager extends Command {

    private CommandInfo info = getClass().getAnnotation(CommandInfo.class);

    /**
     * Constructor de la clase
     */
    protected CommandManager() {
        super("");
        setName(info.name());
        setDescription(info.desc());
        setAliases(Arrays.asList(info.aliases()));
        setUsage(info.name() + " " + info.usage());
        permissionMessage(Component.text("You do not have permission to run this command."));
    }

    /**
     * Obtiene el nombre del comando
     * 
     * @return Nombre del comando
     */
    public String getName() {
        return info.name();
    }

    /**
     * Obtiene la descripción del comando
     * 
     * @return Descripción del comando
     */
    public String getDescription() {
        return info.desc();
    }

    /**
     * Obtiene el permiso del comando
     * 
     * @return Permiso del comando
     */
    public String getPermission() {
        return info.perm();
    }

    /**
     * Obtiene el uso del comando
     * 
     * @return Uso del comando
     */
    public String getUsage() {
        return info.usage();
    }

    /**
     * Obtiene los alias del comando
     * 
     * @return Lista de alias
     */
    public List<String> getAliases() {
        return Arrays.asList(info.aliases());
    }

    /**
     * Envia un mensaje al usuario sin color
     * 
     * @param sender   Usuario al que se le enviará el mensaje
     * @param messages Mensaje a enviar
     */
    public void sendMessage(CommandSender sender, String... messages) {
        for (String message : messages) {
            sender.sendMessage(Utils.colorize(message));
        }
    }

    /**
     * Envia un mensaje al usuario con el color azul, para indicar información
     * 
     * @param sender   El usuario al que se le enviará el mensaje
     * @param messages Mensaje a enviar
     */
    public void sendInfoMessage(CommandSender sender, String... messages) {
        for (String message : messages) {
            sender.sendMessage(ChatColor.AQUA + Utils.colorize(message));
        }
    }

    /**
     * Envia un mensaje al usuario con el color rojo, para indicar un error
     * 
     * @param sender   Usuario al que se le enviará el mensaje
     * @param messages Mensaje a enviar
     */
    public void sendErrorMessage(CommandSender sender, String... messages) {
        for (String message : messages) {
            sender.sendMessage(ChatColor.RED + Utils.colorize(message));
        }
    }

    /**
     * Método para ejecutar el comando
     * 
     * @param sender Usuario que ejecuta el comando
     * @param label  Nombre del comando
     * @param args   Argumentos del comando
     */
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Player player = (Player) sender;
            sendErrorMessage(sender, ConfigManager.getInstance().getMessage("only_players", player));
            return true;
        }

        if (!(sender.hasPermission(info.perm()))) {
            sendErrorMessage(sender, LegacyComponentSerializer.legacySection().serialize(permissionMessage()));
            return true;
        }

        Player player = (Player) sender;
        onCommand(player, args);
        return true;
    }

    /**
     * Método para completar el comando
     * 
     * @param sender Usuario que ejecuta el comando
     * @param label  Nombre del comando
     * @param args   Argumentos del comando
     * @return Lista de posibles completaciones
     */
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        List<String> completions = onTabComplete((Player) sender, args);
        if (completions == null)
            return new ArrayList<String>();
        return completions;
    }

    public abstract void onCommand(Player p, String[] args);
    public abstract List<String> onTabComplete(Player p, String[] args);
}

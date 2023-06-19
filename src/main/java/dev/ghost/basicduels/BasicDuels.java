package dev.ghost.basicduels;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import dev.ghost.basicduels.manager.command.CommandInfo;
import dev.ghost.basicduels.manager.command.CommandManager;
import dev.ghost.basicduels.manager.duel.Arena;
import dev.ghost.basicduels.manager.duel.DuelManager;
import dev.ghost.basicduels.utils.ColorUtils;

public class BasicDuels extends JavaPlugin {

    private static SimpleCommandMap simpleCommandMap;
    private PluginManager pluginManager;
    private static BasicDuels instance;
    public DuelManager duelManager;

    @Override
    public void onEnable() {
        instance = this;
        ColorUtils.sendConsoleMessage("&aStarting BasicDuels...");
        setupSimpleCommandMap();

        registerCommands();
        ColorUtils.sendConsoleMessage("");
        registerListeners();

        Location location = new Location(Bukkit.getWorld("world"), 3150, 63.00, 4106);
        Location location2 = new Location(Bukkit.getWorld("world"), 3162, 63.00, 4106);
        Arena arena = new Arena(1, "Oasis", location, location2);
        duelManager = new DuelManager(this);
        duelManager.addArena(arena);
    }

    @Override
    public void onDisable() {
        instance = this;
        ColorUtils.sendConsoleMessage("&cStopping BasicDuels...");
    }

    private void registerCommands() {
        ColorUtils.sendConsoleMessage("&3Registering commands....");
        String packageName = this.getClass().getPackage().getName();
        for (Class<?> clazz : new Reflections(packageName + ".commands").getSubTypesOf(CommandManager.class)) {
            try {
                Command command = (Command) clazz.getDeclaredConstructor().newInstance();
                CommandInfo commandInfo = clazz.getAnnotation(CommandInfo.class);

                if (commandInfo != null && !commandInfo.isSubCommand()) {
                    ColorUtils.sendConsoleMessage("&a + &8Command " + clazz.getSimpleName());
                    simpleCommandMap.register("pluginname", command);
                }
            } catch (Exception e) {
                ColorUtils.sendConsoleMessage("&cFailed to register command " + clazz.getSimpleName());
            }
        }
    }

    private void registerListeners() {
        ColorUtils.sendConsoleMessage("&3Registering listeners....");
        String packageName = this.getClass().getPackage().getName();
        for (Class<?> clazz : new Reflections(packageName + ".listeners").getSubTypesOf(Listener.class)) {
            try {
                Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();
                pluginManager.registerEvents(listener, this);
                ColorUtils.sendConsoleMessage("&a + &8Listener " + clazz.getSimpleName());
            } catch (Exception e) {
                ColorUtils.sendConsoleMessage("&cFailed to register listener " + clazz.getSimpleName());
            }
        }
    }

    private void setupSimpleCommandMap() {
        pluginManager = this.getServer().getPluginManager();
        Field commandMapField;
        try {
            commandMapField = pluginManager.getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(pluginManager);
            simpleCommandMap = (SimpleCommandMap) commandMap;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static SimpleCommandMap getCommandMap() {
        return simpleCommandMap;
    }

    public static BasicDuels getInstance() {
        return instance;
    }

}

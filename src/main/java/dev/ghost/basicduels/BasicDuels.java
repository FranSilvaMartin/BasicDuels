package dev.ghost.basicduels;

import java.io.File;
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

import dev.ghost.basicduels.manager.ConfigManager;
import dev.ghost.basicduels.manager.command.CommandInfo;
import dev.ghost.basicduels.manager.command.CommandManager;
import dev.ghost.basicduels.manager.duel.Arena;
import dev.ghost.basicduels.manager.duel.DuelManager;
import dev.ghost.basicduels.utils.Utils;
import dev.ghost.basicduels.utils.PlayerSavings;

public class BasicDuels extends JavaPlugin {

    private static SimpleCommandMap simpleCommandMap;
    private PluginManager pluginManager;
    private static BasicDuels instance;
    public DuelManager duelManager;

    @Override
    public void onEnable() {
        instance = this;
        Utils.sendConsoleMessage("&aStarting BasicDuels...");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlayerSavings(this);
            setupSimpleCommandMap();
            registerConfig();

            registerCommands();
            registerListeners();

            Location location = new Location(Bukkit.getWorld("world"), 3150, 63.00, 4106);
            Location location2 = new Location(Bukkit.getWorld("world"), 3162, 63.00, 4106);
            Arena arena = new Arena(1, "Oasis", location, location2);
            duelManager = new DuelManager(this);
            duelManager.addArena(arena);
            Utils.sendConsoleMessage("&aBasicDuels is ready");
        } else {
            getLogger().severe("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        instance = this;
        Utils.sendConsoleMessage("&cStopping BasicDuels...");
    }

    private void registerCommands() {
        String packageName = this.getClass().getPackage().getName();
        String commands = "";
        for (Class<?> clazz : new Reflections(packageName + ".commands").getSubTypesOf(CommandManager.class)) {
            try {
                Command command = (Command) clazz.getDeclaredConstructor().newInstance();
                CommandInfo commandInfo = clazz.getAnnotation(CommandInfo.class);

                if (commandInfo != null && !commandInfo.isSubCommand()) {
                    commands += clazz.getSimpleName() + ", ";
                    simpleCommandMap.register("pluginname", command);
                }
            } catch (Exception e) {
                Utils.sendConsoleMessage("&cFailed to register command " + clazz.getSimpleName());
            }
        }
        Utils.sendConsoleMessage("  ⚙️ &3Registering commands: &8" + commands);

    }

    private void registerListeners() {
        String packageName = this.getClass().getPackage().getName();
        String listeners = "";
        for (Class<?> clazz : new Reflections(packageName + ".listeners").getSubTypesOf(Listener.class)) {
            try {
                Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();
                pluginManager.registerEvents(listener, this);

                listeners += clazz.getSimpleName() + ", ";
            } catch (Exception e) {
                Utils.sendConsoleMessage("&cFailed to register listener " + clazz.getSimpleName());
            }
        }

        Utils.sendConsoleMessage("  🎉 &3Registering listeners: &8" + listeners);
    }

    private void registerConfig() {
        ConfigManager.getInstance().setPlugin(this);
        ConfigManager.getInstance().getConfig("config.yml");

        File languageFolder = new File(this.getDataFolder() + File.separator + "language");
        if (!languageFolder.exists()) {
            languageFolder.mkdirs();
        }

        File[] languageFiles = languageFolder.listFiles();
        String[] languages = { "en.yml", "es.yml" };

        for (String lang : languages) {
            ConfigManager.getInstance().getConfig("language" + File.separator + lang);
        }

        String list = "";
        for (File lang : languageFiles) {
            ConfigManager.getInstance().getConfig("language" + File.separator + lang.getName());
            String[] name = lang.getName().split("\\.");

            list += name[0] + ", ";
        }

        Utils.sendConsoleMessage("  🚩 &3Loading languages: &8" + list);
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

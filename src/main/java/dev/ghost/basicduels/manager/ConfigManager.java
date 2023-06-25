package dev.ghost.basicduels.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev.ghost.basicduels.utils.Utils;
import me.clip.placeholderapi.PlaceholderAPI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// player.sendMessage(ConfigManager.getInstance().getString(ConfigManager.getInstance().getConfig("config.yml"), "language", player));
// player.sendMessage(ConfigManager.getInstance().getMessage("broadcast.announce", player));

public class ConfigManager {
    private static ConfigManager single_inst = null;

    private ArrayList<FileConfiguration> customConfigs = new ArrayList<FileConfiguration>();
    private ArrayList<String> configNames = new ArrayList<String>();
    private Plugin plugin = null;

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public FileConfiguration getConfig(String name) {
        if (customConfigs.size() > 0) {
            for (FileConfiguration conf : customConfigs) {
                if (conf.getName().equalsIgnoreCase(name)) {
                    return conf;
                }
            }
        }

        return createNewCustomConfig(name);
    }

    public void reloadConfigs() {
        customConfigs.clear();
        configNames.clear();
    }

    private FileConfiguration createNewCustomConfig(String name) {
        FileConfiguration fileConfiguration;
        File configFile = new File(plugin.getDataFolder(), name);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try {
                plugin.saveResource(name, false);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        fileConfiguration = new YamlConfiguration();
        try {
            fileConfiguration.load(configFile);
            customConfigs.add(fileConfiguration);
            configNames.add(name);
            return fileConfiguration;
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Returns true if saved successfully, returns false in case of error and prints
    // error to console
    public boolean setData(FileConfiguration conf, String path, Object data) {
        conf.set(path, data);
        return saveData(conf);
    }

    private boolean saveData(FileConfiguration conf) {
        try {
            conf.save(new File(plugin.getDataFolder(), configNames.get(customConfigs.indexOf(conf))));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Remove if not using placeholderAPI
    public String getString(FileConfiguration conf, String path, Player player) {
        // Create dummy if not available
        if (!conf.contains(path)) {
            setData(conf, path, "dummy_string");
        }

        if (conf == null || conf.getString(path) == null || conf.getString(path).equalsIgnoreCase("dummy_string")) {
            Utils.sendConsoleMessage("&cError in variable: " + path + " in config" + ".yml");
            Utils.sendConsoleMessage("&cIf it is not possible to solve it, it is recommended to delete this file");
            return Utils.colorize(
                    PlaceholderAPI.setPlaceholders(player, "&cCheck config files, error found in variable: " + path
                            + " in config" + ".yml"));
        }

        return Utils.colorize(PlaceholderAPI.setPlaceholders(player, conf.getString(path)));
    }

    public String getMessage(String path, Player player) {
        FileConfiguration conf = getConfig("config.yml");
        String language = getStringRaw(conf, "language") == null ? "en" : getStringRaw(conf, "language");
        FileConfiguration confLang = getConfig("language/" + language + ".yml");

        // Create dummy if not available
        if (!confLang.contains(path)) {
            setData(confLang, path, "dummy_string");
        }

        if (confLang == null || confLang.getString(path) == null
                || confLang.getString(path).equalsIgnoreCase("dummy_string")) {
            Utils.sendConsoleMessage("&c" + language + ".yml is missing or error in variable: " + path + " in "
                    + language + ".yml");
            Utils.sendConsoleMessage("&cIf it is not possible to solve it, it is recommended to delete this file");
            return Utils.colorize(
                    PlaceholderAPI.setPlaceholders(player, "&cCheck config files, error found in variable: " + path
                            + " in " + language + ".yml"));
        }

        return PlaceholderAPI.setPlaceholders(player, confLang.getString(path));
    }

    public String getStringRaw(FileConfiguration conf, String path) {
        // Create dummy if not available
        if (!conf.contains(path)) {
            setData(conf, path, "dummy_string");
        }
        return Utils.colorize(conf.getString(path));
    }

    public int getInt(FileConfiguration conf, String path) {
        // Create dummy if not available
        if (!conf.contains(path)) {
            setData(conf, path, 1);
        }
        return conf.getInt(path);
    }

    public double getDouble(FileConfiguration conf, String path) {
        // Create dummy if not available
        if (!conf.contains(path)) {
            setData(conf, path, 1.0);
        }
        return conf.getDouble(path);
    }

    public boolean getBoolean(FileConfiguration conf, String path) {
        // Create dummy if not available
        if (!conf.contains(path)) {
            setData(conf, path, false);
        }
        return conf.getBoolean(path);
    }

    public List<?> getList(FileConfiguration conf, String path) {
        // Create dummy list if not available
        if (!conf.contains(path)) {
            setData(conf, path, new ArrayList<Location>().add(new Location(Bukkit.getWorld("world"), 10, 10, 10)));
        }
        return conf.getList(path);
    }

    public boolean addLocation(FileConfiguration conf, Location location, String path) {
        conf.set(String.format("%s.world", path), location.getWorld().getName());
        conf.set(String.format("%s.x", path), location.getX());
        conf.set(String.format("%s.y", path), location.getY());
        conf.set(String.format("%s.z", path), location.getZ());

        return saveData(conf);
    }

    public Location getLocation(FileConfiguration conf, String path) {
        String worldName = getStringRaw(conf, String.format("%s.world", path));
        Bukkit.getServer().createWorld(new WorldCreator(worldName));

        World world = Bukkit.getWorld(worldName);
        int x = getInt(conf, String.format("%s.x", path));
        int y = getInt(conf, String.format("%s.y", path));
        int z = getInt(conf, String.format("%s.z", path));

        return new Location(world, x, y, z);
    }

    private ConfigManager() {
    }

    public static ConfigManager getInstance() {
        if (single_inst == null) {
            single_inst = new ConfigManager();
        }
        return single_inst;
    }
}

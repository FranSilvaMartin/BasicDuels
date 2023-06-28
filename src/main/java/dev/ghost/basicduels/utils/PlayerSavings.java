package dev.ghost.basicduels.utils;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import dev.ghost.basicduels.exceptions.SingletonException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * This class allow us to save the player data (location + inventory + armor) to
 * a file when he join a game or a
 * task, in this way, we can easily retrieve it once the user finish his task
 * and in case of crash/leave, he still
 * can retrieve his stuff.
 */
public class PlayerSavings {
    private static PlayerSavings instance;
    private JavaPlugin plugin;
    private File folder;

    public static PlayerSavings getInstance() {
        return instance;
    }

    public PlayerSavings(JavaPlugin plugin) {
        if (instance != null)
            throw new SingletonException(this.getClass().getName());
        instance = this;
        this.plugin = plugin;
        initFolder();
    }

    /**
     * This method allow us to init the folder containing the player-data, it also
     * create it if it does not exists.
     */
    public void initFolder() {
        folder = new File(plugin.getDataFolder(), "player-data");
        if (!folder.exists())
            folder.mkdirs();
    }

    /**
     * This function allow us to get the File object pointing the the configs of the
     * player
     * 
     * @param player The player to get the file from
     * @return The File object associated to the configs of the player (does not
     *         check anything).
     */
    private File getPlayerFile(Player player) {
        return new File(folder, player.getUniqueId().toString());
    }

    /**
     * This method allow us to get the YamlConfiguration of a player or null if no
     * config is found
     * 
     * @param p The player to get the configuration from
     * @return The config of the player or null if no config is found or there is an
     *         error
     */
    private FileConfiguration getPlayerConfig(Player p) {
        File playerFile = getPlayerFile(p);
        if (!playerFile.exists())
            return null;

        YamlConfiguration playerConfig = new YamlConfiguration();
        try {
            playerConfig.load(playerFile);
            return playerConfig;
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method allow us to save the configs of a player
     * 
     * @param p      The player to save the config from
     * @param config The config that has to be saved
     */
    private void save(Player p, FileConfiguration config) {
        File playerFile = getPlayerFile(p);
        try {
            config.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method allow us to delete the file of the player if it exists
     * 
     * @param p The player to remove the file from
     */
    private void removeConfigs(Player p) {
        File playerFile = getPlayerFile(p);
        if (playerFile.exists())
            playerFile.delete();
    }

    /**
     * This method allow us to save the location, inventory, armor, health, hunger
     * and gamemode of a player.
     * Call this method before teleporting the player inside of a new game
     * 
     * @param p The player to save the stuff of.
     */
    public void savePlayer(Player p) {
        YamlConfiguration playerConfig = new YamlConfiguration();
        playerConfig.set("location", LocationUtils.locToConfigSection(p.getLocation()));
        playerConfig.set("inventory", p.getInventory().getContents());
        playerConfig.set("health", p.getHealth());
        playerConfig.set("hunger", p.getFoodLevel());
        playerConfig.set("gamemode", p.getGameMode().toString());
        playerConfig.set("exp", p.getTotalExperience());
        save(p, playerConfig);
    }

    /**
     * This function restore the location, inventory and armor of the player.
     * It actually does more, it also checks if the player is connected if not it
     * does nothing.
     * It also remove the save of the player from configs
     * And it check if the player has something saved otherwise it does nothing
     * 
     * @param p The player to restore the location, inventory and armor from
     */
    public void restorePlayer(Player p) {
        if (Bukkit.getPlayerExact(p.getName()) == null)
            return;

        FileConfiguration playerConfig = getPlayerConfig(p);
        if (playerConfig == null)
            return;

        Location playerLoc = LocationUtils.configSectionToLocation(playerConfig.getConfigurationSection("location"));
        List<ItemStack> contentList = (List<ItemStack>) playerConfig.get("inventory");
        ItemStack[] inventoryContent = contentList.toArray(new ItemStack[contentList.size()]);
        double health = playerConfig.getDouble("health");
        int hunger = playerConfig.getInt("hunger");
        GameMode gameMode = GameMode.valueOf(playerConfig.getString("gamemode"));
        int exp = playerConfig.getInt("exp");

        p.teleport(playerLoc);
        p.getInventory().setContents(inventoryContent);
        p.setHealth(health);
        p.setFoodLevel(hunger);
        p.setGameMode(gameMode);
        p.setTotalExperience(exp);

        removeConfigs(p);
    }

    /**
     * This function restore the location, inventory and armor of the player.
     * It actually does more, it also checks if the player is connected if not it
     * does nothing.
     * It also remove the save of the player from configs
     * And it check if the player has something saved otherwise it does nothing
     * 
     * @param p The player to restore the location, inventory and armor from
     */
    public void restoreWithoutItemsPlayer(Player p) {
        if (Bukkit.getPlayerExact(p.getName()) == null)
            return;

        FileConfiguration playerConfig = getPlayerConfig(p);
        if (playerConfig == null)
            return;

        Location playerLoc = LocationUtils.configSectionToLocation(playerConfig.getConfigurationSection("location"));
        double health = playerConfig.getDouble("health");
        int hunger = playerConfig.getInt("hunger");
        GameMode gameMode = GameMode.valueOf(playerConfig.getString("gamemode"));
        int exp = playerConfig.getInt("exp");

        p.teleport(playerLoc);
        p.setHealth(health);
        p.setFoodLevel(hunger);
        p.setGameMode(gameMode);
        p.setTotalExperience(exp);

        removeConfigs(p);
    }
}

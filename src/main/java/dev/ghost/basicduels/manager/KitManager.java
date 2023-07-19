package dev.ghost.basicduels.manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import dev.ghost.basicduels.BasicDuels;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KitManager {

    private BasicDuels plugin;

    /**
     * Our constructor.
     *
     * @param plugin
     */
    public KitManager(BasicDuels plugin) {
        this.plugin = plugin;
    }

    public static void createKit(Player player, String kitName) {
        ConfigManager configManager = ConfigManager.getInstance();
        FileConfiguration config = configManager.getConfig("kits.yml");
        PlayerInventory inventory = player.getInventory();

        player.sendMessage(ChatColor.GREEN + "Creating kit " + kitName + "...");

        String path = "kits." + kitName + ".";
        if (config.getConfigurationSection(path) != null) {
            player.sendMessage(kitName + " already exists!");
            return;
        }

        config.createSection(path + "items");

        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = inventory.getItem(i);

            if (itemStack == null || itemStack.getType() == Material.AIR)
                continue;

            String slotPath = path + "items." + i;
            setItemData(config, slotPath, itemStack);
        }

        setArmorData(config, path + "armor", inventory);

        ItemStack iconItem = inventory.getItemInHand();
        String iconPath = path + "icon.";
        if (iconItem != null && iconItem.getType() != Material.AIR) {
            setItemData(config, iconPath, iconItem);
        } else {
            configManager.setData(config, iconPath + "type", "diamond_sword");
            configManager.setData(config, iconPath + "amount", 1);
            configManager.setData(config, iconPath + "name", "&a" + kitName);
            // Agregarle un lore
            List<String> lore = new ArrayList<String>();
            lore.add(ChatColor.GRAY + "Kit: " + ChatColor.GREEN + kitName);
            lore.add(ChatColor.GRAY + "Click to select this kit.");
            configManager.setData(config, iconPath + "lore", lore);
        }

        player.sendMessage(ChatColor.GREEN + "Kit icon set to item in hand." + ChatColor.GRAY
                + " (If you want to change it, just hold the item you want to be the icon and type /kit seticon "
                + kitName + ")");
        player.sendMessage(ChatColor.GREEN + "Kit " + kitName + " has been created!");
    }

    private static void setItemData(FileConfiguration config, String path, ItemStack itemStack) {
        ConfigManager configManager = ConfigManager.getInstance();
        configManager.setData(config, path + ".type", itemStack.getType().toString().toLowerCase());
        configManager.setData(config, path + ".amount", itemStack.getAmount());

        if (itemStack.hasItemMeta()) {
            if (itemStack.getItemMeta().hasDisplayName())
                configManager.setData(config, path + ".name", itemStack.getItemMeta().getDisplayName());

            if (itemStack.getItemMeta().hasLore())
                configManager.setData(config, path + ".lore", itemStack.getItemMeta().getLore());

            if (itemStack.getItemMeta().hasEnchants()) {
                List<String> enchantList = new ArrayList<String>();
                for (Map.Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
                    Enchantment enchantment = entry.getKey();
                    int level = entry.getValue();
                    enchantList.add(enchantment.getName().toLowerCase() + ":" + level);
                }
                configManager.setData(config, path + ".enchants", enchantList);
            }
        }
    }

    private static void setArmorData(FileConfiguration config, String path, PlayerInventory inventory) {
        ConfigManager configManager = ConfigManager.getInstance();
        ItemStack helmet = inventory.getHelmet();
        if (helmet != null) {
            configManager.setData(config, path + ".helmet", helmet.getType().toString().toLowerCase());

            if (helmet.getItemMeta().hasDisplayName()) {
                configManager.setData(config, path + ".helmet-name", helmet.getItemMeta().getDisplayName());
            }

            if (helmet.getItemMeta().hasLore()) {
                configManager.setData(config, path + ".helmet-lore", helmet.getItemMeta().getLore());
            }

            setEnchantData(config, path + ".helmet-enchants", helmet.getEnchantments());
        } else {
            configManager.setData(config, path + ".helmet", "air");
        }

        ItemStack chestplate = inventory.getChestplate();
        if (chestplate != null) {
            configManager.setData(config, path + ".chestplate", chestplate.getType().toString().toLowerCase());

            if (chestplate.getItemMeta().hasDisplayName()) {
                configManager.setData(config, path + ".chestplate-name", chestplate.getItemMeta().getDisplayName());
            }

            if (chestplate.getItemMeta().hasLore()) {
                configManager.setData(config, path + ".chestplate-lore", chestplate.getItemMeta().getLore());
            }

            setEnchantData(config, path + ".chestplate-enchants", chestplate.getEnchantments());
        } else {
            configManager.setData(config, path + ".chestplate", "air");
        }

        ItemStack leggings = inventory.getLeggings();
        if (leggings != null) {
            configManager.setData(config, path + ".leggings", leggings.getType().toString().toLowerCase());

            if (leggings.getItemMeta().hasDisplayName()) {
                configManager.setData(config, path + ".leggings-name", leggings.getItemMeta().getDisplayName());
            }

            if (leggings.getItemMeta().hasLore()) {
                configManager.setData(config, path + ".leggings-lore", leggings.getItemMeta().getLore());
            }

            setEnchantData(config, path + ".leggings-enchants", leggings.getEnchantments());
        } else {
            configManager.setData(config, path + ".leggings", "air");
        }

        ItemStack boots = inventory.getBoots();
        if (boots != null) {
            configManager.setData(config, path + ".boots", boots.getType().toString().toLowerCase());

            if (boots.getItemMeta().hasDisplayName()) {
                configManager.setData(config, path + ".boots-name", boots.getItemMeta().getDisplayName());
            }

            if (boots.getItemMeta().hasLore()) {
                configManager.setData(config, path + ".boots-lore", boots.getItemMeta().getLore());
            }

            setEnchantData(config, path + ".boots-enchants", boots.getEnchantments());
        } else {
            configManager.setData(config, path + ".boots", "air");
        }
    }

    private static void setEnchantData(FileConfiguration config, String path, Map<Enchantment, Integer> enchantments) {
        ConfigManager configManager = ConfigManager.getInstance();
        List<String> enchantList = new ArrayList<String>();
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int level = entry.getValue();
            enchantList.add(enchantment.getName().toLowerCase() + ":" + level);
        }
        configManager.setData(config, path, enchantList);
    }

    public static void giveKit(Player player, String kitName) {
        ConfigManager configManager = ConfigManager.getInstance();
        FileConfiguration config = configManager.getConfig("kits.yml");

        if (config.getConfigurationSection("kits." + kitName) == null) {
            player.sendMessage(kitName + " does not exist!");
            return;
        }

        PlayerInventory inventory = player.getInventory();
        inventory.clear();

        String path = "kits." + kitName + ".";
        ConfigurationSection itemSection = config.getConfigurationSection(path + "items");

        if (itemSection == null) {
            return;
        }

        for (String slotString : itemSection.getKeys(false)) {
            int slot = Integer.parseInt(slotString);

            if (slot < 0 || slot > 35) {
                continue;
            }

            String itemPath = path + "items." + slot + ".";
            ItemStack itemStack = createItemFromConfig(config, itemPath);
            inventory.setItem(slot, itemStack);
        }

        setArmorFromConfig(config, path + "armor", inventory);

        player.updateInventory();
    }

    private static ItemStack createItemFromConfig(FileConfiguration config, String itemPath) {
        ConfigManager configManager = ConfigManager.getInstance();
        String itemType = config.getString(itemPath + "type");
        String itemName = config.getString(itemPath + "name");
        List<String> itemLore = config.getStringList(itemPath + "lore");
        List<String> itemEnchants = config.getStringList(itemPath + "enchants");
        int itemAmount = config.getInt(itemPath + "amount");

        ItemStack itemStack = new ItemStack(Material.matchMaterial(itemType.toUpperCase()), itemAmount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            if (itemName != null) {
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
            }

            if (itemLore != null) {
                List<String> translatedLore = itemLore.stream()
                        .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                        .collect(Collectors.toList());
                itemMeta.setLore(translatedLore);
            }

            if (itemEnchants != null) {
                for (String enchant : itemEnchants) {
                    String[] parts = enchant.split(":");
                    String enchantName = parts[0].toUpperCase();
                    int enchantLevel = Integer.parseInt(parts[1]);
                    itemMeta.addEnchant(Enchantment.getByName(enchantName), enchantLevel, true);
                }
            }

            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    public static ItemStack getKitIcon(String kitName) {
        ConfigManager configManager = ConfigManager.getInstance();
        FileConfiguration config = configManager.getConfig("kits.yml");

        String path = "kits." + kitName + ".icon.";
        String itemType = config.getString(path + "type");
        String itemName = config.getString(path + "name");
        List<String> itemLore = config.getStringList(path + "lore");
        List<String> itemEnchants = config.getStringList(path + "enchants");
        int itemAmount = config.getInt(path + "amount");    

        Bukkit.getLogger().info("itemType: " + itemType);
        Bukkit.getLogger().info("itemName: " + itemName);

        ItemStack itemStack = new ItemStack(Material.matchMaterial(itemType), itemAmount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            if (itemName != null) {
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
            }

            if (itemLore != null) {
                List<String> translatedLore = itemLore.stream()
                        .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                        .collect(Collectors.toList());
                itemMeta.setLore(translatedLore);
            }

            if (itemEnchants != null) {
                for (String enchant : itemEnchants) {
                    String[] parts = enchant.split(":");
                    String enchantName = parts[0].toUpperCase();
                    int enchantLevel = Integer.parseInt(parts[1]);
                    itemMeta.addEnchant(Enchantment.getByName(enchantName), enchantLevel, true);
                }
            }

            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    private static void setArmorFromConfig(FileConfiguration config, String path, PlayerInventory inventory) {
        ConfigManager configManager = ConfigManager.getInstance();
        String helmetType = config.getString(path + ".helmet", "AIR").toUpperCase();
        String chestplateType = config.getString(path + ".chestplate", "AIR").toUpperCase();
        String leggingsType = config.getString(path + ".leggings", "AIR").toUpperCase();
        String bootsType = config.getString(path + ".boots", "AIR").toUpperCase();

        ItemStack helmet = new ItemStack(Material.matchMaterial(helmetType));
        ItemStack chestplate = new ItemStack(Material.matchMaterial(chestplateType));
        ItemStack leggings = new ItemStack(Material.matchMaterial(leggingsType));
        ItemStack boots = new ItemStack(Material.matchMaterial(bootsType));

        setItemLoreFromConfig(config, path + ".helmet-lore", helmet);
        setItemLoreFromConfig(config, path + ".chestplate-lore", chestplate);
        setItemLoreFromConfig(config, path + ".leggings-lore", leggings);
        setItemLoreFromConfig(config, path + ".boots-lore", boots);

        setDisplayNameFromConfig(config, path + ".helmet-name", helmet);
        setDisplayNameFromConfig(config, path + ".chestplate-name", chestplate);    
        setDisplayNameFromConfig(config, path + ".leggings-name", leggings);
        setDisplayNameFromConfig(config, path + ".boots-name", boots);

        setItemEnchantsFromConfig(config, path + ".helmet-enchants", helmet);
        setItemEnchantsFromConfig(config, path + ".chestplate-enchants", chestplate);
        setItemEnchantsFromConfig(config, path + ".leggings-enchants", leggings);
        setItemEnchantsFromConfig(config, path + ".boots-enchants", boots);

        if (inventory.getHelmet() == null) {
            inventory.setHelmet(helmet);
        } else {
            inventory.addItem(helmet);
        }

        if (inventory.getChestplate() == null) {
            inventory.setChestplate(chestplate);
        } else {
            inventory.addItem(chestplate);
        }

        if (inventory.getLeggings() == null) {
            inventory.setLeggings(leggings);
        } else {
            inventory.addItem(leggings);
        }

        if (inventory.getBoots() == null) {
            inventory.setBoots(boots);
        } else {
            inventory.addItem(boots);
        }
    }

    private static void setItemLoreFromConfig(FileConfiguration config, String path, ItemStack itemStack) {
        ConfigManager configManager = ConfigManager.getInstance();
        List<String> itemLore = config.getStringList(path);

        if (itemLore != null && itemStack.getType() != Material.AIR) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(itemLore);
            itemStack.setItemMeta(itemMeta);
        }
    }

        private static void setDisplayNameFromConfig(FileConfiguration config, String path, ItemStack itemStack) {
        String itemName = config.getString(path);

        if (itemStack.getType() != Material.AIR) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(itemName);
            itemStack.setItemMeta(itemMeta);
        }
    }

    private static void setItemEnchantsFromConfig(FileConfiguration config, String path, ItemStack itemStack) {
        ConfigManager configManager = ConfigManager.getInstance();
        List<String> itemEnchants = config.getStringList(path);

        if (itemEnchants != null) {
            for (String enchant : itemEnchants) {
                String[] parts = enchant.split(":");
                String enchantName = parts[0].toUpperCase();
                int enchantLevel = Integer.parseInt(parts[1]);
                itemStack.addUnsafeEnchantment(Enchantment.getByName(enchantName), enchantLevel);
            }
        }
    }

    public static void deleteKit(Player p, String kitName) {
        ConfigManager configManager = ConfigManager.getInstance();
        FileConfiguration config = configManager.getConfig("kits.yml");

        if (config.getConfigurationSection("kits." + kitName) == null) {
            p.sendMessage(kitName + " does not exist!");
            return;
        }

        configManager.setData(config, "kits." + kitName, null);
        p.sendMessage(ChatColor.GREEN + "Kit " + kitName + " has been deleted!");
    }

    public static List<String> getKitIds() {
        ConfigManager configManager = ConfigManager.getInstance();
        FileConfiguration config = configManager.getConfig("kits.yml");

        ConfigurationSection kitsSection = config.getConfigurationSection("kits");
        List<String> kitIds = new ArrayList<>();

        if (kitsSection != null) {
            kitIds.addAll(kitsSection.getKeys(false));
        }

        return kitIds;
    }

    // Obtener los kits y su icono
    public static Map<String, ItemStack> getKits() {
        ConfigManager configManager = ConfigManager.getInstance();
        FileConfiguration config = configManager.getConfig("kits.yml");

        Map<String, ItemStack> kits = new HashMap<>();
        List<String> kitIds = getKitIds();

        for (String kitId : kitIds) {
            ItemStack itemStack = getKitIcon(kitId);
            kits.put(kitId, itemStack);
        }

        return kits;
    }

    public BasicDuels getPlugin() {
        return plugin;
    }

}

package dev.ghost.basicduels.menusystem.menu;

import dev.ghost.basicduels.BasicDuels;
import dev.ghost.basicduels.manager.duel.DuelGamemode;
import dev.ghost.basicduels.menusystem.PaginatedMenu;
import dev.ghost.basicduels.menusystem.PlayerMenuUtility;
import dev.ghost.basicduels.utils.Utils;
import net.kyori.adventure.text.Component;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class DuelMenu extends PaginatedMenu {

    private Player target;

    public DuelMenu(PlayerMenuUtility playerMenuUtility, Player target) {
        super(playerMenuUtility);
        this.target = target;
    }

    @Override
    public String getMenuName() {
        return "Select the type of duel";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        String itemName = e.getCurrentItem().getItemMeta().displayName().toString().trim().replaceAll(" ", "");
        for (DuelGamemode duelGamemode : DuelGamemode.values()) {
            if (itemName.contains(duelGamemode.name().trim())) {
                BasicDuels.getInstance().duelManager.sendDuelRequest(player, target, duelGamemode);
            }
        }

        player.closeInventory();
    }

    @Override
    public void setMenuItems() {

        ItemStack duelDiamond = new ItemStack(Material.DIAMOND, 1);
        ItemMeta duelDiamond_meta = duelDiamond.getItemMeta();
        duelDiamond_meta.displayName(Component.text(Utils.colorize("&aDiamond")));
        ArrayList<Component> duelDiamond_lore = new ArrayList<>();
        duelDiamond_lore.add(Component.text(Utils.colorize("&7Click to duel with a diamond sword")));
        duelDiamond_lore.add(Component.text(Utils.colorize("&7and a full set of diamond armor")));
        duelDiamond_lore.add(Component.text(Utils.colorize("")));
        duelDiamond_lore.add(Component.text(Utils.colorize("&7Good luck and enjoy the fight")));
        duelDiamond_meta.lore(duelDiamond_lore);
        duelDiamond.setItemMeta(duelDiamond_meta);

        ItemStack duelLoss = new ItemStack(Material.ENDER_CHEST, 1);
        ItemMeta duelLoss_meta = duelLoss.getItemMeta();
        duelLoss_meta.displayName(Component.text(Utils.colorize("&aLoss Fight")));
        ArrayList<Component> duelLoss_lore = new ArrayList<>();
        duelLoss_lore.add(Component.text(Utils.colorize("&7A fight where people compete")));
        duelLoss_lore.add(Component.text(Utils.colorize("&7to protect their belongings,")));
        duelLoss_lore.add(Component.text(Utils.colorize("&7but the loser has to give")));
        duelLoss_lore.add(Component.text(Utils.colorize("&7them up to the winner.")));
        duelLoss_lore.add(Component.text(Utils.colorize("")));
        duelLoss_lore.add(Component.text(Utils.colorize("&7Good luck and enjoy the fight")));
        duelLoss_meta.lore(duelLoss_lore);
        duelLoss.setItemMeta(duelLoss_meta);

        ItemStack information = new ItemStack(Material.BOOK, 1);
        ItemMeta information_meta = information.getItemMeta();
        information_meta.displayName(Component.text(Utils.colorize("&3Information")));
        ArrayList<Component> info_lore = new ArrayList<>();
        info_lore.add(Component.text(Utils.colorize("&7Select a player to duel")));
        info_lore.add(Component.text(Utils.colorize("&7You can choose the mode you want")));
        info_lore.add(Component.text(Utils.colorize("")));
        info_lore.add(Component.text(Utils.colorize("&7You can only duel with one player")));
        info_lore.add(Component.text(Utils.colorize("&7at a time, once the duel has started,")));
        info_lore.add(Component.text(Utils.colorize("&7you will be teleported to an arena")));
        info_lore.add(Component.text(Utils.colorize("&7and you will have to fight to the death")));
        information_meta.lore(info_lore);
        information.setItemMeta(information_meta);

        inventory.setItem(0, duelLoss);
        inventory.setItem(1, duelDiamond);
        inventory.setItem(getSlots() - 1, information);
    }
}

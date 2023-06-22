package dev.ghost.basicduels.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import dev.ghost.basicduels.menusystem.Menu;

public class MenuListener implements Listener {

    /**
     * Evento de clickear en un menú (inventario)
     * Comprueba si el inventario es un menú y si lo es, llama al método handleMenu
     * 
     * @param e Evento de clickear en un inventario
     */
    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();

        if (holder instanceof Menu) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }

            Menu menu = (Menu) holder;
            menu.handleMenu(e);
        }

    }

}

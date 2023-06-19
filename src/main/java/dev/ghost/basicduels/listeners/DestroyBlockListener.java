package dev.ghost.basicduels.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import dev.ghost.basicduels.BasicDuels;

public class DestroyBlockListener implements Listener {
    
    public DestroyBlockListener() {
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();

        boolean isPlayerInDuel = BasicDuels.getInstance().duelManager.isPlayerInDuel(player);

        if (isPlayerInDuel) {
            event.setCancelled(true);
            return;
        }
    }
    
}

package dev.ghost.basicduels.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dev.ghost.basicduels.utils.PlayerSavings;

public class PlayerJoin implements Listener {

    /**
     * Restaura los datos del jugador al unirse
     * 
     * @param event Evento de unirse
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerSavings.getInstance().restorePlayer(event.getPlayer());
    }
}

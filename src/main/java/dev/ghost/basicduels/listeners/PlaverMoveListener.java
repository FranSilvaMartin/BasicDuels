package dev.ghost.basicduels.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import dev.ghost.basicduels.BasicDuels;
import dev.ghost.basicduels.cooldowns.Cooldown;

public class PlaverMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getFrom().getBlockX() != e.getTo().getBlockX()
                || e.getFrom().getBlockY() != e.getTo().getBlockY()
                || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
            Cooldown cooldown = BasicDuels.getInstance().getCooldownManager().findCooldownByPlayer(e.getPlayer());
            if (cooldown != null) {
                // cooldown.cancel();
                e.setCancelled(true);
            }
        }
    }

    // Si el jugador recibe daño, cancelar el cooldown
    @EventHandler
    public void onPlayerDamage(org.bukkit.event.entity.EntityDamageEvent e) {
        if (e.getEntity() instanceof org.bukkit.entity.Player) {
            org.bukkit.entity.Player player = (org.bukkit.entity.Player) e.getEntity();
            Cooldown cooldown = BasicDuels.getInstance().getCooldownManager().findCooldownByPlayer(player);
            if (cooldown != null) {
                cooldown.cancel();
                player.sendMessage("§cHas recibido daño, se ha cancelado el cooldown");
            }
        }
    }

}

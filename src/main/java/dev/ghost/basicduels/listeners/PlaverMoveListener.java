package dev.ghost.basicduels.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import dev.ghost.basicduels.BasicDuels;
import dev.ghost.basicduels.cooldowns.Cooldown;

public class PlaverMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        if (e.getFrom().getBlockX() != e.getTo().getBlockX()
                || e.getFrom().getBlockY() != e.getTo().getBlockY()
                || e.getFrom().getBlockZ() != e.getTo().getBlockZ())
        {
            Cooldown cooldown = BasicDuels.getInstance().getCooldownManager().get(BasicDuels.getInstance().getCooldownManager().getTeleportCountdown(), e.getPlayer());
            if (cooldown != null)
            {
                cooldown.cancel();
            }
        }
    }

}

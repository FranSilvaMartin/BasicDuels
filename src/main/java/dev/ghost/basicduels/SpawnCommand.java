package dev.ghost.basicduels;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.ghost.basicduels.cooldowns.Cooldown;
import java.util.concurrent.TimeUnit;

public class SpawnCommand implements CommandExecutor {

    private final BasicDuels plugin;

    public SpawnCommand(BasicDuels plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Cooldown cooldown = plugin.getCooldownManager().get(plugin.getCooldownManager().getTeleportCooldown(), player);
            if (cooldown == null) {
                Cooldown countdown = plugin.getCooldownManager().get(plugin.getCooldownManager().getTeleportCountdown(), player);
                if (countdown == null) {
                    countdown = plugin.getCooldownManager().create(plugin.getCooldownManager().getTeleportCountdown(), player, 5, TimeUnit.SECONDS)
                            .whenStarted(() -> player.sendMessage("Teleporting in 5 seconds."))
                            .addStep(1, () -> player.sendMessage("Teleporting in 4 seconds."))
                            .addStep(2, () -> player.sendMessage("Teleporting in 3 seconds."))
                            .addStep(3, () -> player.sendMessage("Teleporting in 2 seconds."))
                            .addStep(4, () -> player.sendMessage("Teleporting in 1 seconds."))
                            .whenCancelled(() -> player.sendMessage("Teleport cancelled."))
                            
                            .whenCompleted(() -> {
                                player.sendMessage("Teleporting to spawn.");
                                player.teleport(player.getWorld().getSpawnLocation());
                                plugin.getCooldownManager().create(plugin.getCooldownManager().getTeleportCooldown(), player, 10, TimeUnit.SECONDS).start();
                            });
                    countdown.start();
                }
            } else {
                player.sendMessage("This command will be available in " + cooldown.getTimeLeft() + " seconds");
            }
        }
        return true;
    }
}
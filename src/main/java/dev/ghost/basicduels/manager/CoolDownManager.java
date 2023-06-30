package dev.ghost.basicduels.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import dev.ghost.basicduels.BasicDuels;

public class CoolDownManager {

    private Map<UUID, List<Integer>> playerCoolDownMap = new HashMap<>();

    public CoolDownManager(BasicDuels plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : playerCoolDownMap.keySet()) {
                    Player player = plugin.getServer().getPlayer(uuid);
                    List<Integer> cooldowns = playerCoolDownMap.get(uuid);

                    for (int i = 0; i < cooldowns.size(); i++) {
                        int time = cooldowns.get(i);

                        if (time == 1) {
                            cooldowns.remove(i);
                            player.sendMessage("§aYour cooldown has expired!");
                            i--;
                            continue;
                        }
                        cooldowns.set(i, time - 1);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public Map<UUID, List<Integer>> getPlayerCoolDownMap() {
        return playerCoolDownMap;
    }

    public void addPlayerToMap(Player player, Integer time) {

        if (playerCoolDownMap.containsKey(player.getUniqueId())) {
            return;
        }

        UUID uuid = player.getUniqueId();
        List<Integer> cooldowns = playerCoolDownMap.getOrDefault(uuid, new ArrayList<>());
        cooldowns.add(time);
        playerCoolDownMap.put(uuid, cooldowns);
    }

    public boolean isPlayerInCooldown(Player player) {
        List<Integer> cooldowns = playerCoolDownMap.get(player.getUniqueId());
        return cooldowns != null && !cooldowns.isEmpty();
    }

    public List<Integer> getCooldowns(Player player) {
        return playerCoolDownMap.getOrDefault(player.getUniqueId(), new ArrayList<>());
    }
}
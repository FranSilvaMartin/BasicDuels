package dev.ghost.basicduels.cooldowns;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class CooldownManager {
    private Map<CooldownCategory, Map<Object, Cooldown>> registry = new HashMap<>();

    /**
     * Create a new instance of CooldownManager
     *
     * @param plugin Your plugin instance
     * @param delay  The delay between each cooldown check
     */
    public CooldownManager(@Nonnull Plugin plugin, long delay) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Map<Object, Cooldown> category : registry.values()) {
                Iterator<Map.Entry<Object, Cooldown>> iterator = category.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Object, Cooldown> entry = iterator.next();
                    Cooldown cooldown = entry.getValue();
                    if (!cooldown.isCancelled()) {
                        cooldown.tick();
                    }
                    if (cooldown.isCompleted() || cooldown.isCancelled()) {
                        iterator.remove();
                    }
                }
            }
        }, 0L, delay);

        // Haz un for de registry
        for (Map.Entry<CooldownCategory, Map<Object, Cooldown>> entry : registry.entrySet()) {
            CooldownCategory category = entry.getKey();
            Map<Object, Cooldown> cooldowns = entry.getValue();
            for (Map.Entry<Object, Cooldown> entry2 : cooldowns.entrySet()) {
                Object owner = entry2.getKey();
                Cooldown cooldown = entry2.getValue();
                if (cooldown.isCompleted() || cooldown.isCancelled()) {
                    cooldowns.remove(owner);
                }
            }
        }
    }

    /**
     * Create a new cooldown
     * If a value is already mapped to the category/owner, the value will be
     * override
     *
     * @param category Category of the cooldown
     * @param owner    Owner of the cooldown
     * @param time     Time of the cooldown
     * @param unit     TimeUnit used by the cooldown
     * @return the cooldown created
     */
    public Cooldown create(CooldownCategory category, Object owner, int time,
            TimeUnit unit) {
        Map<Object, Cooldown> cooldowns = registry.computeIfAbsent(category, k -> new HashMap<>());
        return cooldowns.compute(owner, (k, v) -> new Cooldown(time, unit));
    }

    /**
     * Get an existing cooldown
     *
     * @param category Category of the cooldown
     * @param owner    Owner of the cooldown
     * @return The cooldown mapped to category/owner or null if none
     */
    public Cooldown get(CooldownCategory category, Object owner) {
        Map<Object, Cooldown> cooldowns = registry.get(category);
        return cooldowns != null ? cooldowns.get(owner) : null;
    }

    // Find Cooldown by player
    public Cooldown findCooldownByPlayer(Object owner) {
        for (Map.Entry<CooldownCategory, Map<Object, Cooldown>> entry : registry.entrySet()) {
            Map<Object, Cooldown> cooldowns = entry.getValue();
            for (Map.Entry<Object, Cooldown> entry2 : cooldowns.entrySet()) {
                Object owner2 = entry2.getKey();
                Cooldown cooldown = entry2.getValue();
                if (owner2 == owner) {
                    return cooldown;
                }
            }
        }
        return null;
    }
}
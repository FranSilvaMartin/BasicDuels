package dev.ghost.basicduels.utils;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import dev.ghost.basicduels.BasicDuels;
import dev.ghost.basicduels.manager.KitManager;
import dev.ghost.basicduels.manager.duel.Duel;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class BasicDuelsExpansion extends PlaceholderExpansion {

    @Override
    public String getAuthor() {
        return "BetterGhost";
    }

    @Override
    public String getIdentifier() {
        return "basicduels";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {

        if (player == null) {
            return "";
        }

        if (params.equalsIgnoreCase("name")) {
            return player == null ? null : player.getName();
        }

        if (params.equalsIgnoreCase("kits_list")) {
            List<String> kitList = KitManager.getKitIds();
            return kitList.toString().replace("[", "").replace("]", "") + "31231.";
        }

        Duel duel = BasicDuels.getInstance().duelManager.getDuelByPlayer(player.getPlayer());
        if (duel != null) {
            if (params.equalsIgnoreCase("player")) {
                return duel.getSender().getName();
            }

            if (params.equalsIgnoreCase("target")) {
                return duel.getTarget().getName();
            }
        } else {
            return "No Duel";
        }

        return null;
    }
}

package dev.ghost.basicduels.utils;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import dev.ghost.basicduels.BasicDuels;
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

        Duel duel = BasicDuels.getInstance().duelManager.getDuelByPlayer(player.getPlayer());
        if (duel == null) {
            return "No Duel";
        }

        if (params.equalsIgnoreCase("name")) {
            return player == null ? null : player.getName(); // "name" requires the player to be valid
        }

        if (params.equalsIgnoreCase("player")) {
            return duel.getSender().getName();
        }

        if (params.equalsIgnoreCase("target")) {
            return duel.getTarget().getName();
        }

        return null;
    }
}

package dev.ghost.basicduels.manager.duel;

import dev.ghost.basicduels.BasicDuels;
import dev.ghost.basicduels.manager.ConfigManager;
import dev.ghost.basicduels.utils.Utils;
import dev.ghost.basicduels.utils.PlayerSavings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DuelManager {
    private final Map<Integer, Arena> arenas;
    private final Map<Integer, Duel> duelRequests;

    public DuelManager(BasicDuels plugin) {
        this.duelRequests = new HashMap<>();
        this.arenas = new HashMap<>();
    }

    public void addArena(Arena arena) {
        arenas.put(arenas.size() + 1, arena);
    }

    public void removeArena(int id) {
        arenas.remove(id);
    }

    public Arena getArena(int id) {
        return arenas.get(id);
    }

    public Duel getDuelRequest(int id) {
        return duelRequests.get(id);
    }

    @SuppressWarnings("deprecation")
    public void sendDuelRequest(Player sender, Player receiver, DuelGamemode duelGamemode) {
        int duelID = duelRequests.size() + 1;

        if (sender == receiver) {
            sender.sendMessage(ConfigManager.getInstance().getMessage("duel_request_yourself", sender));
            return;
        }

        if (isPlayerInDuel(receiver) || isPlayerInDuel(sender)) {
            sender.sendMessage(ConfigManager.getInstance().getMessage("player_in_duel", sender));
            return;
        }

        if (isDuelPending(sender, receiver)) {
            sender.sendMessage(ConfigManager.getInstance().getMessage("duel_request_already_sended", sender));
            return;
        }

        if (!isArenaAvailable()) {
            sender.sendMessage(ConfigManager.getInstance().getMessage("no_arena_found", sender));
            return;
        }
        addPendingDuel(duelID, sender, receiver);

        sender.sendMessage(ConfigManager.getInstance().getMessage("duel_request_sended", sender));
        TextComponent duelRequest = messageSendDuelRequest(sender, duelID);

        receiver.sendMessage(duelRequest);
    }

    private int countdown(Player sender, Player receiver, int duelID) {
        int taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(BasicDuels.getInstance(), () -> {
            if (duelRequests.containsKey(duelID) && duelRequests.get(duelID).getState() == DuelState.PENDING) {
                sender.sendMessage(ConfigManager.getInstance().getMessage("duel_request_expired_player", sender));
                receiver.sendMessage(ConfigManager.getInstance().getMessage("duel_request_expired_target", sender));
                finishDuel(duelID);
            }
        }, 20 * 30);
        return taskID;
    }

    private TextComponent messageSendDuelRequest(Player sender, int duelID) {
        TextComponent duelRequest = new TextComponent(
                Utils.colorize(("&3" + sender.getName() + " has sent you a duel request. Click to ")));

        TextComponent accept = new TextComponent(ChatColor.GREEN + "/accept");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel accept " + duelID));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new Text(ChatColor.GREEN + "Click to accept the duel request")));

        TextComponent deny = new TextComponent(ChatColor.RED + "/deny");
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel deny " + duelID));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new Text(ChatColor.RED + "Click to deny the duel request")));

        duelRequest.addExtra(new TextComponent(Utils.colorize("&3'")));
        duelRequest.addExtra(accept);
        duelRequest.addExtra(new TextComponent(Utils.colorize("&3' &3or '")));
        duelRequest.addExtra(deny);
        duelRequest.addExtra(new TextComponent(Utils.colorize("&3'&3.")));
        return duelRequest;
    }

    private void addPendingDuel(int duelID, Player sender, Player receiver) {
        Duel duel = new Duel(sender, receiver);
        int taskID = countdown(sender, receiver, duelID);
        duel.setTaskID(taskID);
        duelRequests.put(duelID, duel);
    }

    public void acceptDuelRequest(int duelID) {
        if (!duelRequests.containsKey(duelID)) {
            return;
        }

        Duel duel = getDuelRequest(duelID);
        Player sender = duel.getSender();
        Player receiver = duel.getTarget();
        Arena arena = getArenaAvailable();

        if (arena == null) {
            sender.sendMessage(ConfigManager.getInstance().getMessage("no_arena_found", sender));
            receiver.sendMessage(ConfigManager.getInstance().getMessage("no_arena_found", receiver));
            return;
        }

        arena.setInUse(true);
        duel.setArena(arena);
        duel.setState(DuelState.COUNTDOWN);

        sender.sendMessage(ConfigManager.getInstance().getMessage("duel_request_accept", sender));
        receiver.sendMessage(ConfigManager.getInstance().getMessage("duel_request_accept_target", sender));

        PlayerSavings.getInstance().savePlayer(sender);
        PlayerSavings.getInstance().savePlayer(receiver);

        startDuel(duelID);
    }

    public void startDuel(int duelID) {

        Duel duel = getDuelRequest(duelID);
        Arena arena = duel.getArena();
        Player sender = duel.getSender();
        Player receiver = duel.getTarget();
        duel.setState(DuelState.STARTED);

        sender.teleport(arena.getLocationPlayer1());
        receiver.teleport(arena.getLocationPlayer2());

        sender.sendMessage(ConfigManager.getInstance().getMessage("duel_starting_countdown", sender));
        receiver.sendMessage(ConfigManager.getInstance().getMessage("duel_starting_countdown", sender));
        addItems(sender, receiver);

        int taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(BasicDuels.getInstance(), () -> {
            if (duelRequests.containsKey(duelID) && duelRequests.get(duelID).getState() == DuelState.STARTED) {

                sender.sendMessage(ConfigManager.getInstance().getMessage("duel_finished_countdown", sender));
                receiver.sendMessage(ConfigManager.getInstance().getMessage("duel_finished_countdown", sender));
                finishDuel(duelID);
            }
        }, 20 * 60);
        duel.setTaskID(taskID);
    }

    public void denyDuelRequest(int duelID) {
        if (!duelRequests.containsKey(duelID)) {
            return;
        }

        Duel duel = getDuelRequest(duelID);
        Player sender = duel.getSender();
        Player receiver = duel.getTarget();

        sender.sendMessage(ConfigManager.getInstance().getMessage("duel_request_denied", sender));
        receiver.sendMessage(ConfigManager.getInstance().getMessage("duel_request_denied_target", sender));

        duel.setState(DuelState.CALCELLED);
        duelRequests.remove(duelID);

    }

    public boolean isArenaAvailable() {
        boolean arenaAvailable = false;
        for (Entry<Integer, Arena> entry : arenas.entrySet()) {
            if (!entry.getValue().isInUse()) {
                arenaAvailable = true;
                break;
            }
        }
        return arenaAvailable;
    }

    public Arena getArenaAvailable() {
        Arena arenaAvailable = null;
        for (Entry<Integer, Arena> entry : arenas.entrySet()) {
            if (!entry.getValue().isInUse()) {
                arenaAvailable = entry.getValue();
                break;
            }
        }
        return arenaAvailable;
    }

    public boolean isPlayerInDuel(Player player) {
        for (Entry<Integer, Duel> entry : duelRequests.entrySet()) {
            if (entry.getValue().getSender().equals(player) || entry.getValue().getTarget().equals(player)) {
                if (entry.getValue().getState() != DuelState.PENDING) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDuelPending(Player player, Player receiver) {
        for (Entry<Integer, Duel> entry : duelRequests.entrySet()) {
            if (entry.getValue().getSender().equals(player) && entry.getValue().getTarget().equals(receiver)
                    || entry.getValue().getSender().equals(receiver) && entry.getValue().getTarget().equals(player)) {
                return true;
            }
        }
        return false;
    }

    public void finishDuel(Player player) {
        for (Entry<Integer, Duel> entry : duelRequests.entrySet()) {
            if (entry.getValue().getSender().equals(player) || entry.getValue().getTarget().equals(player)) {
                finishDuel(entry.getKey());
                break;
            }
        }

    }

    public void finishDuel(int duelID) {
        Duel duel = duelRequests.get(duelID);
        duelRequests.remove(duelID);

        if (duel == null || duel.getArena() == null) {
            return;
        }

        duel.getArena().setInUse(false);

        PlayerSavings.getInstance().restorePlayer(duel.getSender());
        PlayerSavings.getInstance().restorePlayer(duel.getTarget());

        Bukkit.getScheduler().cancelTask(duel.getTaskID());
    }

    // Get duel by player
    public Duel getDuelByPlayer(Player player) {
        for (Entry<Integer, Duel> entry : duelRequests.entrySet()) {
            if (entry.getValue().getSender().equals(player) || entry.getValue().getTarget().equals(player)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void addItems(Player sender, Player receiver) {
        sender.getInventory().clear();
        receiver.getInventory().clear();
        sender.getInventory().setArmorContents(null);
        receiver.getInventory().setArmorContents(null);

        sender.setHealth(20);
        receiver.setHealth(20);
        sender.setFoodLevel(20);
        receiver.setFoodLevel(20);

        sender.setGameMode(GameMode.SURVIVAL);
        receiver.setGameMode(GameMode.SURVIVAL);

        sender.setAllowFlight(false);
        receiver.setAllowFlight(false);

        sender.setFlying(false);
        receiver.setFlying(false);

        sender.setFireTicks(0);
        receiver.setFireTicks(0);

        sender.setSaturation(20);
        receiver.setSaturation(20);

        sender.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
        sender.getInventory().addItem(new ItemStack(Material.BOW));
        sender.getInventory().addItem(new ItemStack(Material.ARROW, 64));
        sender.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 5));
        sender.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
        sender.getInventory().addItem(new ItemStack(Material.FISHING_ROD));
        sender.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));

        sender.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        sender.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        sender.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        sender.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));

        receiver.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
        receiver.getInventory().addItem(new ItemStack(Material.BOW));
        receiver.getInventory().addItem(new ItemStack(Material.ARROW, 64));
        receiver.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 5));
        receiver.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
        receiver.getInventory().addItem(new ItemStack(Material.FISHING_ROD));
        receiver.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));

        receiver.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        receiver.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        receiver.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        receiver.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
    }
}
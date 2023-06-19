package dev.ghost.basicduels.manager.duel;

import dev.ghost.basicduels.BasicDuels;
import dev.ghost.basicduels.utils.ColorUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
    public void sendDuelRequest(Player sender, Player receiver) {
        int duelID = duelRequests.size() + 1;

        if (sender == receiver) {
            sender.sendMessage(ColorUtils.colorize("&cYou cannot duel yourself."));
            return;
        }

        if (isPlayerInDuel(receiver) || isPlayerInDuel(sender)) {
            sender.sendMessage(
                    ColorUtils.colorize("&c" + receiver.getName() + " is already in a duel or you are already in one"));
            return;
        }

        if (isDuelPending(sender, receiver)) {
            Bukkit.getLogger().info("Duel pending");
            Bukkit.getLogger().info("Sender: " + sender.getName());
            Bukkit.getLogger().info("Receiver: " + receiver.getName());
            sender.sendMessage(
                    ColorUtils.colorize("&cYou already have a duel request pending with " + receiver.getName()));
            return;
        }

        if (!isArenaAvailable()) {
            sender.sendMessage("There are no arenas available.");
            return;
        }

        sender.sendMessage(ColorUtils.colorize("&3You have sent a duel request to &f" + receiver.getName() + "&3."));
        TextComponent duelRequest = messageSendDuelRequest(sender, duelID);

        receiver.sendMessage(duelRequest);
        addPendingDuel(duelID, sender, receiver);
    }

    private int countdown(Player sender, Player receiver, int duelID) {
        int taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(BasicDuels.getInstance(), () -> {
            if (duelRequests.containsKey(duelID) && duelRequests.get(duelID).getState() == DuelState.PENDING) {
                finishDuel(duelID);
                sender.sendMessage(
                        ColorUtils.colorize("&3The duel request to &f" + receiver.getName() + " &3has expired."));
                receiver.sendMessage(
                        ColorUtils.colorize("&3The duel request from &f" + sender.getName() + " &3has expired."));
            }
        }, 20 * 30);
        return taskID;
    }

    private TextComponent messageSendDuelRequest(Player sender, int duelID) {
        TextComponent duelRequest = new TextComponent(
                ColorUtils.colorize(("&3" + sender.getName() + " has sent you a duel request. Click to ")));

        TextComponent accept = new TextComponent(ChatColor.GREEN + "/accept");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel accept " + duelID));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new Text(ChatColor.GREEN + "Click to accept the duel request")));

        TextComponent deny = new TextComponent(ChatColor.RED + "/deny");
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel deny " + duelID));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new Text(ChatColor.RED + "Click to deny the duel request")));

        duelRequest.addExtra(new TextComponent(ColorUtils.colorize("&3'")));
        duelRequest.addExtra(accept);
        duelRequest.addExtra(new TextComponent(ColorUtils.colorize("&3' &3or '")));
        duelRequest.addExtra(deny);
        duelRequest.addExtra(new TextComponent(ColorUtils.colorize("&3'&3.")));
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
        Player sender = duel.getPlayer1();
        Player receiver = duel.getPlayer2();
        Arena arena = getArenaAvailable();

        if (arena == null) {
            sender.sendMessage("There are no arenas available. Please try again later.");
            receiver.sendMessage("There are no arenas available. Please try again later.");
            return;
        }

        arena.setInUse(true);
        duel.setArena(arena);
        duel.setState(DuelState.COUNTDOWN);

        sender.sendMessage(
                ColorUtils.colorize("&3You have accepted the duel request from &f" + receiver.getName() + "&3."));
        receiver.sendMessage(
                ColorUtils.colorize("&3You have accepted the duel request from &f" + sender.getName() + "&3."));

        startDuel(duelID);
    }

    public void startDuel(int duelID) {

        Duel duel = getDuelRequest(duelID);
        Arena arena = duel.getArena();
        Player sender = duel.getPlayer1();
        Player receiver = duel.getPlayer2();
        duel.setState(DuelState.STARTED);

        sender.teleport(arena.getLocationPlayer1());
        receiver.teleport(arena.getLocationPlayer2());

        int taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(BasicDuels.getInstance(), () -> {
            if (duelRequests.containsKey(duelID) && duelRequests.get(duelID).getState() == DuelState.STARTED) {
                finishDuel(duelID);
                sender.sendMessage(
                        ColorUtils.colorize("&3The duel has finished. "));
                receiver.sendMessage(ColorUtils.colorize("&3The duel has finished. "));
            }
        }, 20 * 60);

        duel.setTaskID(taskID);
    }

    public void denyDuelRequest(int duelID) {
        if (!duelRequests.containsKey(duelID)) {
            return;
        }

        Duel duel = getDuelRequest(duelID);
        Player sender = duel.getPlayer1();
        Player receiver = duel.getPlayer2();
        duel.setState(DuelState.CALCELLED);
        duelRequests.remove(duelID);

        sender.sendMessage(
                ColorUtils.colorize("&3" + receiver.getName() + " has denied your duel request."));
        receiver.sendMessage(
                ColorUtils.colorize("&3You have denied the duel request from &f" + sender.getName() + "&3."));

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
            if (entry.getValue().getPlayer1().equals(player) || entry.getValue().getPlayer2().equals(player)) {
                if (entry.getValue().getState() != DuelState.PENDING) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDuelPending(Player player, Player receiver) {
        for (Entry<Integer, Duel> entry : duelRequests.entrySet()) {
            if (entry.getValue().getPlayer1().equals(player) && entry.getValue().getPlayer2().equals(receiver)
                    || entry.getValue().getPlayer1().equals(receiver) && entry.getValue().getPlayer2().equals(player)) {
                return true;
            }
        }
        return false;
    }

    public void finishDuel(Player player) {
        for (Entry<Integer, Duel> entry : duelRequests.entrySet()) {
            if (entry.getValue().getPlayer1().equals(player) || entry.getValue().getPlayer2().equals(player)) {
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

        Location location = new Location(Bukkit.getWorld("world"), 3165, 63.00, 4096);
        duel.getPlayer1().teleport(location);
        duel.getPlayer2().teleport(location);
        Bukkit.getScheduler().cancelTask(duel.getTaskID());
    }
}
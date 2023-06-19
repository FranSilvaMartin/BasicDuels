package dev.ghost.basicduels.manager.duel;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Arena {

    private int id;
    private String name;
    private boolean inUse;
    private Location locationPlayer1;
    private Location locationPlayer2;

    private Player player1;
    private Player player2;

    /**
     * Arena Constructor
     * 
     * @param id    ID of the arena
     * @param name  Name of the arena
     * @param inUse If the arena is in use
     * @param x     X coordinate of the arena
     * @param y     Y coordinate of the arena
     * @param z     Z coordinate of the arena
     * @param yaw   Yaw of the arena
     * @param pitch Pitch of the arena
     */
    public Arena(int id, String name, Location locationPlayer1, Location locationPlayer2) {
        this.id = id;
        this.name = name;
        this.inUse = false;
        this.locationPlayer1 = locationPlayer1;
        this.locationPlayer2 = locationPlayer2;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isInUse() {
        return inUse;
    }

    public Location getLocationPlayer1() {
        return locationPlayer1;
    }

    public Location getLocationPlayer2() {
        return locationPlayer2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

}

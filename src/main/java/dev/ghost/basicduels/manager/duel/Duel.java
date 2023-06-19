package dev.ghost.basicduels.manager.duel;

import org.bukkit.entity.Player;

public class Duel {
    
    private DuelState state;
    private Player player1;
    private Player player2;
    private Arena arena;
    private int taskID;

    public Duel(Player player1, Player player2) {
        this.state = DuelState.PENDING;
        this.player1 = player1;
        this.player2 = player2;
        this.taskID = -1;
    }

    public DuelState getState() {
        return state;
    }

    public void setState(DuelState state) {
        this.state = state;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }
}

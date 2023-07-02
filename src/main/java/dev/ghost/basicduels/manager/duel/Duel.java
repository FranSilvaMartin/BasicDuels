package dev.ghost.basicduels.manager.duel;

import org.bukkit.entity.Player;

public class Duel {
    
    private DuelState state;
    private Player sender;
    private Player target;
    private Arena arena;

    public Duel(Player sender, Player target) {
        this.state = DuelState.PENDING;
        this.sender = sender;
        this.target = target;
    }

    public DuelState getState() {
        return state;
    }

    public void setState(DuelState state) {
        this.state = state;
    }

    public Player getSender() {
        return sender;
    }

    public Player getTarget() {
        return target;
    }
    
    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }
}

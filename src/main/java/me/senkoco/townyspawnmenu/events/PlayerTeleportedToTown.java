package me.senkoco.townyspawnmenu.events;

import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerTeleportedToTown extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Town town;

    public PlayerTeleportedToTown(Player player, Town town) {
        this.player = player;
        this.town = town;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getPlayer(){
        return this.player;
    }

    public Town getTown() { return this.town; }
}

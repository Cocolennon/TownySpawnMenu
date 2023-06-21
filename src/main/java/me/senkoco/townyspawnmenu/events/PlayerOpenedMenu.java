package me.senkoco.townyspawnmenu.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerOpenedMenu extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;

    public PlayerOpenedMenu(Player player) {
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getPlayer(){
        return this.player;
    }
}

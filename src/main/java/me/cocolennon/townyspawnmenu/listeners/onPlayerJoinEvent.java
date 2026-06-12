package me.cocolennon.townyspawnmenu.listeners;

import me.cocolennon.townyspawnmenu.Main;
import me.cocolennon.townyspawnmenu.utils.Localization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onPlayerJoinEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(!player.hasPermission("townyspawnui.*")) return;
        if(Main.getUsingOldVersion()) player.sendMessage(Localization.get(player, "old-version", true, Main.getLatestVersion()));
    }
}

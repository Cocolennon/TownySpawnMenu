package me.senkoco.townyspawnmenu.listeners;

import me.senkoco.townyspawnmenu.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onPlayerJoinEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if(!player.hasPermission("townyspawnui.*")) return;
        if(Main.getUsingOldVersion()){
            player.sendMessage("§6[Towny Spawn Menu] §3You are using an older version of Towny Spawn Menu, please update to version " + Main.getLatestVersion());
        }
    }
}

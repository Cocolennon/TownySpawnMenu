package me.senkoco.townyspawnmenu.listeners;

import com.palmergames.bukkit.towny.TownyAPI;
import me.senkoco.townyspawnmenu.utils.menu.Nations;
import me.senkoco.townyspawnmenu.utils.menu.Towns;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class onClickEvent implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        Player player = (Player)event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        if(inv.getItem(0) != null && current != null && current.getItemMeta().hasLocalizedName()){
            event.setCancelled(true);
            String currentDName = current.getItemMeta().getDisplayName();
            String currentLName = current.getItemMeta().getLocalizedName();
            if(inv.getItem(0).getItemMeta().getLocalizedName().equals("nationMenu")) {
                switch(currentLName){
                    case "noNation", "atWar":
                        Nations.openTownsOfNation(current, player, true, null);
                        break;
                    case "nationMenu":
                        break;
                    default:
                        Nations.openTownsOfNation(current, player, false, null);
                        break;
                }
            }else if(inv.getItem(0).getItemMeta().getLocalizedName().equals("townMenu")) {
                if(currentLName.equals("townMenu")) return;
                if(currentDName.equals("§6§lNext Page") || currentDName.equals("§6§lPrevious Page") || currentDName.equals("§6§lBack to Nations")){
                    Nations.openTownsOfNation(current, player, true, TownyAPI.getInstance().getNation(inv.getItem(26).getItemMeta().getLocalizedName()));
                }else{
                    Towns.teleportToTown(player, currentLName);
                }
            }
        }
    }
}

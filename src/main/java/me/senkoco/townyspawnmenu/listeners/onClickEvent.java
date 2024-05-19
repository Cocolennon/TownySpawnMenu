package me.senkoco.townyspawnmenu.listeners;

import com.palmergames.bukkit.towny.TownyAPI;
import io.github.townyadvanced.townymenus.gui.MenuHistory;
import me.senkoco.townyspawnmenu.Main;
import me.senkoco.townyspawnmenu.utils.menu.Nations;
import me.senkoco.townyspawnmenu.utils.menu.Towns;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.Objects;

public class onClickEvent implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        Player player = (Player)event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        if(inv.getItem(0) == null) return;
        if(current == null) return;
        if(!current.hasItemMeta()) return;
        NamespacedKey buttonAction = new NamespacedKey(Main.getInstance(), "buttonAction");
        PersistentDataContainer pdc = current.getItemMeta().getPersistentDataContainer();
        if(!pdc.has(buttonAction)) return;

        event.setCancelled(true);
        String currentDName = current.getItemMeta().getDisplayName();
        String currentLName = pdc.get(buttonAction, PersistentDataType.STRING);
        switch(inv.getItem(0).getItemMeta().getPersistentDataContainer().get(buttonAction, PersistentDataType.STRING)){
            case "nationMenu":
                switch(currentLName){
                    case "noNation", "atWar":
                        Nations.openTownsOfNation(current, player, true, null);
                        return;
                    case "nationMenu":
                        return;
                    case "BTTM":
                        MenuHistory.last(player);
                        return;
                    default:
                        Nations.openTownsOfNation(current, player, false, null);
                        return;
                }
            case "townMenu":
                if(currentLName.equals("townMenu")) return;
                if(currentDName.equals("§6§lNext Page") || currentDName.equals("§6§lPrevious Page") || currentDName.equals("§6§lBack to Nations")){
                    Nations.openTownsOfNation(current, player, true, TownyAPI.getInstance().getNation(Objects.requireNonNull(Objects.requireNonNull(inv.getItem(26)).getItemMeta()).getPersistentDataContainer().get(buttonAction, PersistentDataType.STRING)));
                }else{
                    if(!player.hasPermission("townyspawnmenu.menu.teleport")) { player.sendMessage("§6[Towny Spawn Menu] §cYou can't do that!"); return; }
                    Towns.teleportToTown(player, currentLName);
                }
        }
    }
}

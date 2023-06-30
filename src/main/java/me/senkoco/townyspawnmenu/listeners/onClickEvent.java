package me.senkoco.townyspawnmenu.listeners;

import com.palmergames.bukkit.towny.TownyAPI;
import io.github.townyadvanced.townymenus.gui.MenuHistory;
import me.senkoco.townyspawnmenu.utils.menu.Nations;
import me.senkoco.townyspawnmenu.utils.menu.Towns;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        if(getItemMeta(current).hasLocalizedName()) return;

        event.setCancelled(true);
        String currentDName = getItemMeta(current).getDisplayName();
        String currentLName = getItemMeta(current).getLocalizedName();
        switch(getItemMeta(inv, 0).getLocalizedName()){
            case "nationMenu":
                switch(currentLName){
                    case "noNation", "atWar":
                        Nations.openTownsOfNation(current, player, true, null);
                        break;
                    case "nationMenu":
                        break;
                    case "BTTM":
                        MenuHistory.last(player);
                        break;
                    default:
                        Nations.openTownsOfNation(current, player, false, null);
                        break;
                }
            case "townMenu":
                if(currentLName.equals("townMenu")) return;
                if(currentDName.equals("§6§lNext Page") || currentDName.equals("§6§lPrevious Page") || currentDName.equals("§6§lBack to Nations")){
                    Nations.openTownsOfNation(current, player, true, TownyAPI.getInstance().getNation(Objects.requireNonNull(Objects.requireNonNull(inv.getItem(26)).getItemMeta()).getLocalizedName()));
                }else{
                    if(!player.hasPermission("townyspawnmenu.menu.teleport")) { player.sendMessage("§6[Towny Spawn Menu] §cYou can't do that!"); return; }
                    Towns.teleportToTown(player, currentLName);
                }
        }
    }

    @Nonnull
    public ItemMeta getItemMeta(Inventory inventory, int slot) {
        return Objects.requireNonNull(Objects.requireNonNull(inventory.getItem(slot)).getItemMeta());
    }

    @Nonnull
    public ItemMeta getItemMeta(ItemStack item) {
        return Objects.requireNonNull(item.getItemMeta());
    }
}

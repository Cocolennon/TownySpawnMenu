package me.cocolennon.townyspawnmenu.listeners;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.cocolennon.townyspawnmenu.utils.menu.MenuType;
import me.cocolennon.townyspawnmenu.utils.menu.Nations;
import me.cocolennon.townyspawnmenu.utils.menu.SpawnMenuInventoryHolder;
import me.cocolennon.townyspawnmenu.utils.menu.Towns;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if(inventory == null || !(inventory.getHolder() instanceof SpawnMenuInventoryHolder invHolder)) return;
        TownyAPI townyAPI = TownyAPI.getInstance();
        Player player = (Player) event.getWhoClicked();
        Resident resident = townyAPI.getResident(player);
        ItemStack current = event.getCurrentItem();
        event.setCancelled(true);
        String buttonAction = invHolder.getButtonAction(current);
        switch(invHolder.getType()) {
            case NATIONS -> {
                switch(buttonAction) {
                    case "nation" -> {
                        Nation nation = townyAPI.getNation(invHolder.getNationName(current));
                        Towns.getPages(resident, nation, MenuType.TOWNS).getFirst().openInventory(player);
                    }
                    case "noNation" -> Towns.getPages(resident, null, MenuType.NATIONLESS).getFirst().openInventory(player);
                    case "atWar" -> Towns.getPages(resident, null, MenuType.AT_WAR).getFirst().openInventory(player);
                    case "notPublic" -> Towns.getPages(resident, null, MenuType.PRIVATE).getFirst().openInventory(player);
                    case "nextPage", "previousPage" -> {
                        int pageNumber = invHolder.getPageNumber(current);
                        Nations.getPages(resident).get(pageNumber).openInventory(player);
                    }
                    default -> {}
                }
            }
            case TOWNS, NATIONLESS, PRIVATE, AT_WAR -> {
                switch(buttonAction) {
                    case "town" -> {
                        Town town = townyAPI.getTown(invHolder.getNationName(current));
                        Towns.teleportToTown(player, town.getName());
                    }
                    case "nextPage", "previousPage" -> {
                        int pageNumber = invHolder.getPageNumber(current);
                        Towns.getPages(resident, invHolder.getNation(), invHolder.getType()).get(pageNumber).openInventory(player);
                    }
                    case "backToNations" -> {
                        int pageNumber = invHolder.getPageNumber(current);
                        Nations.getPages(resident).get(pageNumber).openInventory(player);
                    }
                    default -> {}
                }
            }
        }
    }
}

package me.cocolennon.townyspawnmenu.utils.menu;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.cocolennon.townyspawnmenu.Main;
import me.cocolennon.townyspawnmenu.events.PlayerTeleportedToTown;
import me.cocolennon.townyspawnmenu.utils.Localization;
import me.cocolennon.townyspawnmenu.utils.Metadata;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Towns {
    private static Main main = Main.getInstance();

    public static List<SpawnMenuInventoryHolder> getPages(Resident resident, Nation nation, MenuType type) {
        TownyAPI townyAPI = TownyAPI.getInstance();
        final List<Town> allTowns = type.getTowns(nation, townyAPI);
        int allTownsCount = allTowns.size();
        int townsInPage = 0;
        int inventorySlots = 7;
        List<SpawnMenuInventoryHolder> inventories = new ArrayList<>();
        int pageCount = allTownsCount / inventorySlots;
        for(int pageNumber = 0; pageNumber <= pageCount; pageNumber++){
            String localeNode = "menu.title.towns." + switch(type) {
                case TOWNS -> "main";
                case NATIONLESS -> "nationless";
                case PRIVATE -> "private";
                case AT_WAR -> "at-war";
                default -> "error";
            };
            Component title = type.equals(MenuType.TOWNS) ? Localization.get(resident, localeNode, false, nation.getName(), pageNumber + 1, pageCount + 1) : Localization.get(resident, localeNode, false, pageNumber + 1, pageCount + 1);
            SpawnMenuInventoryHolder newPage = new SpawnMenuInventoryHolder(resident, 27, title, type, nation);
            List<Town> townsInCurrentPage = new ArrayList<>();
            if(pageNumber == pageCount) inventorySlots = allTownsCount - townsInPage;
            for(int j = 0; j < inventorySlots; j++){
                townsInCurrentPage.add(allTowns.get(townsInPage));
                townsInPage++;
            }
            int menuSlot = 10;
            for (Town town : townsInCurrentPage) {
                if(Metadata.getTownHidden(town) && !town.hasResident(resident)) {
                    newPage.addHiddenTownItem(menuSlot);
                    menuSlot++;
                    continue;
                }
                newPage.addTownItem(town, menuSlot);
                menuSlot++;
            }
            if(pageNumber < pageCount) newPage.addPageItem("Next", pageNumber + 1, 23);
            if(pageNumber > 0) newPage.addPageItem("Previous", pageNumber - 1, 21);
            newPage.addBackToNationsItem(0);
            newPage.fillEmpty();
            inventories.add(newPage);
        }
        return inventories;
    }

    public static void teleportToTown(Player player, String townName) {
        if(!player.hasPermission("townyspawnmenu.menu.teleport")) {
            player.sendMessage(Localization.get(player, "error.permission", true));
            return;
        }
        Town town = TownyAPI.getInstance().getTown(townName);
        if(!town.isPublic()) return;
        player.performCommand("t spawn " + townName + " -ignore");
        PlayerTeleportedToTown playerTeleportedToTown = new PlayerTeleportedToTown(player, town);
        Bukkit.getPluginManager().callEvent(playerTeleportedToTown);
    }
}

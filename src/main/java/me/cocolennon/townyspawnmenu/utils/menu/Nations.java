package me.cocolennon.townyspawnmenu.utils.menu;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import me.cocolennon.townyspawnmenu.Main;
import me.cocolennon.townyspawnmenu.utils.Localization;
import me.cocolennon.townyspawnmenu.utils.Metadata;

import java.util.ArrayList;
import java.util.List;

public class Nations {
    private static Main main = Main.getInstance();

    public static List<SpawnMenuInventoryHolder> getPages(Resident resident){
        List<Nation> allNations = TownyAPI.getInstance().getNations();
        int allNationsCount = allNations.size();
        int nationsCount = 0;
        int inventorySlots = 7;
        List<SpawnMenuInventoryHolder> inventories = new ArrayList<>();
        int pageCount = allNationsCount / inventorySlots;
        for(int pageNumber = 0; pageNumber <= pageCount; pageNumber++){
            SpawnMenuInventoryHolder newPage = new SpawnMenuInventoryHolder(resident, 27, Localization.get(resident, "menu.title.nations", false, pageNumber + 1, pageCount + 1), MenuType.NATIONS, null);
            List<Nation> nationsInCurrentPage = new ArrayList<>();
            if(pageNumber == pageCount) inventorySlots = allNationsCount - nationsCount;
            for(int i = 0; i < inventorySlots; i++){
                nationsInCurrentPage.add(allNations.get(nationsCount));
                nationsCount++;
            }
            int menuSlot = 10;
            for (Nation nation : nationsInCurrentPage) {
                if(Metadata.getNationHidden(nation) && !nation.hasResident(resident)) {
                    newPage.addHiddenNationItem(menuSlot);
                    menuSlot++;
                    continue;
                }
                newPage.addNationItem(nation, menuSlot);
                menuSlot++;
            }
            newPage.addMiscTownItems();
            if(pageNumber < pageCount) newPage.addPageItem("Next", pageNumber + 1, 23);
            if(pageNumber > 0) newPage.addPageItem("Previous", pageNumber - 1, 21);
            newPage.fillEmpty();
            inventories.add(newPage);
        }
        return inventories;
    }
}

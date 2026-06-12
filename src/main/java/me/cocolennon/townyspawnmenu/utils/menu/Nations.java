package me.cocolennon.townyspawnmenu.utils.menu;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;
import me.cocolennon.townyspawnmenu.Main;
import me.cocolennon.townyspawnmenu.utils.Metadata;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static me.cocolennon.townyspawnmenu.utils.menu.General.getPagesCount;

public class Nations {
    private static Main main = Main.getInstance();
    public static ItemStack noNation = General.getItem(main.config().noNationItem, "<#FF5555><bold>Nation-less Towns", "noNation");
    public static ItemStack notPublic = General.getItem(main.config().privateItem, "<#FF5555><bold>Private Towns", "notPublic");
    public static ItemStack atWar = General.getItem(main.config().warItem, "<#FF5555><bold>Towns at War", "atWar");

    public static List<Inventory> getPages(Resident res){
        List<Nation> allNations = new LinkedList<>(TownyAPI.getInstance().getNations());
        int allNationsCount = allNations.size();

        int nationsCount = 0;
        int inventorySlots = 7;
        List<Inventory> inventories = new LinkedList<>();

        for(int pageNumber = 0; pageNumber <= getPagesCount(allNationsCount); pageNumber++){
            Inventory newPage = Bukkit.createInventory(null, 27, Component.text("<#FFAA00><bold>Towny§f<bold>: <#00AAAA>Nations (" + (pageNumber+1) + "/" + (getPagesCount(allNationsCount)+1) + ")"));
            List<Nation> nationsInCurrentPage = new LinkedList<>();
            if(pageNumber == getPagesCount(allNationsCount)) inventorySlots = allNationsCount - nationsCount;
            for(int i = 0; i < inventorySlots; i++){
                nationsInCurrentPage.add(allNations.get(nationsCount));
                nationsCount++;
            }
            int menuSlot = 10;
            for (Nation nation : nationsInCurrentPage) {
                if(Metadata.getNationHidden(nation)) {
                    if(!nation.hasResident(res)) {
                        newPage.setItem(menuSlot, General.getItem(Material.RED_STAINED_GLASS_PANE, "<#FF5555><bold>Hidden Nation", "hiddenNation"));
                        menuSlot++;
                        continue;
                    }
                }
                Material material = main.config().defaultItem;
                if(MetaDataUtil.hasMeta(nation, Metadata.blockInMenu)) {
                    material = Material.valueOf(Metadata.getBlockInMenu(nation));
                }
                newPage.setItem(menuSlot, General.getItem(material, "<#FF5555><bold>" + nation.getName(), nation.getName(), setGlobalLore(nation)));
                menuSlot++;
            }
            addNoNationsItem(newPage);
            addPrivatesItem(newPage);
            addAtWarItem(newPage);
            if(getPagesCount(allNationsCount) > 0){
                if(pageNumber == 0){
                    newPage.setItem(23, General.getItem(Material.ARROW, "<#FFAA00><bold>Next Page", String.valueOf((pageNumber + 1))));
                }else if(pageNumber == getPagesCount(allNationsCount)){
                    newPage.setItem(21, General.getItem(Material.ARROW, "<#FFAA00><bold>Previous Page", String.valueOf(pageNumber - 1)));
                }else{
                    newPage.setItem(23, General.getItem(Material.ARROW, "<#FFAA00><bold>Next Page", String.valueOf(pageNumber + 1)));
                    newPage.setItem(21, General.getItem(Material.ARROW, "<#FFAA00><bold>Previous Page", String.valueOf(pageNumber - 1)));
                }
            }
            General.fillEmpty(newPage, General.getItem(main.config().menuFiller, " ", "nationMenu"));
            inventories.add(newPage);
        }
        return inventories;
    }

    public static ArrayList<Component> setGlobalLore(Nation nation){
        ArrayList<Component> itemlore = new ArrayList<>();
        itemlore.add(Component.text("<#FFAA00><bold>Leader§f<bold>: <#00AAAA>" + nation.getKing().getName()));
        itemlore.add(Component.text("<#FFAA00><bold>Capital§f<bold>: <#00AA00>" + nation.getCapital().getName()));
        itemlore.add(Component.text("<#FFAA00><bold>Towns§f<bold>: <#5555FF>" + nation.getTowns().size()));
        itemlore.add(Component.text("<#FFAA00><bold>Total Residents§f<bold>: <#FF55FF>" + nation.getResidents().size()));
        return itemlore;
    }

    public static void addPrivatesItem(Inventory inv){
        int privateTownsCount = 0;
        for(int j = 0; j < TownyAPI.getInstance().getTowns().size(); j++) if(!TownyAPI.getInstance().getTowns().get(j).isPublic()) privateTownsCount++;

        if(privateTownsCount == 0) return;
        inv.setItem(18, notPublic);
    }

    public static void addNoNationsItem(Inventory inv){
        if(TownyAPI.getInstance().getTownsWithoutNation().isEmpty()) return;
        inv.setItem(22, noNation);
    }

    public static void addAtWarItem(Inventory inv){
        int townsAtWarCount = 0;
        for(int j = 0; j < TownyAPI.getInstance().getTowns().size(); j++) if(TownyAPI.getInstance().getTowns().get(j).hasActiveWar()) townsAtWarCount++;

        if(townsAtWarCount == 0) return;
        inv.setItem(26, atWar);
    }

    public static void openTownsOfNation(ItemStack current, Player player, boolean isTownMenu, Nation nation){
        String currentDName = Objects.requireNonNull(current.getItemMeta()).displayName().toString();
        NamespacedKey buttonAction = new NamespacedKey(Main.getInstance(), "buttonAction");
        PersistentDataContainer pdc = current.getItemMeta().getPersistentDataContainer();
        String currentLName = pdc.get(buttonAction, PersistentDataType.STRING);
        switch (currentDName) {
            case "<#FFAA00><bold>Next Page", "<#FFAA00><bold>Previous Page" -> {
                if (!isTownMenu) {
                    General.openInventory(player, Integer.parseInt(currentLName), Nations.getPages(TownyAPI.getInstance().getResident(player)));
                } else {
                    General.openInventory(player, Integer.parseInt(currentLName), Towns.getPages(TownyAPI.getInstance().getResident(player), nation, false, false));
                }
                return;
            }
            case "<#FFAA00><bold>Back to Nations" -> {
                General.openInventory(player, Integer.parseInt(currentLName), Nations.getPages(TownyAPI.getInstance().getResident(player)));
                return;
            }
        }
        switch (currentLName) {
            case "noNation" -> {
                General.openInventory(player, 0, Towns.getPages(TownyAPI.getInstance().getResident(player),null, false, false));
                return;
            }
            case "atWar" -> {
                General.openInventory(player, 0, Towns.getPages(TownyAPI.getInstance().getResident(player), null, true, false));
                return;
            }
            case "notPublic" -> {
                General.openInventory(player, 0, Towns.getPages(TownyAPI.getInstance().getResident(player),null, false, true));
                return;
            }
        }
        General.openInventory(player, 0, Towns.getPages(TownyAPI.getInstance().getResident(player), TownyAPI.getInstance().getNation(currentLName), false, false));
    }
}

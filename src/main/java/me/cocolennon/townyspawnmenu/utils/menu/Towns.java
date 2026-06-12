package me.cocolennon.townyspawnmenu.utils.menu;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;
import me.cocolennon.townyspawnmenu.Main;
import me.cocolennon.townyspawnmenu.events.PlayerTeleportedToTown;
import me.cocolennon.townyspawnmenu.utils.Metadata;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static me.cocolennon.townyspawnmenu.utils.menu.General.getPagesCount;
import static org.bukkit.Bukkit.getPluginManager;

public class Towns {
    private static final String reset = "<obfuscated:false><bold:false><strikethrough:false><underlined:false><italic:false><#FFFFFF>";

    public static List<Inventory> getPages(Resident res, Nation nation, boolean warMenu, boolean privateMenu){
        Main main = Main.getInstance();
        MiniMessage miniMessage = MiniMessage.miniMessage();
        List<Town> allTownsInNation;
        if(nation == null) {
            List<Town> allTowns = new LinkedList<>(TownyAPI.getInstance().getTowns());
            int allTownsCount = allTowns.size();
            if(warMenu){
                allTownsInNation = new LinkedList<>();
                for (Town allTown : allTowns) if (allTown.hasActiveWar()) allTownsInNation.add(allTown);
            }else if(privateMenu) {
                allTownsInNation = new LinkedList<>();
                for(int i = 0; i < allTownsCount; i++) if(!allTowns.get(i).isPublic()) allTownsInNation.add(allTowns.get(i));
            }else{ allTownsInNation = new LinkedList<>(TownyAPI.getInstance().getTownsWithoutNation()); }
        }else{ allTownsInNation = new LinkedList<>(nation.getTowns()); }
        int allTownsCount = allTownsInNation.size();
        int townsInPage = 0;
        int inventorySlots = 7;
        List<Inventory> inventories = new LinkedList<>();

        for(int pageNumber = 0; pageNumber < getPagesCount(allTownsCount)+1; pageNumber++){
            Inventory newPage;
            if(nation == null) {
                if(warMenu){
                    newPage = Bukkit.createInventory(null, 27, miniMessage.deserialize("<#FFAA00><bold>Towns" + reset + "<bold>: <#00AAAA>At War (" + (pageNumber+1 + "/" + (getPagesCount(allTownsCount)+1) + ")")));
                }else if(privateMenu){
                    newPage = Bukkit.createInventory(null, 27, miniMessage.deserialize("<#FFAA00><bold>Towns" + reset + "<bold>: <#00AAAA>Private (" + (pageNumber+1 + "/" + (getPagesCount(allTownsCount)+1) + ")")));
                }else{
                    newPage = Bukkit.createInventory(null, 27, miniMessage.deserialize("<#FFAA00><bold>Towns" + reset + "<bold>: <#00AAAA>Nation-less (" + (pageNumber+1 + "/" + (getPagesCount(allTownsCount)+1) + ")")));
                }
            }else{ newPage = Bukkit.createInventory(null, 27, miniMessage.deserialize("<#FFAA00><bold>" + nation.getName() + reset + "<bold>: <#00AAAA>Towns (" + (pageNumber+1 + "/" + (getPagesCount(allTownsCount)+1) + ")"))); }
            List<Town> townsInCurrentPage = new LinkedList<>();
            if(pageNumber == getPagesCount(allTownsCount)) inventorySlots = allTownsCount - townsInPage;
            for(int j = 0; j < inventorySlots; j++){
                townsInCurrentPage.add(allTownsInNation.get(townsInPage));
                townsInPage++;
            }
            int menuSlot = 10;
            for (Town town : townsInCurrentPage) {
                if(Metadata.getTownHidden(town)) {
                    if(!town.hasResident(res)) {
                        newPage.setItem(menuSlot, General.getItem(Material.RED_STAINED_GLASS_PANE, "<#FF5555><bold>Hidden Town", "hiddenTown"));
                        menuSlot++;
                        continue;
                    }
                }
                Material material = Main.getInstance().config().defaultItem;
                if (MetaDataUtil.hasMeta(town, Metadata.blockInMenu)) {
                    material = Material.valueOf(Metadata.getBlockInMenu(town));
                }
                newPage.setItem(menuSlot, General.getItem(material, "<#FF5555><bold>" + town.getName(), town.getName(), setGlobalLore(town)));
                menuSlot++;
            }
            if(getPagesCount(allTownsCount) > 0){
                if(pageNumber == 0){
                    newPage.setItem(23, General.getItem(Material.ARROW, "<#FFAA00><bold>Next Page", String.valueOf(pageNumber + 1)));
                }else if(pageNumber == getPagesCount(allTownsCount)){
                    newPage.setItem(21, General.getItem(Material.ARROW, "<#FFAA00><bold>Previous Page", String.valueOf(pageNumber - 1)));
                }else{
                    newPage.setItem(23, General.getItem(Material.ARROW, "<#FFAA00><bold>Next Page", String.valueOf(pageNumber + 1)));
                    newPage.setItem(21, General.getItem(Material.ARROW, "<#FFAA00><bold>Previous Page", String.valueOf(pageNumber - 1)));
                }
            }
            newPage.setItem(22, General.getItem(Material.ARROW, "<#FFAA00><bold>Back to Nations", "0"));
            if(nation == null){
                if(warMenu){
                    newPage.setItem(26, General.getItem(main.config().menuFiller, " ", "atWar"));
                }else if(privateMenu){
                    newPage.setItem(26, General.getItem(main.config().menuFiller, " ", "notPublic"));
                }else{
                    newPage.setItem(26, General.getItem(main.config().menuFiller, " ", "noNation"));
                }
            }else{ newPage.setItem(26, General.getItem(main.config().menuFiller, " ", nation.getName())); }
            General.fillEmpty(newPage, General.getItem(main.config().menuFiller, " ", "townMenu"));
            inventories.add(newPage);
        }
        return inventories;
    }

    public static ArrayList<Component> setGlobalLore(Town town){
        String spawnCost = String.valueOf(town.getSpawnCost());
        if(!town.isPublic()) spawnCost = "Private";

        MiniMessage miniMessage = MiniMessage.miniMessage();
        ArrayList<Component> itemlore = new ArrayList<>();
        if(town.hasNation()) itemlore.add(miniMessage.deserialize("<#FFAA00><bold>Nation" + reset + "<bold>: <#00AAAA>" + Objects.requireNonNull(town.getNationOrNull()).getName()));
        itemlore.add(miniMessage.deserialize("\"<#FFAA00><bold>Mayor" + reset + "<bold>: <#00AA00>\" + town.getMayor().getName()"));
        itemlore.add(miniMessage.deserialize("<#FFAA00><bold>Residents" + reset + "<bold>: <#FF55FF>" + town.getResidents().size()));
        itemlore.add(miniMessage.deserialize("<#FFAA00><bold>Spawn Cost" + reset + "<bold>: <#FF5555>" + spawnCost));
        return itemlore;
    }

    public static void teleportToTown(Player player, String townName){
        MiniMessage miniMessage = MiniMessage.miniMessage();
        if(!player.hasPermission("townyspawnmenu.menu.teleport")) {
            player.sendMessage(miniMessage.deserialize("<#FFAA00>[Towny Spawn Menu] <#FF5555>You can't do that!"));
            return;
        }
        Town town = TownyAPI.getInstance().getTown(townName);
        assert town != null;
        if(!town.isPublic()) return;
        player.performCommand("t spawn " + townName + " -ignore");
        PlayerTeleportedToTown playerTeleportedToTown = new PlayerTeleportedToTown(player, town);
        getPluginManager().callEvent(playerTeleportedToTown);
    }
}

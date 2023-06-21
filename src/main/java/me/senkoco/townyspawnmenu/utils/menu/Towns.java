package me.senkoco.townyspawnmenu.utils.menu;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;
import me.senkoco.townyspawnmenu.Main;
import me.senkoco.townyspawnmenu.utils.Metadata;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static me.senkoco.townyspawnmenu.utils.menu.General.getPagesCount;
import static org.bukkit.Bukkit.getPluginManager;

public class Towns {
    static Plugin plugin = getPluginManager().getPlugin("TownySpawnMenu");

    public static List<Inventory> getPages(Nation nation, boolean warMenu, boolean privateMenu){
        if(plugin != null){
            if(plugin instanceof Main){
                Main main = (Main) plugin;
            }
        }

        List<Town> allTownsInNation;
        if(nation == null) {
            List<Town> allTowns = new LinkedList<Town>(TownyAPI.getInstance().getTowns());
            int allTownsCount = allTowns.size();
            if(warMenu){
                allTownsInNation = new LinkedList<Town>();
                for(int i = 0; i < allTownsCount; i++){
                    if(allTowns.get(i).hasActiveWar()){
                        allTownsInNation.add(allTowns.get(i));
                    }
                }
            }else if(privateMenu) {
                allTownsInNation = new LinkedList<Town>();
                for(int i = 0; i < allTownsCount; i++){
                    if(!allTowns.get(i).isPublic()){
                        allTownsInNation.add(allTowns.get(i));
                    }
                }
            }else{
                allTownsInNation = new LinkedList<Town>(TownyAPI.getInstance().getTownsWithoutNation());
            }
        }
        else { allTownsInNation = new LinkedList<Town>(nation.getTowns()); }
        int allTownsCount = allTownsInNation.size();

        int townsInPage = 0;
        int inventorySlots = 7;
        List<Inventory> inventories = new LinkedList<Inventory>();

        for(int pageNumber = 0; pageNumber < getPagesCount(allTownsCount)+1; pageNumber++){
            Inventory newPage;
            if(nation == null) {
                if(warMenu){
                    newPage = Bukkit.createInventory(null, 27, "§6§lTowns§f§l: §3At War (" + (pageNumber+1 + "/" + (getPagesCount(allTownsCount)+1) + ")"));
                }else if(privateMenu){
                    newPage = Bukkit.createInventory(null, 27, "§6§lTowns§f§l: §3Private (" + (pageNumber+1 + "/" + (getPagesCount(allTownsCount)+1) + ")"));
                }else{
                    newPage = Bukkit.createInventory(null, 27, "§6§lNation-less§f§l: §3Towns (" + (pageNumber+1 + "/" + (getPagesCount(allTownsCount)+1) + ")"));
                }
            }
            else { newPage = Bukkit.createInventory(null, 27, "§6§l" + nation.getName() + "§f§l: §3Towns (" + (pageNumber+1 + "/" + (getPagesCount(allTownsCount)+1) + ")")); }
            List<Town> townsInCurrentPage = new LinkedList<Town>();
            if(pageNumber == getPagesCount(allTownsCount)) inventorySlots = allTownsCount - townsInPage;
            for(int j = 0; j < inventorySlots; j++){
                townsInCurrentPage.add(allTownsInNation.get(townsInPage));
                townsInPage++;
            }
            int menuSlot = 10;
            for(int k = 0; k < townsInCurrentPage.size(); k++){
                Town town = townsInCurrentPage.get(k);
                Material material = Material.valueOf(plugin.getConfig().getString("menu.defaultItem"));
                if(MetaDataUtil.hasMeta(town, Metadata.blockInMenu)){
                    material = Material.valueOf(Metadata.getBlockInMenu(town));
                }
                newPage.setItem(menuSlot, General.getItem(material, "§c§l" + town.getName(), town.getName(), setGlobalLore(town)));
                menuSlot++;
            }
            if(getPagesCount(allTownsCount) > 0){
                if(pageNumber == 0){
                    newPage.setItem(23, General.getItem(Material.ARROW, "§6§lNext Page", "" + (pageNumber + 1)));
                }else if(pageNumber == getPagesCount(allTownsCount)){
                    newPage.setItem(21, General.getItem(Material.ARROW, "§6§lPrevious Page", "" + (pageNumber - 1)));
                }else{
                    newPage.setItem(23, General.getItem(Material.ARROW, "§6§lNext Page", "" + (pageNumber + 1)));
                    newPage.setItem(21, General.getItem(Material.ARROW, "§6§lPrevious Page", "" + (pageNumber - 1)));
                }
            }
            newPage.setItem(22, General.getItem(Material.ARROW, "§6§lBack to Nations", "0"));
            if(nation == null){
                if(warMenu){
                    newPage.setItem(26, General.getItem(Material.BLACK_STAINED_GLASS_PANE, " ", "atWar"));
                }else if(privateMenu){
                    newPage.setItem(26, General.getItem(Material.BLACK_STAINED_GLASS_PANE, " ", "notPublic"));
                }else{
                    newPage.setItem(26, General.getItem(Material.BLACK_STAINED_GLASS_PANE, " ", "noNation"));
                }
            }else { newPage.setItem(26, General.getItem(Material.BLACK_STAINED_GLASS_PANE, " ", nation.getName())); }
            General.fillEmpty(newPage, General.getItem(Material.BLACK_STAINED_GLASS_PANE, " ", "townMenu"));
            inventories.add(newPage);
        }
        return inventories;
    }

    public static ArrayList<String> setGlobalLore(Town town){
        String spawnCost = String.valueOf(town.getSpawnCost());
        if(!town.isPublic()) { spawnCost = "Private"; }

        ArrayList<String> itemlore = new ArrayList<>();
        if(town.hasNation()) itemlore.add("§6§lNation§f§l: §3§l" + town.getNationOrNull().getName());
        itemlore.add("§6§lMayor§f§l: §2" + town.getMayor().getName());
        itemlore.add("§6§lResidents§f§l: §d" + town.getResidents().size());
        itemlore.add("§6§lSpawn Cost§f§l: §c" + spawnCost);
        return itemlore;
    }

    public static void teleportToTown(Player player, String townName){
        Town town = TownyAPI.getInstance().getTown(townName);
        if(!town.isPublic()) return;
        player.performCommand("t spawn " + townName + " -ignore");
    }
}

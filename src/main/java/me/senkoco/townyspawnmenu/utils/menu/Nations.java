package me.senkoco.townyspawnmenu.utils.menu;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;
import me.senkoco.townyspawnmenu.utils.Metadata;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static me.senkoco.townyspawnmenu.utils.menu.General.getPagesCount;
import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.Bukkit.getServer;

public class Nations {
    static Plugin plugin = getPluginManager().getPlugin("TownySpawnMenu");
    public static ItemStack noNation = General.getItem(Material.BLUE_STAINED_GLASS_PANE, "§c§lNation-less Towns", "noNation");
    public static ItemStack notPublic = General.getItem(Material.LIME_STAINED_GLASS_PANE, "§c§lPrivate Towns", "notPublic");
    public static ItemStack atWar = General.getItem(Material.PURPLE_STAINED_GLASS_PANE, "§c§lTowns at War", "atWar");

    public static List<Inventory> getPages(){
        List<Nation> allNations = new LinkedList<>(TownyAPI.getInstance().getNations());
        int allNationsCount = allNations.size();

        int nationsCount = 0;
        int inventorySlots = 7;
        List<Inventory> inventories = new LinkedList<>();

        for(int pageNumber = 0; pageNumber <= getPagesCount(allNationsCount); pageNumber++){
            Inventory newPage = Bukkit.createInventory(null, 27, "§6§lTowny§f§l: §3Nations (" + (pageNumber+1) + "/" + (getPagesCount(allNationsCount)+1) + ")");
            List<Nation> nationsInCurrentPage = new LinkedList<>();
            if(pageNumber == getPagesCount(allNationsCount)) inventorySlots = allNationsCount - nationsCount;
            for(int i = 0; i < inventorySlots; i++){
                nationsInCurrentPage.add(allNations.get(nationsCount));
                nationsCount++;
            }
            int menuSlot = 10;
            for (Nation nation : nationsInCurrentPage) {
                Material material = Material.valueOf(plugin.getConfig().getString("menu.defaultItem"));
                if (MetaDataUtil.hasMeta(nation, Metadata.blockInMenu)) {
                    material = Material.valueOf(Metadata.getBlockInMenu(nation));
                }
                newPage.setItem(menuSlot, General.getItem(material, "§c§l" + nation.getName(), nation.getName(), setGlobalLore(nation)));
                menuSlot++;
            }
            addNoNationsItem(newPage);
            addPrivatesItem(newPage);
            addAtWarItem(newPage);
            if(getPagesCount(allNationsCount) > 0){
                if(pageNumber == 0){
                    newPage.setItem(23, General.getItem(Material.ARROW, "§6§lNext Page", String.valueOf((pageNumber + 1))));
                }else if(pageNumber == getPagesCount(allNationsCount)){
                    newPage.setItem(21, General.getItem(Material.ARROW, "§6§lPrevious Page", String.valueOf(pageNumber - 1)));
                }else{
                    newPage.setItem(23, General.getItem(Material.ARROW, "§6§lNext Page", String.valueOf(pageNumber + 1)));
                    newPage.setItem(21, General.getItem(Material.ARROW, "§6§lPrevious Page", String.valueOf(pageNumber - 1)));
                }
            }
            //if(getServer().getPluginManager().getPlugin("TownyMenus") != null) newPage.setItem(22, General.getItem(Material.ARROW, "§6§lBack to Towny Menus", "BTTM"));
            General.fillEmpty(newPage, General.getItem(Material.BLACK_STAINED_GLASS_PANE, " ", "nationMenu"));
            inventories.add(newPage);
        }
        return inventories;
    }

    public static ArrayList<String> setGlobalLore(Nation nation){
        ArrayList<String> itemlore = new ArrayList<>();
        itemlore.add("§6§lLeader§f§l: §3" + nation.getKing().getName());
        itemlore.add("§6§lCapital§f§l: §2" + nation.getCapital().getName());
        itemlore.add("§6§lTowns§f§l: §9" + nation.getTowns().size());
        itemlore.add("§6§lTotal Residents§f§l: §d" + nation.getResidents().size());
        return itemlore;
    }

    public static void addPrivatesItem(Inventory inv){
        int privateTownsCount = 0;
        for(int j = 0; j < TownyAPI.getInstance().getTowns().size(); j++){
            if(!TownyAPI.getInstance().getTowns().get(j).isPublic()){
                privateTownsCount++;
            }
        }

        if(privateTownsCount != 0){
            inv.setItem(18, notPublic);
        }
    }

    public static void addNoNationsItem(Inventory inv){
        if(TownyAPI.getInstance().getTownsWithoutNation().size() != 0){
            inv.setItem(22, noNation);
        }
    }

    public static void addAtWarItem(Inventory inv){
        int townsAtWarCount = 0;
        for(int j = 0; j < TownyAPI.getInstance().getTowns().size(); j++){
            if(TownyAPI.getInstance().getTowns().get(j).hasActiveWar()){
                townsAtWarCount++;
            }
        }

        if(townsAtWarCount != 0){
            inv.setItem(26, atWar);
        }
    }

    public static void openTownsOfNation(ItemStack current, Player player, boolean isTownMenu, Nation nation){
        String currentDName = Objects.requireNonNull(current.getItemMeta()).getDisplayName();
        String currentLName = current.getItemMeta().getLocalizedName();
        if(currentDName.equals("§6§lNext Page") || currentDName.equals("§6§lPrevious Page")){
            if(!isTownMenu){
                General.openInventory(player, Integer.parseInt(currentLName), Nations.getPages());
            }else{
                General.openInventory(player, Integer.parseInt(current.getItemMeta().getLocalizedName()), Towns.getPages(nation, false, false));
            }
            return;
        }else if(currentDName.equals("§6§lBack to Nations")){
            General.openInventory(player, Integer.parseInt(currentLName), Nations.getPages());
            return;
        }else if(currentLName.equals("noNation")){
            General.openInventory(player, 0, Towns.getPages(null, false, false));
            return;
        }else if(currentLName.equals("atWar")){
            General.openInventory(player, 0, Towns.getPages(null, true, false));
            return;
        }else if(currentLName.equals("notPublic")){
            General.openInventory(player, 0, Towns.getPages(null, false, true));
            return;
        }
        General.openInventory(player, 0, Towns.getPages(TownyAPI.getInstance().getNation(currentLName), false, false));
    }
}

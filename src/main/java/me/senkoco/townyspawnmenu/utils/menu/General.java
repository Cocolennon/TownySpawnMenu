package me.senkoco.townyspawnmenu.utils.menu;

import me.senkoco.townyspawnmenu.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class General {
    public static void fillEmpty(Inventory inv, ItemStack item){
        for(int i = 0; i < inv.getSize(); i++) if(inv.getItem(i) == null) inv.setItem(i, item);
    }

    public static ItemStack getItem(Material material, String newName, String localizedName){
        ItemStack it = new ItemStack(material, 1);
        ItemMeta itM = it.getItemMeta();
        assert itM != null;
        if(newName != null) itM.setDisplayName(newName);
        NamespacedKey buttonAction = new NamespacedKey(Main.getInstance(), "buttonAction");
        PersistentDataContainer pdc = itM.getPersistentDataContainer();
        if(localizedName != null) pdc.set(buttonAction, PersistentDataType.STRING, localizedName);
        it.setItemMeta(itM);
        return it;
    }

    public static ItemStack getItem(Material material, String newName, String localizedName, ArrayList<String> itemlore){
        ItemStack it = new ItemStack(material, 1);
        ItemMeta itM = it.getItemMeta();
        assert itM != null;
        if(newName != null) itM.setDisplayName(newName);
        NamespacedKey buttonAction = new NamespacedKey(Main.getInstance(), "buttonAction");
        PersistentDataContainer pdc = itM.getPersistentDataContainer();
        if(localizedName != null) pdc.set(buttonAction, PersistentDataType.STRING, localizedName);
        if(itemlore != null) itM.setLore(itemlore);
        it.setItemMeta(itM);
        return it;
    }

    public static void openInventory(Player player, int page, List<Inventory> inventories) {
        player.openInventory(inventories.get(page));
    }

    public static int getPagesCount(int count){ return (count / 7); }
}

package me.cocolennon.townyspawnmenu.utils.menu;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;
import me.cocolennon.townyspawnmenu.Main;
import me.cocolennon.townyspawnmenu.utils.Metadata;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class SpawnMenuInventoryHolder implements InventoryHolder {
    private Main main;
    private MiniMessage miniMessage;
    private Inventory inventory;
    private int size;
    private MenuType type;
    private Nation nation;

    NamespacedKey buttonKey = new NamespacedKey(main, "buttonAction");
    NamespacedKey nationKey = new NamespacedKey(main, "nationName");
    NamespacedKey townKey = new NamespacedKey(main, "townName");
    NamespacedKey pageNumberKey = new NamespacedKey(main, "pageNumber");

    public SpawnMenuInventoryHolder(int size, String title, MenuType type, Nation nation) {
        this.main = Main.getInstance();
        this.miniMessage = MiniMessage.miniMessage();
        this.size = size;
        this.type = type;
        this.nation = nation;
        this.inventory = Bukkit.createInventory(this, size, MiniMessage.miniMessage().deserialize(title));
    }

    public void addNationItem(Nation nation, int slot) {
        Material material = MetaDataUtil.hasMeta(nation, Metadata.blockInMenu) ? Material.matchMaterial(Metadata.getBlockInMenu(nation)) : main.config().defaultItem;
        ItemStack itemStack = new ItemStackBuilder(material, 1)
                .displayName(miniMessage.deserialize("<#FF5555><bold>" + nation.getName()))
                .setKeyValue(buttonKey, PersistentDataType.STRING, "nation")
                .setKeyValue(nationKey, PersistentDataType.STRING, nation.getName())
                .lore(getNationLore(nation)).get();
        setItem(slot, itemStack);
    }

    public void addHiddenNationItem(int slot) {
        ItemStack itemStack = new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, 1)
                .displayName(miniMessage.deserialize("<#FF5555><bold>Hidden Nation"))
                .setKeyValue(buttonKey, PersistentDataType.STRING, "hiddenNation").get();
        setItem(slot, itemStack);
    }

    public void addTownItem(Town town, int slot) {
        Material material = MetaDataUtil.hasMeta(town, Metadata.blockInMenu) ? Material.matchMaterial(Metadata.getBlockInMenu(town)) : main.config().defaultItem;
        ItemStack itemStack = new ItemStackBuilder(material, 1)
                .displayName(miniMessage.deserialize("<#FF5555><bold>" + town.getName()))
                .setKeyValue(buttonKey, PersistentDataType.STRING, "town")
                .setKeyValue(townKey, PersistentDataType.STRING, town.getName())
                .lore(getTownLore(town)).get();
        setItem(slot, itemStack);
    }

    public void addHiddenTownItem(int slot) {
        ItemStack itemStack = new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, 1)
                .displayName(miniMessage.deserialize("<#FF5555><bold>Hidden Town"))
                .setKeyValue(buttonKey, PersistentDataType.STRING, "hiddenTown").get();
        setItem(slot, itemStack);
    }

    public void addPageItem(String context, int page, int slot) {
        ItemStack itemStack = new ItemStackBuilder(Material.ARROW, 1)
                .displayName(miniMessage.deserialize("<#FFAA00><bold>" + context + "Page"))
                .setKeyValue(buttonKey, PersistentDataType.STRING, context.toLowerCase() + "Page")
                .setKeyValue(pageNumberKey, PersistentDataType.INTEGER, page).get();
        setItem(slot, itemStack);
    }

    public void addBackToNationsItem(int page) {
        ItemStack itemStack = new ItemStackBuilder(Material.ARROW, 1)
                .displayName(miniMessage.deserialize("<#FFAA00><bold>Back to Nations"))
                .setKeyValue(buttonKey, PersistentDataType.STRING, "backToNations")
                .setKeyValue(pageNumberKey, PersistentDataType.INTEGER, page).get();
        setItem(22, itemStack);
    }

    public void addMiscTownItems() {
        TownyAPI townyAPI = TownyAPI.getInstance();
        ItemStack noNation = new ItemStackBuilder(main.config().noNationItem, 1)
                .displayName(miniMessage.deserialize("<#FF5555><bold>Nation-less Towns"))
                .setKeyValue(buttonKey, PersistentDataType.STRING, "noNation").get();
        ItemStack notPublic = new ItemStackBuilder(main.config().privateItem, 1)
                .displayName(miniMessage.deserialize("<#FF5555><bold>Private Towns"))
                .setKeyValue(buttonKey, PersistentDataType.STRING, "notPublic").get();
        ItemStack atWar = new ItemStackBuilder(main.config().warItem, 1)
                .displayName(miniMessage.deserialize("<#FF5555><bold>Towns at War Towns"))
                .setKeyValue(buttonKey, PersistentDataType.STRING, "atWar").get();
        int privateTowns = 0;
        int townsAtWar = 0;
        for(Town town : townyAPI.getTowns()) {
            if(!town.isPublic()) privateTowns++;
            if(town.hasActiveWar()) townsAtWar++;
        }
        if(privateTowns > 0) inventory.setItem(18, notPublic);
        if(!townyAPI.getTownsWithoutNation().isEmpty()) inventory.setItem(22, noNation);
        if(townsAtWar > 0) inventory.setItem(26, atWar);
    }

    public static ArrayList<Component> getNationLore(Nation nation) {
        ArrayList<Component> itemLore = new ArrayList<>();
        itemLore.add(Component.text("<#FFAA00><bold>Leader<#FFFFFF>: <#00AAAA>" + nation.getKing().getName()));
        itemLore.add(Component.text("<#FFAA00><bold>Capital<#FFFFFF>: <#00AA00>" + nation.getCapital().getName()));
        itemLore.add(Component.text("<#FFAA00><bold>Towns<#FFFFFF>: <#5555FF>" + nation.getTowns().size()));
        itemLore.add(Component.text("<#FFAA00><bold>Total Residents<#FFFFFF>: <#FF55FF>" + nation.getResidents().size()));
        return itemLore;
    }

    public ArrayList<Component> getTownLore(Town town) {
        ArrayList<Component> itemLore = new ArrayList<>();
        if(town.hasNation()) itemLore.add(miniMessage.deserialize("<#FFAA00><bold>Nation<#FFFFFF>: <#00AAAA>" + town.getNationOrNull().getName()));
        itemLore.add(miniMessage.deserialize("<#FFAA00><bold>Mayor<#FFFFFF>: <#00AA00>" + town.getMayor().getName()));
        itemLore.add(miniMessage.deserialize("<#FFAA00><bold>Residents<#FFFFFF>: <#FF55FF>" + town.getResidents().size()));
        itemLore.add(miniMessage.deserialize("<#FFAA00><bold>Spawn Cost<#FFFFFF>: <#FF5555>" + (town.isPublic() ? town.getSpawnCost() : "Private")));
        return itemLore;
    }

    public String getButtonAction(ItemStack itemStack) {
        return itemStack.getItemMeta().getPersistentDataContainer().get(buttonKey, PersistentDataType.STRING);
    }

    public String getNationName(ItemStack itemStack) {
        return itemStack.getItemMeta().getPersistentDataContainer().get(nationKey, PersistentDataType.STRING);
    }

    public String getTownName(ItemStack itemStack) {
        return itemStack.getItemMeta().getPersistentDataContainer().get(nationKey, PersistentDataType.STRING);
    }

    public int getPageNumber(ItemStack itemStack) {
        return itemStack.getItemMeta().getPersistentDataContainer().get(pageNumberKey, PersistentDataType.INTEGER);
    }

    public void setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }

    public void fillEmpty(){
        ItemStack fillerItem = new ItemStackBuilder(main.config().menuFiller, 1)
                .displayName(Component.text(" "))
                .setKeyValue(buttonKey, PersistentDataType.STRING, "filler").get();
        for(int i = 0; i < size; i++) if(inventory.getItem(i) == null) inventory.setItem(i, fillerItem);
    }

    public int getSize() {
        return size;
    }

    public MenuType getType() {
        return type;
    }

    public Nation getNation() {
        return nation;
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
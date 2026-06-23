package me.cocolennon.townyspawnmenu.utils.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ItemStackBuilder {
    private ItemStack itemStack;

    public ItemStackBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    public ItemStackBuilder displayName(Component displayName) {
        itemStack.getItemMeta().displayName(displayName);
        return this;
    }

    public <P, C> ItemStackBuilder setKeyValue(NamespacedKey key, PersistentDataType<P, C> type, C value) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(key, type, value);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder lore(List<Component> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStack get() {
        return this.itemStack;
    }
}

package me.cocolennon.townyspawnmenu.commands.sub;

import com.palmergames.bukkit.towny.TownyAPI;
import me.cocolennon.townyspawnmenu.events.PlayerOpenedMenu;
import me.cocolennon.townyspawnmenu.utils.menu.Nations;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.LinkedList;
import java.util.List;

import static org.bukkit.Bukkit.getPluginManager;

public class MenuSubCommand {
    public static boolean execute(Player player) {
        if (!player.hasPermission("townyspawnmenu.menu.open")) {
            player.sendMessage("§6[Towny Spawn Menu] §cYou can't do that!");
            return false;
        }
        List<Inventory> inventories = new LinkedList<>(Nations.getPages(TownyAPI.getInstance().getResident(player)));
        player.openInventory(inventories.get(0));
        PlayerOpenedMenu playerOpenedMenu = new PlayerOpenedMenu(player);
        getPluginManager().callEvent(playerOpenedMenu);
        return true;
    }
}

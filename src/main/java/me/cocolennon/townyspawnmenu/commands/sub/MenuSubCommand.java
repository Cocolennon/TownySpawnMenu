package me.cocolennon.townyspawnmenu.commands.sub;

import com.palmergames.bukkit.towny.TownyAPI;
import me.cocolennon.townyspawnmenu.events.PlayerOpenedMenu;
import me.cocolennon.townyspawnmenu.utils.Localization;
import me.cocolennon.townyspawnmenu.utils.menu.Nations;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getPluginManager;

public class MenuSubCommand {
    public static boolean execute(Player player) {
        if (!player.hasPermission("townyspawnmenu.menu.open")) {
            player.sendMessage(Localization.get(player, "error.permission", true));
            return false;
        }
        player.playSound(player.getLocation(), Sound.BLOCK_BARREL_OPEN, 1.0f, 1.0f);
        Nations.getPages(TownyAPI.getInstance().getResident(player)).getFirst().openInventory(player, false);
        PlayerOpenedMenu playerOpenedMenu = new PlayerOpenedMenu(player);
        getPluginManager().callEvent(playerOpenedMenu);
        return true;
    }
}

package me.senkoco.townyspawnmenu.commands.sub;

import org.bukkit.entity.Player;

public class DefaultSubCommand {
    public static boolean execute(Player player, String label) {
        player.sendMessage("§6[Towny Spawn Menu] §cUsage: /" + label + "<info/menu>\n§c<> = Mandatory");
        return true;
    }
}
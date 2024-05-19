package me.senkoco.townyspawnmenu.commands.sub;

import me.senkoco.townyspawnmenu.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ConfigSubCommand {
    private static final Main instance = Main.getInstance();

    public static boolean execute(Player player, String[] args) {
        if(args.length == 1) {
            player.sendMessage("§6[Towny Spawn Menu] §cYou have to provide an option!");
            return false;
        }else if(args.length == 2) {
            player.sendMessage("§6[Towny Spawn Menu] §cYou must provide an item!");
            return false;
        }
        String option = args[1].toLowerCase();
        String itemName = args[2].replace("minecraft:", "");
        Material item = Material.getMaterial(itemName.toUpperCase());
        if(item == null) {
            player.sendMessage("§6[Towny Spawn Menu] §cYou must provide a valid item!\");");
            return false;
        }

        switch(option) {
            case "menu-filler" -> {
                instance.getConfig().set("menu.menuFiller", itemName);
                instance.saveConfig();
                player.sendMessage("§6[Towny Spawn Menu] §3Successfully set the item to fill in the empty space!");
                return true;
            }
            case "war-item" -> {
                instance.getConfig().set("menu.warItem", itemName);
                instance.saveConfig();
                player.sendMessage("§6[Towny Spawn Menu] §3Successfully set the item for towns at war!");
                return true;
            }
            case "no-nation" -> {
                instance.getConfig().set("menu.noNationItem", itemName);
                instance.saveConfig();
                player.sendMessage("§6[Towny Spawn Menu] §3Successfully set the item for towns which don't have a nation!");
                return true;
            }
            case "private" -> {
                instance.getConfig().set("menu.privateItem", itemName);
                instance.saveConfig();
                player.sendMessage("§6[Towny Spawn Menu] §3Successfully set the item for private towns!");
                return true;
            }
            default -> {
                player.sendMessage("§6[Towny Spawn Menu] §cYou have to provide a valid option!");
                return false;
            }
        }
    }
}

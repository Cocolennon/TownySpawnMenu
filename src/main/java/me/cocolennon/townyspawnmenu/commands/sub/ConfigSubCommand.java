package me.cocolennon.townyspawnmenu.commands.sub;

import me.cocolennon.townyspawnmenu.Main;
import me.cocolennon.townyspawnmenu.utils.Localization;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ConfigSubCommand {
    private static final Main instance = Main.getInstance();
    public static List<String> autoComplete = Arrays.asList("default-item", "menu-filler", "war-item", "no-nation", "private");

    public static boolean execute(Player player, String[] args) {
        if(args.length == 1) {
            player.sendMessage(Localization.get(player, "error.invalid.option", true));
            return false;
        }else if(args.length == 2) {
            player.sendMessage(Localization.get(player, "error.invalid.item", true));
            return false;
        }
        String itemName = args[2].replace("minecraft:", "");
        Material item = Material.getMaterial(itemName.toUpperCase());
        if(item == null) {
            player.sendMessage(Localization.get(player, "error.invalid.item", true));
            return false;
        }

        Main main = Main.getInstance();
        switch(args[1]) {
            case "default-item" -> {
                configSet(main, "default-item", itemName);
                player.sendMessage(Localization.get(player, "success.default-item", true, itemName.replace("_", " ")));
                return true;
            }
            case "menu-filler" -> {
                configSet(main, "menu-filler", itemName);
                player.sendMessage(Localization.get(player, "success.menu-filler", true, itemName.replace("_", " ")));
                return true;
            }
            case "war-item" -> {
                configSet(main, "war-item", itemName);
                player.sendMessage(Localization.get(player, "success.towns-at-war", true, itemName.replace("_", " ")));
                return true;
            }
            case "no-nation" -> {
                configSet(main, "no-nation-item", itemName);
                player.sendMessage(Localization.get(player, "success.nationless", true, itemName.replace("_", " ")));
                return true;
            }
            case "private" -> {
                configSet(main, "private-item", itemName);
                player.sendMessage(Localization.get(player, "success.private", true, itemName.replace("_", " ")));
                return true;
            }
            default -> {
                player.sendMessage(Localization.get(player, "error.invalid.option", true));
                return false;
            }
        }
    }

    private static void configSet(Main main, String node, Object value) {
        main.getConfig().set(node, value);
        main.saveConfig();
        main.loadConfig(true);
    }
}

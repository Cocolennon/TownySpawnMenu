package me.cocolennon.townyspawnmenu.commands.sub;

import me.cocolennon.townyspawnmenu.Main;
import me.cocolennon.townyspawnmenu.utils.Localization;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ConfigSubCommand {
    public static List<String> autoComplete = Arrays.asList("default-item", "menu-filler", "war-item", "no-nation", "private");

    public static boolean execute(Player player, String[] args) {
        if(args.length == 1) {
            player.sendMessage(Localization.get(player, "error.invalid.option", true));
            return false;
        }else if(args.length == 2) {
            player.sendMessage(Localization.get(player, "error.invalid.item", true));
            return false;
        }
        String materialName = args[2].replace("minecraft:", "");
        Material material = Material.matchMaterial(materialName);
        if(material == null) {
            player.sendMessage(Localization.get(player, "error.invalid.item", true));
            return false;
        }

        Main main = Main.getInstance();
        String formattedMaterial = materialName.toLowerCase().replace("_", " ");
        switch(args[1]) {
            case "default-item" -> {
                configSet(main, "default-item", materialName);
                player.sendMessage(Localization.get(player, "success.default-item", true, formattedMaterial));
                return true;
            }
            case "menu-filler" -> {
                configSet(main, "menu-filler", materialName);
                player.sendMessage(Localization.get(player, "success.menu-filler", true, formattedMaterial));
                return true;
            }
            case "war-item" -> {
                configSet(main, "war-item", materialName);
                player.sendMessage(Localization.get(player, "success.towns-at-war", true, formattedMaterial));
                return true;
            }
            case "no-nation" -> {
                configSet(main, "no-nation-item", materialName);
                player.sendMessage(Localization.get(player, "success.nationless", true, formattedMaterial));
                return true;
            }
            case "private" -> {
                configSet(main, "private-item", materialName);
                player.sendMessage(Localization.get(player, "success.private", true, formattedMaterial));
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

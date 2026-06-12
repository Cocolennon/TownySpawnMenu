package me.cocolennon.townyspawnmenu.commands.sub;

import me.cocolennon.townyspawnmenu.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ConfigSubCommand {
    private static final Main instance = Main.getInstance();
    public static List<String> autoComplete = Arrays.asList("default-item", "menu-filler", "war-item", "no-nation", "private");

    public static boolean execute(Player player, String[] args) {
        if(args.length == 1) {
            player.sendMessage("§6[Towny Spawn Menu] §cYou have to provide an option!");
            return false;
        }else if(args.length == 2) {
            player.sendMessage("§6[Towny Spawn Menu] §cYou must provide an item!");
            return false;
        }
        String option = args[1].toLowerCase();
        String itemName = args[2].replace("minecraft:", "").toUpperCase();
        Material item = Material.getMaterial(itemName.toUpperCase());
        if(item == null) {
            player.sendMessage("§6[Towny Spawn Menu] §cYou must provide a valid item!\");");
            return false;
        }

        Main main = Main.getInstance();
        switch(option) {
            case "default-item" -> {
                configSet(main, "default-item", itemName);
                player.sendMessage("§6[Towny Spawn Menu] §3Successfully set the default item for towns!");
                return true;
            }
            case "menu-filler" -> {
                configSet(main, "menu-filler", itemName);
                player.sendMessage("§6[Towny Spawn Menu] §3Successfully set the item to fill in the empty space!");
                return true;
            }
            case "war-item" -> {
                configSet(main, "war-item", itemName);
                player.sendMessage("§6[Towny Spawn Menu] §3Successfully set the item for towns at war!");
                return true;
            }
            case "no-nation" -> {
                configSet(main, "no-nation-item", itemName);
                player.sendMessage("§6[Towny Spawn Menu] §3Successfully set the item for towns which don't have a nation!");
                return true;
            }
            case "private" -> {
                configSet(main, "private-item", itemName);
                player.sendMessage("§6[Towny Spawn Menu] §3Successfully set the item for private towns!");
                return true;
            }
            default -> {
                player.sendMessage("§6[Towny Spawn Menu] §cYou have to provide a valid option!");
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

package me.senkoco.townyspawnmenu.commands;

import com.palmergames.bukkit.towny.Towny;
import me.senkoco.townyspawnmenu.Main;
import me.senkoco.townyspawnmenu.utils.menu.Nations;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CommandInfo implements TabExecutor {
    private String madeFor = "0.98.6.20";
    private String townyVersion = Towny.getPlugin().getVersion();

    private static final List<String> autoComplete = Arrays.asList("help", "info", "menu");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)) return false;

        if(args.length >= 1){
            switch(args[0]) {
                case "info":
                    List<String> info = new LinkedList<String>();
                    info.add("§c§l=========================");
                    info.add("§6§lTowny Spawn Menu v" + Main.getVersion());
                    if(!townyVersion.equals(madeFor)){
                        info.add("§6§lMade for Towny v" + madeFor + " (using Towny v" + townyVersion + ")");
                    }else {
                        info.add("§6§lMade for Towny v" + madeFor);
                    }
                    if(Main.getUsingOldVersion()){
                        info.add("§6§lAn update is available!");
                    }else{
                        info.add("§6§lYou're using the latest version");
                    }
                    info.add("§c§l=========================");

                    info.forEach(sender::sendMessage);
                    return true;
                case "help":
                    return true;
                case "menu":
                    List<Inventory> inventories = new LinkedList<Inventory>(Nations.getPages());
                    player.openInventory(inventories.get(0));
                    return true;
                default:
                    Usage(sender);
                    return false;
            }
        }
        Usage(sender);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            return autoComplete;
        }

        return null;
    }

    private void Usage(CommandSender sender){
        sender.sendMessage("§cUsage: /townyspawnmenu <help/info/menu>\n§c<> = Mandatory");
    }
}

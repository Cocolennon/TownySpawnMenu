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

public class MainCommand implements TabExecutor {
    private String madeFor = "0.99.0.1";
    private String townyVersion = Towny.getPlugin().getVersion();

    private static final List<String> autoComplete = Arrays.asList("help", "info", "menu");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)) return false;

        if(args.length >= 1){
            switch(args[0]) {
                case "info":
                    if(!sender.hasPermission("townyspawnmenu.showinfo")) { sender.sendMessage("§4You can't do that!"); return false; }
                    sendInfo(sender);
                    return true;
                case "help":
                    Usage(sender);
                    return true;
                case "menu":
                    if(!sender.hasPermission("townyspawnmenu.menu.open")) { sender.sendMessage("§4You can't do that!"); return false; }
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
            if(args.length == 1) return autoComplete;
        }

        return null;
    }

    private void sendInfo(CommandSender sender){
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
    }

    private void Usage(CommandSender sender){
        sender.sendMessage("§cUsage: /townyspawnmenu <help/info/menu>\n§c<> = Mandatory");
    }
}

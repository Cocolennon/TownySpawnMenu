package me.senkoco.townyspawnmenu.commands;

import com.palmergames.bukkit.towny.Towny;
import me.senkoco.townyspawnmenu.Main;
import me.senkoco.townyspawnmenu.events.PlayerOpenedMenu;
import me.senkoco.townyspawnmenu.utils.menu.Nations;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.bukkit.Bukkit.getPluginManager;

public class MainCommand implements TabExecutor {
    private final String townyVersion = Towny.getPlugin().getVersion();

    private static final List<String> autoComplete = Arrays.asList("info", "menu");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)) return false;
        if(!(args.length >= 1)) { Usage(sender, label); return false; }
        switch (args[0]) {
            case "info" -> {
                if (!sender.hasPermission("townyspawnmenu.showinfo")) {
                    sender.sendMessage("§6[Towny Spawn Menu] §cYou can't do that!");
                    return false;
                }
                sendInfo(sender);
                return true;
            }
            case "menu" -> {
                if (!sender.hasPermission("townyspawnmenu.menu.open")) {
                    sender.sendMessage("§6[Towny Spawn Menu] §cYou can't do that!");
                    return false;
                }
                List<Inventory> inventories = new LinkedList<>(Nations.getPages());
                player.openInventory(inventories.get(0));
                PlayerOpenedMenu playerOpenedMenu = new PlayerOpenedMenu(player);
                getPluginManager().callEvent(playerOpenedMenu);
                return true;
            }
            default -> {
                Usage(sender, label);
                return false;
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return null;
        if(args.length == 1) return autoComplete;
        return null;
    }

    private void sendInfo(CommandSender sender){
        List<String> info = new LinkedList<>();
        info.add("§c§l=========================");
        info.add("§6§lTowny Spawn Menu " + Main.getVersion());
        String madeFor = "0.100.1.11";
        if(!townyVersion.equals(madeFor)){
            info.add("§6Made for §lTowny " + madeFor + " §6(using §lTowny v" + townyVersion + "§6)");
        }else {
            info.add("§6Made for §lTowny " + madeFor);
        }
        if(Main.getUsingOldVersion()){
            info.add("§6An update is available!");
        }else{
            info.add("§6You're using the latest version");
        }
        info.add("§c§l=========================");

        info.forEach(sender::sendMessage);
    }

    private void Usage(CommandSender sender, String lbl){
        sender.sendMessage("§6[Towny Spawn Menu] §cUsage: /" + lbl + "<info/menu>\n§c<> = Mandatory");
    }
}

package me.senkoco.townyspawnmenu.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DefaultItemCommand implements TabExecutor {
    static Plugin plugin = Bukkit.getPluginManager().getPlugin("TownySpawnMenu");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("townyspawnmenu.set.default")) { sender.sendMessage("§6[Towny Spawn Menu] §cYou can't do that!"); return false; }

        Material material;
        try {
            material = Material.valueOf(args[0].replace("minecraft:", "").toUpperCase());
        }catch(IllegalArgumentException e){
            sender.sendMessage("§6[Towny Spawn Menu] §cPlease provide a valid item or block name!");
            sender.sendMessage("§cExample: nether_star (Case insensitive, spaces must be replaced by underscores)");
            return false;
        }
        plugin.getConfig().set("menu.defaultItem", material.name());
        plugin.saveConfig();
        sender.sendMessage("§6[Towny Spawn Menu] §3The default item for towns and nations in the menu now is: " + args[0].replace("minecraft:", ""));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(args.length == 1) {
                List<Material> allMaterials = new LinkedList<>(Arrays.stream(Material.values()).toList());
                List<String> materials = new LinkedList<>();
                for (Material current : allMaterials) {
                    if (current.name().startsWith("LEGACY_")) break;
                    materials.add("minecraft:" + current.name().toLowerCase());
                }
                return materials;
            }
        }
        return null;
    }
}

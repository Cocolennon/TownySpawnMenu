package me.senkoco.townyspawnmenu.commands.metadata;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.senkoco.townyspawnmenu.utils.Metadata;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MetadataTowns implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Resident res = TownyAPI.getInstance().getResident((Player)sender);
        if(!sender.hasPermission("townyspawnmenu.set.town") || !sender.hasPermission("townyspawnmenu.set.admin")) { sender.sendMessage("§6[Towny Spawn Menu] §cYou can't do that!"); return false; }
        if(!res.hasTown()) { sender.sendMessage("§6[Towny Spawn Menu] §cYou aren't in a town!"); return false; }
        if(!res.isMayor()) { sender.sendMessage("§6[Towny Spawn Menu] §cYou aren't  the mayor of your town!"); return false; }

        Material material;
        try {
            material = Material.valueOf(args[0].replace("minecraft:", "").toUpperCase());
        }catch(IllegalArgumentException e){
            sender.sendMessage("§6[Towny Spawn Menu] §cPlease provide a valid item or block name!");
            sender.sendMessage("§cExample: nether_star (Case insensitive, spaces must be replaced by underscores)");
            return false;
        }

        if(args.length > 1) {
            if(!sender.hasPermission("townyspawnmenu.set.admin")) { sender.sendMessage("§4You can't do that!"); return false; }
            Metadata.setBlockInMenu(TownyAPI.getInstance().getTown(args[1]), material.name());
            sender.sendMessage("§6[Towny Spawn Menu] §3This town's item/block in the menu now is: " + material.name().toLowerCase());
        }else{
            Metadata.setBlockInMenu(res.getTownOrNull(), material.name());
            sender.sendMessage("§6[Towny Spawn Menu] §3Your town's item/block in the menu now is: " + material.name().toLowerCase());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return null;
        if(args.length == 1) {
            List<Material> allMaterials = new LinkedList<>(Arrays.stream(Material.values()).toList());
            List<String> materials = new LinkedList<String>();
            for(int i = 0; i < allMaterials.size(); i++){
                Material current = allMaterials.get(i);
                if(current.name().startsWith("LEGACY_")) break;
                materials.add("minecraft:" + current.name().toLowerCase());
            }
            return materials;
        }
        if(sender.hasPermission("townyspawnmenu.set.admin")) {
            if(args.length == 2) {
                List<Town> allTowns = new LinkedList<Town>(TownyAPI.getInstance().getTowns());
                List<String> townNames = new LinkedList<String>();

                for(int i = 0; i < allTowns.size(); i++) {
                    Town current = allTowns.get(i);
                    townNames.add(current.getName());
                }
                return townNames;
            }
        }
        return null;
    }
}

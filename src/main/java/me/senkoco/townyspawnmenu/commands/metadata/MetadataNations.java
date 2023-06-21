package me.senkoco.townyspawnmenu.commands.metadata;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import me.senkoco.townyspawnmenu.utils.Metadata;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MetadataNations implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Resident res = TownyAPI.getInstance().getResident((Player)sender);
        if(!sender.hasPermission("townyspawnmenu.set.nation") || !sender.hasPermission("townyspawnmenu.set.admin")) { sender.sendMessage("§6[Towny Spawn Menu] §cYou can't do that!"); return false; }
        if(!res.hasNation()) { sender.sendMessage("§6[Towny Spawn Menu] §cYou aren't in a nation!"); return false; }
        if(!res.isKing()) { sender.sendMessage("§6[Towny Spawn Menu] §cYou aren't the king of your nation!"); return false; }

        Material material;
        try {
            material = Material.valueOf(args[0].replace("minecraft:", "").toUpperCase());
        }catch(IllegalArgumentException e){
            sender.sendMessage("§6[Towny Spawn Menu] §cPlease provide a valid item or block name!");
            sender.sendMessage("§cExample: nether_star (Case insensitive)");
            return false;
        }

        if(args.length > 1) {
            if(!sender.hasPermission("townyspawnmenu.set.admin")) { sender.sendMessage("§4You can't do that!"); return false; }
            Metadata.setBlockInMenu(TownyAPI.getInstance().getNation(args[1]), material.name());
            sender.sendMessage("§6[Towny Spawn Menu] §3This nation's item/block in the menu now is: " + material.name().toLowerCase());
        }else{
            Metadata.setBlockInMenu(res.getNationOrNull(), material.name());
            sender.sendMessage("§6[Towny Spawn Menu] §3Your nation's item/block in the menu now is: " + material.name().toLowerCase());
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
        return null;
    }
}

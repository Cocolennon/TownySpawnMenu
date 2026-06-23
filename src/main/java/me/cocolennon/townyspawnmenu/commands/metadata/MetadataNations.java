package me.cocolennon.townyspawnmenu.commands.metadata;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import me.cocolennon.townyspawnmenu.utils.Localization;
import me.cocolennon.townyspawnmenu.utils.Metadata;
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
        Resident resident = TownyAPI.getInstance().getResident((Player) sender);
        assert resident != null;
        if(!sender.hasPermission("townyspawnmenu.set.nation")) {
            sender.sendMessage(Localization.get(sender, "error.permission", true));
            return false;
        }
        if(!resident.hasNation()) {
            sender.sendMessage(Localization.get(sender, "error.towny.not-in-nation", true));
            return false;
        }
        if(!resident.isKing()) {
            sender.sendMessage(Localization.get(sender, "error.towny.not-king", true));
            return false;
        }
        Material material = Material.matchMaterial(args[0]);
        if (material == null) {
            sender.sendMessage(Localization.get(sender, "error.invalid.item", true));
            return false;
        }
        Nation nation = resident.getNationOrNull();
        Metadata.setBlockInMenu(nation, material.name());
        sender.sendMessage(Localization.get(sender, "success.set-menu-item", true, nation.getName(), material.name().toLowerCase()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return null;
        if(args.length == 1) {
            List<Material> allMaterials = new LinkedList<>(Arrays.stream(Material.values()).toList());
            List<String> materials = new LinkedList<>();
            for(Material current : allMaterials) {
                if(current.name().startsWith("LEGACY_")) break;
                materials.add("minecraft:" + current.name().toLowerCase());
            }
            return materials;
        }
        return null;
    }
}

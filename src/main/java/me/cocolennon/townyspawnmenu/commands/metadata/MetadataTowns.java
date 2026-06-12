package me.cocolennon.townyspawnmenu.commands.metadata;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
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
import java.util.Objects;

public class MetadataTowns implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Resident res = TownyAPI.getInstance().getResident((Player)sender);
        assert res != null;
        if(!sender.hasPermission("townyspawnmenu.set.town") || !sender.hasPermission("townyspawnmenu.set.admin")) {
            sender.sendMessage(Localization.get(sender, "error.permission", true));
            return false;
        }
        if(!res.hasTown()) {
            sender.sendMessage(Localization.get(sender, "error.towny.not-in-town", true));
            return false;
        }
        if(!res.isMayor()) {
            sender.sendMessage(Localization.get(sender, "error.towny.not-mayor", true));
            return false;
        }

        Material material;
        try {
            material = Material.valueOf(args[0].replace("minecraft:", "").toUpperCase());
        }catch(IllegalArgumentException e){
            sender.sendMessage(Localization.get(sender, "error.invalid.item", true));
            return false;
        }

        if(args.length > 1) {
            if(!sender.hasPermission("townyspawnmenu.set.admin")) {
                sender.sendMessage(Localization.get(sender, "error.permission", true));
                return false;
            }
            Town town = TownyAPI.getInstance().getTown(args[1]);
            Metadata.setBlockInMenu(Objects.requireNonNull(town), material.name());
            sender.sendMessage(Localization.get(sender, "success.set-menu-item", true, town.getName(), material.name().toLowerCase()));
        }else{
            Town town = res.getTownOrNull();
            Metadata.setBlockInMenu(Objects.requireNonNull(town), material.name());
            sender.sendMessage(Localization.get(sender, "success.set-menu-item", true, town.getName(), material.name().toLowerCase()));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return null;
        if(args.length == 1) {
            List<Material> allMaterials = new LinkedList<>(Arrays.stream(Material.values()).toList());
            List<String> materials = new LinkedList<>();
            for (Material current : allMaterials) {
                if (current.name().startsWith("LEGACY_")) break;
                materials.add("minecraft:" + current.name().toLowerCase());
            }
            return materials;
        }
        if(sender.hasPermission("townyspawnmenu.set.admin")) {
            if(args.length == 2) {
                List<Town> allTowns = new LinkedList<>(TownyAPI.getInstance().getTowns());
                List<String> townNames = new LinkedList<>();

                for (Town current : allTowns) {
                    townNames.add(current.getName());
                }
                return townNames;
            }
        }
        return null;
    }
}

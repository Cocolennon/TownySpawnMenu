package me.cocolennon.townyspawnmenu.commands.sub;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.cocolennon.townyspawnmenu.utils.Localization;
import me.cocolennon.townyspawnmenu.utils.Metadata;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class HideSubCommand {
    public static List<String> autoComplete = Arrays.asList("nation", "town");

    public static boolean execute(Player player, String type) {
        Resident res = TownyAPI.getInstance().getResident(player);
        assert res != null;
        if(!player.hasPermission("townyspawnmenu.hide") || !player.hasPermission("townyspawnmenu.set.admin")) {
            player.sendMessage(Localization.get(player, "error.permission", true));
            return false;
        }
        switch(type) {
            case "town" -> {
                return hideTown(player, res);
            }
            case "nation" -> {
                return hideNation(player, res);
            }
            default -> {
                player.sendMessage(Localization.get(player, "error.invalid.region", true));
                return false;
            }
        }
    }

    public static boolean hideTown(Player player, Resident res) {
        if(!res.hasTown()) {
            player.sendMessage(Localization.get(player, "error.towny.not-in-town", true));
            return false;
        }
        if(!res.isMayor()) {
            player.sendMessage(Localization.get(player, "error.towny.not-mayor", true));
            return false;
        }
        Town town = res.getTownOrNull();
        Metadata.setTownHidden(town);
        if(Metadata.getTownHidden(town)) {
            player.sendMessage(Localization.get(player, "success.hide-town-nation", true, town.getName()));
        }else{
            player.sendMessage(Localization.get(player, "success.show-town-nation", true, town.getName()));
        }
        return true;
    }

    public static boolean hideNation(Player player, Resident res) {
        if(!res.hasNation()) {
            player.sendMessage(Localization.get(player, "error.towny.not-in-nation", true));
            return false;
        }
        if(!res.isKing()) {
            player.sendMessage(Localization.get(player, "error.towny.not-king", true));
            return false;
        }
        Nation nation = res.getNationOrNull();
        Metadata.setNationHidden(nation);
        if(Metadata.getNationHidden(nation)) {
            player.sendMessage(Localization.get(player, "success.hide-town-nation", true, nation.getName()));
        }else{
            player.sendMessage(Localization.get(player, "success.show-town-nation", true, nation.getName()));
        }
        return true;
    }
}

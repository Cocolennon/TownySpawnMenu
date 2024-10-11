package me.senkoco.townyspawnmenu.commands.sub;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import me.senkoco.townyspawnmenu.utils.Metadata;
import org.bukkit.entity.Player;

public class HideSubCommand {
    public static boolean execute(Player player, String type) {
        Resident res = TownyAPI.getInstance().getResident(player);
        assert res != null;
        if(!player.hasPermission("townyspawnmenu.hide") || !player.hasPermission("townyspawnmenu.set.admin")) { player.sendMessage("§6[Towny Spawn Menu] §cYou can't do that!"); return false; }
        switch(type) {
            case "town" -> {
                try {
                    return hideTown(player, res);
                } catch (NotRegisteredException ignored) {
                    player.sendMessage("§6[Towny Spawn Menu] §cSomething went wrong!");
                    return false;
                }
            }
            case "nation" -> {
                try {
                    return hideNation(player, res);
                } catch (TownyException ignored) {
                    player.sendMessage("§6[Towny Spawn Menu] §cSomething went wrong!");
                    return false;
                }
            }
            default -> {
                player.sendMessage("§6[Towny Spawn Menu] §cPlease provide a valid region type! (nation/town)");
                return false;
            }
        }
    }

    public static boolean hideTown(Player player, Resident res) throws NotRegisteredException {
        if(!res.hasTown()) { player.sendMessage("§6[Towny Spawn Menu] §cYou aren't in a town!"); return false; }
        if(!res.isMayor()) { player.sendMessage("§6[Towny Spawn Menu] §cYou aren't the mayor of your town!"); return false; }
        Metadata.setTownHidden(res.getTown());
        player.sendMessage("§6[Towny Spawn Menu] §3Your town is now hidden from everyone but you and your town's residents!");
        return true;
    }

    public static boolean hideNation(Player player, Resident res) throws TownyException {
        if(!res.hasNation()) { player.sendMessage("§6[Towny Spawn Menu] §cYou aren't in a nation!"); return false; }
        if(!res.isKing()) { player.sendMessage("§6[Towny Spawn Menu] §cYou aren't the king of your nation!"); return false; }
        Metadata.setNationHidden(res.getNation());
        player.sendMessage("§6[Towny Spawn Menu] §3Your nation is now hidden from everyone but you and your nation's residents!");
        return true;
    }
}

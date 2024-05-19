package me.senkoco.townyspawnmenu.commands.sub;

import com.palmergames.bukkit.towny.Towny;
import me.senkoco.townyspawnmenu.Main;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class InfoSubCommand {
    private static final String townyVersion = Towny.getPlugin().getVersion();

    public static boolean execute(Player player) {
        if (!player.hasPermission("townyspawnmenu.showinfo")) {
            player.sendMessage("§6[Towny Spawn Menu] §cYou can't do that!");
            return false;
        }
        List<String> info = new LinkedList<>();
        info.add("§c§l=========================");
        info.add("§6§lTowny Spawn Menu " + Main.getVersion());
        String madeFor = "0.100.2.9";
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

        info.forEach(player::sendMessage);
        return true;
    }
}

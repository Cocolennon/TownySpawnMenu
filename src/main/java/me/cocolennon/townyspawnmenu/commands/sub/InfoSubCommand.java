package me.cocolennon.townyspawnmenu.commands.sub;

import com.palmergames.bukkit.towny.Towny;
import me.cocolennon.townyspawnmenu.Main;
import me.cocolennon.townyspawnmenu.utils.Localization;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class InfoSubCommand {
    private static final String townyVersion = Towny.getPlugin().getVersion();

    public static boolean execute(Player player) {
        if (!player.hasPermission("townyspawnmenu.showinfo")) {
            player.sendMessage(Localization.get(player, "error.permission", true));
            return false;
        }
        MiniMessage miniMessage = MiniMessage.miniMessage();
        Main main = Main.getInstance();
        List<Component> info = new LinkedList<>();
        info.add(miniMessage.deserialize("<#FF5555><bold>========================="));
        info.add(miniMessage.deserialize("<#FFAA00><bold>Towny Spawn Menu " + main.getVersion()));
        String madeFor = "0.103.0.0";
        if(!townyVersion.equals(madeFor)){
            info.add(miniMessage.deserialize("<#FFAA00>Made for <bold>Towny " + madeFor + " </bold>(using <bold>Towny v" + townyVersion + "</bold>)"));
        }else {
            info.add(miniMessage.deserialize("<#FFAA00>Made for <bold>Towny " + madeFor));
        }
        if(main.getUsingOldVersion()){
            info.add(miniMessage.deserialize("<#FFAA00>An update is available!"));
        }else{
            info.add(miniMessage.deserialize("<#FFAA00>You're using the latest version"));
        }
        info.add(miniMessage.deserialize("<#FF5555><bold>========================="));
        info.forEach(player::sendMessage);
        return true;
    }
}

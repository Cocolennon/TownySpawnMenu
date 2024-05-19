package me.senkoco.townyspawnmenu.commands;

import com.palmergames.bukkit.towny.Towny;
import me.senkoco.townyspawnmenu.commands.sub.ConfigSubCommand;
import me.senkoco.townyspawnmenu.commands.sub.DefaultSubCommand;
import me.senkoco.townyspawnmenu.commands.sub.InfoSubCommand;
import me.senkoco.townyspawnmenu.commands.sub.MenuSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class MainCommand implements TabExecutor {
    private final String townyVersion = Towny.getPlugin().getVersion();

    private static final List<String> autoComplete = Arrays.asList("info", "menu");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)) return false;
        if(!(args.length >= 1)) return DefaultSubCommand.execute(player, label);
        switch (args[0]) {
            case "info" -> {
                return InfoSubCommand.execute(player);
            }
            case "menu" -> {
                return MenuSubCommand.execute(player);
            }
            case "config", "cfg" -> {
                return ConfigSubCommand.execute(player, args);
            }
            default -> {
                return DefaultSubCommand.execute(player, label);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return null;
        if(args.length == 1) return autoComplete;
        return null;
    }
}

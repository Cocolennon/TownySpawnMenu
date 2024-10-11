package me.senkoco.townyspawnmenu.commands;

import com.palmergames.bukkit.towny.Towny;
import me.senkoco.townyspawnmenu.commands.sub.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainCommand implements TabExecutor {
    private final String townyVersion = Towny.getPlugin().getVersion();

    private static final List<String> autoComplete = Arrays.asList("menu", "config", "info", "hide");

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
            case "hide" -> {
                return HideSubCommand.execute(player, args[1]);
            }
            default -> {
                return DefaultSubCommand.execute(player, label);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return null;
        List<String> fixedAutoComplete = new ArrayList<>();
        if(args.length == 1) {
            for (String current : autoComplete) if (current.startsWith(args[0])) fixedAutoComplete.add(current);
            return fixedAutoComplete;
        }else if(args.length == 2){
            if(args[0].equals("config")){
                for(String current : ConfigSubCommand.autoComplete) if(current.startsWith(args[1])) fixedAutoComplete.add(current);
                return fixedAutoComplete;
            }else if(args[0].equals("hide")) {
                for(String current : HideSubCommand.autoComplete) if(current.startsWith(args[1])) fixedAutoComplete.add(current);
                return fixedAutoComplete;
            }
        }else if(args.length == 3) {
            if(args[0].equals("config")) {
                Material[] items = Material.values();
                for(Material mat : items) if(mat.name().startsWith(args[2])) fixedAutoComplete.add("minecraft:" + mat.name().toLowerCase());
                return fixedAutoComplete;
            }
        }
        return null;
    }
}

package me.senkoco.townyspawnmenu;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import me.senkoco.townyspawnmenu.commands.MainCommand;
import me.senkoco.townyspawnmenu.listeners.onClickEvent;
import me.senkoco.townyspawnmenu.utils.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI.CommandType;

public class Main extends JavaPlugin {
    public static String version;
    public static String latestVersion;
    public static boolean usingOldVersion = false;

    @Override
    public void onEnable() {
        new UpdateChecker(this, 105225).getVersion(cVersion -> {
            version = this.getDescription().getVersion();
            latestVersion = cVersion;
            if (!getVersion().equals(cVersion)) {
                getLogger().info("You are using an older version of Towny Spawn Menu, please update to version " + cVersion);
                usingOldVersion = true;
            }
        });
        registerCommandsAndListeners();

        getLogger().info("Plugin enabled!");
    }

    public void registerCommandsAndListeners(){
        this.getCommand("townyspawnmenu").setExecutor(new MainCommand());
        TownyCommandAddonAPI.addSubCommand(CommandType.TOWN, "spawn-menu", new MainCommand());
        this.getServer().getPluginManager().registerEvents(new onClickEvent(), this);
    }

    public static String getVersion() { return version; }
    public static String getLatestVersion(){
        return latestVersion;
    }
    public static boolean getUsingOldVersion() {
        return usingOldVersion;
    }
}

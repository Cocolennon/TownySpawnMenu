package me.senkoco.townyspawnmenu;

import me.senkoco.townyspawnmenu.commands.CommandInfo;
import me.senkoco.townyspawnmenu.listeners.onClickEvent;
import me.senkoco.townyspawnmenu.utils.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;

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
        this.getCommand("townyspawnmenu").setExecutor(new CommandInfo());
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

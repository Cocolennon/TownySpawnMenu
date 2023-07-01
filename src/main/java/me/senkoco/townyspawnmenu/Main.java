package me.senkoco.townyspawnmenu;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI.CommandType;
import com.palmergames.bukkit.towny.scheduling.TaskScheduler;
import com.palmergames.bukkit.towny.scheduling.impl.BukkitTaskScheduler;
import com.palmergames.bukkit.towny.scheduling.impl.FoliaTaskScheduler;
import com.palmergames.bukkit.util.Version;
import me.senkoco.townyspawnmenu.commands.DefaultItemCommand;
import me.senkoco.townyspawnmenu.commands.MainCommand;
import me.senkoco.townyspawnmenu.commands.metadata.MetadataNations;
import me.senkoco.townyspawnmenu.commands.metadata.MetadataTowns;
import me.senkoco.townyspawnmenu.listeners.onClickEvent;
import me.senkoco.townyspawnmenu.listeners.onPlayerJoinEvent;
import me.senkoco.townyspawnmenu.utils.UpdateChecker;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {
    public static String version;
    public static String latestVersion;
    public static boolean usingOldVersion = false;
    private static final Version requiredTownyVersion = Version.fromString("0.99.0.8");
    FileConfiguration config = getConfig();
    private final Object scheduler;

    public Main(){
        this.scheduler = townyVersionCheck() ? isFoliaClassPresent() ? new FoliaTaskScheduler(this) : new BukkitTaskScheduler(this) : null;
    }

    @Override
    public void onEnable() {
        checkVersion();
        setUpConfig();
        registerCommands();
        registerListeners();
        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        saveConfig();
        getLogger().info("Plugin disabled!");
    }

    public void setUpConfig(){
        config.addDefault("menu.defaultItem", "RED_STAINED_GLASS_PANE");
        config.options().copyDefaults(true);
        saveConfig();
    }

    public void checkVersion() {
        new UpdateChecker(this, this, 105225).getVersion(cVersion -> {
            version = this.getDescription().getVersion();
            latestVersion = cVersion;
            if (!getVersion().equals(cVersion)) {
                getLogger().info("You are using an older version of Towny Spawn Menu, please update to version " + cVersion);
                usingOldVersion = true;
            }
        });
    }

    public void registerCommands(){
        Objects.requireNonNull(getCommand("townyspawnmenu")).setExecutor(new MainCommand());
        TownyCommandAddonAPI.addSubCommand(CommandType.TOWN, "spawn-menu", new MainCommand());
        TownyCommandAddonAPI.addSubCommand(CommandType.TOWN_SET, "menu-item", new MetadataTowns());
        TownyCommandAddonAPI.addSubCommand(CommandType.NATION_SET, "menu-item", new MetadataNations());
        TownyCommandAddonAPI.addSubCommand(CommandType.TOWNYADMIN_SET, "default-item", new DefaultItemCommand());
    }

    public void registerListeners(){
        getServer().getPluginManager().registerEvents(new onClickEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerJoinEvent(), this);
    }

    public static String getVersion() { return version; }
    public static String getLatestVersion(){
        return latestVersion;
    }
    public static boolean getUsingOldVersion() {
        return usingOldVersion;
    }

    private boolean townyVersionCheck() {
        return Version.fromString(Towny.getPlugin().getVersion()).compareTo(requiredTownyVersion) >= 0;
    }

    public TaskScheduler getScheduler() {
        return (TaskScheduler) this.scheduler;
    }

    public static boolean isFoliaClassPresent() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}

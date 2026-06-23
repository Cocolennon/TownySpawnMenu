package me.cocolennon.townyspawnmenu;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI.CommandType;
import com.palmergames.bukkit.towny.scheduling.TaskScheduler;
import com.palmergames.bukkit.towny.scheduling.impl.BukkitTaskScheduler;
import com.palmergames.bukkit.towny.scheduling.impl.FoliaTaskScheduler;
import com.palmergames.bukkit.util.Version;
import me.cocolennon.townyspawnmenu.commands.MainCommand;
import me.cocolennon.townyspawnmenu.commands.metadata.MetadataNations;
import me.cocolennon.townyspawnmenu.commands.metadata.MetadataTowns;
import me.cocolennon.townyspawnmenu.listeners.InventoryClickListener;
import me.cocolennon.townyspawnmenu.listeners.PlayerJoinListener;
import me.cocolennon.townyspawnmenu.utils.Updater;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;
    private Config config;
    public static String version;
    public static String latestVersion;
    public static boolean usingOldVersion = false;
    private static final Version requiredTownyVersion = Version.fromString("0.102.0.0");
    private final Object scheduler;

    public Main(){
        this.scheduler = townyVersionCheck() ? isFoliaClassPresent() ? new FoliaTaskScheduler(this) : new BukkitTaskScheduler(this) : null;
    }

    @Override
    public void onEnable() {
        instance = this;
        loadConfig(false);
        checkVersion();
        registerCommands();
        registerListeners();
        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        saveConfig();
        getLogger().info("Plugin disabled!");
    }

    public void checkVersion() {
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            Updater updater = new Updater(this, "towny-spawn-menu", getFile(), config.autoUpdaterEnabled ? Updater.UpdateType.CHECK_DOWNLOAD : Updater.UpdateType.VERSION_CHECK, false);
            switch(updater.getResult()) {
                case SUCCESS -> {
                    usingOldVersion = true;
                    getLogger().info("Update will be applied after next restart!");
                }
                case UPDATE_FOUND -> {
                    usingOldVersion = true;
                    getLogger().info("You are using an older version of Filtering Hoppers, please update to version " + updater.getVersion());
                }
                case FAILED ->  {
                    usingOldVersion = true;
                    getLogger().warning("An update was found, but the updater failed to download it automatically. You might need to update manually!");
                }
            }
        });
    }

    public void loadConfig(boolean reload) {
        if(!reload) {
            saveDefaultConfig();
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
        reloadConfig();
        config = new Config(this);
    }

    public void registerCommands(){
        getCommand("townyspawnmenu").setExecutor(new MainCommand());
        TownyCommandAddonAPI.addSubCommand(CommandType.TOWN, "spawn-menu", new MainCommand());
        TownyCommandAddonAPI.addSubCommand(CommandType.TOWN_SET, "menu-item", new MetadataTowns());
        TownyCommandAddonAPI.addSubCommand(CommandType.NATION_SET, "menu-item", new MetadataNations());
    }

    public void registerListeners(){
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    public String getVersion() { return version; }
    public String getLatestVersion(){
        return latestVersion;
    }
    public boolean getUsingOldVersion() {
        return usingOldVersion;
    }

    private boolean townyVersionCheck() {
        return Version.fromString(Towny.getPlugin().getVersion()).compareTo(requiredTownyVersion) >= 0;
    }

    public TaskScheduler getScheduler() {
        return (TaskScheduler) this.scheduler;
    }

    public boolean isFoliaClassPresent() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public Config config() { return config; }
    public static Main getInstance() {
        return instance;
    }
}

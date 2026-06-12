package me.cocolennon.townyspawnmenu;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public final class Config {
    public final String defaultLocale;
    public final boolean autoUpdaterEnabled;
    public final Material menuFiller;
    public final Material warItem;
    public final Material noNationItem;
    public final Material privateItem;
    public final Material defaultItem;

    public Config(Main plugin) {
        FileConfiguration config = plugin.getConfig();
        this.defaultLocale = config.getString("default-locale");
        // Init Localization
        this.autoUpdaterEnabled = config.getBoolean("auto-updater-enabled");
        this.menuFiller = Material.valueOf(config.getString("menu-filler"));
        this.warItem = Material.valueOf(config.getString("war-item"));
        this.noNationItem = Material.valueOf(config.getString("no-nation-item"));
        this.privateItem = Material.valueOf(config.getString("private-item"));
        this.defaultItem = Material.valueOf(config.getString("default-item"));
    }
}
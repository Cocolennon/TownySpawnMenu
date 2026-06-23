package me.cocolennon.townyspawnmenu;

import me.cocolennon.townyspawnmenu.utils.Localization;
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
        Localization.init(plugin, defaultLocale);
        this.autoUpdaterEnabled = config.getBoolean("auto-updater-enabled");
        this.menuFiller = Material.matchMaterial(config.getString("menu-filler"));
        this.warItem = Material.matchMaterial(config.getString("war-item"));
        this.noNationItem = Material.matchMaterial(config.getString("no-nation-item"));
        this.privateItem = Material.matchMaterial(config.getString("private-item"));
        this.defaultItem = Material.matchMaterial(config.getString("default-item"));
    }
}
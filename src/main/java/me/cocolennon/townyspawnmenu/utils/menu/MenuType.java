package me.cocolennon.townyspawnmenu.utils.menu;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;

import java.util.List;
import java.util.function.BiFunction;

public enum MenuType {
    NATIONS((nation, api) -> api.getTowns()),
    TOWNS((nation, api) -> nation.getTowns()),
    NATIONLESS((nation, api) -> api.getTownsWithoutNation()),
    PRIVATE((nation, api) -> api.getTowns().stream().filter(town -> !town.isPublic()).toList()),
    AT_WAR((nation, api) -> api.getTowns().stream().filter(Town::hasActiveWar).toList());

    private final BiFunction<Nation, TownyAPI, List<Town>> townSupplier;

    MenuType(BiFunction<Nation, TownyAPI, List<Town>> townSupplier) {
        this.townSupplier = townSupplier;
    }

    public List<Town> getTowns(Nation nation, TownyAPI api) {
        return townSupplier.apply(nation, api);
    }
}
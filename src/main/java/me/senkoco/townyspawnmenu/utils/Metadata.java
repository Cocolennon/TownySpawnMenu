package me.senkoco.townyspawnmenu.utils;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.BooleanDataField;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;

public class Metadata {
    public static StringDataField blockInMenu = new StringDataField("townyspawnui_blockinmenu");
    public static BooleanDataField isHidden = new BooleanDataField("townyspawnmenu_ishidden");

    public static String getBlockInMenu(Nation nation) {
        return MetaDataUtil.getString(nation, blockInMenu);
    }
    public static String getBlockInMenu(Town town){
        return MetaDataUtil.getString(town, blockInMenu);
    }
    public static Boolean getNationHidden(Nation nation) { return MetaDataUtil.getBoolean(nation, isHidden); }
    public static Boolean getTownHidden(Town town) { return MetaDataUtil.getBoolean(town, isHidden); }

    public static void setBlockInMenu(Nation nation, String blockName){
        if(!nation.hasMeta("townyspawnui_blockinmenu")){
            MetaDataUtil.addNewStringMeta(nation, "townyspawnui_blockinmenu", blockName, true);
            return;
        }
        MetaDataUtil.setString(nation, blockInMenu, blockName, true);
    }

    public static void setBlockInMenu(Town town, String blockName){
        if(!town.hasMeta("townyspawnui_blockinmenu")){
            MetaDataUtil.addNewStringMeta(town, "townyspawnui_blockinmenu", blockName, true);
            return;
        }
        MetaDataUtil.setString(town, blockInMenu, blockName, true);
    }

    public static void setNationHidden(Nation nation) {
        if(!nation.hasMeta("townyspawnmenu_ishidden")) {
            MetaDataUtil.addNewBooleanMeta(nation, "townyspawnmenu_ishidden", true, true);
            return;
        }
        boolean isNationHidden = getNationHidden(nation);
        MetaDataUtil.setBoolean(nation, isHidden, !isNationHidden, true);
    }

    public static void setTownHidden(Town town) {
        if(!town.hasMeta("townyspawnmenu_ishidden")) {
            MetaDataUtil.addNewBooleanMeta(town, "townyspawnmenu_ishidden", true, true);
            return;
        }
        boolean isTownHidden = getTownHidden(town);
        MetaDataUtil.setBoolean(town, isHidden, !isTownHidden, true);
    }
}

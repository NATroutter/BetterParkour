package fi.natroutter.betterparkour.items;

import fi.natroutter.natlibs.objects.BaseItem;
import fi.natroutter.betterparkour.files.Lang;
import org.bukkit.Material;

public class Items {

    public static BaseItem wand() {
        BaseItem item = new BaseItem(Material.STICK);
        item.name(Lang.Wand_Name);
        item.lore(Lang.Wand_Lore);
        return item;
    }

}

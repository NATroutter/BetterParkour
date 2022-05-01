package net.natroutter.betterparkour.items;

import net.natroutter.betterparkour.Handler;
import net.natroutter.betterparkour.files.Translations;
import net.natroutter.natlibs.objects.BaseItem;
import org.bukkit.Material;

public class GeneralItems {

    private Handler handler;

    public GeneralItems(Handler handler) {
        this.handler = handler;
    }

    public BaseItem wand() {
        BaseItem item = new BaseItem(Material.STICK);
        item.setDisplayName(handler.getLang().get(Translations.Wand_Name));
        item.setLore(handler.getLang().get(Translations.Wand_Lore));
        return item;
    }

}

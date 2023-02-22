package fi.natroutter.betterparkour.items;

import fi.natroutter.natlibs.objects.BaseItem;
import fi.natroutter.betterparkour.Handler;
import fi.natroutter.betterparkour.files.Translations;
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

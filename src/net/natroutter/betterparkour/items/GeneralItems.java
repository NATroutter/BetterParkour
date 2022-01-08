package net.natroutter.betterparkour.items;

import net.natroutter.betterparkour.Handler;
import net.natroutter.natlibs.objects.BaseItem;
import org.bukkit.Material;

public class GeneralItems {

    private Handler handler;

    public GeneralItems(Handler handler) {
        this.handler = handler;
    }

    public BaseItem wand() {
        BaseItem item = new BaseItem(Material.STICK);
        item.setDisplayName(handler.getLang().getWand().name);
        item.setLore(handler.getLang().getWand().lore);
        return item;
    }

}

package net.natroutter.betterparkour.listeners;

import net.natroutter.betterparkour.Handler;
import net.natroutter.betterparkour.handlers.CourseBuilder;
import net.natroutter.betterparkour.items.GeneralItems;
import net.natroutter.natlibs.handlers.LangHandler.language.LangManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class WandUseListener implements Listener {

    Handler handler;
    GeneralItems items;
    CourseBuilder course;

    public WandUseListener(Handler handler) {
        this.handler = handler;
        this.items = handler.getItems();
        this.course = handler.getCourseBuilder();
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.hasItem() && e.getItem() != null && e.hasBlock() && e.getClickedBlock() != null) {
            Block block = e.getClickedBlock();
            ItemStack item = e.getItem();
            String name = item.getItemMeta().getDisplayName();

            if (name.equalsIgnoreCase(items.wand().getDisplayName())) {
                if (e.getHand() != EquipmentSlot.HAND) {return;}
                e.setCancelled(true);

                Action act = e.getAction();

                if (act == Action.LEFT_CLICK_BLOCK) {
                    course.setPos1(p, block.getLocation());
                } else if (act == Action.RIGHT_CLICK_BLOCK) {
                    course.setPos2(p, block.getLocation());
                }
            }
        }
    }

}

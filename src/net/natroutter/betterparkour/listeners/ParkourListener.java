package net.natroutter.betterparkour.listeners;

import net.natroutter.betterparkour.Handler;
import net.natroutter.betterparkour.files.Lang;
import net.natroutter.betterparkour.handlers.Courses;
import net.natroutter.betterparkour.handlers.ParkourHandler;
import net.natroutter.betterparkour.objs.Course;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

import java.util.HashMap;
import java.util.UUID;

public class ParkourListener implements Listener {

    private Courses courses;
    private ParkourHandler parkourHandler;
    private Lang lang;

    public HashMap<UUID, Long> cooldown = new HashMap<>();

    public ParkourListener(Handler handler) {
        this.courses = handler.getCourses();
        this.parkourHandler = handler.getParkourHandler();
        this.lang = handler.getLang();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        cooldown.remove(e.getPlayer().getUniqueId());
        parkourHandler.leave(e.getPlayer());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (parkourHandler.inCourse(p)) {
            parkourHandler.fellOff(e.getPlayer());
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String cmd = e.getMessage().trim();
        if (parkourHandler.inCourse(p)) {
            if (!cmd.startsWith("/bp") && !cmd.startsWith("/betterparkour")) {
                e.setCancelled(true);
                p.sendMessage(lang.Prefix + lang.CantWhileInCourse);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPressurePlate(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.PHYSICAL) {
            if (!e.hasBlock() || e.getClickedBlock() == null) {return;}
            Location plate = e.getClickedBlock().getLocation();

            if(cooldown.containsKey(p.getUniqueId())) {
                long secondsLeft = ((cooldown.get(p.getUniqueId())/1000)+1) - (System.currentTimeMillis()/1000);
                if(secondsLeft>0) {
                    return;
                }
            }
            cooldown.put(p.getUniqueId(), System.currentTimeMillis());

            Course course = courses.getCourse(p);
            if (course == null) {return;}

            if (plate.equals(course.getStart())) {
                parkourHandler.start(p, course);
            } else if (plate.equals(course.getEnd())) {
                parkourHandler.end(p);
            } else if (course.getChecks().contains(plate)) {
                parkourHandler.setCheck(p, plate);
            }
        } else {
            if (parkourHandler.inCourse(p)) {
                e.setCancelled(true);
                p.sendMessage(lang.Prefix + lang.CantWhileInCourse);
            }
        }

    }

}

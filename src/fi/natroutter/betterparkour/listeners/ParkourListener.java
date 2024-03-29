package fi.natroutter.betterparkour.listeners;

import fi.natroutter.betterparkour.BetterParkour;
import fi.natroutter.betterparkour.files.Lang;
import fi.natroutter.betterparkour.handlers.Courses;
import fi.natroutter.betterparkour.handlers.ParkourHandler;
import fi.natroutter.betterparkour.objects.Course;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.UUID;

public class ParkourListener implements Listener {

    private Courses courses = BetterParkour.getCourses();
    private ParkourHandler parkourHandler = BetterParkour.getParkourHandler();
    public HashMap<UUID, Long> cooldown = new HashMap<>();


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        for (PotionEffect eff : p.getActivePotionEffects()) {
            p.removePotionEffect(eff.getType());
        }
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
                p.sendMessage(Lang.CantWhileInCourse.prefixed());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleEnter(VehicleEnterEvent e) {
        if (!(e.getEntered() instanceof Player p)) {
            return;
        }
        if (parkourHandler.inCourse(p)) {
            e.setCancelled(true);
            p.sendMessage(Lang.CantWhileInCourse.prefixed());
        } else {
            if (e.getVehicle().getVehicle() instanceof Player v) {
                if (parkourHandler.inCourse(v)) {
                    e.setCancelled(true);
                    p.sendMessage(Lang.CantWhileInCourse.prefixed());
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPressurePlate(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.PHYSICAL) {
            if (!e.hasBlock() || e.getClickedBlock() == null) {return;}
            Location plate = e.getClickedBlock().getLocation();

            Course course = courses.getCourse(p);
            if (course == null) {return;}
            e.setCancelled(true);

            if(cooldown.containsKey(p.getUniqueId())) {
                long secondsLeft = ((cooldown.get(p.getUniqueId())/1000)+1) - (System.currentTimeMillis()/1000);
                if(secondsLeft>0) {
                    return;
                }
            }
            cooldown.put(p.getUniqueId(), System.currentTimeMillis());

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
                p.sendMessage(Lang.CantWhileInCourse.prefixed());
            }
        }

    }

}

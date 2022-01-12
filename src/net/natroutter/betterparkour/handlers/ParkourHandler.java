package net.natroutter.betterparkour.handlers;

import net.kyori.adventure.text.Component;
import net.natroutter.betterparkour.Handler;
import net.natroutter.betterparkour.files.Lang;
import net.natroutter.betterparkour.objs.ActiveCourse;
import net.natroutter.betterparkour.objs.Course;
import net.natroutter.betterparkour.objs.Statistic;
import net.natroutter.natlibs.utilities.StringHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ParkourHandler {

    private Handler handler;
    private Lang lang;
    private StatisticHandler statisticHandler;

    public ConcurrentHashMap<UUID, ActiveCourse> active = new ConcurrentHashMap<>();

    public ParkourHandler(Handler handler) {
        this.handler = handler;
        this.lang = handler.getLang();
        this.statisticHandler = handler.getStatisticHandler();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(handler.getInstance(), ()->{
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!inCourse(p)) {continue;}
                ActiveCourse ac = active.get(p.getUniqueId());
                StringHandler bar = new StringHandler(lang.ActionBar);
                bar.replaceAll("%name%", ac.getCourse().getName());
                bar.replaceAll("%diff%", ac.getCourse().getDiff());
                bar.replaceAll("%time%", (System.currentTimeMillis() - ac.getStartTime()));
                p.sendActionBar(Component.text(bar.build()));
            }
        }, 0, 5);

    }

    public void start(Player p, Course course) {
        if(active.containsKey(p.getUniqueId())) {return;}
        active.put(p.getUniqueId(), new ActiveCourse(System.currentTimeMillis(), null, null, course));
        p.setAllowFlight(false);
        p.setFlying(false);
        p.setGameMode(GameMode.ADVENTURE);
        p.setWalkSpeed(0.2F);
        p.setFlySpeed(0.1F);
        if (p.getPassengers().size() > 0) {
            for(Entity ent : p.getPassengers()) {
                ent.eject();
            }
        }
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);

    }

    public void leave(Player p) {
        if(!active.containsKey(p.getUniqueId())) {return;}
        ActiveCourse ac = active.get(p.getUniqueId());
        p.teleport(ac.getCourse().getSpawn());
        active.remove(p.getUniqueId());
    }

    public void end(Player p) {
        if(!active.containsKey(p.getUniqueId())) {return;}
        ActiveCourse ac = active.get(p.getUniqueId());
        active.remove(p.getUniqueId());
        ac.setEndTime(System.currentTimeMillis());

        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 100, 1);

        Statistic stat = new Statistic(ac.getCourse().getId(), p.getUniqueId(), p.getName(), (ac.getEndTime() - ac.getStartTime()));
        statisticHandler.set(stat);

        for (String line : lang.CourseFinished) {
            StringHandler str = new StringHandler(line);
            str.replaceAll("%courseName%", ac.getCourse().getName());
            str.replaceAll("%diff%", ac.getCourse().getDiff());
            str.replaceAll("%courseTime%", (ac.getEndTime() - ac.getStartTime()));
            str.send(p);
        }
    }

    public void setCheck(Player p, Location check) {
        if(!active.containsKey(p.getUniqueId())) {return;}
        ActiveCourse ac = active.get(p.getUniqueId());
        if (ac.getLastCheck() != null) {
            if (sameLoc(ac.getLastCheck(), check)) {return;}
        }

        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 100, 1);

        ac.setLastCheck(check);
        active.put(p.getUniqueId(), ac);
    }

    private boolean sameLoc(Location l1, Location l2) {
        return (l1.getWorld() == l2.getWorld() && l1.getBlockX() == l2.getBlockX() && l1.getBlockY() == l2.getBlockY() && l1.getBlockZ() == l2.getBlockZ());
    }

    public void fellOff(Player p) {
        if(!active.containsKey(p.getUniqueId())) {return;}
        ActiveCourse ac = active.get(p.getUniqueId());
        if (ac == null) {return;}

        if (!ac.getCourse().getArena().contains(p.getLocation())) {
            if (ac.getLastCheck() != null) {
                p.teleport(ac.getLastCheck());
                p.sendMessage(lang.Prefix + lang.LeaveInfoMessage);
            } else {
                p.teleport(ac.getCourse().getSpawn());
                active.remove(p.getUniqueId());
            }
        }


    }

    public boolean inCourse(Player p) {
        return active.containsKey(p.getUniqueId());
    }

}

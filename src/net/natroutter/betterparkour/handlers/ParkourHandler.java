package net.natroutter.betterparkour.handlers;

import net.kyori.adventure.text.Component;
import net.natroutter.betterparkour.Handler;
import net.natroutter.betterparkour.events.*;
import net.natroutter.betterparkour.files.Config;
import net.natroutter.betterparkour.files.Translations;
import net.natroutter.betterparkour.objs.ActiveCourse;
import net.natroutter.betterparkour.objs.Course;
import net.natroutter.betterparkour.objs.Statistic;
import net.natroutter.natlibs.handlers.LangHandler.language.LangManager;
import net.natroutter.natlibs.utilities.StringHandler;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ParkourHandler {

    private Handler handler;
    private LangManager lang;
    private StatisticHandler statisticHandler;
    private Config config;

    public ConcurrentHashMap<UUID, ActiveCourse> active = new ConcurrentHashMap<>();

    public ParkourHandler(Handler handler) {
        this.handler = handler;
        this.lang = handler.getLang();
        this.statisticHandler = handler.getStatisticHandler();
        this.config = handler.getConfig();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(handler.getInstance(), ()->{
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!inCourse(p)) {continue;}
                ActiveCourse ac = active.get(p.getUniqueId());
                StringHandler bar = new StringHandler(lang.get(Translations.ActionBar));
                long time = (System.currentTimeMillis() - ac.getStartTime());

                bar.replaceAll("%name%", ac.getCourse().getName());
                bar.replaceAll("%diff%", ac.getCourse().getDiff());

                long secs = TimeUnit.MILLISECONDS.toSeconds(time);
                long mills = time - (secs * 1000);

                bar.replaceAll("%secs%", secs);
                bar.replaceAll("%mills%", mills);

                p.sendActionBar(Component.text(bar.build()));

                if (time >= 86400000) { //leaves from parkour if more than 24h
                    leave(p);
                }

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
        if (p.getVehicle() != null) {
            p.getVehicle().eject();
        }
        for (PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }

        for (ItemStack armor : p.getInventory().getArmorContents()) {
            if (armor == null) {continue;}
            if (armor.getType() != Material.AIR) {
                armor.setAmount(0);
            }
        }

        p.eject();
        if (p.getPassengers().size() > 0) {
            for(Entity ent : p.getPassengers()) {
                p.removePassenger(ent);
                ent.eject();
            }
        }
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
        if (config.InvisibleInCourse) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false, false));
        }

        Bukkit.getPluginManager().callEvent(new ParkourJoinEvent(p, course));

    }

    public void leave(Player p) {
        if(!active.containsKey(p.getUniqueId())) {return;}
        ActiveCourse ac = active.get(p.getUniqueId());
        p.teleport(ac.getCourse().getSpawn());
        active.remove(p.getUniqueId());
        Bukkit.getPluginManager().callEvent(new ParkourLeaveEvent(p, ac.getCourse(), ac.getStartTime()));
    }

    public void end(Player p) {
        if(!active.containsKey(p.getUniqueId())) {return;}

        ActiveCourse ac = active.get(p.getUniqueId());
        ac.setEndTime(System.currentTimeMillis());

        active.remove(p.getUniqueId());
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 100, 1);

        ParkourFinishedEvent event = new ParkourFinishedEvent(p, ac.getCourse(), ac.getStartTime(), ac.getEndTime(), (ac.getEndTime() - ac.getStartTime()));
        Bukkit.getPluginManager().callEvent(event);

        for (String line : lang.getList(Translations.CourseFinished)) {
            StringHandler str = new StringHandler(line);
            str.replaceAll("%courseName%", ac.getCourse().getName());
            str.replaceAll("%diff%", ac.getCourse().getDiff());

            long time = (ac.getEndTime() - ac.getStartTime());
            long secs = TimeUnit.MILLISECONDS.toSeconds(time);
            long mills = time - (secs * 1000);

            str.replaceAll("%secs%", secs);
            str.replaceAll("%mills%", mills);

            str.send(p);
        }

        if (config.InvisibleInCourse) {
            p.removePotionEffect(PotionEffectType.INVISIBILITY);
        }

        if (!event.isCancelled()) {
            Statistic stat = new Statistic(ac.getCourse().getId(), p.getUniqueId(), p.getName(), (ac.getEndTime() - ac.getStartTime()));
            statisticHandler.set(stat);
        }
    }

    public void setCheck(Player p, Location check) {
        if(!active.containsKey(p.getUniqueId())) {return;}
        ActiveCourse ac = active.get(p.getUniqueId());
        if (ac.getLastCheck() != null) {
            if (sameLoc(ac.getLastCheck(), check)) {return;}
        }

        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 100, 1);

        Bukkit.getPluginManager().callEvent(new ParkourCheckPointEvent(p, ac.getCourse(), check));

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
                lang.send(p, Translations.Prefix, Translations.LeaveInfoMessage);
            } else {
                p.teleport(ac.getCourse().getSpawn());
                active.remove(p.getUniqueId());
            }
            Bukkit.getPluginManager().callEvent(new ParkourFellOffEvent(p, ac.getCourse(), ac.getStartTime(), ac.getLastCheck()));
        }


    }

    public boolean inCourse(Player p) {
        return active.containsKey(p.getUniqueId());
    }

}

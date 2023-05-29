package fi.natroutter.betterparkour.handlers;

import fi.natroutter.betterparkour.BetterParkour;
import fi.natroutter.betterparkour.events.*;
import fi.natroutter.natlibs.helpers.LangHelper;
import fi.natroutter.natlibs.utilities.StringHandler;
import net.kyori.adventure.text.Component;
import fi.natroutter.betterparkour.files.Config;
import fi.natroutter.betterparkour.files.Lang;
import fi.natroutter.betterparkour.objs.ActiveCourse;
import fi.natroutter.betterparkour.objs.Course;
import fi.natroutter.betterparkour.objs.Statistic;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class ParkourHandler {

    private StatisticHandler statisticHandler = BetterParkour.getStatisticHandler();
    private LangHelper lh = BetterParkour.getLangHelper();

    public ConcurrentHashMap<UUID, ActiveCourse> active = new ConcurrentHashMap<>();

    public ParkourHandler() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(BetterParkour.getInstance(), ()->{
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!inCourse(p)) {continue;}
                ActiveCourse ac = active.get(p.getUniqueId());
                long time = (System.currentTimeMillis() - ac.getStartTime());
                long secs = TimeUnit.MILLISECONDS.toSeconds(time);
                long mills = time - (secs * 1000);

                p.sendActionBar(Lang.ActionBar.asComponent(List.of(
                        Placeholder.parsed("name",ac.getCourse().getName()),
                        Placeholder.parsed("diff",ac.getCourse().getDiff()),
                        Placeholder.parsed("secs",String.valueOf(secs)),
                        Placeholder.parsed("mills",String.valueOf(mills))
                )));

                if (time >= 86400000) { //leaves from parkour if more than 24h (stupid safety feature)
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
        if (Config.InvisibleInCourse.asBoolean()) {
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

        long time = (ac.getEndTime() - ac.getStartTime());
        long secs = TimeUnit.MILLISECONDS.toSeconds(time);
        long mills = time - (secs * 1000);

        p.sendMessage(Lang.CourseFinished.asComponent(List.of(
                Placeholder.parsed("courseName",ac.getCourse().getName()),
                Placeholder.parsed("diff",ac.getCourse().getDiff()),
                Placeholder.parsed("secs",String.valueOf(secs)),
                Placeholder.parsed("mills",String.valueOf(mills))
        )));

        for (PotionEffect eff : p.getActivePotionEffects()) {
            p.removePotionEffect(eff.getType());
        }

        ParkourFinishedEvent event = new ParkourFinishedEvent(p, ac.getCourse(), ac.getStartTime(), ac.getEndTime(), (ac.getEndTime() - ac.getStartTime()));
        Bukkit.getPluginManager().callEvent(event);

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

        for (PotionEffect eff : p.getActivePotionEffects()) {
            p.removePotionEffect(eff.getType());
        }

        if (!ac.getCourse().getArena().contains(p.getLocation())) {
            if (ac.getLastCheck() != null) {
                p.teleport(ac.getLastCheck());
                lh.prefix(p, Lang.LeaveInfoMessage);
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

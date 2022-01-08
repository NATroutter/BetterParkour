package net.natroutter.betterparkour.handlers;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import net.natroutter.betterparkour.Handler;
import net.natroutter.betterparkour.files.Lang;
import net.natroutter.betterparkour.objs.Course;
import net.natroutter.betterparkour.objs.Statistic;
import net.natroutter.natlibs.handlers.Database.YamlDatabase;
import net.natroutter.natlibs.utilities.StringHandler;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TopHologramHandler {

    private Handler handler;
    private Lang lang;
    private Courses courses;
    private StatisticHandler statisticHandler;
    private YamlDatabase yaml;

    private ConcurrentHashMap<UUID, Hologram> holograms = new ConcurrentHashMap<>();
    private ConcurrentHashMap<UUID, List<Statistic>> datas = new ConcurrentHashMap<>();

    public TopHologramHandler(Handler handler) {
        this.handler = handler;
        this.lang = handler.getLang();
        this.courses = handler.getCourses();
        this.statisticHandler = handler.getStatisticHandler();
        this.yaml = handler.getYaml();
        this.lang = handler.getLang();

        if (handler.getHooks() == null || !handler.getHooks().getHolographicDisplays().isHooked()) {
            handler.console("§9[BetterParkour] §bToplist are not enabled because HolographicDisplays are not hooked!");
            return;
        }

        loadData();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(handler.getInstance(), ()->{
           loadHolograms();
        }, 20, 20*60);
    }

    public void loadHolograms() {
        if (handler.getHooks() == null || !handler.getHooks().getHolographicDisplays().isHooked()) {return;}
        for (Map.Entry<UUID, Hologram> entry : holograms.entrySet()) {
            if (entry.getValue() == null || entry.getKey() == null) {continue;}

            Course course = courses.getCourse(entry.getKey());
            Hologram holo = entry.getValue();

            if (course == null || holo == null) {continue;}
            holo.clearLines();

            for (String line : lang.getTopList().top) {
                StringHandler topL = new StringHandler(line);
                topL.replaceAll("%courseName%", course.getName());
                topL.replaceAll("%diff%", course.getDiff());
                holo.appendTextLine(topL.build());
            }
            if (datas != null) {
                if (datas.containsKey(course.getId())) {
                    List<Statistic> stats = datas.get(course.getId());

                    if (stats != null && !stats.isEmpty()) {
                        for (int i = 0; i < stats.size(); i++){
                            Statistic stat = stats.get(i);
                            if (stat.getName() == null) {continue;}
                            StringHandler placement = new StringHandler(lang.getTopList().entry);
                            placement.replaceAll("%pos%", (i+1));
                            placement.replaceAll("%name%", stat.getName());
                            placement.replaceAll("%time%", stat.getTime());
                            holo.appendTextLine(placement.build());
                        }
                    } else {
                        holo.appendTextLine(lang.getTopList().noentries);
                    }
                } else {
                    holo.appendTextLine(lang.getTopList().noentries);
                }
            } else {
                holo.appendTextLine(lang.getTopList().noentries);
            }
            for (String line : lang.getTopList().bottom) {
                StringHandler bottomL = new StringHandler(line);
                bottomL.replaceAll("%courseName%", course.getName());
                bottomL.replaceAll("%diff%", course.getDiff());
                holo.appendTextLine(bottomL.build());
            }


            statisticHandler.getTop10(course.getId(), (data)->{
                datas.put(course.getId(), data);
            });

        }
    }

    public void loadData() {
        if (handler.getHooks() == null || !handler.getHooks().getHolographicDisplays().isHooked()) {return;}
        for (Map.Entry<UUID, Hologram> entry : holograms.entrySet()) {
            if (entry.getValue() != null) {
                entry.getValue().delete();
            }
        }
        holograms.clear();
        for(Course course : courses.getCourses()) {
            if (course.getToplist() != null) {
                holograms.put(course.getId(), HologramsAPI.createHologram(handler.getInstance(), course.getToplist()));
            }
        }
    }

}

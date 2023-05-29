package fi.natroutter.betterparkour.handlers;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import fi.natroutter.betterparkour.BetterParkour;
import fi.natroutter.betterparkour.objs.Course;
import fi.natroutter.natlibs.helpers.LangHelper;
import fi.natroutter.natlibs.utilities.StringHandler;
import fi.natroutter.betterparkour.files.Lang;
import fi.natroutter.betterparkour.objs.Statistic;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class TopHologramHandler {

    private Courses courses = BetterParkour.getCourses();
    private StatisticHandler statisticHandler = BetterParkour.getStatisticHandler();
    private Hooks hooks = BetterParkour.getHooks();
    private LangHelper lh = BetterParkour.getLangHelper();

    private ConcurrentHashMap<UUID, Hologram> holograms = new ConcurrentHashMap<>();
    private ConcurrentHashMap<UUID, List<Statistic>> datas = new ConcurrentHashMap<>();

    public TopHologramHandler() {

        if (hooks == null || !hooks.getHolographicDisplays().isHooked()) {
            BetterParkour.log("§9[BetterParkour] §bToplist are not enabled because HolographicDisplays are not hooked!");
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(BetterParkour.getInstance(), this::loadHolograms, 20*10);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(BetterParkour.getInstance(), this::loadHolograms, 20, 20*60);
    }

    public void loadHolograms() {
        if (hooks == null || !hooks.getHolographicDisplays().isHooked()) {return;}
        loadData();
        for (Map.Entry<UUID, Hologram> entry : holograms.entrySet()) {
            if (entry.getValue() == null || entry.getKey() == null) {continue;}

            Course course = courses.getCourse(entry.getKey());
            Hologram holo = entry.getValue();

            if (course == null || holo == null) {continue;}
            holo.clearLines();

            Lang.TopList_Top.asLegacyList(List.of(
                    Placeholder.parsed("courseName", course.getName()),
                    Placeholder.parsed("diff", course.getDiff())
            )).forEach(holo::appendTextLine);

            if (datas != null) {
                if (datas.containsKey(course.getId())) {
                    List<Statistic> stats = datas.get(course.getId());

                    if (stats != null && !stats.isEmpty()) {
                        for (int i = 0; i < stats.size(); i++){
                            Statistic stat = stats.get(i);
                            if (stat.getName() == null) {continue;}

                            long secs = TimeUnit.MILLISECONDS.toSeconds(stat.getTime());
                            long mills = stat.getTime() - (secs * 1000);

                            holo.appendTextLine(Lang.TopList_Entry.asLegacy(List.of(
                                    Placeholder.parsed("pos",String.valueOf(i+1)),
                                    Placeholder.parsed("name",stat.getName()),
                                    Placeholder.parsed("secs",String.valueOf(secs)),
                                    Placeholder.parsed("mills",String.valueOf(mills))
                            )));

                        }
                    } else {
                        holo.appendTextLine(Lang.TopList_NoEntries.asLegacy());
                    }
                } else {
                    holo.appendTextLine(Lang.TopList_NoEntries.asLegacy());
                }
            } else {
                holo.appendTextLine(Lang.TopList_NoEntries.asLegacy());
            }

            Lang.TopList_Bottom.asLegacyList(List.of(
                    Placeholder.parsed("courseName", course.getName()),
                    Placeholder.parsed("diff", course.getDiff())
            )).forEach(holo::appendTextLine);


            statisticHandler.getTop10(course.getId(), (data)->{
                datas.put(course.getId(), data);
            });

        }
    }

    public void loadData() {
        if (hooks == null || !hooks.getHolographicDisplays().isHooked()) {return;}
        for (Map.Entry<UUID, Hologram> entry : holograms.entrySet()) {
            if (entry.getValue() != null) {
                entry.getValue().delete();
            }
        }
        holograms.clear();
        for(Course course : courses.getCourses()) {
            if (course.getToplist() != null) {
                holograms.put(course.getId(), HologramsAPI.createHologram(BetterParkour.getInstance(), course.getToplist()));
            }
        }
    }

}

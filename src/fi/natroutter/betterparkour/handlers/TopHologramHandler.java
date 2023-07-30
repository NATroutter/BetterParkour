package fi.natroutter.betterparkour.handlers;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import fi.natroutter.betterparkour.BetterParkour;
import fi.natroutter.betterparkour.files.Lang;
import fi.natroutter.betterparkour.handlers.Database.Database;
import fi.natroutter.betterparkour.objects.Course;
import fi.natroutter.betterparkour.objects.Statistic;
import fi.natroutter.natlibs.handlers.Hook;
import fi.natroutter.natlibs.utilities.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TopHologramHandler {

    private Courses courses = BetterParkour.getCourses();
    private Database database = BetterParkour.getDatabase();
    private Hook hologramHook = BetterParkour.getHologramHook();

    public TopHologramHandler() {

        if (hologramHook == null || !hologramHook.isHooked()) {
            BetterParkour.log("§9[BetterParkour] §bToplist are not enabled because DecentHolograms is not installed!");
            return;
        }

        loadHolograms();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(BetterParkour.getInstance(), this::loadHolograms, 20, 20*60);
    }

    private Hologram getHologram(Course course) {
        String holoName = "BetterParkour-"+course.getId().toString();
        Hologram holo = DHAPI.getHologram(holoName);
        if (holo == null) {
            holo = DHAPI.createHologram(holoName, course.getToplist());
        }
        return holo;
    }

    public void loadHolograms() {
        if (hologramHook == null || !hologramHook.isHooked()) {return;}

        for(Course course : courses.getCourses()) {
            if (course.getToplist() == null) continue;

            database.getTop10(course.getId(), (stats) -> {
                List<Component> data = new ArrayList<>();
                data.add(Lang.TopList_Top.asSingleComponent(
                        Placeholder.parsed("course", course.getName()),
                        Placeholder.parsed("diff", course.getDiff())
                ));

                if (stats != null && !stats.isEmpty()) {
                    for (int i = 0; i < stats.size(); i++){
                        Statistic stat = stats.get(i);
                        if (stat.getName() == null) {continue;}

                        long secs = TimeUnit.MILLISECONDS.toSeconds(stat.getTime());
                        long mills = stat.getTime() - (secs * 1000);

                        data.add(Lang.TopList_Entry.asComponent(
                                Placeholder.parsed("pos", String.valueOf(i+1)),
                                Placeholder.parsed("name", stat.getName()),
                                Placeholder.parsed("secs", String.valueOf(secs)),
                                Placeholder.parsed("mills", String.valueOf(mills))
                        ));

                    }
                } else {
                    data.add(Lang.TopList_NoEntries.asComponent());
                }

                data.add(Lang.TopList_Bottom.asSingleComponent(
                        Placeholder.parsed("course", course.getName()),
                        Placeholder.parsed("diff", course.getDiff())
                ));
                List<String> legacy = new ArrayList<>();
                for (Component line : data) {
                    legacy.add(Utilities.legacy(line));
                }

                Hologram holo = getHologram(course);
                DHAPI.setHologramLines(holo, legacy);
            });
        }
    }

}

package net.natroutter.betterparkour.handlers;

import net.natroutter.betterparkour.Handler;
import net.natroutter.betterparkour.objs.Course;
import net.natroutter.betterparkour.objs.Statistic;
import net.natroutter.natlibs.handlers.database.YamlDatabase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class StatisticHandler {

    private Handler handler;
    private YamlDatabase yaml;

    private Database database;
    private Courses courses;

    public ConcurrentHashMap<String, Statistic> statisticCache = new ConcurrentHashMap<>();
    public ConcurrentHashMap<UUID, List<Statistic>> statisticTop = new ConcurrentHashMap<>();

    public StatisticHandler(Handler handler) {
        this.handler = handler;
        this.yaml = handler.getYaml();
        this.database = handler.getDatabase();
        this.courses = handler.getCourses();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(handler.getInstance(), ()->{
            if (Bukkit.getOnlinePlayers().size() > 0) {
                save(true);
            }
        }, 0, 20*60*5);
    }

    public void save(boolean async) {
        database.saveBatch(statisticCache, async, (b)->{
            if (async) {
                for (Course course : courses.getCourses()) {
                    database.getTop10(course.getId(), list -> {
                        if (list != null) {
                            statisticTop.put(course.getId(), list);
                        } else {
                            handler.console("§9[BetterParkour][Database] §bDatabase returned null list! (1)");
                        }
                    });
                }
                List<UUID> onlineUUIDS = Bukkit.getOnlinePlayers().stream().flatMap(p -> Stream.of(p.getUniqueId())).toList();

                statisticCache.entrySet().stream().filter((entry) -> !onlineUUIDS.contains(entry.getValue().getPlayerID())).forEach(entry -> {
                    statisticCache.remove(entry.getKey());
                });
            }
        });

    }

    public void remove(UUID playerID, UUID courseID) {
        statisticCache.remove(playerID + "~" + courseID);
        List<Statistic> data = statisticTop.get(courseID);
        if (data != null) {
            for (int i = 0 ; i < data.size(); i++) {
                Statistic stat = data.get(i);
                if (stat.getPlayerID().equals(playerID)) {
                    data.remove(i);
                }
            }
        }
        database.deleteStats(playerID, courseID);
    }

    public void set(Statistic stat) {
        Statistic oldStats = statisticCache.get(stat.getKey());
        if (oldStats != null) {
            if (stat.getTime() < statisticCache.get(stat.getKey()).getTime()) {
                statisticCache.put(stat.getKey(), stat);
            }
        } else {
            statisticCache.put(stat.getKey(), stat);
        }

    }

    public void getTop10(UUID courseID, Consumer<List<Statistic>> consumer) {
        if (statisticTop.get(courseID) != null && !statisticTop.get(courseID).isEmpty()) {
            consumer.accept(statisticTop.get(courseID));
        } else {
            database.getTop10(courseID, list -> {
                if (list != null) {
                    statisticTop.put(courseID, list);
                    consumer.accept(list);
                } else {
                    handler.console("§9[BetterParkour][Database] §bDatabase returned null list! (2)");
                }
            });
        }
    }

}

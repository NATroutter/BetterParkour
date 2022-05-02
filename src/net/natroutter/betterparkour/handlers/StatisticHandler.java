package net.natroutter.betterparkour.handlers;

import net.natroutter.betterparkour.Handler;
import net.natroutter.betterparkour.objs.Course;
import net.natroutter.betterparkour.objs.Statistic;
import net.natroutter.natlibs.handlers.Database.YamlDatabase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class StatisticHandler {

    private Handler handler;
    private YamlDatabase yaml;

    private Database database;
    private Courses courses;

    ConcurrentHashMap<String, Statistic> statisticCache = new ConcurrentHashMap<>();
    ConcurrentHashMap<UUID, List<Statistic>> statisticTop = new ConcurrentHashMap<>();

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

    public void save(boolean async) {save(async, (b)->{});}
    public void save(boolean async, Consumer<Boolean> consumer) {
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
                Collection<? extends Player> players = Bukkit.getOnlinePlayers();
                List<UUID> uuids = players.stream().flatMap(p -> Stream.of(p.getUniqueId())).toList();

                statisticCache.entrySet().stream().filter((entry) -> !uuids.contains(entry.getValue().getPlayerID())).forEach(entry -> {
                    statisticCache.remove(entry.getKey());
                });
            }
            consumer.accept(true);
        });

    }

    public void remove(UUID playerID, UUID courseID) {
        database.deleteStats(playerID, courseID);
    }

    public void set(Statistic stat) {
        statisticCache.put(stat.getKey(), stat);
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

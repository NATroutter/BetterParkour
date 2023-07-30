package fi.natroutter.betterparkour.handlers.Database;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import fi.natroutter.betterparkour.BetterParkour;
import fi.natroutter.betterparkour.files.Config;
import fi.natroutter.betterparkour.objects.Course;
import fi.natroutter.betterparkour.objects.Statistic;
import fi.natroutter.natlibs.objects.MongoConfig;
import org.bson.conversions.Bson;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class Database extends MongoConnector {


    public Database(JavaPlugin instance) {
        super(instance, new MongoConfig(
                Config.DB_Database.asString(),
                Config.DB_User.asString(),
                Config.DB_Pass.asString(),
                Config.DB_Host.asString(),
                Config.DB_Port.asInteger()
        ));
        registerCollection("stats");
    }

    public MongoCollection<Statistic> getStats() {
        MongoDatabase db = getDatabase();
        if (db == null)return null;
        return db.getCollection("stats", Statistic.class);
    }

    public Statistic getStats(UUID playerID, Course course) {
        MongoCollection<Statistic> stats = getStats();
        if (stats == null) {
            BetterParkour.log("MongoDB : failed to get stats");
            return null;
        }

        Statistic entry = stats.find(Filters.and(
                Filters.eq("courseID", course.getId().toString()),
                Filters.eq("playerID", playerID.toString())
        )).first();

        if (entry == null) {
            entry = new Statistic(course.getId(), playerID, course.getName(), 0L);
            stats.insertOne(entry);
        }
        return entry;
    }

    public void save(Object entry) {
        if (entry instanceof Statistic entryStats) {
            MongoCollection<Statistic> stats = getStats();
            if (stats == null) {
                BetterParkour.log("MongoDB : failed to save stats " + entryStats.getPlayerID()+"~"+entryStats.getCourseID());
                return;
            }

            Bson cond = Filters.and(
                    Filters.eq("playerID", entryStats.getPlayerID()),
                    Filters.eq("courseID", entryStats.getCourseID())
            );

            Statistic oldStat = stats.find(cond).first();
            if (oldStat != null) {
                if (oldStat.getTime() < entryStats.getTime()) {
                    return;
                }
                stats.findOneAndReplace(cond, entryStats);
            } else {
                stats.insertOne(entryStats);
            }
        }
    }


    public void deleteStats(UUID playerID, UUID courseID) {
        MongoCollection<Statistic> stats = getStats();
        if (stats == null) {
            BetterParkour.log("MongoDB : failed to delete stats");
            return;
        }
        Bson cond = Filters.and(
                Filters.eq("playerID", playerID.toString()),
                Filters.eq("courseID", courseID.toString())
        );
        stats.deleteOne(cond);
    }

    public void getTop10(UUID courseID, Consumer<List<Statistic>> consumer) {
        MongoCollection<Statistic> stats = getStats();
        if (stats == null) {
            BetterParkour.log("MongoDB : failed to get top10");
            return;
        }
        Bson cond = Filters.eq("courseID", courseID.toString());
        consumer.accept(stats.find(cond).sort(Filters.eq("time", 1)).limit(10).into(new ArrayList<>()));
    }

    public void deleteStats(UUID courseID) {
        MongoCollection<Statistic> stats = getStats();
        if (stats == null) {
            BetterParkour.log("MongoDB : failed to delete stats");
            return;
        }
        Bson cond = Filters.eq("courseID", courseID.toString());
        stats.deleteMany(cond);
    }

}

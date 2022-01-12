package net.natroutter.betterparkour;

import net.natroutter.betterparkour.files.Config;
import net.natroutter.betterparkour.files.Lang;
import net.natroutter.betterparkour.handlers.CourseBuilder;
import net.natroutter.betterparkour.handlers.Courses;
import net.natroutter.betterparkour.handlers.ParkourHandler;
import net.natroutter.betterparkour.handlers.StatisticHandler;
import org.bukkit.entity.Player;

public class ParkourAPI {

    private Handler handler;

    public ParkourAPI(Handler handler) {
        this.handler = handler;
    }

    public ParkourHandler getParkourHandler() {
        return handler.getParkourHandler();
    }

    public Courses getCourses() {
        return handler.getCourses();
    }

    public StatisticHandler getStatistics() {
        return handler.getStatisticHandler();
    }

    public CourseBuilder getCourseBuilder() {return handler.getCourseBuilder();}

    public Lang getLang() {return handler.getLang();}

    public Config getConfig() {return handler.getConfig();}


}

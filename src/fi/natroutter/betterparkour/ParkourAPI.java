package fi.natroutter.betterparkour;

import fi.natroutter.betterparkour.handlers.ParkourHandler;
import fi.natroutter.natlibs.handlers.langHandler.language.LangManager;
import fi.natroutter.betterparkour.files.Config;
import fi.natroutter.betterparkour.handlers.CourseBuilder;
import fi.natroutter.betterparkour.handlers.Courses;
import fi.natroutter.betterparkour.handlers.StatisticHandler;

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

    public LangManager getLang() {return handler.getLang();}

    public Config getConfig() {return handler.getConfig();}


}

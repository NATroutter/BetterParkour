package fi.natroutter.betterparkour.files;

import fi.natroutter.betterparkour.BetterParkour;
import fi.natroutter.natlibs.config.IConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter @AllArgsConstructor
public enum Config implements IConfig {

    Language("Language"),
    InvisibleInCourse("InvisibleInCourse"),

    DB_Host("MongoDB.Host"),
    DB_Port("MongoDB.Port"),
    DB_User("MongoDB.Username"),
    DB_Pass("MongoDB.Password"),
    DB_Database("MongoDB.Database"),

    ;

    String path;

    @Override
    public JavaPlugin getPlugin() {
        return BetterParkour.getInstance();
    }
}

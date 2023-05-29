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

    Sql_Host("Sql.Host"),
    Sql_Port("Sql.Port"),
    Sql_User("Sql.Username"),
    Sql_Pass("Sql.Password"),
    Sql_Database("Sql.Database"),

    ;

    String path;

    @Override
    public JavaPlugin getPlugin() {
        return BetterParkour.getInstance();
    }
}

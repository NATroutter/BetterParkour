package fi.natroutter.betterparkour;

import fi.natroutter.betterparkour.commands.BetterParkourCMD;
import fi.natroutter.betterparkour.files.Lang;
import fi.natroutter.betterparkour.handlers.*;
import fi.natroutter.betterparkour.handlers.Database.Database;
import fi.natroutter.betterparkour.listeners.ParkourListener;
import fi.natroutter.betterparkour.listeners.WandUseListener;
import fi.natroutter.natlibs.bstats.bukkit.Metrics;
import fi.natroutter.natlibs.handlers.Hook;
import fi.natroutter.natlibs.handlers.database.YamlDatabase;
import fi.natroutter.natlibs.utilities.MojangAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterParkour extends JavaPlugin {

    @Getter private static BetterParkour instance;
    @Getter private static Hook hologramHook;
    @Getter private static YamlDatabase yaml;
    @Getter private static CourseBuilder courseBuilder;
    @Getter private static Courses courses;
    @Getter private static ParkourHandler parkourHandler;
    @Getter private static Database database;
    @Getter private static MojangAPI mojangAPI;
    @Getter private static TopHologramHandler topHologramHandler;

    @Override
    public void onEnable() {
        instance = this;

        PluginDescriptionFile pdf = instance.getDescription();
        ConsoleCommandSender console = Bukkit.getConsoleSender();

        yaml = new YamlDatabase(instance);

        console.sendMessage("§8─────────────────────────────────────────");
        console.sendMessage("§8┌[ §9BetterParkour §bv"+pdf.getVersion()+" §9Enabled §8]");
        console.sendMessage("§8├ §7Plugin by: §bNATroutter");
        console.sendMessage("§8├ §7Website: §bNATroutter.fi");
        console.sendMessage("§8└ §7Hooks:");
        hologramHook = new Hook.Builder(this, "DecentHolograms", true)
                .setHookedMessage("  §9+ §b<plugin> §7Hooked succesfully!")
                .setHookingFailedMessage("  §9- §b<plugin> §7Failed to hook!")
                .build();
        console.sendMessage("§8─────────────────────────────────────────");

        mojangAPI = new MojangAPI(instance);

        database = new Database(this);

        courses = new Courses();
        parkourHandler = new ParkourHandler();
        topHologramHandler = new TopHologramHandler();
        courseBuilder = new CourseBuilder();


        PluginManager pm = getServer().getPluginManager();
        CommandMap map = getServer().getCommandMap();

        pm.registerEvents(new WandUseListener(), this);
        pm.registerEvents(new ParkourListener(), this);

        map.register("betterparkour", new BetterParkourCMD());

        new Metrics(this, 15081);

    }

    public static boolean hasPerm(CommandSender sender, String perm) {
        if (sender.hasPermission(perm)) {
            return true;
        } else {
            sender.sendMessage(Lang.NoPerm.prefixed());
            return false;
        }
    }

    public static void log(String msg) {
        Bukkit.getConsoleSender().sendMessage(msg);
    }
}

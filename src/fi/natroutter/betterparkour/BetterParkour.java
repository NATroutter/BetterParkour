package fi.natroutter.betterparkour;

import fi.natroutter.betterparkour.files.Lang;
import fi.natroutter.betterparkour.handlers.*;
import fi.natroutter.betterparkour.listeners.WandUseListener;
import fi.natroutter.natlibs.bstats.bukkit.Metrics;
import fi.natroutter.betterparkour.commands.BetterParkourCMD;
import fi.natroutter.betterparkour.listeners.ParkourListener;
import fi.natroutter.natlibs.handlers.database.YamlDatabase;
import fi.natroutter.natlibs.helpers.LangHelper;
import fi.natroutter.natlibs.utilities.MojangAPI;
import fi.natroutter.natlibs.utilities.Utilities;
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
    @Getter private static LangHelper langHelper;
    @Getter private static Hooks hooks;
    @Getter private static YamlDatabase yaml;
    @Getter private static CourseBuilder courseBuilder;
    @Getter private static Courses courses;
    @Getter private static ParkourHandler parkourHandler;
    @Getter private static Database database;
    @Getter private static StatisticHandler statisticHandler;
    @Getter private static MojangAPI mojangAPI;
    @Getter private static TopHologramHandler topHologramHandler;

    @Override
    public void onEnable() {
        instance = this;
        langHelper = new LangHelper(Lang.Prefix);

        PluginDescriptionFile pdf = instance.getDescription();
        ConsoleCommandSender console = Bukkit.getConsoleSender();

        yaml = new YamlDatabase(instance);

        console.sendMessage("§8─────────────────────────────────────────");
        console.sendMessage("§8┌[ §9BetterParkour §bv"+pdf.getVersion()+" §9Enabled §8]");
        console.sendMessage("§8├ §7Plugin by: §bNATroutter");
        console.sendMessage("§8├ §7Website: §bNATroutter.fi");
        console.sendMessage("§8└ §7Hooks:");
        hooks = new Hooks(instance);
        console.sendMessage("§8─────────────────────────────────────────");

        mojangAPI = new MojangAPI(instance);

        database = new Database();
        if (!database.isValid()){return;}

        courses = new Courses();
        statisticHandler = new StatisticHandler();
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

    @Override
    public void onDisable() {
        if (getStatisticHandler() != null) {
            getStatisticHandler().save(false);
        }
    }

    public static boolean hasPerm(CommandSender sender, String perm) {
        if (sender.hasPermission(perm)) {
            return true;
        } else {
            langHelper.prefix(sender, Lang.NoPerm);
            return false;
        }
    }

    public static void log(String msg) {
        Bukkit.getConsoleSender().sendMessage(msg);
    }
}

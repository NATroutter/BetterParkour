package net.natroutter.betterparkour;

import net.natroutter.betterparkour.files.Config;
import net.natroutter.betterparkour.files.Lang;
import net.natroutter.betterparkour.handlers.*;
import net.natroutter.betterparkour.items.GeneralItems;
import net.natroutter.natlibs.handlers.Database.YamlDatabase;
import net.natroutter.natlibs.handlers.FileManager;
import net.natroutter.natlibs.objects.ConfType;
import net.natroutter.natlibs.utilities.MojangAPI;
import net.natroutter.natlibs.utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

public class Handler {

    private BetterParkour instance;
    private Hooks hooks;
    private Lang lang;
    private Config config;
    private GeneralItems items;
    private YamlDatabase yaml;
    private Utilities util;
    private CourseBuilder courseBuilder;
    private Courses courses;
    private ParkourHandler parkourHandler;
    private Database database;
    private StatisticHandler statisticHandler;
    private MojangAPI mojangAPI;
    private TopHologramHandler topHologramHandler;

    public BetterParkour getInstance() {return instance;}
    public Hooks getHooks() {return hooks;}
    public Lang getLang() {return lang;}
    public Config getConfig() {return config;}
    public YamlDatabase getYaml() {return yaml;}
    public Utilities getUtil() {return util;}
    public CourseBuilder getCourseBuilder() {return courseBuilder;}
    public Courses getCourses() {return courses;}
    public ParkourHandler getParkourHandler() {return parkourHandler;}
    public Database getDatabase() {return database;}
    public StatisticHandler getStatisticHandler() {return statisticHandler;}
    public MojangAPI getMojangAPI() {return mojangAPI;}
    public TopHologramHandler getTopHologramHandler() {return topHologramHandler;}

    public GeneralItems getItems() {return items;}

    public Handler(BetterParkour instance) {
        PluginDescriptionFile pdf = instance.getDescription();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage("§8─────────────────────────────────────────");
        console.sendMessage("§8┌[ §9BetterParkour §bv"+pdf.getVersion()+" §9Enabled §8]");
        console.sendMessage("§8├ §7Plugin by: §bNATroutter");
        console.sendMessage("§8├ §7Website: §bNATroutter.net");
        console.sendMessage("§8└ §7Hooks:");
        this.hooks = new Hooks(instance);
        console.sendMessage("§8─────────────────────────────────────────");

        this.instance = instance;
        this.util = new Utilities(instance);

        this.lang = new FileManager(instance, ConfType.Lang).load(Lang.class);
        this.config = new FileManager(instance, ConfType.Config).load(Config.class);
        this.yaml = new YamlDatabase(instance);

        this.items = new GeneralItems(this);

        this.mojangAPI = new MojangAPI(instance);

        this.database = new Database(this);

        this.courses = new Courses(this);

        this.statisticHandler = new StatisticHandler(this);
        this.parkourHandler = new ParkourHandler(this);

        this.topHologramHandler = new TopHologramHandler(this);

        this.courseBuilder = new CourseBuilder(this);

    }

    public void console(String msg) {
        Bukkit.getConsoleSender().sendMessage(msg);
    }

    public boolean hasPerm(CommandSender p, String perm) {
        if (p.hasPermission("betterparkour." + perm)) {
            return true;
        }
        p.sendMessage(lang.Prefix + lang.NoPerm);
        return false;
    }

}

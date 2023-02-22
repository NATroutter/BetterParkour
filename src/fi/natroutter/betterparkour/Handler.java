package fi.natroutter.betterparkour;

import fi.natroutter.betterparkour.files.Config;
import fi.natroutter.betterparkour.handlers.*;
import fi.natroutter.betterparkour.items.GeneralItems;
import fi.natroutter.natlibs.handlers.configuration.ConfigManager;
import fi.natroutter.natlibs.handlers.database.YamlDatabase;
import fi.natroutter.natlibs.handlers.langHandler.language.LangManager;
import fi.natroutter.natlibs.handlers.langHandler.language.Language;
import fi.natroutter.natlibs.handlers.langHandler.language.key.LanguageKey;
import fi.natroutter.natlibs.utilities.MojangAPI;
import fi.natroutter.natlibs.utilities.Utilities;
import fi.natroutter.betterparkour.files.Translations;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.Optional;

public class Handler {

    private BetterParkour instance;
    private Hooks hooks;
    private LangManager lang;
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

    public Config getConfig() {return config;}
    public Database getDatabase() {return database;}
    public BetterParkour getInstance() {return instance;}
    public Courses getCourses() {return courses;}
    public CourseBuilder getCourseBuilder() {return courseBuilder;}
    public Hooks getHooks() {return hooks;}
    public ParkourHandler getParkourHandler() {return parkourHandler;}
    public StatisticHandler getStatisticHandler() {return statisticHandler;}
    public LangManager getLang() {return lang;}
    public TopHologramHandler getTopHologramHandler() {return topHologramHandler;}
    public MojangAPI getMojangAPI() {return mojangAPI;}
    public Utilities getUtil() {return util;}
    public YamlDatabase getYaml() {return yaml;}
    public GeneralItems getItems() {return items;}

    public Handler(BetterParkour instance) {
        PluginDescriptionFile pdf = instance.getDescription();
        ConsoleCommandSender console = Bukkit.getConsoleSender();

        this.config = new ConfigManager(instance).load(Config.class);

        Optional<Language> language = Language.getFromKey(LanguageKey.of(config.language));
        this.lang = language.map(value -> new LangManager(instance, value)).orElseGet(() -> {
            console.sendMessage("§4["+instance.getName()+"][Lang] §cLanguage file defined in config not found, Using default!");
            return new LangManager(instance, Language.ENGLISH);
        });

        this.yaml = new YamlDatabase(instance);

        console.sendMessage("§8─────────────────────────────────────────");
        console.sendMessage("§8┌[ §9BetterParkour §bv"+pdf.getVersion()+" §9Enabled §8]");
        console.sendMessage("§8├ §7Plugin by: §bNATroutter");
        console.sendMessage("§8├ §7Website: §bNATroutter.net");
        console.sendMessage("§8└ §7Hooks:");
        this.hooks = new Hooks(instance);
        console.sendMessage("§8─────────────────────────────────────────");

        this.instance = instance;
        this.util = new Utilities(instance);
        this.items = new GeneralItems(this);
        this.mojangAPI = new MojangAPI(instance);

        this.database = new Database(this);
        if (!database.isValid()){return;}

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
        lang.send(p, Translations.Prefix, Translations.NoPerm);
        return false;
    }

}

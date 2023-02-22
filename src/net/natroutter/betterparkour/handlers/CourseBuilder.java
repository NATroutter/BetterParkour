package net.natroutter.betterparkour.handlers;

import fi.natroutter.natlibs.handlers.database.YamlDatabase;
import fi.natroutter.natlibs.handlers.langHandler.language.LangManager;
import fi.natroutter.natlibs.utilities.StringHandler;
import fi.natroutter.natlibs.utilities.Utilities;
import net.natroutter.betterparkour.Handler;
import net.natroutter.betterparkour.files.Translations;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CourseBuilder {

    private YamlDatabase yaml;
    private LangManager lang;
    private Utilities util;
    private Courses courses;
    private Database database;
    private Handler handler;
    private TopHologramHandler topHolo;

    public UUID CourseID;

    public CourseBuilder(Handler handler) {
        this.handler = handler;
        this.yaml = handler.getYaml();
        this.lang = handler.getLang();
        this.util = handler.getUtil();
        this.courses = handler.getCourses();
        this.database = handler.getDatabase();
        this.topHolo = handler.getTopHologramHandler();
    }

    public void abort(Player p) {
        if (CourseID != null) {
            yaml.save("courses", CourseID.toString(), null);
            CourseID = null;
            lang.send(p, Translations.Prefix, Translations.CourseCreatingAborted);
        } else {
            lang.send(p, Translations.Prefix, Translations.CourseNotSelected);
        }
    }

    public boolean save(Player p) {
        if (CourseID == null) {
            lang.send(p, Translations.Prefix, Translations.CourseNotSelected);
            return false;
        }
        if (courses.validateCourse(CourseID)) {
            lang.send(p, Translations.Prefix, Translations.CourseEditFinished);
            CourseID = null;
            courses.loadCourses();
            topHolo.loadData();
            topHolo.loadHolograms();
            return true;
        } else {
            lang.send(p, Translations.Prefix, Translations.CourseNotGood);
            return false;
        }
    }

    public boolean setSpawn(Player p, Location loc) {
        return setLoc(p,"spawn",loc, lang.get(Translations.SpawnSet));
    }
    public boolean setTopList(Player p, Location loc) {
        return setLoc(p,"toplist",loc, lang.get(Translations.TopListSet));
    }
    public boolean setPos1(Player p, Location loc) {
        return setLoc(p, "pos1",loc, lang.get(Translations.FirstCornerSet));
    }
    public boolean setPos2(Player p, Location loc) {
        return setLoc(p,"pos2",loc, lang.get(Translations.SecondCornerSet));
    }
    public boolean setStart(Player p) {
        return set(p,"start", lang.get(Translations.StartSet));
    }
    public boolean setEnd(Player p) {
        return set(p,"end", lang.get(Translations.EndSet));
    }
    public boolean setDiff(Player p, String val) {
        return setVal(p,"diff", val, lang.get(Translations.DifficultySet));
    }
    public boolean setName(Player p, String val) {
        return setVal(p,"name", val, lang.get(Translations.CourseRenamed));
    }
    public boolean addCheckpoint(Player p) {
        return add(p,"check", lang.get(Translations.CheckpointAdded));
    }

    public void giveWand(Player p) {
        if (CourseID == null) {
            lang.send(p, Translations.Prefix, Translations.CourseNotSelected);
            return;
        }
        p.getInventory().addItem(handler.getItems().wand());
        lang.send(p, Translations.Prefix, Translations.WandGive);
    }

    public void printCourses(Player p) {
        Set<String> keys = yaml.getKeys("courses");
        if (keys.size() > 0) {
            lang.sendList(p, Translations.CourseList_Top);
            for (String key : yaml.getKeys("courses")) {
                String name = yaml.getString("courses." + key, "name");
                String diff = yaml.getString("courses." + key, "diff");
                StringHandler line = new StringHandler(lang.get(Translations.CourseList_Entry));
                line.replaceAll("%name%",name);
                line.replaceAll("%diff%",diff);
                line.send(p);
            }
            lang.sendList(p, Translations.CourseList_Bottom);
        } else {
            lang.send(p, Translations.Prefix, Translations.CourseList_NoCourses);
        }
    }

    public void create(Player p, String name) {
        if (CourseID != null) {
            lang.send(p, Translations.Prefix, Translations.CourseInEdit);
            return;
        }

        if (yaml.getKeys("courses") != null) {
            for (String key : yaml.getKeys("courses")) {
                String courseN = yaml.getString("courses." + key, "name");
                if (courseN.equalsIgnoreCase(name)) {
                    lang.send(p, Translations.Prefix, Translations.CourseExist);
                    return;
                }
            }
        }

        UUID id = UUID.randomUUID();
        CourseID = id;
        yaml.save("courses." + id, "name", name);
        lang.send(p, Translations.Prefix, Translations.CourseCreated);
    }

    public void remove(Player p, String name) {
        for (String key : yaml.getKeys("courses")) {
            String courseN = yaml.getString("courses." + key, "name");
            if (courseN.equalsIgnoreCase(name)) {
                yaml.save("courses", key, null);
                database.deleteCourse(UUID.fromString(key));

                lang.send(p, Translations.Prefix, Translations.CourseRemoved);
                return;
            }
        }
        lang.send(p, Translations.Prefix, Translations.CourseDoesntExits);
    }

    public void edit(Player p, String name) {
        for (String key : yaml.getKeys("courses")) {
            String courseN = yaml.getString("courses." + key, "name");
            if (courseN.equalsIgnoreCase(name)) {
                CourseID = UUID.fromString(key);
                StringHandler msg = new StringHandler(lang.get(Translations.CourseEditSelected));
                msg.setPrefix(lang.get(Translations.Prefix));
                msg.replaceAll("%course%", courseN);
                msg.send(p);
                return;
            }
        }
        lang.send(p, Translations.Prefix, Translations.CourseDoesntExits);
    }

    public void printValidation(Player p) {
        if (CourseID == null) {
            lang.send(p, Translations.Prefix, Translations.CourseNotSelected);
            return;
        }
        ArrayList<String> resps = new ArrayList<>();

        String name = yaml.getString("courses." + CourseID, "name");
        Location pos1 = yaml.getLocation("courses." + CourseID, "pos1");
        Location pos2 = yaml.getLocation("courses." + CourseID, "pos2");
        Location start = yaml.getLocation("courses." + CourseID, "start");
        Location end = yaml.getLocation("courses." + CourseID, "end");
        String diff = yaml.getString("courses." + CourseID, "diff");
        Location spawn = yaml.getLocation("courses." + CourseID, "spawn");

        if (name == null || name.length() < 1) {
            resps.add(lang.get(Translations.ValidateCourse_Err_Name));
        } else {
            resps.add(lang.get(Translations.ValidateCourse_Good_Name));
        }

        if (pos1 == null) {
            resps.add(lang.get(Translations.ValidateCourse_Err_FirstCorner));
        } else {
            resps.add(lang.get(Translations.ValidateCourse_Good_FirstCorner));
        }

        if (pos2 == null) {
            resps.add(lang.get(Translations.ValidateCourse_Err_SecCorner));
        } else {
            resps.add(lang.get(Translations.ValidateCourse_Good_SecCorner));
        }

        if (start == null) {
            resps.add(lang.get(Translations.ValidateCourse_Err_Staring));
        } else {
            resps.add(lang.get(Translations.ValidateCourse_Good_Staring));
        }

        if (end == null) {
            resps.add(lang.get(Translations.ValidateCourse_Err_Ending));
        } else {
            resps.add(lang.get(Translations.ValidateCourse_Good_Ending));
        }

        if (diff == null || diff.length() < 1) {
            resps.add(lang.get(Translations.ValidateCourse_Err_Difficulty));
        } else {
            resps.add(lang.get(Translations.ValidateCourse_Good_Difficulty));
        }

        if (spawn == null) {
            resps.add(lang.get(Translations.ValidateCourse_Err_Spawn));
        } else {
            resps.add(lang.get(Translations.ValidateCourse_Good_Spawn));
        }

        for(String line : lang.getList(Translations.ValidateCourse_top)) {
            StringHandler m = new StringHandler(line);
            m.replaceAll("%id%", CourseID);
            m.replaceAll("%name%", name);
            m.send(p);
        }
        for (String part : resps) {
            StringHandler m = new StringHandler(lang.getList(Translations.ValidateCourse_Entry));
            m.replaceAll("%part%", part);
            m.send(p);
        }
        for(String line : lang.getList(Translations.ValidateCourse_Bottom)) {
            StringHandler m = new StringHandler(line);
            m.send(p);
        }


    }


    private boolean setLoc(Player p, String set, Location loc, String successMessage) {
        if (CourseID == null) {
            lang.send(p, Translations.Prefix, Translations.CourseNotSelected);
            return false;
        }
        yaml.saveLoc("courses." + CourseID, set, loc);
        p.sendMessage(lang.get(Translations.Prefix) + successMessage);
        return true;
    }

    private boolean set(Player p, String set, String successMessage) {
        if (CourseID == null) {
            lang.send(p, Translations.Prefix, Translations.CourseNotSelected);
            return false;
        }
        Block b = p.getTargetBlock(10);
        if (b == null) {
            lang.send(p, Translations.Prefix, Translations.InvalidBlockTarget);
            return false;
        }
        if (!b.getType().name().toLowerCase().endsWith("pressure_plate")) {
            lang.send(p, Translations.Prefix, Translations.PressurePlateNeeded);
            return false;
        }
        yaml.saveLoc("courses." + CourseID, set, b.getLocation());
        p.sendMessage(lang.get(Translations.Prefix) + successMessage);
        return true;
    }

    private boolean add(Player p, String set, String successMessage) {
        if (CourseID == null) {
            lang.send(p, Translations.Prefix, Translations.CourseNotSelected);
            return false;
        }
        Block b = p.getTargetBlock(10);
        if (b == null) {lang.send(p, Translations.Prefix, Translations.InvalidBlockTarget);}
        if (!b.getType().name().toLowerCase().endsWith("pressure_plate")) {
            lang.send(p, Translations.Prefix, Translations.PressurePlateNeeded);
            return false;
        }
        List<String> locs = yaml.getStringList("courses." + CourseID, set);
        locs.add(util.serializeLocation(b.getLocation(), '~'));
        yaml.save("courses." + CourseID, set, locs);
        p.sendMessage(lang.get(Translations.Prefix) + successMessage);
        return true;
    }

    private boolean setVal(Player p, String set, String val, String successMessage) {
        if (CourseID == null) {
            lang.send(p, Translations.Prefix, Translations.CourseNotSelected);
            return false;
        }

        yaml.save("courses." + CourseID, set, val);
        p.sendMessage(lang.get(Translations.Prefix) + successMessage);
        return true;
    }

}

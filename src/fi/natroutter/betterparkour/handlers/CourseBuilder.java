package fi.natroutter.betterparkour.handlers;

import fi.natroutter.betterparkour.BetterParkour;
import fi.natroutter.betterparkour.items.GItems;
import fi.natroutter.natlibs.handlers.database.YamlDatabase;
import fi.natroutter.natlibs.helpers.LangHelper;
import fi.natroutter.natlibs.utilities.StringHandler;
import fi.natroutter.natlibs.utilities.Utilities;
import fi.natroutter.betterparkour.files.Lang;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Handler;

public class CourseBuilder {

    private YamlDatabase yaml;
    private LangHelper lh;
    private Courses courses;
    private Database database;
    private Handler handler;
    private TopHologramHandler topHolo;

    public UUID CourseID;

    public CourseBuilder() {
        this.yaml = BetterParkour.getYaml();
        this.lh = BetterParkour.getLangHelper();
        this.courses = BetterParkour.getCourses();
        this.database = BetterParkour.getDatabase();
        this.topHolo = BetterParkour.getTopHologramHandler();
    }

    public void abort(Player p) {
        if (CourseID != null) {
            yaml.save("courses", CourseID.toString(), null);
            CourseID = null;
            lh.prefix(p, Lang.CourseCreatingAborted);
        } else {
            lh.prefix(p, Lang.CourseNotSelected);
        }
    }

    public boolean save(Player p) {
        if (CourseID == null) {
            lh.prefix(p, Lang.CourseNotSelected);
            return false;
        }
        if (courses.validateCourse(CourseID)) {
            lh.prefix(p, Lang.CourseEditFinished);
            CourseID = null;
            courses.loadCourses();
            topHolo.loadData();
            topHolo.loadHolograms();
            return true;
        } else {
            lh.prefix(p, Lang.CourseNotGood);
            return false;
        }
    }

    public boolean setSpawn(Player p, Location loc) {
        return setLoc(p,"spawn",loc, Lang.SpawnSet.asString());
    }
    public boolean setTopList(Player p, Location loc) {
        return setLoc(p,"toplist",loc, Lang.TopListSet.asString());
    }
    public boolean setPos1(Player p, Location loc) {
        return setLoc(p, "pos1",loc, Lang.FirstCornerSet.asString());
    }
    public boolean setPos2(Player p, Location loc) {
        return setLoc(p,"pos2",loc, Lang.SecondCornerSet.asString());
    }
    public boolean setStart(Player p) {
        return set(p,"start", Lang.StartSet.asString());
    }
    public boolean setEnd(Player p) {
        return set(p,"end", Lang.EndSet.asString());
    }
    public boolean setDiff(Player p, String val) {
        return setVal(p,"diff", val, Lang.DifficultySet.asString());
    }
    public boolean setName(Player p, String val) {
        return setVal(p,"name", val, Lang.CourseRenamed.asString());
    }
    public boolean addCheckpoint(Player p) {
        return add(p,"check", Lang.CheckpointAdded.asString());
    }

    public void giveWand(Player p) {
        if (CourseID == null) {
            lh.prefix(p, Lang.CourseNotSelected);
            return;
        }
        p.getInventory().addItem(GItems.wand());
        lh.prefix(p, Lang.WandGive);
    }

    public void printCourses(Player p) {
        Set<String> keys = yaml.getKeys("courses");
        if (keys.size() > 0) {
            lh.sendList(p, Lang.CourseList_Top);
            for (String key : yaml.getKeys("courses")) {
                String name = yaml.getString("courses." + key, "name");
                String diff = yaml.getString("courses." + key, "diff");
                StringHandler line = new StringHandler(Lang.CourseList_Entry.asString());
                line.replaceAll("%name%",name);
                line.replaceAll("%diff%",diff);
                line.send(p);
            }
            lh.sendList(p, Lang.CourseList_Bottom);
        } else {
            lh.prefix(p, Lang.CourseList_NoCourses);
        }
    }

    public void create(Player p, String name) {
        if (CourseID != null) {
            lh.prefix(p, Lang.CourseInEdit);
            return;
        }

        if (yaml.getKeys("courses") != null) {
            for (String key : yaml.getKeys("courses")) {
                String courseN = yaml.getString("courses." + key, "name");
                if (courseN.equalsIgnoreCase(name)) {
                    lh.prefix(p, Lang.CourseExist);
                    return;
                }
            }
        }

        UUID id = UUID.randomUUID();
        CourseID = id;
        yaml.save("courses." + id, "name", name);
        lh.prefix(p, Lang.CourseCreated);
    }

    public void remove(Player p, String name) {
        for (String key : yaml.getKeys("courses")) {
            String courseN = yaml.getString("courses." + key, "name");
            if (courseN.equalsIgnoreCase(name)) {
                yaml.save("courses", key, null);
                database.deleteCourse(UUID.fromString(key));

                lh.prefix(p, Lang.CourseRemoved);
                return;
            }
        }
        lh.prefix(p, Lang.CourseDoesntExits);
    }

    public void edit(Player p, String name) {
        for (String key : yaml.getKeys("courses")) {
            String courseN = yaml.getString("courses." + key, "name");
            if (courseN.equalsIgnoreCase(name)) {
                CourseID = UUID.fromString(key);
                StringHandler msg = new StringHandler(Lang.CourseEditSelected.asString());
                msg.setPrefix(Lang.Prefix.asString());
                msg.replaceAll("%course%", courseN);
                msg.send(p);
                return;
            }
        }
        lh.prefix(p, Lang.CourseDoesntExits);
    }

    public void printValidation(Player p) {
        if (CourseID == null) {
            lh.prefix(p, Lang.CourseNotSelected);
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
            resps.add(Lang.ValidateCourse_Err_Name.asString());
        } else {
            resps.add(Lang.ValidateCourse_Good_Name.asString());
        }

        if (pos1 == null) {
            resps.add(Lang.ValidateCourse_Err_FirstCorner.asString());
        } else {
            resps.add(Lang.ValidateCourse_Good_FirstCorner.asString());
        }

        if (pos2 == null) {
            resps.add(Lang.ValidateCourse_Err_SecCorner.asString());
        } else {
            resps.add(Lang.ValidateCourse_Good_SecCorner.asString());
        }

        if (start == null) {
            resps.add(Lang.ValidateCourse_Err_Staring.asString());
        } else {
            resps.add(Lang.ValidateCourse_Good_Staring.asString());
        }

        if (end == null) {
            resps.add(Lang.ValidateCourse_Err_Ending.asString());
        } else {
            resps.add(Lang.ValidateCourse_Good_Ending.asString());
        }

        if (diff == null || diff.length() < 1) {
            resps.add(Lang.ValidateCourse_Err_Difficulty.asString());
        } else {
            resps.add(Lang.ValidateCourse_Good_Difficulty.asString());
        }

        if (spawn == null) {
            resps.add(Lang.ValidateCourse_Err_Spawn.asString());
        } else {
            resps.add(Lang.ValidateCourse_Good_Spawn.asString());
        }

        for(String line : Lang.ValidateCourse_top.asStringList()) {
            StringHandler m = new StringHandler(line);
            m.replaceAll("%id%", CourseID);
            m.replaceAll("%name%", name);
            m.send(p);
        }
        for (String part : resps) {
            StringHandler m = new StringHandler(Lang.ValidateCourse_Entry.asString());
            m.replaceAll("%part%", part);
            m.send(p);
        }
        for(String line : Lang.ValidateCourse_Bottom.asStringList()) {
            StringHandler m = new StringHandler(line);
            m.send(p);
        }


    }


    private boolean setLoc(Player p, String set, Location loc, String successMessage) {
        if (CourseID == null) {
            lh.prefix(p, Lang.CourseNotSelected);
            return false;
        }
        yaml.save("courses." + CourseID, set, loc);
        p.sendMessage(Lang.Prefix.asLegacy() + successMessage);
        return true;
    }

    private boolean set(Player p, String set, String successMessage) {
        if (CourseID == null) {
            lh.prefix(p, Lang.CourseNotSelected);
            return false;
        }
        Block b = p.getTargetBlockExact(20);
        if (b == null) {
            lh.prefix(p, Lang.InvalidBlockTarget);
            return false;
        }
        if (!b.getType().name().toLowerCase().endsWith("pressure_plate")) {
            lh.prefix(p, Lang.PressurePlateNeeded);
            return false;
        }
        yaml.save("courses." + CourseID, set, b.getLocation());
        p.sendMessage(Lang.Prefix.asLegacy() + successMessage);
        return true;
    }

    private boolean add(Player p, String set, String successMessage) {
        if (CourseID == null) {
            lh.prefix(p, Lang.CourseNotSelected);
            return false;
        }
        Block b = p.getTargetBlockExact(20);
        if (b == null) {lh.prefix(p, Lang.InvalidBlockTarget);}
        if (!b.getType().name().toLowerCase().endsWith("pressure_plate")) {
            lh.prefix(p, Lang.PressurePlateNeeded);
            return false;
        }
        List<String> locs = yaml.getStringList("courses." + CourseID, set);
        locs.add(Utilities.serializeLocation(b.getLocation(), '~'));
        yaml.save("courses." + CourseID, set, locs);
        p.sendMessage(Lang.Prefix.asLegacy() + successMessage);
        return true;
    }

    private boolean setVal(Player p, String set, String val, String successMessage) {
        if (CourseID == null) {
            lh.prefix(p, Lang.CourseNotSelected);
            return false;
        }

        yaml.save("courses." + CourseID, set, val);
        p.sendMessage(Lang.Prefix.asLegacy() + successMessage);
        return true;
    }

}

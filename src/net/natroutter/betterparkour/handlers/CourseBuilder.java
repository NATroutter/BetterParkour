package net.natroutter.betterparkour.handlers;

import net.natroutter.betterparkour.Handler;
import net.natroutter.betterparkour.files.Lang;
import net.natroutter.natlibs.handlers.Database.YamlDatabase;
import net.natroutter.natlibs.utilities.StringHandler;
import net.natroutter.natlibs.utilities.Utilities;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CourseBuilder {

    private YamlDatabase yaml;
    private Lang lang;
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
            p.sendMessage(lang.Prefix + lang.CourseCreatingAborted);
        } else {
            p.sendMessage(lang.Prefix + lang.CourseNotSelected);
        }
    }

    public boolean save(Player p) {
        if (CourseID == null) {
            p.sendMessage(lang.Prefix + lang.CourseNotSelected);
            return false;
        }
        if (courses.validateCourse(CourseID)) {
            p.sendMessage(lang.Prefix + lang.CourseEditFinished);
            CourseID = null;
            courses.loadCourses();
            topHolo.loadData();
            topHolo.loadHolograms();
            return true;
        } else {
            p.sendMessage(lang.Prefix + lang.CourseNotGood);
            return false;
        }
    }

    public boolean setSpawn(Player p, Location loc) {
        return setLoc(p,"spawn",loc, lang.SpawnSet);
    }
    public boolean setTopList(Player p, Location loc) {
        return setLoc(p,"toplist",loc, lang.TopListSet);
    }
    public boolean setPos1(Player p, Location loc) {
        return setLoc(p, "pos1",loc, lang.FirstCornerSet);
    }
    public boolean setPos2(Player p, Location loc) {
        return setLoc(p,"pos2",loc, lang.SecondCornerSet);
    }
    public boolean setStart(Player p) {
        return set(p,"start", lang.StartSet);
    }
    public boolean setEnd(Player p) {
        return set(p,"end", lang.EndSet);
    }
    public boolean setDiff(Player p, String val) {
        return setVal(p,"diff", val, lang.DifficultySet);
    }
    public boolean setName(Player p, String val) {
        return setVal(p,"name", val, lang.CourseRenamed);
    }
    public boolean addCheckpoint(Player p) {
        return add(p,"check", lang.CheckpointAdded);
    }

    public void giveWand(Player p) {
        if (CourseID == null) {
            p.sendMessage(lang.Prefix + lang.CourseNotSelected);
            return;
        }
        p.getInventory().addItem(handler.getItems().wand());
        p.sendMessage(lang.Prefix + lang.WandGive);
    }

    public void printCourses(Player p) {
        Set<String> keys = yaml.getKeys("courses");
        if (keys.size() > 0) {
            lang.getCourseList().top.forEach(p::sendMessage);
            for (String key : yaml.getKeys("courses")) {
                String name = yaml.getString("courses." + key, "name");
                String diff = yaml.getString("courses." + key, "diff");
                StringHandler line = new StringHandler(lang.getCourseList().entry);
                line.replaceAll("%name%",name);
                line.replaceAll("%diff%",diff);
                line.send(p);
            }
            lang.getCourseList().bottom.forEach(p::sendMessage);
        } else {
            p.sendMessage(lang.Prefix + lang.getCourseList().noCourses);
        }
    }

    public void create(Player p, String name) {
        if (CourseID != null) {
            p.sendMessage(lang.Prefix + lang.CourseInEdit);
            return;
        }

        if (yaml.getKeys("courses") != null) {
            for (String key : yaml.getKeys("courses")) {
                String courseN = yaml.getString("courses." + key, "name");
                if (courseN.equalsIgnoreCase(name)) {
                    p.sendMessage(lang.Prefix + lang.CourseExist);
                    return;
                }
            }
        }

        UUID id = UUID.randomUUID();
        CourseID = id;
        yaml.save("courses." + id, "name", name);
        p.sendMessage(lang.Prefix + lang.CourseCreated);
    }

    public void remove(Player p, String name) {
        for (String key : yaml.getKeys("courses")) {
            String courseN = yaml.getString("courses." + key, "name");
            if (courseN.equalsIgnoreCase(name)) {
                yaml.save("courses", key, null);
                database.deleteCourse(UUID.fromString(key));

                p.sendMessage(lang.Prefix + lang.CourseRemoved);
                return;
            }
        }
        p.sendMessage(lang.Prefix + lang.CourseDoesntExits);
    }

    public void edit(Player p, String name) {
        for (String key : yaml.getKeys("courses")) {
            String courseN = yaml.getString("courses." + key, "name");
            if (courseN.equalsIgnoreCase(name)) {
                CourseID = UUID.fromString(key);
                StringHandler msg = new StringHandler(lang.CourseEditSelected);
                msg.setPrefix(lang.Prefix);
                msg.replaceAll("%course%", courseN);
                msg.send(p);
                return;
            }
        }
        p.sendMessage(lang.Prefix + lang.CourseDoesntExits);
    }

    public void printValidation(Player p) {
        if (CourseID == null) {
            p.sendMessage(lang.Prefix + lang.CourseNotSelected);
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
            resps.add(lang.getValidateCourse().err_Name);
        } else {
            resps.add(lang.getValidateCourse().good_Name);
        }

        if (pos1 == null) {
            resps.add(lang.getValidateCourse().err_FirstCorner);
        } else {
            resps.add(lang.getValidateCourse().good_FirstCorner);
        }

        if (pos2 == null) {
            resps.add(lang.getValidateCourse().err_SecCorner);
        } else {
            resps.add(lang.getValidateCourse().good_SecCorner);
        }

        if (start == null) {
            resps.add(lang.getValidateCourse().err_Staring);
        } else {
            resps.add(lang.getValidateCourse().good_Staring);
        }

        if (end == null) {
            resps.add(lang.getValidateCourse().err_Ending);
        } else {
            resps.add(lang.getValidateCourse().good_Ending);
        }

        if (diff == null || diff.length() < 1) {
            resps.add(lang.getValidateCourse().err_Difficulty);
        } else {
            resps.add(lang.getValidateCourse().good_Difficulty);
        }

        if (spawn == null) {
            resps.add(lang.getValidateCourse().err_Spawn);
        } else {
            resps.add(lang.getValidateCourse().good_Spawn);
        }

        for(String line : lang.getValidateCourse().top) {
            StringHandler m = new StringHandler(line);
            m.replaceAll("%id%", CourseID);
            m.replaceAll("%name%", name);
            m.send(p);
        }
        for (String part : resps) {
            StringHandler m = new StringHandler(part);
            m.replaceAll("%part%", part);
            m.send(p);
        }
        for(String line : lang.getValidateCourse().bottom) {
            StringHandler m = new StringHandler(line);
            m.send(p);
        }


    }


    private boolean setLoc(Player p, String set, Location loc, String successMessage) {
        if (CourseID == null) {
            p.sendMessage(lang.Prefix + lang.CourseNotSelected);
            return false;
        }
        yaml.saveLoc("courses." + CourseID, set, loc);
        p.sendMessage(lang.Prefix + successMessage);
        return true;
    }

    private boolean set(Player p, String set, String successMessage) {
        if (CourseID == null) {
            p.sendMessage(lang.Prefix + lang.CourseNotSelected);
            return false;
        }
        Block b = p.getTargetBlock(10);
        if (b == null) {
            p.sendMessage(lang.Prefix + lang.InvalidBlockTarget);
            return false;
        }
        if (!b.getType().name().toLowerCase().endsWith("pressure_plate")) {
            p.sendMessage(lang.Prefix + lang.PressurePlateNeeded);
            return false;
        }
        yaml.saveLoc("courses." + CourseID, set, b.getLocation());
        p.sendMessage(lang.Prefix + successMessage);
        return true;
    }

    private boolean add(Player p, String set, String successMessage) {
        if (CourseID == null) {
            p.sendMessage(lang.Prefix + lang.CourseNotSelected);
            return false;
        }
        Block b = p.getTargetBlock(10);
        if (b == null) {p.sendMessage(lang.Prefix + lang.InvalidBlockTarget);}
        if (!b.getType().name().toLowerCase().endsWith("pressure_plate")) {
            p.sendMessage(lang.Prefix + lang.PressurePlateNeeded);
            return false;
        }
        List<String> locs = yaml.getStringList("courses." + CourseID, set);
        locs.add(util.serializeLocation(b.getLocation(), '~'));
        yaml.save("courses." + CourseID, set, locs);
        p.sendMessage(lang.Prefix + successMessage);
        return true;
    }

    private boolean setVal(Player p, String set, String val, String successMessage) {
        if (CourseID == null) {
            p.sendMessage(lang.Prefix + lang.CourseNotSelected);
            return false;
        }

        yaml.save("courses." + CourseID, set, val);
        p.sendMessage(lang.Prefix + successMessage);
        return true;
    }

}

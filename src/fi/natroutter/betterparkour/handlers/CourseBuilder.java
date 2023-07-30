package fi.natroutter.betterparkour.handlers;

import fi.natroutter.betterparkour.BetterParkour;
import fi.natroutter.betterparkour.files.Lang;
import fi.natroutter.betterparkour.handlers.Database.Database;
import fi.natroutter.betterparkour.items.Items;
import fi.natroutter.natlibs.handlers.database.YamlDatabase;
import fi.natroutter.natlibs.utilities.Utilities;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Handler;

public class CourseBuilder {

    private YamlDatabase yaml;
    private Courses courses;
    private Database database;
    private Handler handler;
    private TopHologramHandler topHolo;

    public UUID CourseID;

    public CourseBuilder() {
        this.yaml = BetterParkour.getYaml();
        this.courses = BetterParkour.getCourses();
        this.database = BetterParkour.getDatabase();
        this.topHolo = BetterParkour.getTopHologramHandler();
    }

    public void abort(Player p) {
        if (CourseID != null) {
            yaml.save("courses", CourseID.toString(), null);
            CourseID = null;
            p.sendMessage(Lang.CourseCreatingAborted.prefixed());
        } else {
            p.sendMessage(Lang.CourseNotSelected.prefixed());
        }
    }

    public boolean save(Player p) {
        if (CourseID == null) {
            p.sendMessage(Lang.CourseNotSelected.prefixed());
            return false;
        }
        if (courses.validateCourse(CourseID)) {
            p.sendMessage(Lang.CourseEditFinished.prefixed());
            CourseID = null;
            courses.loadCourses();
            topHolo.loadHolograms();
            return true;
        } else {
            p.sendMessage(Lang.CourseNotGood.prefixed());
            return false;
        }
    }

    public boolean setSpawn(Player p, Location loc) {
        return setLoc(p,"spawn",loc, Lang.SpawnSet);
    }
    public boolean setTopList(Player p, Location loc) {
        return setLoc(p,"toplist",loc, Lang.TopListSet);
    }
    public boolean setPos1(Player p, Location loc) {
        return setLoc(p, "pos1",loc, Lang.FirstCornerSet);
    }
    public boolean setPos2(Player p, Location loc) {
        return setLoc(p,"pos2",loc, Lang.SecondCornerSet);
    }
    public boolean setStart(Player p) {
        return set(p,"start", Lang.StartSet);
    }
    public boolean setEnd(Player p) {
        return set(p,"end", Lang.EndSet);
    }
    public boolean setDiff(Player p, String val) {
        return setVal(p,"diff", val, Lang.DifficultySet);
    }
    public boolean setName(Player p, String val) {
        return setVal(p,"name", val, Lang.CourseRenamed);
    }
    public boolean addCheckpoint(Player p) {
        return add(p,"check", Lang.CheckpointAdded);
    }

    public void giveWand(Player p) {
        if (CourseID == null) {
            p.sendMessage(Lang.CourseNotSelected.prefixed());
            return;
        }
        p.getInventory().addItem(Items.wand());
        p.sendMessage(Lang.WandGive.prefixed());
    }

    public void printCourses(Player p) {
        Set<String> keys = yaml.getKeys("courses");
        if (keys != null && keys.size() > 0) {
            p.sendMessage(Lang.CourseList_Top.asSingleComponent());
            for (String key : keys) {
                String name = yaml.getString("courses." + key, "name");
                String diff = yaml.getString("courses." + key, "diff");
                if (name == null || diff == null) {
                    yaml.save("courses", key, null);
                    continue;
                }
                p.sendMessage(Lang.CourseList_Entry.asComponent(
                        Placeholder.parsed("name", name),
                        Placeholder.parsed("diff", diff)
                ));

            }
            p.sendMessage(Lang.CourseList_Bottom.asSingleComponent());
        } else {
            p.sendMessage(Lang.CourseList_NoCourses.prefixed());
        }
    }

    public void create(Player p, String name) {
        if (CourseID != null) {
            p.sendMessage(Lang.CourseInEdit.prefixed());
            return;
        }

        Set<String> keys = yaml.getKeys("courses");
        if (keys != null) {
            for (String key : keys) {
                String courseN = yaml.getString("courses." + key, "name");
                if (courseN.equalsIgnoreCase(name)) {
                    p.sendMessage(Lang.CourseExist.prefixed());
                    return;
                }
            }
        }

        UUID id = UUID.randomUUID();
        CourseID = id;
        yaml.save("courses." + id, "name", name);
        p.sendMessage(Lang.CourseCreated.prefixed());
    }

    public void remove(Player p, String name) {
        Set<String> keys = yaml.getKeys("courses");
        if (keys == null) {
            p.sendMessage(Lang.CourseDoesntExits.prefixed());
            return;
        }
        for (String key : keys) {
            String courseN = yaml.getString("courses." + key, "name");
            if (courseN.equalsIgnoreCase(name)) {
                yaml.save("courses", key, null);
                database.deleteStats(UUID.fromString(key));

                p.sendMessage(Lang.CourseRemoved.prefixed());
                return;
            }
        }
        p.sendMessage(Lang.CourseDoesntExits.prefixed());
    }

    public void edit(Player p, String name) {
        Set<String> keys = yaml.getKeys("courses");
        if (keys == null) {
            p.sendMessage(Lang.CourseDoesntExits.prefixed());
            return;
        }
        for (String key : keys) {
            String courseN = yaml.getString("courses." + key, "name");
            if (courseN.equalsIgnoreCase(name)) {
                CourseID = UUID.fromString(key);

                p.sendMessage(Lang.CourseEditSelected.asComponent(
                        Placeholder.parsed("course", courseN)
                ));

                return;
            }
        }
        p.sendMessage(Lang.CourseDoesntExits.prefixed());
    }

    public void printValidation(Player p) {
        if (CourseID == null) {
            p.sendMessage(Lang.CourseNotSelected.prefixed());
            return;
        }
        ArrayList<Lang> resps = new ArrayList<>();

        String name = yaml.getString("courses." + CourseID, "name");
        Location pos1 = yaml.getLocation("courses." + CourseID, "pos1");
        Location pos2 = yaml.getLocation("courses." + CourseID, "pos2");
        Location start = yaml.getLocation("courses." + CourseID, "start");
        Location end = yaml.getLocation("courses." + CourseID, "end");
        String diff = yaml.getString("courses." + CourseID, "diff");
        Location spawn = yaml.getLocation("courses." + CourseID, "spawn");

        if (name == null || name.length() < 1) {
            resps.add(Lang.ValidateCourse_Err_Name);
        } else {
            resps.add(Lang.ValidateCourse_Good_Name);
        }

        if (pos1 == null) {
            resps.add(Lang.ValidateCourse_Err_FirstCorner);
        } else {
            resps.add(Lang.ValidateCourse_Good_FirstCorner);
        }

        if (pos2 == null) {
            resps.add(Lang.ValidateCourse_Err_SecCorner);
        } else {
            resps.add(Lang.ValidateCourse_Good_SecCorner);
        }

        if (start == null) {
            resps.add(Lang.ValidateCourse_Err_Staring);
        } else {
            resps.add(Lang.ValidateCourse_Good_Staring);
        }

        if (end == null) {
            resps.add(Lang.ValidateCourse_Err_Ending);
        } else {
            resps.add(Lang.ValidateCourse_Good_Ending);
        }

        if (diff == null || diff.length() < 1) {
            resps.add(Lang.ValidateCourse_Err_Difficulty);
        } else {
            resps.add(Lang.ValidateCourse_Good_Difficulty);
        }

        if (spawn == null) {
            resps.add(Lang.ValidateCourse_Err_Spawn);
        } else {
            resps.add(Lang.ValidateCourse_Good_Spawn);
        }

        p.sendMessage(Lang.ValidateCourse_top.asSingleComponent(
                Placeholder.parsed("id", CourseID.toString()),
                Placeholder.parsed("name", name)
        ));

        for (Lang part : resps) {

            p.sendMessage(Lang.ValidateCourse_Entry.asComponent(
                    Placeholder.component("part", part.asComponent())
            ));
        }

        p.sendMessage(Lang.ValidateCourse_Bottom.asSingleComponent());

    }


    private boolean setLoc(Player p, String set, Location loc, Lang successMessage) {
        if (CourseID == null) {
            p.sendMessage(Lang.CourseNotSelected.prefixed());
            return false;
        }
        yaml.save("courses." + CourseID, set, loc);
        p.sendMessage(successMessage.prefixed());
        return true;
    }

    private boolean set(Player p, String set, Lang successMessage) {
        if (CourseID == null) {
            p.sendMessage(Lang.CourseNotSelected.prefixed());
            return false;
        }
        Block b = p.getTargetBlockExact(20);
        if (b == null) {
            p.sendMessage(Lang.InvalidBlockTarget.prefixed());
            return false;
        }
        if (!b.getType().name().toLowerCase().endsWith("pressure_plate")) {
            p.sendMessage(Lang.PressurePlateNeeded.prefixed());
            return false;
        }
        yaml.save("courses." + CourseID, set, b.getLocation());
        p.sendMessage(successMessage.prefixed());
        return true;
    }

    private boolean add(Player p, String set, Lang successMessage) {
        if (CourseID == null) {
            p.sendMessage(Lang.CourseNotSelected.prefixed());
            return false;
        }
        Block b = p.getTargetBlockExact(20);
        if (b == null) {p.sendMessage(Lang.InvalidBlockTarget.prefixed());}
        if (!b.getType().name().toLowerCase().endsWith("pressure_plate")) {
            p.sendMessage(Lang.PressurePlateNeeded.prefixed());
            return false;
        }
        List<String> locs = yaml.getStringList("courses." + CourseID, set);
        locs.add(Utilities.serializeLocation(b.getLocation(), '~'));
        yaml.save("courses." + CourseID, set, locs);
        p.sendMessage(successMessage.prefixed());
        return true;
    }

    private boolean setVal(Player p, String set, String val, Lang successMessage) {
        if (CourseID == null) {
            p.sendMessage(Lang.CourseNotSelected.prefixed());
            return false;
        }

        yaml.save("courses." + CourseID, set, val);
        p.sendMessage(successMessage.prefixed());
        return true;
    }

}

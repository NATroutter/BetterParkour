package net.natroutter.betterparkour.handlers;

import net.natroutter.betterparkour.Handler;
import net.natroutter.betterparkour.objs.Course;
import net.natroutter.natlibs.handlers.Database.YamlDatabase;
import net.natroutter.natlibs.objects.Cuboid;
import net.natroutter.natlibs.utilities.Utilities;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Courses {

    private Handler handler;
    private YamlDatabase yaml;
    private Utilities util;

    private ArrayList<Course> courses = new ArrayList<>();

    public Courses(Handler handler) {
        this.handler = handler;
        this.yaml = handler.getYaml();
        this.util = handler.getUtil();
        loadCourses();
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public Course getCourse(UUID courseID) {
        return courses.stream().filter(c->c.getId().equals(courseID)).findAny().orElse(null);
    }

    public Course getCourse(Player p) {
        for (Course course : courses) {
            if (course.getArena().contains(p.getLocation())) {
                return course;
            }
        }
        return null;
    }

    public void loadCourses() {
        courses.clear();
        Set<String> keys = yaml.getKeys("courses");
        if (keys == null) {return; }
        if (keys.size() > 0) {
            for (String key : yaml.getKeys("courses")) {
                UUID id = UUID.fromString(key);
                if (!validateCourse(id)) {
                    handler.console("§9[BetterParkour] §7Invalid course in database! §b("+id+")");
                    continue;
                }

                String name = yaml.getString("courses." + key, "name");
                String diff = yaml.getString("courses." + key, "diff");
                Location start = yaml.getLocation("courses." + key, "start");
                Location end = yaml.getLocation("courses." + key, "end");
                Location spawn = yaml.getLocation("courses." + key, "spawn");

                Location pos1 = yaml.getLocation("courses." + key, "pos1");
                Location pos2 = yaml.getLocation("courses." + key, "pos2");
                Location toplist = yaml.getLocation("courses." + key, "toplist");
                Cuboid arena = new Cuboid(pos1, pos2);

                List<String> rawChecks = yaml.getStringList("courses." + key, "check");
                List<Location> checks = new ArrayList<>();
                for (String line : rawChecks) {
                    Location loc = util.deserializeLocation(line, '~');
                    if (loc != null) {
                        checks.add(loc);
                    }
                }
                courses.add(new Course(id,  name,  diff,  start,  end,  checks,  arena, spawn, toplist));
            }
        }
    }

    public boolean validateCourse(UUID CourseID) {
        if (CourseID == null) {
            return false;
        }

        String name = yaml.getString("courses." + CourseID, "name");
        Location pos1 = yaml.getLocation("courses." + CourseID, "pos1");
        Location pos2 = yaml.getLocation("courses." + CourseID, "pos2");
        Location start = yaml.getLocation("courses." + CourseID, "start");
        Location end = yaml.getLocation("courses." + CourseID, "end");
        String diff = yaml.getString("courses." + CourseID, "diff");
        Location spawn = yaml.getLocation("courses." + CourseID, "spawn");

        if (name == null || name.length() < 1) {
            return false;
        } else if (pos1 == null) {
            return false;
        } else if (pos2 == null) {
            return false;
        } else if (start == null) {
            return false;
        } else if (end == null) {
            return false;
        } else if (spawn == null) {
            return false;
        } else if (diff == null || diff.length() < 1) {
            return false;
        }
        return true;
    }

}

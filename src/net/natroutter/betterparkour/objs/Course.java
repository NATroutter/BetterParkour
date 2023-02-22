package net.natroutter.betterparkour.objs;

import fi.natroutter.natlibs.objects.Cuboid;
import org.bukkit.Location;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Course {

    private UUID id;
    private String name;
    private String diff;
    private Location start;
    private Location end;
    private List<Location> checks;
    private Cuboid arena;
    private Location spawn;
    private Location toplist;

    public Course(UUID id, String name, String diff, Location start, Location end, List<Location> checks, Cuboid arena, Location spawn, Location toplist) {
        this.id = id;
        this.name = name;
        this.diff = diff;
        this.start = start;
        this.end = end;
        this.checks = checks;
        this.arena = arena;
        this.spawn = spawn;
        this.toplist = toplist;
    }

    public Location getToplist() {
        return toplist;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getEnd() {
        return end;
    }

    public void setEnd(Location end) {
        this.end = end;
    }

    public List<Location> getChecks() {
        return checks;
    }

    public void setChecks(List<Location> checks) {
        this.checks = checks;
    }

    public Cuboid getArena() {
        return arena;
    }

    public void setArena(Cuboid arena) {
        this.arena = arena;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }
}

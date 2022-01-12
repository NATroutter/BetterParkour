package net.natroutter.betterparkour.events;

import net.natroutter.betterparkour.objs.Course;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ParkourFellOffEvent extends Event {


    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Course course;
    private final long startTime;
    private final Location lastCheck;

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ParkourFellOffEvent(Player player, Course course, long startTime, Location lastCheck) {
        this.player = player;
        this.course = course;
        this.startTime = startTime;
        this.lastCheck = lastCheck;
    }

    public Player getPlayer() {
        return player;
    }

    public Course getCourse() {
        return course;
    }

    public long getStartTime() {
        return startTime;
    }

    public Location getLastCheckpoint() {
        return lastCheck;
    }

}

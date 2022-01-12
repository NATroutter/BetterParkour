package net.natroutter.betterparkour.events;

import net.natroutter.betterparkour.objs.ActiveCourse;
import net.natroutter.betterparkour.objs.Course;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParkourLeaveEvent extends Event {


    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Course course;
    private final long startTime;

    @Override
    public @NotNull HandlerList getHandlers() {
        return null;
    }

    public ParkourLeaveEvent(Player player, Course course, long startTime) {
        this.player = player;
        this.course = course;
        this.startTime = startTime;
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

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}

package net.natroutter.betterparkour.events;

import net.natroutter.betterparkour.objs.ActiveCourse;
import net.natroutter.betterparkour.objs.Course;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ParkourFinishedEvent extends Event {


    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Course course;
    private final long startTime;
    private final long endTime;
    private final long timeTook;

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ParkourFinishedEvent(Player player, Course course, long startTime, long endTime, long timeTook) {
        this.player = player;
        this.course = course;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeTook = timeTook;
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

    public long getEndTime() {
        return endTime;
    }

    public long getTimeTook() {
        return timeTook;
    }

}

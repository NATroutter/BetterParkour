package fi.natroutter.betterparkour.events;

import fi.natroutter.betterparkour.objs.Course;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ParkourFinishedEvent extends Event implements Cancellable {


    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Course course;
    private final long startTime;
    private final long endTime;
    private final long timeTook;
    private boolean isCancelled;

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
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

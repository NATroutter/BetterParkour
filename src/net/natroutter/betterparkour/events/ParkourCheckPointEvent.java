package net.natroutter.betterparkour.events;

import net.natroutter.betterparkour.objs.Course;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ParkourCheckPointEvent extends Event {


    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Course course;
    private final Location checkpoint;

    @Override
    public @NotNull HandlerList getHandlers() {
        return null;
    }

    public ParkourCheckPointEvent(Player player, Course course, Location checkpoint) {
        this.player = player;
        this.course = course;
        this.checkpoint = checkpoint;
    }

    public Player getPlayer() {
        return player;
    }

    public Course getCourse() {
        return course;
    }

    public Location getCheckpoint() {
        return checkpoint;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

package net.natroutter.betterparkour.events;

import net.natroutter.betterparkour.objs.Course;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ParkourJoinEvent extends Event {


    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Course course;

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public ParkourJoinEvent(Player player, Course course) {
        this.player = player;
        this.course = course;
    }

    public Player getPlayer() {
        return player;
    }

    public Course getCourse() {
        return course;
    }

}

package net.natroutter.betterparkour.objs;

import java.util.UUID;

public class Statistic {

    UUID courseID;
    UUID playerID;
    Long time;
    String name;

    public Statistic(UUID courseID, UUID playerID, String name, Long time) {
        this.courseID = courseID;
        this.playerID = playerID;
        this.time = time;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return playerID.toString()+"~"+courseID.toString();
    }

    public UUID getCourseID() {
        return courseID;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public Long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "courseID=" + courseID +
                ", playerID=" + playerID +
                ", time=" + time +
                ", name='" + name + '\'' +
                '}';
    }
}

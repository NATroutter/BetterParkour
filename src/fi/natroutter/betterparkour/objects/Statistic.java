package fi.natroutter.betterparkour.objects;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class Statistic {

    private String courseID;
    private String playerID;
    private Long time;
    private String name;

    public Statistic() {}

    public Statistic(UUID courseID, UUID playerID, String name, Long time) {
        this.courseID = courseID.toString();
        this.playerID = playerID.toString();
        this.time = time;
        this.name = name;
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

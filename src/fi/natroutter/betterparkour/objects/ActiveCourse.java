package fi.natroutter.betterparkour.objects;

import org.bukkit.Location;

public class ActiveCourse {

    Long startTime;
    Long endTime;
    Location lastCheck;
    Course course;

    public ActiveCourse(Long startTime, Long endTime, Location lastCheck, Course course) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.lastCheck = lastCheck;
        this.course = course;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public void setLastCheck(Location lastCheck) {
        this.lastCheck = lastCheck;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Location getLastCheck() {
        return lastCheck;
    }

    public Course getCourse() {
        return course;
    }
}

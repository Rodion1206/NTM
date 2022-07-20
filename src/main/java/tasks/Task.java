package tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected int id;
    protected String name;
    protected String desc;
    protected Status status;
    protected TaskType type;
    protected LocalDateTime startTime;
    protected Long duration;

    public Task(int id, String name, String desc, Status status, LocalDateTime startTime, Long duration) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.status = status;
        this.type = TaskType.TASK;

        this.startTime = startTime;
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime(){
        return startTime.plusMinutes(duration);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, desc);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(id, otherTask.id) &&
                Objects.equals(name, otherTask.name) &&
                Objects.equals(desc, otherTask.desc);
    }

    @Override
    public String toString() {
        // id,type,name,status,description,---epicId---
        return "" + getId() + ","
                + getType() + ","
                + getName() + ","
                + getStatus() + ","
                + getDesc() +","
                + " " +","
                + getStartTime()+","
                + getDuration();
    }

}

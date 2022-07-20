package tasks;

import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {
    private Map<Integer, Subtask> subtasksOfEpic;

    public Epic(int id, String name, String desc, Status status) {
        super(id, name, desc, status, null, 0L);
        subtasksOfEpic = new HashMap<>();
        this.type = TaskType.EPIC;
    }


    public void defineEpicStatus() {
        int amountNew = 0;
        int amountDone = 0;
        int amountInProgress = 0;

        int numberOfSubtasks = this.subtasksOfEpic.size();

        for (Subtask s : subtasksOfEpic.values()) {
            Status currentStatus = s.getStatus();
            if (currentStatus == Status.NEW) {
                amountNew += 1;
            } else if (currentStatus == Status.IN_PROGRESS) {
                amountInProgress += 1;
            } else if (currentStatus == Status.DONE) {
                amountDone += 1;
            }
        }

        if (numberOfSubtasks == amountNew || numberOfSubtasks == 0) {
            this.setStatus(Status.NEW);
            return;
        } else if (numberOfSubtasks == amountDone) {
            this.setStatus(Status.DONE);
            return;
        } else {
            this.setStatus(Status.IN_PROGRESS);
            return;
        }
    }

    public void addSubtaskToEpic(Subtask subtask) {
        subtasksOfEpic.put(subtask.getId(), subtask);
        defineEpicStatus();
        //пересчет времени
        LocalDateTime startTime = getStartTime();
        Long duration = getDuration();
        this.startTime = startTime;
        this.duration = duration;
    }

    public void removeSubtaskFromEpic(Subtask subtask) {
        if (subtasksOfEpic.containsKey(subtask.getId())) {
            subtasksOfEpic.remove(subtask.getId());
            this.defineEpicStatus();
            //пересчет времени
            LocalDateTime startTime = getStartTime();
            Long duration = getDuration();
            this.startTime = startTime;
            this.duration = duration;
        }
    }

    @Override
    public LocalDateTime getStartTime() {
        List<Subtask> subtasks = new ArrayList<>();
        for (Subtask s : subtasksOfEpic.values()) {
            subtasks.add(s);
        }
        Optional<LocalDateTime> result = subtasks.stream().map(s -> s.getStartTime()).min(Comparator.naturalOrder());

        if (result.isPresent()){
            return result.get();
        } else {
            return null;
        }

    }

    @Override
    public Long getDuration() {
        long totalDuration = 0;
        for (Subtask s : subtasksOfEpic.values()) {
            totalDuration += s.getDuration();
        }
        return totalDuration;
    }

    @Override
    public LocalDateTime getEndTime() {
        List<Subtask> subtasks = new ArrayList<>();
        for (Subtask s : subtasksOfEpic.values()) {
            subtasks.add(s);
        }
        Optional<LocalDateTime> result = subtasks.stream().map(s -> s.getStartTime().plusMinutes(s.getDuration()))
                .max(Comparator.naturalOrder());
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public Status getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public TaskType getType() {
        return this.type;
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
        Epic otherEpic = (Epic) obj;
        return Objects.equals(id, otherEpic.id) && Objects.equals(name, otherEpic.name) && Objects.equals(desc, otherEpic.desc);
    }

    @Override
    public String toString() {
        // id,type,name,status,description,---epicId---
        return this.getId() + ","
                + this.getType() + ","
                + this.getName() + ","
                + this.getStatus() + ","
                + this.getDesc() + ","
                + " " +","
                + this.getStartTime() + ","
                + this.getDuration();
    }

    public List<Subtask> getSubtaskOfEpic() {
        List<Subtask> result = new ArrayList<>();
        for (Subtask s : subtasksOfEpic.values()) {
            result.add(s);
        }
        return result;
    }

    public void setSubtaskOfEpic(Map<Integer, Subtask> subtaskOfEpic) {
        this.subtasksOfEpic = subtaskOfEpic;
        //пересчет времени
        LocalDateTime startTime = getStartTime();
        Long duration = getDuration();
        this.startTime = startTime;
        this.duration = duration;
    }
}

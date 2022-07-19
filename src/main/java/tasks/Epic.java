package tasks;

import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {
    private Map<Integer, Subtask> subtasksOfEpic;

    public Epic(int id, String name, String desc, Status status) {
        super(id, name, desc, status);
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
    }

    public void removeSubtaskFromEpic(Subtask subtask) {
        if (subtasksOfEpic.containsKey(subtask.getId())) {
            subtasksOfEpic.remove(subtask.getId());
            this.defineEpicStatus();
        }
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public String getDesc() {
        return super.getDesc();
    }

    @Override
    public void setDesc(String desc) {
        super.setDesc(desc);
    }

    @Override
    public Status getStatus() {
        return super.getStatus();
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
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
                + this.getDesc();
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
    }
}

package tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private Epic epic;
    private int epicId;

    public Subtask(int id, String name, String desc, Status status) {
        super(id, name, desc, status);
        this.type = TaskType.SUBTASK;
    }

//    public Epic Subtask(int id, String name, String desc, Status status, Epic epic) {
//        super(id, name, desc, status);
//        this.epic = epic;
//        this.type = TaskType.SUBTASK;

    public Subtask(int id, String name, String desc, Status status, Epic epic, int epicId) {
        super(id, name, desc, status);
        this.epic = epic;
        this.type = TaskType.SUBTASK;
        this.epicId = epicId;
    }

    public Epic getEpic() {
        return epic;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
        this.epicId = epic.getId();
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
        Subtask otherSubtask = (Subtask) obj;
        return Objects.equals(id, otherSubtask.id) &&
                Objects.equals(name, otherSubtask.name) &&
                Objects.equals(desc, otherSubtask.desc);
    }

    @Override
    public String toString() {
        // id,type,name,status,description,---epicId---
        return "" + getId() + ","
                + getType() + ","
                + getName() + ","
                + getStatus() + ","
                + getDesc() + ","
                + getEpic().getId();
    }


}

package tasks;

import managers.FileBackedTasksManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class TaskTest {

    @Test
    void getId() {
        FileBackedTasksManager manager = new FileBackedTasksManager("data.csv");
        Task task1 = new Task(manager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        Assertions.assertEquals(0, task1.getId());
    }

    @Test
    void setId() {
        FileBackedTasksManager manager = new FileBackedTasksManager("data.csv");
        Task task1 = new Task(manager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        task1.setId(777);
        Assertions.assertEquals(777, task1.getId());
    }

    @Test
    void getName() {
        FileBackedTasksManager manager = new FileBackedTasksManager("data.csv");
        Task task1 = new Task(manager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        Assertions.assertEquals("Task1", task1.getName());
    }

    @Test
    void setName() {
        FileBackedTasksManager manager = new FileBackedTasksManager("data.csv");
        Task task1 = new Task(manager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        task1.setName("newName");
        Assertions.assertEquals("newName", task1.getName());
    }

    @Test
    void getDesc() {
        FileBackedTasksManager manager = new FileBackedTasksManager("data.csv");
        Task task1 = new Task(manager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        Assertions.assertEquals("desc for task1", task1.getDesc());
    }

    @Test
    void setDesc() {
        FileBackedTasksManager manager = new FileBackedTasksManager("data.csv");
        Task task1 = new Task(manager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        task1.setDesc("new desc");
        Assertions.assertEquals("new desc", task1.getDesc());
    }

    @Test
    void getStatus() {
        FileBackedTasksManager manager = new FileBackedTasksManager("data.csv");
        Task task1 = new Task(manager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        Assertions.assertEquals(Status.NEW, task1.getStatus());
    }

    @Test
    void setStatus() {
        FileBackedTasksManager manager = new FileBackedTasksManager("data.csv");
        Task task1 = new Task(manager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        task1.setStatus(Status.DONE);
        Assertions.assertEquals(Status.DONE, task1.getStatus());
    }

    @Test
    void getType() {
        FileBackedTasksManager manager = new FileBackedTasksManager("data.csv");
        Task task1 = new Task(manager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        Assertions.assertEquals(TaskType.TASK, task1.getType());
    }
}
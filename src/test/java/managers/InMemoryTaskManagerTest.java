package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.List;

class InMemoryTaskManagerTest {

    @Test
    void isIntersection() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Task task1 = new Task(fileBackedTasksManager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        Task task2 = new Task(fileBackedTasksManager.generateId(), "Task2", "desc for task2", Status.IN_PROGRESS,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        fileBackedTasksManager.addTask(task1);
        fileBackedTasksManager.addTask(task2);
        Assertions.assertEquals(1, fileBackedTasksManager.allTasks.size());
    }

    @Test
    void getPrioritizedTasks() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Task task1 = new Task(fileBackedTasksManager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        Task task3 = new Task(fileBackedTasksManager.generateId(), "Task2", "desc for task2", Status.IN_PROGRESS,
                LocalDateTime.of(2002, 1, 1, 0, 0, 0, 0), 20L);
        Task task2 = new Task(fileBackedTasksManager.generateId(), "Task2", "desc for task2", Status.IN_PROGRESS,
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);
        fileBackedTasksManager.addTask(task1);
        fileBackedTasksManager.addTask(task2);
        fileBackedTasksManager.addTask(task3);

        List<Task> res =  fileBackedTasksManager.getPrioritizedTasks();
        Assertions.assertEquals(task3, res.get(0));
        Assertions.assertEquals(task2, res.get(1));
        Assertions.assertEquals(task1, res.get(2));
    }

    @Test
    void getHistory() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Task task1 = new Task(fileBackedTasksManager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        Task task2 = new Task(fileBackedTasksManager.generateId(), "Task2", "desc for task2", Status.IN_PROGRESS,
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);
        Task task3 = new Task(fileBackedTasksManager.generateId(), "Task2", "desc for task2", Status.IN_PROGRESS,
                LocalDateTime.of(2002, 1, 1, 0, 0, 0, 0), 20L);
        fileBackedTasksManager.addTask(task1);
        fileBackedTasksManager.addTask(task2);
        fileBackedTasksManager.addTask(task3);

        fileBackedTasksManager.getTaskById(0);
        fileBackedTasksManager.getTaskById(1);

        List<Task> res = fileBackedTasksManager.getHistory();
        Assertions.assertNotEquals(0, res.size());

    }

    @Test
    void getAllTasksAsList() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Task task1 = new Task(fileBackedTasksManager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        Task task2 = new Task(fileBackedTasksManager.generateId(), "Task2", "desc for task2", Status.IN_PROGRESS,
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);
        fileBackedTasksManager.addTask(task1);
        fileBackedTasksManager.addTask(task2);

        List<Task> res = fileBackedTasksManager.getAllTasksAsList();
        Assertions.assertEquals(res.size(), 2);
    }

    @Test
    void getAllSubtasksAsList() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Epic epic1 = new Epic(fileBackedTasksManager.generateId(), "Epic1 with tree subtasks", "desc", Status.NEW);
        Epic epic2 = new Epic(fileBackedTasksManager.generateId(), "Epic2 with 1 subtask", "desc of epic2", Status.NEW);

        Subtask subtask1 = new Subtask(fileBackedTasksManager.generateId(), "subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask2 = new Subtask(fileBackedTasksManager.generateId(), "subtask2", "desc of subtask2", Status.IN_PROGRESS,
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask3 = new Subtask(fileBackedTasksManager.generateId(), "subtask3", "desc of subtask3", Status.DONE,
                LocalDateTime.of(2002, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask4 = new Subtask(fileBackedTasksManager.generateId(), "subtask3", "desc of subtask3", Status.DONE,
                LocalDateTime.of(2003, 1, 1, 0, 0, 0, 0), 20L);

        subtask1.setEpic(epic1);
        subtask2.setEpic(epic1);
        subtask3.setEpic(epic1);

        epic1.addSubtaskToEpic(subtask1);
        epic1.addSubtaskToEpic(subtask2);
        epic1.addSubtaskToEpic(subtask4);

        subtask4.setEpic(epic2);
        epic2.addSubtaskToEpic(subtask4);

        fileBackedTasksManager.addEpic(epic1); // 2 эпик с тремя подзадачами
        fileBackedTasksManager.addEpic(epic2); // 3 эпик с одной подзадачей
        fileBackedTasksManager.addSubtask(subtask1); // 4 подзадача эпика 1
        fileBackedTasksManager.addSubtask(subtask2); // 5 подзадача эпика 1
        fileBackedTasksManager.addSubtask(subtask3); // 6 подзадача эпика 1
        fileBackedTasksManager.addSubtask(subtask4); // 7 подзадача эпика 2

        List<Subtask> res = fileBackedTasksManager.getAllSubtasksAsList();
        Assertions.assertEquals(res.size(), 4);
    }

    @Test
    void getAllEpicsAsList() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Epic epic1 = new Epic(fileBackedTasksManager.generateId(), "Epic1 with tree subtasks", "desc", Status.NEW);
        Epic epic2 = new Epic(fileBackedTasksManager.generateId(), "Epic2 with 1 subtask", "desc of epic2", Status.NEW);

        fileBackedTasksManager.addEpic(epic1);
        fileBackedTasksManager.addEpic(epic2);

        List<Epic> res = fileBackedTasksManager.getAllEpicsAsList();

        Assertions.assertEquals(2, res.size());
    }

    @Test
    void removeAllTasks() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Task task1 = new Task(fileBackedTasksManager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        Task task2 = new Task(fileBackedTasksManager.generateId(), "Task2", "desc for task2", Status.IN_PROGRESS,
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);
        fileBackedTasksManager.addTask(task1);
        fileBackedTasksManager.addTask(task2);

        fileBackedTasksManager.removeAllTasks();

        Assertions.assertEquals(0, fileBackedTasksManager.allTasks.size());
    }

    @Test
    void removeAllSubtasks() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Epic epic1 = new Epic(fileBackedTasksManager.generateId(), "Epic1 with tree subtasks", "desc", Status.NEW);
        Epic epic2 = new Epic(fileBackedTasksManager.generateId(), "Epic2 with 1 subtask", "desc of epic2", Status.NEW);

        Subtask subtask1 = new Subtask(fileBackedTasksManager.generateId(), "subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask2 = new Subtask(fileBackedTasksManager.generateId(), "subtask2", "desc of subtask2", Status.IN_PROGRESS,
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask3 = new Subtask(fileBackedTasksManager.generateId(), "subtask3", "desc of subtask3", Status.DONE,
                LocalDateTime.of(2002, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask4 = new Subtask(fileBackedTasksManager.generateId(), "subtask3", "desc of subtask3", Status.DONE,
                LocalDateTime.of(2003, 1, 1, 0, 0, 0, 0), 20L);

        subtask1.setEpic(epic1);
        subtask2.setEpic(epic1);
        subtask3.setEpic(epic1);

        epic1.addSubtaskToEpic(subtask1);
        epic1.addSubtaskToEpic(subtask2);
        epic1.addSubtaskToEpic(subtask4);

        subtask4.setEpic(epic2);
        epic2.addSubtaskToEpic(subtask4);

        fileBackedTasksManager.addEpic(epic1); // 2 эпик с тремя подзадачами
        fileBackedTasksManager.addEpic(epic2); // 3 эпик с одной подзадачей
        fileBackedTasksManager.addSubtask(subtask1); // 4 подзадача эпика 1
        fileBackedTasksManager.addSubtask(subtask2); // 5 подзадача эпика 1
        fileBackedTasksManager.addSubtask(subtask3); // 6 подзадача эпика 1
        fileBackedTasksManager.addSubtask(subtask4); // 7 подзадача эпика 2

        fileBackedTasksManager.removeAllSubtasks();

        Assertions.assertEquals(0, fileBackedTasksManager.allSubtasks.size());
    }

    @Test
    void removeAllEpics() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Epic epic1 = new Epic(fileBackedTasksManager.generateId(), "Epic1 with tree subtasks", "desc", Status.NEW);
        Epic epic2 = new Epic(fileBackedTasksManager.generateId(), "Epic2 with 1 subtask", "desc of epic2", Status.NEW);

        fileBackedTasksManager.addEpic(epic1);
        fileBackedTasksManager.addEpic(epic2);

        fileBackedTasksManager.removeAllEpics();

        Assertions.assertEquals(0, fileBackedTasksManager.allEpics.size());

    }

    @Test
    void getTaskById() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Task task1 = new Task(fileBackedTasksManager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        Task task2 = new Task(fileBackedTasksManager.generateId(), "Task2", "desc for task2", Status.IN_PROGRESS,
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);
        fileBackedTasksManager.addTask(task1);
        fileBackedTasksManager.addTask(task2);


        Assertions.assertEquals(fileBackedTasksManager.getTaskById(1), task2);
    }

    @Test
    void getSubtaskById() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Epic epic1 = new Epic(fileBackedTasksManager.generateId(), "Epic1 with tree subtasks", "desc", Status.NEW);
        Epic epic2 = new Epic(fileBackedTasksManager.generateId(), "Epic2 with 1 subtask", "desc of epic2", Status.NEW);

        Subtask subtask1 = new Subtask(fileBackedTasksManager.generateId(), "subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask2 = new Subtask(fileBackedTasksManager.generateId(), "subtask2", "desc of subtask2", Status.IN_PROGRESS,
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask3 = new Subtask(fileBackedTasksManager.generateId(), "subtask3", "desc of subtask3", Status.DONE,
                LocalDateTime.of(2002, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask4 = new Subtask(fileBackedTasksManager.generateId(), "subtask3", "desc of subtask3", Status.DONE,
                LocalDateTime.of(2003, 1, 1, 0, 0, 0, 0), 20L);

        subtask1.setEpic(epic1);
        subtask2.setEpic(epic1);
        subtask3.setEpic(epic1);

        epic1.addSubtaskToEpic(subtask1);
        epic1.addSubtaskToEpic(subtask2);
        epic1.addSubtaskToEpic(subtask4);

        subtask4.setEpic(epic2);
        epic2.addSubtaskToEpic(subtask4);

        fileBackedTasksManager.addEpic(epic1); // 2 эпик с тремя подзадачами
        fileBackedTasksManager.addEpic(epic2); // 3 эпик с одной подзадачей
        fileBackedTasksManager.addSubtask(subtask1); // 4 подзадача эпика 1
        fileBackedTasksManager.addSubtask(subtask2); // 5 подзадача эпика 1
        fileBackedTasksManager.addSubtask(subtask3); // 6 подзадача эпика 1
        fileBackedTasksManager.addSubtask(subtask4); // 7 подзадача эпика 2

        Assertions.assertEquals(fileBackedTasksManager.getSubtaskById(2), subtask1);
    }

    @Test
    void getEpicById() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Epic epic1 = new Epic(fileBackedTasksManager.generateId(), "Epic1 with tree subtasks", "desc", Status.NEW);
        Epic epic2 = new Epic(fileBackedTasksManager.generateId(), "Epic2 with 1 subtask", "desc of epic2", Status.NEW);

        fileBackedTasksManager.addEpic(epic1);
        fileBackedTasksManager.addEpic(epic2);

        Assertions.assertEquals(fileBackedTasksManager.getEpicById(1), epic2);
    }

    @Test
    void addTask() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Task task1 = new Task(fileBackedTasksManager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        fileBackedTasksManager.addTask(task1);
        Assertions.assertEquals(1, fileBackedTasksManager.allTasks.size());
    }

    @Test
    void addSubtask() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Epic epic1 = new Epic(fileBackedTasksManager.generateId(), "Epic1 with tree subtasks", "desc", Status.NEW);
        Epic epic2 = new Epic(fileBackedTasksManager.generateId(), "Epic2 with 1 subtask", "desc of epic2", Status.NEW);

        Subtask subtask1 = new Subtask(fileBackedTasksManager.generateId(), "subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask2 = new Subtask(fileBackedTasksManager.generateId(), "subtask2", "desc of subtask2", Status.IN_PROGRESS,
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask3 = new Subtask(fileBackedTasksManager.generateId(), "subtask3", "desc of subtask3", Status.DONE,
                LocalDateTime.of(2002, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask4 = new Subtask(fileBackedTasksManager.generateId(), "subtask3", "desc of subtask3", Status.DONE,
                LocalDateTime.of(2003, 1, 1, 0, 0, 0, 0), 20L);

        subtask1.setEpic(epic1);
        subtask2.setEpic(epic1);
        subtask3.setEpic(epic1);

        epic1.addSubtaskToEpic(subtask1);
        epic1.addSubtaskToEpic(subtask2);
        epic1.addSubtaskToEpic(subtask4);

        subtask4.setEpic(epic2);
        epic2.addSubtaskToEpic(subtask4);

        fileBackedTasksManager.addEpic(epic1); // 2 эпик с тремя подзадачами
        fileBackedTasksManager.addEpic(epic2); // 3 эпик с одной подзадачей
        fileBackedTasksManager.addSubtask(subtask1); // 4 подзадача эпика 1
        fileBackedTasksManager.addSubtask(subtask2); // 5 подзадача эпика 1
        fileBackedTasksManager.addSubtask(subtask3); // 6 подзадача эпика 1
        fileBackedTasksManager.addSubtask(subtask4); // 7 подзадача эпика 2

        Assertions.assertEquals(4, fileBackedTasksManager.allSubtasks.size());

    }

    @Test
    void addEpic() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Epic epic1 = new Epic(fileBackedTasksManager.generateId(), "Epic1 with tree subtasks", "desc", Status.NEW);
        fileBackedTasksManager.addEpic(epic1);
        Assertions.assertEquals(1, fileBackedTasksManager.allEpics.size());
    }

    @Test
    void updateTask() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Task task1 = new Task(fileBackedTasksManager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        fileBackedTasksManager.addTask(task1);

        Task task2 = new Task(0, "Task1", "desc for task1", Status.DONE,
                LocalDateTime.of(2004, 2, 1, 0, 0, 0, 0), 20L);

        fileBackedTasksManager.updateTask(task2);

        Assertions.assertEquals(fileBackedTasksManager.getTaskById(0).getStartTime(),
                LocalDateTime.of(2004, 2, 1, 0, 0, 0, 0));
    }

    @Test
    void updateSubtask() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");

        Epic epic1 = new Epic(fileBackedTasksManager.generateId(), "Epic1 with tree subtasks", "desc", Status.NEW);
        Subtask subtask1 = new Subtask(fileBackedTasksManager.generateId(), "subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);

        epic1.addSubtaskToEpic(subtask1);
        subtask1.setEpic(epic1);
        fileBackedTasksManager.addEpic(epic1);
        fileBackedTasksManager.addSubtask(subtask1); // 7 подзадача эпика 2

        Subtask subtask2 = new Subtask(1, "updated subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2016, 1, 1, 0, 0, 0, 0), 20L);
        subtask2.setEpic(epic1);

        fileBackedTasksManager.updateSubtask(subtask2);

        Assertions.assertEquals(fileBackedTasksManager.getSubtaskById(0).getName(), "updated subtask1");
    }

    @Test
    void updateEpic() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");

        Epic epic1 = new Epic(1, "Epic1 with tree subtasks", "desc", Status.NEW);

        Epic epic2 = new Epic(1, "updated Epic1 with tree subtasks", "desc", Status.NEW);

        fileBackedTasksManager.updateEpic(epic2);

        Assertions.assertEquals(fileBackedTasksManager.getEpicById(1).getDesc(), "updated Epic1 with tree subtasks");
    }

    @Test
    void removeTaskById() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Task task1 = new Task(fileBackedTasksManager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        Task task2 = new Task(fileBackedTasksManager.generateId(), "Task2", "desc for task2", Status.IN_PROGRESS,
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);
        fileBackedTasksManager.addTask(task1);
        fileBackedTasksManager.addTask(task2);
        fileBackedTasksManager.removeTaskById(0);
        Assertions.assertEquals(fileBackedTasksManager.allTasks.size(), 1);
    }

    @Test
    void removeSubtaskById() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Subtask subtask1 = new Subtask(fileBackedTasksManager.generateId(), "subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        fileBackedTasksManager.removeSubtaskById(0);
        Assertions.assertEquals(fileBackedTasksManager.allSubtasks.size(), 0);
    }

    @Test
    void removeEpicById() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Epic epic1 = new Epic(fileBackedTasksManager.generateId(), "Epic1 with tree subtasks", "desc", Status.NEW);
        fileBackedTasksManager.addEpic(epic1);
        fileBackedTasksManager.removeEpicById(0);
        Assertions.assertEquals(0, fileBackedTasksManager.allEpics.size());
    }

    @Test
    void getAllSubtasksOfEpic() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Epic epic1 = new Epic(fileBackedTasksManager.generateId(), "Epic1 with tree subtasks", "desc", Status.NEW);
        Epic epic2 = new Epic(fileBackedTasksManager.generateId(), "Epic2 with 1 subtask", "desc of epic2", Status.NEW);

        Subtask subtask1 = new Subtask(fileBackedTasksManager.generateId(), "subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask2 = new Subtask(fileBackedTasksManager.generateId(), "subtask2", "desc of subtask2", Status.IN_PROGRESS,
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask3 = new Subtask(fileBackedTasksManager.generateId(), "subtask3", "desc of subtask3", Status.DONE,
                LocalDateTime.of(2002, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask4 = new Subtask(fileBackedTasksManager.generateId(), "subtask3", "desc of subtask3", Status.DONE,
                LocalDateTime.of(2003, 1, 1, 0, 0, 0, 0), 20L);

        subtask1.setEpic(epic1);
        subtask2.setEpic(epic1);
        subtask3.setEpic(epic1);

        epic1.addSubtaskToEpic(subtask1);
        epic1.addSubtaskToEpic(subtask2);
        epic1.addSubtaskToEpic(subtask4);

        subtask4.setEpic(epic2);
        epic2.addSubtaskToEpic(subtask4);

        fileBackedTasksManager.addEpic(epic1); // 2 эпик с тремя подзадачами
        fileBackedTasksManager.addEpic(epic2); // 3 эпик с одной подзадачей
        fileBackedTasksManager.addSubtask(subtask1); // 4 подзадача эпика 1
        fileBackedTasksManager.addSubtask(subtask2); // 5 подзадача эпика 1
        fileBackedTasksManager.addSubtask(subtask3); // 6 подзадача эпика 1
        fileBackedTasksManager.addSubtask(subtask4); // 7 подзадача эпика 2

        List<Subtask> res = fileBackedTasksManager.getAllSubtasksOfEpic(epic1);
        Assertions.assertEquals(3, res.size());
    }
}
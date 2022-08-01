package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HTTPTaskManagerTest {

    @BeforeEach
    void init() throws IOException, InterruptedException {
//        try {
//            new KVServer().start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        HTTPTaskManager httpTaskManager = new HTTPTaskManager("/localhost:8078/");
//
//        // Блок для теста httpTaskManager
//        //FileBackedTasksManager fileBackedTasksManager = Managers.getDefault();
//        Task task1 = new Task(httpTaskManager.generateId(), "Task1", "desc for task1", Status.NEW,
//                LocalDateTime.of(2010, 1, 1, 0, 0, 0, 0), 20L);
//        Task task2 = new Task(httpTaskManager.generateId(), "Task2", "desc for task2", Status.IN_PROGRESS,
//                LocalDateTime.of(2012, 1, 1, 0, 0, 0, 0), 20L);
//
//        // 2
//        Epic epic1 = new Epic(httpTaskManager.generateId(), "Epic1 with three subtasks", "desc", Status.NEW);
//        Epic epic2 = new Epic(httpTaskManager.generateId(), "Epic2 with 1 subtask", "desc of epic2", Status.NEW);
//
//        Subtask subtask1 = new Subtask(httpTaskManager.generateId(), "subtask1", "desc of subtask1", Status.NEW,
//                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
//
//        Subtask subtask2 = new Subtask(httpTaskManager.generateId(), "subtask2", "desc of subtask2", Status.IN_PROGRESS,
//                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);
//
//        Subtask subtask3 = new Subtask(httpTaskManager.generateId(), "subtask3", "desc of subtask3", Status.DONE,
//                LocalDateTime.of(2002, 1, 1, 0, 0, 0, 0), 20L);
//
//        Subtask subtask4 = new Subtask(httpTaskManager.generateId(), "subtask4", "desc of subtask4", Status.DONE,
//                LocalDateTime.of(2003, 1, 1, 0, 0, 0, 0), 20L);
//
//        subtask1.setEpic(epic1);
//        subtask2.setEpic(epic1);
//        subtask3.setEpic(epic1);
//
//        epic1.addSubtaskToEpic(subtask1);
//        epic1.addSubtaskToEpic(subtask2);
//        epic1.addSubtaskToEpic(subtask4);
//
//        subtask4.setEpic(epic2);
//        epic2.addSubtaskToEpic(subtask4);
//
//
//        httpTaskManager.addTask(task1); // 0
//        httpTaskManager.addTask(task2); // 1  две задачи
//        httpTaskManager.addEpic(epic1); // 2 эпик с тремя подзадачами
//        httpTaskManager.addEpic(epic2); // 3 эпик с одной подзадачей
//        httpTaskManager.addSubtask(subtask1); // 4 подзадача эпика 1
//        httpTaskManager.addSubtask(subtask2); // 5 подзадача эпика 1
//        httpTaskManager.addSubtask(subtask3); // 6 подзадача эпика 1
//        httpTaskManager.addSubtask(subtask4); // 7 подзадача эпика 2
//
//        httpTaskManager.getTaskById(1);
//        httpTaskManager.getTaskById(0);
//        httpTaskManager.getSubtaskById(4);  // -
//        httpTaskManager.getSubtaskById(5);  // -
//        httpTaskManager.getSubtaskById(6);  // -
//        httpTaskManager.getEpicById(2); // Эпик будет удален со всеми подзадачами
//        httpTaskManager.getEpicById(3);
//        httpTaskManager.getSubtaskById(7);
    }

    @Test
    void removeAllTasks() {
        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HTTPTaskManager httpTaskManager = null;

        httpTaskManager = new HTTPTaskManager("/localhost:8078/");



        // Блок для теста httpTaskManager
        //FileBackedTasksManager fileBackedTasksManager = Managers.getDefault();
        Task task1 = new Task(httpTaskManager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2010, 1, 1, 0, 0, 0, 0), 20L);
        Task task2 = new Task(httpTaskManager.generateId(), "Task2", "desc for task2", Status.IN_PROGRESS,
                LocalDateTime.of(2012, 1, 1, 0, 0, 0, 0), 20L);

        // 2
        Epic epic1 = new Epic(httpTaskManager.generateId(), "Epic1 with three subtasks", "desc", Status.NEW);
        Epic epic2 = new Epic(httpTaskManager.generateId(), "Epic2 with 1 subtask", "desc of epic2", Status.NEW);

        Subtask subtask1 = new Subtask(httpTaskManager.generateId(), "subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask2 = new Subtask(httpTaskManager.generateId(), "subtask2", "desc of subtask2", Status.IN_PROGRESS,
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask3 = new Subtask(httpTaskManager.generateId(), "subtask3", "desc of subtask3", Status.DONE,
                LocalDateTime.of(2002, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask4 = new Subtask(httpTaskManager.generateId(), "subtask4", "desc of subtask4", Status.DONE,
                LocalDateTime.of(2003, 1, 1, 0, 0, 0, 0), 20L);

        subtask1.setEpic(epic1);
        subtask2.setEpic(epic1);
        subtask3.setEpic(epic1);

        epic1.addSubtaskToEpic(subtask1);
        epic1.addSubtaskToEpic(subtask2);
        epic1.addSubtaskToEpic(subtask4);

        subtask4.setEpic(epic2);
        epic2.addSubtaskToEpic(subtask4);


        httpTaskManager.addTask(task1); // 0
        httpTaskManager.addTask(task2); // 1  две задачи
        httpTaskManager.addEpic(epic1); // 2 эпик с тремя подзадачами
        httpTaskManager.addEpic(epic2); // 3 эпик с одной подзадачей
        httpTaskManager.addSubtask(subtask1); // 4 подзадача эпика 1
        httpTaskManager.addSubtask(subtask2); // 5 подзадача эпика 1
        httpTaskManager.addSubtask(subtask3); // 6 подзадача эпика 1
        httpTaskManager.addSubtask(subtask4); // 7 подзадача эпика 2

        httpTaskManager.getTaskById(1);
        httpTaskManager.getTaskById(0);
        httpTaskManager.getSubtaskById(4);  // -
        httpTaskManager.getSubtaskById(5);  // -
        httpTaskManager.getSubtaskById(6);  // -
        httpTaskManager.getEpicById(2); // Эпик будет удален со всеми подзадачами
        httpTaskManager.getEpicById(3);
        httpTaskManager.getSubtaskById(7);

        httpTaskManager.removeAllTasks();

        Assertions.assertEquals(0, httpTaskManager.allTasks.size());
    }

    @Test
    void removeAllSubtasks() {
        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HTTPTaskManager httpTaskManager = null;

        httpTaskManager = new HTTPTaskManager("/localhost:8078/");


        // Блок для теста httpTaskManager
        //FileBackedTasksManager fileBackedTasksManager = Managers.getDefault();
        Task task1 = new Task(httpTaskManager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2010, 1, 1, 0, 0, 0, 0), 20L);
        Task task2 = new Task(httpTaskManager.generateId(), "Task2", "desc for task2", Status.IN_PROGRESS,
                LocalDateTime.of(2012, 1, 1, 0, 0, 0, 0), 20L);

        // 2
        Epic epic1 = new Epic(httpTaskManager.generateId(), "Epic1 with three subtasks", "desc", Status.NEW);
        Epic epic2 = new Epic(httpTaskManager.generateId(), "Epic2 with 1 subtask", "desc of epic2", Status.NEW);

        Subtask subtask1 = new Subtask(httpTaskManager.generateId(), "subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask2 = new Subtask(httpTaskManager.generateId(), "subtask2", "desc of subtask2", Status.IN_PROGRESS,
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask3 = new Subtask(httpTaskManager.generateId(), "subtask3", "desc of subtask3", Status.DONE,
                LocalDateTime.of(2002, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask4 = new Subtask(httpTaskManager.generateId(), "subtask4", "desc of subtask4", Status.DONE,
                LocalDateTime.of(2003, 1, 1, 0, 0, 0, 0), 20L);

        subtask1.setEpic(epic1);
        subtask2.setEpic(epic1);
        subtask3.setEpic(epic1);

        epic1.addSubtaskToEpic(subtask1);
        epic1.addSubtaskToEpic(subtask2);
        epic1.addSubtaskToEpic(subtask4);

        subtask4.setEpic(epic2);
        epic2.addSubtaskToEpic(subtask4);


        httpTaskManager.addTask(task1); // 0
        httpTaskManager.addTask(task2); // 1  две задачи
        httpTaskManager.addEpic(epic1); // 2 эпик с тремя подзадачами
        httpTaskManager.addEpic(epic2); // 3 эпик с одной подзадачей
        httpTaskManager.addSubtask(subtask1); // 4 подзадача эпика 1
        httpTaskManager.addSubtask(subtask2); // 5 подзадача эпика 1
        httpTaskManager.addSubtask(subtask3); // 6 подзадача эпика 1
        httpTaskManager.addSubtask(subtask4); // 7 подзадача эпика 2

        httpTaskManager.getTaskById(1);
        httpTaskManager.getTaskById(0);
        httpTaskManager.getSubtaskById(4);  // -
        httpTaskManager.getSubtaskById(5);  // -
        httpTaskManager.getSubtaskById(6);  // -
        httpTaskManager.getEpicById(2); // Эпик будет удален со всеми подзадачами
        httpTaskManager.getEpicById(3);
        httpTaskManager.getSubtaskById(7);

        httpTaskManager.removeAllSubtasks();

        Assertions.assertEquals(0, httpTaskManager.allSubtasks.size());
    }

    @Test
    void removeAllEpics() throws IOException, InterruptedException {
        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HTTPTaskManager httpTaskManager = new HTTPTaskManager("/localhost:8078/");

        // Блок для теста httpTaskManager
        //FileBackedTasksManager fileBackedTasksManager = Managers.getDefault();
        Task task1 = new Task(httpTaskManager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2010, 1, 1, 0, 0, 0, 0), 20L);
        Task task2 = new Task(httpTaskManager.generateId(), "Task2", "desc for task2", Status.IN_PROGRESS,
                LocalDateTime.of(2012, 1, 1, 0, 0, 0, 0), 20L);

        // 2
        Epic epic1 = new Epic(httpTaskManager.generateId(), "Epic1 with three subtasks", "desc", Status.NEW);
        Epic epic2 = new Epic(httpTaskManager.generateId(), "Epic2 with 1 subtask", "desc of epic2", Status.NEW);

        Subtask subtask1 = new Subtask(httpTaskManager.generateId(), "subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask2 = new Subtask(httpTaskManager.generateId(), "subtask2", "desc of subtask2", Status.IN_PROGRESS,
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask3 = new Subtask(httpTaskManager.generateId(), "subtask3", "desc of subtask3", Status.DONE,
                LocalDateTime.of(2002, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask4 = new Subtask(httpTaskManager.generateId(), "subtask4", "desc of subtask4", Status.DONE,
                LocalDateTime.of(2003, 1, 1, 0, 0, 0, 0), 20L);

        subtask1.setEpic(epic1);
        subtask2.setEpic(epic1);
        subtask3.setEpic(epic1);

        epic1.addSubtaskToEpic(subtask1);
        epic1.addSubtaskToEpic(subtask2);
        epic1.addSubtaskToEpic(subtask4);

        subtask4.setEpic(epic2);
        epic2.addSubtaskToEpic(subtask4);


        httpTaskManager.addTask(task1); // 0
        httpTaskManager.addTask(task2); // 1  две задачи
        httpTaskManager.addEpic(epic1); // 2 эпик с тремя подзадачами
        httpTaskManager.addEpic(epic2); // 3 эпик с одной подзадачей
        httpTaskManager.addSubtask(subtask1); // 4 подзадача эпика 1
        httpTaskManager.addSubtask(subtask2); // 5 подзадача эпика 1
        httpTaskManager.addSubtask(subtask3); // 6 подзадача эпика 1
        httpTaskManager.addSubtask(subtask4); // 7 подзадача эпика 2

        httpTaskManager.getTaskById(1);
        httpTaskManager.getTaskById(0);
        httpTaskManager.getSubtaskById(4);  // -
        httpTaskManager.getSubtaskById(5);  // -
        httpTaskManager.getSubtaskById(6);  // -
        httpTaskManager.getEpicById(2); // Эпик будет удален со всеми подзадачами
        httpTaskManager.getEpicById(3);
        httpTaskManager.getSubtaskById(7);

        httpTaskManager.removeAllEpics();

        Assertions.assertEquals(0, httpTaskManager.allEpics.size());
    }

    @Test
    void getTaskById() throws IOException, InterruptedException {
        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HTTPTaskManager httpTaskManager = new HTTPTaskManager("/localhost:8078/");

        // Блок для теста httpTaskManager
        //FileBackedTasksManager fileBackedTasksManager = Managers.getDefault();
        Task task1 = new Task(httpTaskManager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2010, 1, 1, 0, 0, 0, 0), 20L);
        Task task2 = new Task(httpTaskManager.generateId(), "Task2", "desc for task2", Status.IN_PROGRESS,
                LocalDateTime.of(2012, 1, 1, 0, 0, 0, 0), 20L);

        // 2
        Epic epic1 = new Epic(httpTaskManager.generateId(), "Epic1 with three subtasks", "desc", Status.NEW);
        Epic epic2 = new Epic(httpTaskManager.generateId(), "Epic2 with 1 subtask", "desc of epic2", Status.NEW);

        Subtask subtask1 = new Subtask(httpTaskManager.generateId(), "subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask2 = new Subtask(httpTaskManager.generateId(), "subtask2", "desc of subtask2", Status.IN_PROGRESS,
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask3 = new Subtask(httpTaskManager.generateId(), "subtask3", "desc of subtask3", Status.DONE,
                LocalDateTime.of(2002, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask4 = new Subtask(httpTaskManager.generateId(), "subtask4", "desc of subtask4", Status.DONE,
                LocalDateTime.of(2003, 1, 1, 0, 0, 0, 0), 20L);

        subtask1.setEpic(epic1);
        subtask2.setEpic(epic1);
        subtask3.setEpic(epic1);

        epic1.addSubtaskToEpic(subtask1);
        epic1.addSubtaskToEpic(subtask2);
        epic1.addSubtaskToEpic(subtask4);

        subtask4.setEpic(epic2);
        epic2.addSubtaskToEpic(subtask4);


        httpTaskManager.addTask(task1); // 0
        httpTaskManager.addTask(task2); // 1  две задачи
        httpTaskManager.addEpic(epic1); // 2 эпик с тремя подзадачами
        httpTaskManager.addEpic(epic2); // 3 эпик с одной подзадачей
        httpTaskManager.addSubtask(subtask1); // 4 подзадача эпика 1
        httpTaskManager.addSubtask(subtask2); // 5 подзадача эпика 1
        httpTaskManager.addSubtask(subtask3); // 6 подзадача эпика 1
        httpTaskManager.addSubtask(subtask4); // 7 подзадача эпика 2

        httpTaskManager.getTaskById(1);
        httpTaskManager.getTaskById(0);
        httpTaskManager.getSubtaskById(4);  // -
        httpTaskManager.getSubtaskById(5);  // -
        httpTaskManager.getSubtaskById(6);  // -
        httpTaskManager.getEpicById(2); // Эпик будет удален со всеми подзадачами
        httpTaskManager.getEpicById(3);
        httpTaskManager.getSubtaskById(7);

        Task task = httpTaskManager.getTaskById(1);

        Assertions.assertEquals(1, task.getId());
    }

    @Test
    void getSubtaskById() {
        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HTTPTaskManager httpTaskManager = null;

        httpTaskManager = new HTTPTaskManager("/localhost:8078/");


        // Блок для теста httpTaskManager
        //FileBackedTasksManager fileBackedTasksManager = Managers.getDefault();
        Task task1 = new Task(httpTaskManager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2010, 1, 1, 0, 0, 0, 0), 20L);
        Task task2 = new Task(httpTaskManager.generateId(), "Task2", "desc for task2", Status.IN_PROGRESS,
                LocalDateTime.of(2012, 1, 1, 0, 0, 0, 0), 20L);

        // 2
        Epic epic1 = new Epic(httpTaskManager.generateId(), "Epic1 with three subtasks", "desc", Status.NEW);
        Epic epic2 = new Epic(httpTaskManager.generateId(), "Epic2 with 1 subtask", "desc of epic2", Status.NEW);

        Subtask subtask1 = new Subtask(httpTaskManager.generateId(), "subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask2 = new Subtask(httpTaskManager.generateId(), "subtask2", "desc of subtask2", Status.IN_PROGRESS,
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask3 = new Subtask(httpTaskManager.generateId(), "subtask3", "desc of subtask3", Status.DONE,
                LocalDateTime.of(2002, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask4 = new Subtask(httpTaskManager.generateId(), "subtask4", "desc of subtask4", Status.DONE,
                LocalDateTime.of(2003, 1, 1, 0, 0, 0, 0), 20L);

        subtask1.setEpic(epic1);
        subtask2.setEpic(epic1);
        subtask3.setEpic(epic1);

        epic1.addSubtaskToEpic(subtask1);
        epic1.addSubtaskToEpic(subtask2);
        epic1.addSubtaskToEpic(subtask4);

        subtask4.setEpic(epic2);
        epic2.addSubtaskToEpic(subtask4);


        httpTaskManager.addTask(task1); // 0
        httpTaskManager.addTask(task2); // 1  две задачи
        httpTaskManager.addEpic(epic1); // 2 эпик с тремя подзадачами
        httpTaskManager.addEpic(epic2); // 3 эпик с одной подзадачей
        httpTaskManager.addSubtask(subtask1); // 4 подзадача эпика 1
        httpTaskManager.addSubtask(subtask2); // 5 подзадача эпика 1
        httpTaskManager.addSubtask(subtask3); // 6 подзадача эпика 1
        httpTaskManager.addSubtask(subtask4); // 7 подзадача эпика 2

        httpTaskManager.getTaskById(1);
        httpTaskManager.getTaskById(0);
        httpTaskManager.getSubtaskById(4);  // -
        httpTaskManager.getSubtaskById(5);  // -
        httpTaskManager.getSubtaskById(6);  // -
        httpTaskManager.getEpicById(2); // Эпик будет удален со всеми подзадачами
        httpTaskManager.getEpicById(3);
        httpTaskManager.getSubtaskById(7);

        Subtask subtask = httpTaskManager.getSubtaskById(4);

        Assertions.assertEquals(4, subtask.getId());
    }

    @Test
    void getEpicById() {
        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HTTPTaskManager httpTaskManager = null;
        httpTaskManager = new HTTPTaskManager("/localhost:8078/");


        // Блок для теста httpTaskManager
        //FileBackedTasksManager fileBackedTasksManager = Managers.getDefault();
        Task task1 = new Task(httpTaskManager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2010, 1, 1, 0, 0, 0, 0), 20L);
        Task task2 = new Task(httpTaskManager.generateId(), "Task2", "desc for task2", Status.IN_PROGRESS,
                LocalDateTime.of(2012, 1, 1, 0, 0, 0, 0), 20L);

        // 2
        Epic epic1 = new Epic(httpTaskManager.generateId(), "Epic1 with three subtasks", "desc", Status.NEW);
        Epic epic2 = new Epic(httpTaskManager.generateId(), "Epic2 with 1 subtask", "desc of epic2", Status.NEW);

        Subtask subtask1 = new Subtask(httpTaskManager.generateId(), "subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask2 = new Subtask(httpTaskManager.generateId(), "subtask2", "desc of subtask2", Status.IN_PROGRESS,
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask3 = new Subtask(httpTaskManager.generateId(), "subtask3", "desc of subtask3", Status.DONE,
                LocalDateTime.of(2002, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask4 = new Subtask(httpTaskManager.generateId(), "subtask4", "desc of subtask4", Status.DONE,
                LocalDateTime.of(2003, 1, 1, 0, 0, 0, 0), 20L);

        subtask1.setEpic(epic1);
        subtask2.setEpic(epic1);
        subtask3.setEpic(epic1);

        epic1.addSubtaskToEpic(subtask1);
        epic1.addSubtaskToEpic(subtask2);
        epic1.addSubtaskToEpic(subtask4);

        subtask4.setEpic(epic2);
        epic2.addSubtaskToEpic(subtask4);


        httpTaskManager.addTask(task1); // 0
        httpTaskManager.addTask(task2); // 1  две задачи
        httpTaskManager.addEpic(epic1); // 2 эпик с тремя подзадачами
        httpTaskManager.addEpic(epic2); // 3 эпик с одной подзадачей
        httpTaskManager.addSubtask(subtask1); // 4 подзадача эпика 1
        httpTaskManager.addSubtask(subtask2); // 5 подзадача эпика 1
        httpTaskManager.addSubtask(subtask3); // 6 подзадача эпика 1
        httpTaskManager.addSubtask(subtask4); // 7 подзадача эпика 2

        httpTaskManager.getTaskById(1);
        httpTaskManager.getTaskById(0);
        httpTaskManager.getSubtaskById(4);  // -
        httpTaskManager.getSubtaskById(5);  // -
        httpTaskManager.getSubtaskById(6);  // -
        httpTaskManager.getEpicById(2); // Эпик будет удален со всеми подзадачами
        httpTaskManager.getEpicById(3);
        httpTaskManager.getSubtaskById(7);

        Epic epic = httpTaskManager.getEpicById(2);

        Assertions.assertEquals(2, epic.getId());
    }

    @Test
    void addTask() {
        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HTTPTaskManager httpTaskManager = null;

        httpTaskManager = new HTTPTaskManager("/localhost:8078/");


        // Блок для теста httpTaskManager
        //FileBackedTasksManager fileBackedTasksManager = Managers.getDefault();
        Task task1 = new Task(httpTaskManager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2010, 1, 1, 0, 0, 0, 0), 20L);

        httpTaskManager.addTask(task1);

        Assertions.assertEquals(1, httpTaskManager.allTasks.size());
    }

    @Test
    void addSubtask() {
        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HTTPTaskManager httpTaskManager = null;
        httpTaskManager = new HTTPTaskManager("/localhost:8078/");


        // Блок для теста httpTaskManager
        //FileBackedTasksManager fileBackedTasksManager = Managers.getDefault();
        Subtask subtask1 = new Subtask(httpTaskManager.generateId(), "subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);

        httpTaskManager.addSubtask(subtask1);

        Assertions.assertEquals(1, httpTaskManager.allSubtasks.size());
    }

    @Test
    void addEpic() throws IOException, InterruptedException {
        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HTTPTaskManager httpTaskManager = new HTTPTaskManager("/localhost:8078/");

        Epic epic1 = new Epic(httpTaskManager.generateId(), "Epic1", "desc", Status.NEW);


        Subtask subtask1 = new Subtask(httpTaskManager.generateId(), "subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);

        epic1.addSubtaskToEpic(subtask1);
        subtask1.setEpic(epic1);

        httpTaskManager.addEpic(epic1);



        Assertions.assertEquals(1, httpTaskManager.allEpics.size());
    }

    @Test
    void updateTask() {
        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HTTPTaskManager httpTaskManager = null;
        httpTaskManager = new HTTPTaskManager("/localhost:8078/");


        // Блок для теста httpTaskManager
        //FileBackedTasksManager fileBackedTasksManager = Managers.getDefault();
        Task task1 = new Task(0, "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2010, 1, 1, 0, 0, 0, 0), 20L);

        Task task2 = new Task(0, "Task1", "desc for task1", Status.DONE,
                LocalDateTime.of(2010, 1, 1, 0, 0, 0, 0), 20L);

        httpTaskManager.addTask(task1);
        httpTaskManager.updateTask(task2);

        Assertions.assertEquals(httpTaskManager.allTasks.get(0).getDuration(), 20L);
    }

    @Test
    void updateSubtask() {
        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HTTPTaskManager httpTaskManager = null;
        httpTaskManager = new HTTPTaskManager("/localhost:8078/");

        Epic epic1 = new Epic(httpTaskManager.generateId(), "Epic1", "desc", Status.NEW);

        Subtask subtask1 = new Subtask(0, "subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);

        Subtask subtask2 = new Subtask(0, "subtask1", "desc of subtask1", Status.DONE,
                LocalDateTime.of(1998, 1, 1, 0, 0, 0, 0), 20L);

        epic1.addSubtaskToEpic(subtask1);
        epic1.addSubtaskToEpic(subtask2);
        subtask1.setEpic(epic1);
        subtask2.setEpic(epic1);

        httpTaskManager.addSubtask(subtask1);
        httpTaskManager.updateSubtask(subtask2);

        Assertions.assertEquals(Status.DONE, httpTaskManager.allSubtasks.get(0).getStatus());
    }

    @Test
    void updateEpic() {
    }

    @Test
    void removeTaskById() {
        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HTTPTaskManager httpTaskManager = null;

        httpTaskManager = new HTTPTaskManager("/localhost:8078/");


        // Блок для теста httpTaskManager
        //FileBackedTasksManager fileBackedTasksManager = Managers.getDefault();
        Task task1 = new Task(0, "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2010, 1, 1, 0, 0, 0, 0), 20L);

        Task task2 = new Task(0, "Task1", "desc for task1", Status.DONE,
                LocalDateTime.of(2010, 1, 1, 0, 0, 0, 0), 20L);

        httpTaskManager.addTask(task1);
        httpTaskManager.addTask(task2);
        httpTaskManager.removeTaskById(0);
        Assertions.assertEquals(0, httpTaskManager.allTasks.size());
    }

    @Test
    void removeSubtaskById() {
        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HTTPTaskManager httpTaskManager = null;
        httpTaskManager = new HTTPTaskManager("/localhost:8078/");


        // Блок для теста httpTaskManager
        //FileBackedTasksManager fileBackedTasksManager = Managers.getDefault();
        Epic epic1 = new Epic(1, "Epic1", "desc", Status.NEW);
        Subtask subtask1 = new Subtask(0, "subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0), 20L);
        Subtask subtask2 = new Subtask(2, "subtask1", "desc of subtask1", Status.NEW,
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0), 20L);

        epic1.addSubtaskToEpic(subtask1);
        epic1.addSubtaskToEpic(subtask2);
        subtask1.setEpic(epic1);
        subtask2.setEpic(epic1);

        httpTaskManager.addEpic(epic1);
        httpTaskManager.addSubtask(subtask1);
        httpTaskManager.addSubtask(subtask2);

        httpTaskManager.removeSubtaskById(0);

        Assertions.assertEquals(1, httpTaskManager.allSubtasks.size());
    }

    @Test
    void removeEpicById() {
    }

    @Test
    void removeAllSubtasksOfEpic() {
    }

    @Test
    void save() {
    }

    @Test
    void loadFromServer() {
    }
}
import managers.FileBackedTasksManager;
import managers.HttpTaskServer;
import managers.KVServer;
import managers.Managers;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

//        try {
//            HttpTaskServer server = new HttpTaskServer();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Блок для теста fileBacked
        FileBackedTasksManager fileBackedTasksManager = Managers.getDefault();
        Task task1 = new Task(fileBackedTasksManager.generateId(), "Task1", "desc for task1", Status.NEW,
                LocalDateTime.of(2010, 1, 1, 0, 0, 0, 0), 20L);
        Task task2 = new Task(fileBackedTasksManager.generateId(), "Task2", "desc for task2", Status.IN_PROGRESS,
                LocalDateTime.of(2012, 1, 1, 0, 0, 0, 0), 20L);

        // 2
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


        fileBackedTasksManager.addTask(task1); // 0
        fileBackedTasksManager.addTask(task2); // 1  две задачи
        fileBackedTasksManager.addEpic(epic1); // 2 эпик с тремя подзадачами
        fileBackedTasksManager.addEpic(epic2); // 3 эпик с одной подзадачей
        fileBackedTasksManager.addSubtask(subtask1); // 4 подзадача эпика 1
        fileBackedTasksManager.addSubtask(subtask2); // 5 подзадача эпика 1
        fileBackedTasksManager.addSubtask(subtask3); // 6 подзадача эпика 1
        fileBackedTasksManager.addSubtask(subtask4); // 7 подзадача эпика 2

        fileBackedTasksManager.getTaskById(1);
        fileBackedTasksManager.getTaskById(0);
        fileBackedTasksManager.getSubtaskById(4);  // -
        fileBackedTasksManager.getSubtaskById(5);  // -
        fileBackedTasksManager.getSubtaskById(6);  // -
        fileBackedTasksManager.getEpicById(2); // Эпик будет удален со всеми подзадачами
        fileBackedTasksManager.getEpicById(3);
        fileBackedTasksManager.getSubtaskById(7);

        //fileBackedTasksManager.removeEpicById(2);
        // Блок для теста fileBacked

//        // загрузка истории
//        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile();
//        List<Task> history = fileBackedTasksManager.getHistory();
//        System.out.println("Загруженная история");
//        for (Task t : history) {
//            System.out.println(t);
//        }

    }
}

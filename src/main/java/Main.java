import managers.*;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        try {
            HttpTaskServer server = new HttpTaskServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HTTPTaskManager httpTaskManager = new HTTPTaskManager("/localhost:8078/");

        // Блок для теста fileBacked
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




        HTTPTaskManager manager2 = new HTTPTaskManager("/localhost:8078/");
        System.out.println("Print response from server!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        manager2.loadFromServer();
        System.out.println("Завершение");

        //fileBackedTasksManager.removeEpicById(2);
        // Блок для теста fileBacked


//        KVTaskClient kvTaskClient = new KVTaskClient();
//        kvTaskClient.put("312", "некое значение");
//        kvTaskClient.put("77777", "еще одно строковое значение");
//
//        System.out.println(kvTaskClient.load("312"));
//        System.out.println(kvTaskClient.load("77777"));




//        // загрузка истории
//        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile();
//        List<Task> history = fileBackedTasksManager.getHistory();
//        System.out.println("Загруженная история");
//        for (Task t : history) {
//            System.out.println(t);
//        }

    }
}

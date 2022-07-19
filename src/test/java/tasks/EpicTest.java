package tasks;

import managers.FileBackedTasksManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void defineEpicStatusTest(){
        //a. Пустой список подзадач.
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        Epic epic1 = new Epic(fileBackedTasksManager.generateId(), "Epic1 with tree subtasks", "desc", Status.DONE);
        epic1.defineEpicStatus();
        Assertions.assertEquals(Status.NEW, epic1.getStatus());
        //b. Все подзадачи со статусом NEW.
        Subtask subtask1 = new Subtask(fileBackedTasksManager.generateId(), "subtask1", "desc of subtask1", Status.NEW);
        Subtask subtask2 = new Subtask(fileBackedTasksManager.generateId(), "subtask2", "desc of subtask2", Status.NEW);
        epic1.addSubtaskToEpic(subtask1);
        epic1.addSubtaskToEpic(subtask2);
        epic1.defineEpicStatus();
        Assertions.assertEquals(Status.NEW, epic1.getStatus());
        //c. Все подзадачи со статусом DONE.
        Epic epic2 = new Epic(fileBackedTasksManager.generateId(), "Epic1 with tree subtasks", "desc", Status.NEW);
        Subtask subtask3 = new Subtask(fileBackedTasksManager.generateId(), "subtask1", "desc of subtask1", Status.DONE);
        Subtask subtask4 = new Subtask(fileBackedTasksManager.generateId(), "subtask2", "desc of subtask2", Status.DONE);
        epic2.addSubtaskToEpic(subtask3);
        epic2.addSubtaskToEpic(subtask4);
        epic2.defineEpicStatus();
        Assertions.assertEquals(Status.DONE, epic2.getStatus());
        //d. Подзадачи со статусами NEW и DONE.
        Epic epic3 = new Epic(fileBackedTasksManager.generateId(), "Epic1 with tree subtasks", "desc", Status.NEW);
        Subtask subtask5 = new Subtask(fileBackedTasksManager.generateId(), "subtask2", "desc of subtask2", Status.DONE);
        Subtask subtask6 = new Subtask(fileBackedTasksManager.generateId(), "subtask2", "desc of subtask2", Status.NEW);
        epic3.addSubtaskToEpic(subtask5);
        epic3.addSubtaskToEpic(subtask6);
        epic3.defineEpicStatus();
        Assertions.assertEquals(Status.IN_PROGRESS, epic3.getStatus());
        //e. Подзадачи со статусом IN_PROGRESS.
        Epic epic4 = new Epic(fileBackedTasksManager.generateId(), "Epic1 with tree subtasks", "desc", Status.NEW);
        Subtask subtask7 = new Subtask(fileBackedTasksManager.generateId(), "subtask2", "desc of subtask2", Status.IN_PROGRESS);
        Subtask subtask8 = new Subtask(fileBackedTasksManager.generateId(), "subtask2", "desc of subtask2", Status.IN_PROGRESS);
        epic4.addSubtaskToEpic(subtask7);
        epic4.addSubtaskToEpic(subtask8);
        epic4.defineEpicStatus();
        Assertions.assertEquals(Status.IN_PROGRESS, epic4.getStatus());
    }





}
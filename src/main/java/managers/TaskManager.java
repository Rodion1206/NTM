package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {

    // Простотр истории задач
    List<Task> getHistory();

    // Получение списка всех задач
    List<Task> getAllTasksAsList();

    List<Subtask> getAllSubtasksAsList();

    List<Epic> getAllEpicsAsList();

    // Удаление всех задач
    void removeAllTasks();

    void removeAllSubtasks();

    void removeAllEpics();

    // Получение по идентификатору
    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    // Создание. Сам объект должен передаваться в качестве параметра
    void addTask(Task t);

    void addSubtask(Subtask s);

    void addEpic(Epic e);

    // Обновление. Новая версия объекта с верным идентификатором передается в виде параметра
    void updateTask(Task t);

    void updateSubtask(Subtask s);

    void updateEpic(Epic e);

    // Удаление по идентификатору
    void removeTaskById(int id);

    void removeSubtaskById(int id);

    void removeEpicById(int id);

    // определить наличие в мапе
    boolean isAllTasksContainsThisKey(int key);

    boolean isAllSubtasksContainsThisKey(int key);

    boolean isAllEpicsContainsThisKey(int key);

    // Получение списка всех подзадач определенного эпика
    List<Subtask> getAllSubtasksOfEpic(Epic e);

    void removeAllSubtasksOfEpic(int id);
}

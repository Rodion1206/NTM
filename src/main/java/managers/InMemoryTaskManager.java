package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int idCounter;
    protected Map<Integer, Task> allTasks;
    protected Map<Integer, Subtask> allSubtasks;
    protected Map<Integer, Epic> allEpics;
    private final static int MAX_LIST_SIZE = 10;
    protected HistoryManager historyManager;
    protected TreeMap<LocalDateTime, Task> tasksByPriority;


    public InMemoryTaskManager() {
        this.idCounter = -1;
        this.allTasks = new HashMap<>();
        this.allSubtasks = new HashMap<>();
        this.allEpics = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    public int generateId() {
        this.idCounter += 1;
        return this.idCounter;
    }

    public boolean isIntersection(Task task, Task t) {
        if (t.getStartTime().isBefore(task.getStartTime()) && t.getEndTime().isBefore(task.getEndTime())
                && t.getEndTime().isAfter(task.getStartTime())) {
            return true;
        }
        if (t.getStartTime().isAfter(task.getStartTime()) && t.getEndTime().isAfter(task.getEndTime())
                && t.getStartTime().isBefore(task.getEndTime())){
            return true;
        }
        if (t.getStartTime().isAfter(task.getStartTime()) && t.getEndTime().isBefore(task.getEndTime())) {
            return true;
        }
        if (t.getStartTime().isBefore(task.getStartTime()) && t.getEndTime().isAfter(task.getEndTime())) {
            return true;
        }
        if (t.getStartTime().equals(task.getStartTime()) && t.getEndTime().equals(task.getEndTime())) {
            return true;
        }
        if (t.getStartTime().equals(task.getStartTime())) {
            return true;
        }
        if (t.getEndTime().equals(task.getEndTime())) {
            return true;
        }

        return false;
    }

    // Просмотр истории задач - просмотром считается вызов по идентификатору
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Получение списка всех задач
    @Override
    public List<Task> getAllTasksAsList() {
        List<Task> result = new ArrayList<>();
        for (Task t : allTasks.values()) {
            result.add(t);
        }
        return result;
    }

    @Override
    public List<Subtask> getAllSubtasksAsList() {
        List<Subtask> result = new ArrayList<>();
        for (Subtask s : allSubtasks.values()) {
            result.add(s);
        }
        return result;
    }

    @Override
    public List<Epic> getAllEpicsAsList() {
        List<Epic> result = new ArrayList<>();
        for (Epic e : allEpics.values()) {
            result.add(e);
        }
        return result;
    }

    // Удаление всех задач
    @Override
    public void removeAllTasks() {
        allTasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        allSubtasks.clear();

        for (Epic e : allEpics.values()) {
            e.setSubtaskOfEpic(new HashMap<>());
            e.defineEpicStatus();
            e.setDuration(0L);
            e.setStartTime(null);
            allEpics.put(e.getId(), e);
        }
    }

    @Override
    public void removeAllEpics() {
        allEpics.clear();
        allSubtasks.clear();
    }

    // Получение по идентификатору
    @Override
    public Task getTaskById(int id) {
        if (allTasks.containsKey(id)) {
            // add to history
            historyManager.add(allTasks.get(id));
            // add to history
            return allTasks.get(id);
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (allSubtasks.containsKey(id)) {
            // add to history
            historyManager.add(allSubtasks.get(id));
            // add to history
            return allSubtasks.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpicById(int id) {
        if (allEpics.containsKey(id)) {
            // add to history
            historyManager.add(allEpics.get(id));
            // add to history
            return allEpics.get(id);
        }
        return null;
    }

    // Создание. Сам объект должен передаваться в качестве параметра
    @Override
    public void addTask(Task t) {
        if (allTasks.containsKey(t.getId())) {
            return;
        }

        // проверять на пересечение по времени
        for (Task task : allTasks.values()) {
            if (isIntersection(task, t)) {
                System.out.println("Задача " + t.getId() + " пересекается по времени с задачей " + task.getId());
                return;
            }
        }

        for (Subtask subtask : allSubtasks.values()) {
            if (isIntersection(subtask, t)) {
                System.out.println("Задача " + t.getId() + " пересекается по времени с подзадачей " + subtask.getId());
                return;
            }
        }
        // проверять на пересечение по времени

        allTasks.put(t.getId(), t);
    }

    @Override
    public void addSubtask(Subtask s) {
        if (allSubtasks.containsKey(s.getId())) {
            return;
        }

        // проверять на пересечение по времени
        for (Task task : allTasks.values()) {
            if (isIntersection(task, s)) {
                System.out.println("Подзадача " + s.getId() + " пересекается по времени с задачей " + task.getId());
                return;
            }
        }

        for (Subtask subtask : allSubtasks.values()) {
            if (isIntersection(subtask, s)) {
                System.out.println("Подзадача " + s.getId() + " пересекается по времени с подзадачей " + subtask.getId());
                return;
            }
        }
        // проверять на пересечение по времени

        allSubtasks.put(s.getId(), s);
    }

    @Override
    public void addEpic(Epic e) {
        if (allEpics.containsKey(e.getId())) {
            return;
        }
        allEpics.put(e.getId(), e);
    }

    // Обновление. Новая версия объекта с верным идентификатором передается в виде параметра
    @Override
    public void updateTask(Task t) {
        if (!allTasks.containsKey(t.getId())) {
            return;
        }
        // проверять на пересечение по времени
        for (Task task : allTasks.values()) {
            if (isIntersection(task, t)) {
                System.out.println("Задача " + t.getId() + " пересекается по времени с задачей " + task.getId());
                return;
            }
        }

        for (Subtask subtask : allSubtasks.values()) {
            if (isIntersection(subtask, t)) {
                System.out.println("Задача " + t.getId() + " пересекается по времени с подзадачей " + subtask.getId());
                return;
            }
        }
        // проверять на пересечение по времени

        allTasks.put(t.getId(), t);
    }

    @Override
    public void updateSubtask(Subtask s) {
        if (!allSubtasks.containsKey(s.getId())) {
            return;
        }
        // проверять на пересечение по времени
        for (Task task : allTasks.values()) {
            if (isIntersection(task, s)) {
                System.out.println("Подзадача " + s.getId() + " пересекается по времени с задачей " + task.getId());
                return;
            }
        }

        for (Subtask subtask : allSubtasks.values()) {
            if (isIntersection(subtask, s)) {
                System.out.println("Подзадача " + s.getId() + " пересекается по времени с подзадачей " + subtask.getId());
                return;
            }
        }
        // проверять на пересечение по времени

        Epic checkedEpic = s.getEpic();
        allSubtasks.put(s.getId(), s);
        checkedEpic.addSubtaskToEpic(s);
        allEpics.put(checkedEpic.getId(), checkedEpic);

    }

    @Override
    public void updateEpic(Epic e) {
        if (allEpics.containsKey(e.getId())) {
            Epic OldEpic = allEpics.get(e.getId());

            // всем подзадачам присвоить новый эпик
            for (Subtask s : allSubtasks.values()) {
                if (s.getEpic().equals(OldEpic)) {
                    s.setEpic(e);
                    e.addSubtaskToEpic(s);
                    allSubtasks.put(s.getId(), s);
                }
            }
            e.defineEpicStatus();
            allEpics.put(e.getId(), e);
        }
        return;
    }

    // Удаление по идентификатору
    @Override
    public void removeTaskById(int id) {
        if (allTasks.containsKey(id)) {
            // remove from history
            historyManager.remove(id);
            // remove from history
            allTasks.remove(id);
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        if (allSubtasks.containsKey(id)) {
            // remove from history
            historyManager.remove(id);
            // remove from history
            Subtask removableSubtask = allSubtasks.remove(id);
            Epic checkedEpic = removableSubtask.getEpic();
            checkedEpic.removeSubtaskFromEpic(removableSubtask);
            allEpics.put(checkedEpic.getId(), checkedEpic);
        }
    }

    @Override
    public void removeEpicById(int id) {
        if (allEpics.containsKey(id)) {
            // remove Epic from history
            historyManager.remove(id);
            // remove from history
            Epic removableEpic = allEpics.remove(id);

            List<Integer> NodeIndexesForRemoveFromHistory = new ArrayList<>();
            for (Subtask s : allSubtasks.values()) {
                if (s.getEpic().equals(removableEpic)) {
                    NodeIndexesForRemoveFromHistory.add(s.getId());
                }
            }

            for (Integer i : NodeIndexesForRemoveFromHistory) {
                // remove subtasks from history
                historyManager.remove(i);
                // remove subtasks from history
            }

            // такой способ выдает ConcurrentModificationException
//            for (Subtask s : allSubtasks.values()) {
//                if (s.getEpic().equals(removableEpic)) {
//                    // remove from history
//                        historyManager.remove(s.getId());
//                    // remove from history
//                    allSubtasks.remove(s.getId());
//                }
//            }
        }
    }

    // Получение списка всех подзадач определенного эпика
    @Override
    public List<Subtask> getAllSubtasksOfEpic(Epic e) {
        return e.getSubtaskOfEpic();
    }

    public void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
    }
}

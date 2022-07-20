package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private String pathToFile;

    public FileBackedTasksManager(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    // Простотр истории задач
    @Override
    public List<Task> getHistory() {
        try {
            return super.getHistory();
        } finally {
            save();
        }

    }

    // Получение списка всех задач
    @Override
    public List<Task> getAllTasksAsList() {
        try {
            return super.getAllTasksAsList();
        } finally {
            save();
        }

    }

    @Override
    public List<Subtask> getAllSubtasksAsList() {
        try {
            return super.getAllSubtasksAsList();
        } finally {
            save();
        }

    }

    @Override
    public List<Epic> getAllEpicsAsList() {
        try {
            return super.getAllEpicsAsList();
        } finally {
            save();
        }

    }

    // Удаление всех задач
    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    // Получение по идентификатору
    @Override
    public Task getTaskById(int id) {
        try {
            return super.getTaskById(id);
        } finally {
            save();
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        try {
            return super.getSubtaskById(id);
        } finally {
            save();
        }

    }

    @Override
    public Epic getEpicById(int id) {
        try {
            return super.getEpicById(id);
        } finally {
            save();
        }

    }

    // Создание. Сам объект должен передаваться в качестве параметра
    @Override
    public void addTask(Task t) {
        super.addTask(t);
        save();
    }

    @Override
    public void addSubtask(Subtask s) {
        super.addSubtask(s);
        save();
    }

    @Override
    public void addEpic(Epic e) {
        super.addEpic(e);
        save();
    }

    // Обновление. Новая версия объекта с верным идентификатором передается в виде параметра
    @Override
    public void updateTask(Task t) {
        super.updateTask(t);
        save();
    }

    @Override
    public void updateSubtask(Subtask s) {
        super.updateSubtask(s);
        save();
    }

    @Override
    public void updateEpic(Epic e) {
        super.updateEpic(e);
        save();
    }

    // Удаление по идентификатору
    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    // Получение списка всех подзадач определенного эпика
    @Override
    public List<Subtask> getAllSubtasksOfEpic(Epic e) {
        try {
            return super.getAllSubtasksOfEpic(e);
        } finally {
            save();
        }

    }

    public void save() {
        try {
            Writer fw = new FileWriter(pathToFile, StandardCharsets.UTF_8, false);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(CsvTaskSerializer.HEADER());
            bw.newLine();

            for (Task task : allTasks.values()) {
                bw.write(task.toString());
                bw.newLine();
            }

            for (Epic epic : allEpics.values()) {
                bw.write(epic.toString());
                bw.newLine();
            }

            for (Subtask subtask : allSubtasks.values()) {
                bw.write(subtask.toString());
                bw.newLine();
            }

            bw.newLine();

            List<Task> history = historyManager.getHistory();
            for (int i = 0; i < history.size(); i++) {
                if (i < history.size() - 1) {
                    bw.write(history.get(i).getId() + ",");
                } else {
                    bw.write(history.get(i).getId() + "");
                }
            }

            bw.close();
            fw.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при работе с файлом");
        }
    }

    public static FileBackedTasksManager loadFromFile() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        int maxId = 0;
        boolean lastString = false;

        try (FileReader fr = new FileReader(new File(fileBackedTasksManager.pathToFile)); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                String taskStr = br.readLine();

                if (taskStr.equals(CsvTaskSerializer.HEADER())) {
                    continue;
                }

                if (taskStr.isEmpty()) {
                    lastString = true;
                    continue;
                }

                // восстановление истории
                if (lastString) {
                    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
                    List<Integer> history = historyFromString(taskStr);

                    for (Integer i : history) {

                        if (fileBackedTasksManager.allTasks.containsKey(i)) {
                            inMemoryHistoryManager.add(fileBackedTasksManager.allTasks.get(i));
                        } else if (fileBackedTasksManager.allSubtasks.containsKey(i)) {
                            inMemoryHistoryManager.add(fileBackedTasksManager.allSubtasks.get(i));
                        } else if (fileBackedTasksManager.allEpics.containsKey(i)) {
                            inMemoryHistoryManager.add(fileBackedTasksManager.allEpics.get(i));
                        }

                    }

                    fileBackedTasksManager.historyManager = inMemoryHistoryManager;
                    fileBackedTasksManager.setIdCounter(maxId + 1);
                    break;

                }

                Task deserialization = CsvTaskSerializer.fromString(taskStr);

                if (deserialization.getId() > maxId) {
                    maxId = deserialization.getId();
                }

                if (deserialization.getType() == TaskType.TASK) {
                    fileBackedTasksManager.allTasks.put(deserialization.getId(), deserialization);
                } else if (deserialization.getType() == TaskType.SUBTASK) {
                    fileBackedTasksManager.allSubtasks.put(deserialization.getId(), (Subtask) deserialization);
                } else {
                    fileBackedTasksManager.allEpics.put(deserialization.getId(), (Epic) deserialization);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при работе с файлом");
        }

        // add epic to subtasks
        for (Integer i : fileBackedTasksManager.allSubtasks.keySet()) {
            Subtask subtask = fileBackedTasksManager.allSubtasks.get(i);
            if (subtask.getEpic() == null) {
                int epicId = subtask.getEpicId();

                Epic epicForSubtask = fileBackedTasksManager.allEpics.get(epicId);

                if (epicForSubtask != null) {
                    subtask.setEpic(epicForSubtask);
                }


                fileBackedTasksManager.allSubtasks.put(subtask.getId(), subtask);
            }
        }

        // add subtask to epics
        for (Integer i : fileBackedTasksManager.allEpics.keySet()){
            Epic epic = fileBackedTasksManager.allEpics.get(i);
            for (Subtask subtask : fileBackedTasksManager.allSubtasks.values()) {
                if (subtask.getEpicId() == epic.getId()) {
                    epic.addSubtaskToEpic(subtask);


                }
            }
        }


        return fileBackedTasksManager;

    }

    private static List<Integer> historyFromString(String value) {
        String[] historyLine = value.split(",");

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < historyLine.length; i++) {
            result.add(Integer.parseInt(historyLine[i]));
        }

        return result;
    }
}

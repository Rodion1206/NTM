package managers;

public class Managers {

    private static HistoryManager historyManager;
    private static FileBackedTasksManager fileBackedTasksManager;

    private Managers() {

    }

    public static FileBackedTasksManager getDefault() {
        if (fileBackedTasksManager == null) {
            fileBackedTasksManager = new FileBackedTasksManager("data.csv");
        }
        return fileBackedTasksManager;
    }

    public static HistoryManager getDefaultHistory() {
        if (historyManager == null) {
            historyManager = new InMemoryHistoryManager();
        }
        return historyManager;
    }


}

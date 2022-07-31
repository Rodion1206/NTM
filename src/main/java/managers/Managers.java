package managers;

public class Managers {

    private static HistoryManager historyManager;
    private static FileBackedTasksManager fileBackedTasksManager;
    private static HTTPTaskManager httpTaskManager;

    private Managers() {

    }

    public static HTTPTaskManager getDefault() {
        if (httpTaskManager == null) {
            httpTaskManager = new HTTPTaskManager("localhost:8087/");
        }
        return httpTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        if (historyManager == null) {
            historyManager = new InMemoryHistoryManager();
        }
        return historyManager;
    }


}

package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;

public class CsvTaskSerializer {
    private CsvTaskSerializer() {

    }
    //           0   1   2     3         4           5       6           7
    public static String HEADER() {
        return "id,type,name,status,description,-epicId-,startTime,duration";
    }

    public static Task fromString(String taskCsv) {
        String[] splitString = taskCsv.split(",");

        if (splitString[1].equals("TASK")) {
            return new Task(Integer.parseInt(splitString[0]), splitString[2], splitString[4], defineStatus(splitString[3]));
        } else if (splitString[1].equals("SUBTASK")) {
            // params - id, name, description, status, epic, epicId
            return new Subtask(Integer.parseInt(splitString[0]), splitString[2], splitString[4], defineStatus(splitString[3]),
                    null, Integer.parseInt(splitString[5]));
        } else {
            return new Epic(Integer.parseInt(splitString[0]), splitString[2], splitString[4], defineStatus(splitString[3]));
        }
    }

    private static Status defineStatus(String statusStr) {
        if (statusStr.equals("NEW")) {
            return Status.NEW;
        } else if (statusStr.equals("IN_PROGRESS")) {
            return Status.IN_PROGRESS;
        } else {
            return Status.DONE;
        }
    }
}

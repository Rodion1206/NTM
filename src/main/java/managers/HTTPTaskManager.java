package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import tasks.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager{

//    Теперь можно создать новую реализацию интерфейса TaskManager — класс HTTPTaskManager. Он будет наследовать от
//    FileBackedTasksManager.
//    Конструктор HTTPTaskManager должен будет вместо имени файла принимать URL к серверу KVServer.
//    Также HTTPTaskManager создаёт KVTaskClient, из которого можно получить исходное состояние менеджера.
//    Вам нужно заменить вызовы сохранения состояния в файлах на вызов клиента.
//    В конце обновите статический метод getDefault() в утилитарном классе Managers, чтобы он возвращал HTTPTaskManager.

    public KVTaskClient kvTaskClient;
    public String urlToKVServer;
    public String key;
    private Gson gson;

    // urlToKVServer  localhost:8087/

    public HTTPTaskManager(String urlToKVServer) {

        this.kvTaskClient = new KVTaskClient();
        this.urlToKVServer = urlToKVServer;
        this.key = "Condition";
        this.gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .registerTypeAdapter(Subtask.class, new SubtaskAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .serializeNulls()
                .create();

    }


    @Override
    public void save() {
        //super.save();


        StringBuilder toSave = new StringBuilder();

        List<Task> listToSave = new ArrayList();

        for (Task t : allTasks.values()) {
            listToSave.add(t);
        }

        for (Subtask s : allSubtasks.values()) {
            listToSave.add(s);
        }

        for (Epic e : allEpics.values()) {
            listToSave.add(e);
        }


        toSave.append(gson.toJson(listToSave));

        toSave.append("\n");
        toSave.append("\n");



        List<Task> history = historyManager.getHistory();
        for (int i = 0; i < history.size(); i++) {
            if (i < history.size() - 1) {
                toSave.append(history.get(i).getId() + ",");
            } else {
                toSave.append(history.get(i).getId() + "");
            }
        }


        try {
            kvTaskClient.put(key, toSave.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void loadFromServer() throws IOException, InterruptedException {
        String response = kvTaskClient.load(key.toString());



        int id = 0;
        String name = "";
        String desc = "";
        String strStatus = "";
        String strType = "";
        String startTime = "";
        String duration = "";
        String epicId = "";

        int maxId = 0;

        InputStream in = new ByteArrayInputStream(response.getBytes(Charset.forName("UTF-8")));
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));


        JsonToken token = JsonToken.BEGIN_ARRAY;

        while (token != JsonToken.END_ARRAY) {
            token = reader.peek();

            if (token == JsonToken.BEGIN_OBJECT) {
                reader.beginObject();
            } else if (token == JsonToken.END_OBJECT) {
                reader.endObject();
                // создать объект
                Task object = createObject(id, name, desc, strStatus, strType, startTime, duration, epicId);

                // добавить в мап, в зависимости от типа
                if (object.getType().equals(TaskType.TASK)) {
                    allTasks.put(object.getId(), object);
                } else if (object.getType().equals(TaskType.SUBTASK)) {
                    allSubtasks.put(object.getId(), (Subtask) object);
                } else {
                    allEpics.put(object.getId(), (Epic) object);
                }

                // обнулить переменные
                id = 0;
                name = "";
                desc = "";
                strStatus = "";
                strType = "";
                startTime = "";
                duration = "";
                epicId = "";

            } else if (token == JsonToken.BEGIN_ARRAY) {
                reader.beginArray();
            } else if (token == JsonToken.END_ARRAY) {
                reader.endArray();
            } else if (token == JsonToken.NAME) {
                String attrName = reader.nextName();
                if ("id".equals(attrName)) {
                    reader.peek();
                    id = reader.nextInt();
                    if (id > maxId) {
                        maxId = id;
                    }
                } else if ("name".equals(attrName)) {
                    reader.peek();
                    name = reader.nextString();
                } else if ("desc".equals(attrName)) {
                    reader.peek();
                    desc = reader.nextString();
                } else if ("status".equals(attrName)) {
                    reader.peek();
                    strStatus = reader.nextString();
                } else if ("type".equals(attrName)) {
                    reader.peek();
                    strType = reader.nextString();
                } else if ("startTime".equals(attrName)) {
                    reader.peek();
                    startTime = reader.nextString();
                } else if ("duration".equals(attrName)) {
                    reader.peek();
                    duration = reader.nextString();
                } else if ("epicId".equals(attrName)) {
                    reader.peek();
                    epicId = reader.nextString();
                }
                else {
                    reader.skipValue();
                }
            }
        }
        reader.close();

        // add epic to subtasks
        for (Integer i : this.allSubtasks.keySet()) {
            Subtask subtask = this.allSubtasks.get(i);
            if (subtask.getEpic() == null) {
                int crEpicId = subtask.getEpicId();

                Epic epicForSubtask = this.allEpics.get(crEpicId);

                if (epicForSubtask != null) {
                    subtask.setEpic(epicForSubtask);
                }

                this.allSubtasks.put(subtask.getId(), subtask);
            }
        }

        // add subtask to epics
        for (Integer i : this.allEpics.keySet()){
            Epic epic = this.allEpics.get(i);
            for (Subtask subtask : this.allSubtasks.values()) {
                if (subtask.getEpicId() == epic.getId()) {
                    epic.addSubtaskToEpic(subtask);
                }
            }
        }


        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

        String[] lines = response.split("\n");
        String historyLine = lines[lines.length-1];

        String[] historyIdsArray = historyLine.split(",");

        int currentId = 0;
        for (String e : historyIdsArray) {
            currentId = Integer.parseInt(e);

            if (this.allTasks.containsKey(e)) {
                inMemoryHistoryManager.add(this.allTasks.get(e));
            } else if (this.allSubtasks.containsKey(e)) {
                inMemoryHistoryManager.add(this.allSubtasks.get(e));
            } else if (this.allEpics.containsKey(e)) {
                inMemoryHistoryManager.add(this.allEpics.get(e));
            }

        }

        this.historyManager = inMemoryHistoryManager;
        this.setIdCounter(maxId + 1);



    }



    //////////////////////////////////////////////////////////////////////////////////////////////////
    // создает объект из переданных параметров
    private Task createObject(int id, String name, String desc, String strStatus, String strType, String startTime,
                              String duration, String epicId){

        if (strType.equals("TASK")) {
            return new Task(id, name, desc, defineStatus(strStatus), LocalDateTime.parse(startTime), Long.parseLong(duration));
        } else if (strType.equals("SUBTASK")) {
            return new Subtask(id, name, desc, defineStatus(strStatus), null, Integer.parseInt(epicId),
                    LocalDateTime.parse(startTime), Long.parseLong(duration));
        } else if (strType.equals("EPIC")) {
            return new Epic(id, name, desc, defineStatus(strStatus));
        }


        return null;
    }

    class TaskAdapter extends TypeAdapter<Task> {

        @Override
        public void write(JsonWriter jsonWriter, Task task) throws IOException {
            jsonWriter.beginObject();
            jsonWriter.name("id");
            jsonWriter.value(task.getId());
            jsonWriter.name("name");
            jsonWriter.value(task.getName());
            jsonWriter.name("desc");
            jsonWriter.value(task.getDesc());
            jsonWriter.name("status");
            jsonWriter.value(task.getStatus().toString());
            jsonWriter.name("type");
            jsonWriter.value(task.getType().toString());
            jsonWriter.name("startTime");
            jsonWriter.value(task.getStartTime().toString());
            jsonWriter.name("duration");
            jsonWriter.value(task.getDuration());
            jsonWriter.endObject();
        }

        @Override
        public Task read(JsonReader reader) throws IOException {
            int id = 0;
            String name = "";
            String desc = "";
            String status = "";
            String type = "";
            String startTime = "";
            String duration = "";

//            InputStream in = new ByteArrayInputStream(str.getBytes(Charset.forName("UTF-8")));
//            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

            while (reader.hasNext()) {
                JsonToken jsonToken = reader.peek();

                if (jsonToken == JsonToken.BEGIN_OBJECT) {
                    reader.beginObject();
                } else if (jsonToken == JsonToken.END_OBJECT) {
                    reader.endObject();
                } else if (jsonToken == JsonToken.NAME) {
                    String attrName = reader.nextName();
                    if ("id".equals(attrName)) {
                        reader.peek();
                        id = reader.nextInt();
                    } else if ("name".equals(attrName)) {
                        reader.peek();
                        name = reader.nextString();
                    } else if ("desc".equals(attrName)) {
                        reader.peek();
                        desc = reader.nextString();
                    } else if ("status".equals(attrName)) {
                        reader.peek();
                        status = reader.nextString();
                    } else if ("type".equals(attrName)) {
                        reader.peek();
                        type = reader.nextString();
                    } else if ("startTime".equals(attrName)) {
                        reader.peek();
                        startTime = reader.nextString();
                    } else if ("duration".equals(attrName)) {
                        reader.peek();
                        duration = reader.nextString();
                    } else {
                        reader.skipValue();
                    }
                }
            }
            reader.close();
            return new Task(id, name, desc, defineStatus(status), LocalDateTime.parse(startTime), Long.parseLong(duration));
        }

        private Status defineStatus(String statusStr) {
            if (statusStr.equals("NEW")) {
                return Status.NEW;
            } else if (statusStr.equals("IN_PROGRESS")) {
                return Status.IN_PROGRESS;
            } else {
                return Status.DONE;
            }
        }

    }

    class SubtaskAdapter extends TypeAdapter<Subtask> {

        @Override
        public void write(JsonWriter jsonWriter, Subtask subtask) throws IOException {
            jsonWriter.beginObject();
            jsonWriter.name("id");
            jsonWriter.value(subtask.getId());
            jsonWriter.name("name");
            jsonWriter.value(subtask.getName());
            jsonWriter.name("desc");
            jsonWriter.value(subtask.getDesc());
            jsonWriter.name("status");
            jsonWriter.value(subtask.getStatus().toString());
            jsonWriter.name("type");
            jsonWriter.value(subtask.getType().toString());
            jsonWriter.name("startTime");
            jsonWriter.value(subtask.getStartTime().toString());
            jsonWriter.name("duration");
            jsonWriter.value(subtask.getDuration());
            jsonWriter.name("epicId");
            jsonWriter.value(subtask.getEpicId());
            jsonWriter.endObject();
        }

        @Override
        public Subtask read(JsonReader reader) throws IOException {
            int id = 0;
            String name = "";
            String desc = "";
            String strStatus = "";
            String strType = "";
            String startTime = "";
            String duration = "";
            int epicId = 0;

            while (reader.hasNext()) {
                JsonToken jsonToken = reader.peek();

                if (jsonToken == JsonToken.BEGIN_OBJECT) {
                    reader.beginObject();
                } else if (jsonToken == JsonToken.END_OBJECT) {
                    reader.endObject();
                } else if (jsonToken == JsonToken.NAME) {
                    String attrName = reader.nextName();
                    if ("id".equals(attrName)) {
                        reader.peek();
                        id = reader.nextInt();
                    } else if ("name".equals(attrName)) {
                        reader.peek();
                        name = reader.nextString();
                    } else if ("desc".equals(attrName)) {
                        reader.peek();
                        desc = reader.nextString();
                    } else if ("status".equals(attrName)) {
                        reader.peek();
                        strStatus = reader.nextString();
                    } else if ("type".equals(attrName)) {
                        reader.peek();
                        strType = reader.nextString();
                    } else if ("startTime".equals(attrName)) {
                        reader.peek();
                        startTime = reader.nextString();
                    } else if ("duration".equals(attrName)) {
                        reader.peek();
                        duration = reader.nextString();
                    } else if ("epicId".equals(attrName)) {
                        reader.peek();
                        epicId = reader.nextInt();
                    } else {
                        reader.skipValue();
                    }
                }
            }
            return new Subtask(id, name, desc, defineStatus(strStatus), null, epicId,
                    LocalDateTime.parse(startTime), Long.parseLong(duration));
        }

    }

    class EpicAdapter extends TypeAdapter<Epic> {

        @Override
        public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
            jsonWriter.beginObject();
            jsonWriter.name("id");
            jsonWriter.value(epic.getId());
            jsonWriter.name("name");
            jsonWriter.value(epic.getName());
            jsonWriter.name("desc");
            jsonWriter.value(epic.getDesc());
            jsonWriter.name("status");
            jsonWriter.value(epic.getStatus().toString());
            jsonWriter.name("type");
            jsonWriter.value(epic.getType().toString());
            jsonWriter.name("startTime");
            jsonWriter.value(epic.getStartTime().toString());
            jsonWriter.name("duration");
            jsonWriter.value(epic.getDuration());
            jsonWriter.endObject();
        }

        @Override
        public Epic read(JsonReader reader) throws IOException {
            int id = 0;
            String name = "";
            String desc = "";
            String strStatus = "";
            String strType = "";
            String startTime = "";
            String duration = "";

            while (reader.hasNext()) {
                JsonToken jsonToken = reader.peek();

                if (jsonToken == JsonToken.BEGIN_OBJECT) {
                    reader.beginObject();
                } else if (jsonToken == JsonToken.END_OBJECT) {
                    reader.endObject();
                } else if (jsonToken == JsonToken.NAME) {
                    String attrName = reader.nextName();
                    if ("id".equals(attrName)) {
                        reader.peek();
                        id = reader.nextInt();
                    } else if ("name".equals(attrName)) {
                        reader.peek();
                        name = reader.nextString();
                    } else if ("desc".equals(attrName)) {
                        reader.peek();
                        desc = reader.nextString();
                    } else if ("status".equals(attrName)) {
                        reader.peek();
                        strStatus = reader.nextString();
                    } else if ("type".equals(attrName)) {
                        reader.peek();
                        strType = reader.nextString();
                    } else if ("startTime".equals(attrName)) {
                        reader.peek();
                        startTime = reader.nextString();
                    } else if ("duration".equals(attrName)) {
                        reader.peek();
                        duration = reader.nextString();
                    } else {
                        reader.skipValue();
                    }
                }
            }
            return new Epic(id, name, desc, defineStatus(strStatus));
        }
    }

    private Status defineStatus(String statusStr) {
        if (statusStr.equals("NEW")) {
            return Status.NEW;
        } else if (statusStr.equals("IN_PROGRESS")) {
            return Status.IN_PROGRESS;
        } else {
            return Status.DONE;
        }
    }
}

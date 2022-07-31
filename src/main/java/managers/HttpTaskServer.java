package managers;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskServer {
    private HttpServer httpServer;
    private TaskManager httpTasksManager;


    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks/task", new TasksHandler());
        httpServer.createContext("/tasks/subtask", new SubtaskHandler());
        httpServer.createContext("/tasks/epic", new EpicHandler());
        httpServer.createContext("/tasks/history", new HistoryHandler());
        httpTasksManager = Managers.getDefault();
        httpServer.start();
        System.out.println("Сервер запущен из мейна на 8080 порту");
    }

    // getAllTasks()                        - GET    "/tasks/task"                  +++++++++++++
    // getTaskById(id)                      - GET    "/tasks/task/?id="             +++++++++++++
    // addTask(task), updateTask(task)      - POST   "/tasks/task/ Body: {task..}"  +++++++++++++
    // deleteTaskById (removeTaskById(id))  - DELETE "/tasks/task/?id="             +++++++++++++
    // deleteAllTasks()                     - DELETE "/tasks/task"                  +++++++++++++

    // getAllSubtasks                                    - GET    "/tasks/subtask"
    // getSubtaskById                                    - GET    "/tasks/subtask/?id="
    // addSubtask(subtask), updateSubtask(subtask)       - POST   "/tasks/subtask/ Body: {task..}"
    // removeSubtaskById()                               - DELETE "/tasks/subtask/?id="
    // removeAllSubtasks()                               - DELETE "/tasks/subtask"

    class HistoryHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            //TaskAdapter taskAdapter = new TaskAdapter();
            Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Task.class, new TaskAdapter())
                    .registerTypeAdapter(Subtask.class, new SubtaskAdapter())
                    .registerTypeAdapter(Epic.class, new EpicAdapter())
                    .create();

            // request body
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            // request method
            String method = exchange.getRequestMethod();

            // path request
            String path = exchange.getRequestURI().getPath();
            String[] splitStrings = path.split("/");

            // request parameters
            String params = exchange.getRequestURI().getRawQuery();

            switch (method) {
                case "GET": {
                    List<Task> result = httpTasksManager.getHistory();
                    try (OutputStream os = exchange.getResponseBody()) {
                        exchange.sendResponseHeaders(200, 0);
                        os.write(gson.toJson(result).getBytes(StandardCharsets.UTF_8));
                        exchange.close();
                        return;
                    }
                }
            }
        }
    }

    class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            //TaskAdapter taskAdapter = new TaskAdapter();
            Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Task.class, new TaskAdapter()).create();

            // request body
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            // request method
            String method = exchange.getRequestMethod();

            // path request
            String path = exchange.getRequestURI().getPath();
            String[] splitStrings = path.split("/");

            // request parameters
            String params = exchange.getRequestURI().getRawQuery();

            switch (method) {
                case "GET": {
                    if (params == null) {
                        // "/tasks/task" -  getAllTasks()
                        List<Task> result = httpTasksManager.getAllTasksAsList();
                        try (OutputStream os = exchange.getResponseBody()) {
                            exchange.sendResponseHeaders(200, 0);
                            os.write(gson.toJson(result).getBytes(StandardCharsets.UTF_8));
                            exchange.close();
                            return;
                        }
                    } else {
                        // "/tasks/task/?id=" - getTaskById(id)
                        int id = Integer.parseInt(params.substring(3));
                        Task result = httpTasksManager.getTaskById(id);

                        try (OutputStream os = exchange.getResponseBody()) {
                            exchange.sendResponseHeaders(200, 0);
                            os.write(gson.toJson(result).getBytes(StandardCharsets.UTF_8));
                            exchange.close();
                            return;
                        }

                    }
                } // +++++++++++++
                case "POST": {
                    // addTask(task), updateTask(task)      - POST   "/tasks/task/ Body: {task..}"
                    if (!body.isEmpty()) {
                        JsonElement jsonElement = JsonParser.parseString(body);
                        if (!jsonElement.isJsonObject()) {
                            try (OutputStream os = exchange.getResponseBody()) {
                                exchange.sendResponseHeaders(400, 0);
                                os.write(gson.toJson("Нужно отправить задачу в теле запроса как Json объект").getBytes(StandardCharsets.UTF_8));
                                exchange.close();
                                return;
                            }
                        }

                        Task task = createTaskFromJson(body);
                        // add or update
                        if (httpTasksManager.isAllTasksContainsThisKey(task.getId())){
                            // update
                            try (OutputStream os = exchange.getResponseBody()) {
                                httpTasksManager.updateTask(task);
                                exchange.sendResponseHeaders(201, 0);
                                os.write(gson.toJson("Задача обновлена").getBytes(StandardCharsets.UTF_8));
                                exchange.close();
                                return;
                            }

                        } else {
                            // add
                            try (OutputStream os = exchange.getResponseBody()) {
                                httpTasksManager.addTask(task);
                                exchange.sendResponseHeaders(201, 0);
                                os.write(gson.toJson("Задача добавлена").getBytes(StandardCharsets.UTF_8));
                                exchange.close();
                                return;
                            }
                        }
                    } else {
                        try (OutputStream os = exchange.getResponseBody()) {
                            exchange.sendResponseHeaders(400, 0);
                            os.write(gson.toJson("Нужно отправить задачу в теле запроса как Json объект").getBytes(StandardCharsets.UTF_8));
                            exchange.close();
                            return;
                        }
                    }

                } // ++++++++++++++
                case "DELETE": {
                    if (params == null) {
                        try (OutputStream os = exchange.getResponseBody()) {
                            httpTasksManager.removeAllTasks();
                            exchange.sendResponseHeaders(200, 0);
                            os.write(gson.toJson("Все задачи удалены").getBytes(StandardCharsets.UTF_8));
                            exchange.close();
                            return;
                        }

                    } else {
                        int id = Integer.parseInt(params.substring(3));
                        try (OutputStream os = exchange.getResponseBody()) {
                            httpTasksManager.removeTaskById(id);
                            exchange.sendResponseHeaders(200, 0);
                            os.write(gson.toJson("Задача с id " + id + " удалена").getBytes(StandardCharsets.UTF_8));
                            exchange.close();
                            return;
                        }

                    }

                } // ++++++++++++++
            }

        }
    }

    class SubtaskHandler implements HttpHandler{

        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Subtask.class, new SubtaskAdapter()).create();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // request body
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            // request method
            String method = exchange.getRequestMethod();

            // path request
            String path = exchange.getRequestURI().getPath();
            String[] splitStrings = path.split("/");

            // request parameters
            String params = exchange.getRequestURI().getRawQuery();

            // getAllSubtasks                                    - GET    "/tasks/subtask" +++++++++++++++++++++++
            // getSubtaskById                                    - GET    "/tasks/subtask/?id=" ++++++++++++++++++
            // addSubtask(subtask), updateSubtask(subtask)       - POST   "/tasks/subtask/ Body: {task..}" ++++++++
            // removeSubtaskById()                               - DELETE "/tasks/subtask/?id="  ++++++++++++++++++
            // removeAllSubtasks()                               - DELETE "/tasks/subtask" +++++++++++++++++++++++

            switch (method) {
                case "GET": {
                    if (params == null) {
                        List<Subtask> result = httpTasksManager.getAllSubtasksAsList();
                        try (OutputStream os = exchange.getResponseBody()) {
                            exchange.sendResponseHeaders(200, 0);
                            os.write(gson.toJson(result).getBytes(StandardCharsets.UTF_8));
                            exchange.close();
                            return;
                        }
                    } else {
                        int id = Integer.parseInt(params.substring(3));
                        Subtask result = httpTasksManager.getSubtaskById(id);

                        try (OutputStream os = exchange.getResponseBody()) {
                            exchange.sendResponseHeaders(200, 0);
                            os.write(gson.toJson(result).getBytes(StandardCharsets.UTF_8));
                            exchange.close();
                            return;
                        }

                    }
                } // ++++++++++++++++++++
                case "POST": {
                    // addSubtask(task), updateSubtask(task)      - POST   "/tasks/subtask/ Body: {task..}"

//                    {
//                        "id": 4,
//                        "name": "subtask1",
//                        "desc": "desc of subtask1",
//                        "status": "NEW",
//                        "type": "SUBTASK",
//                        "startTime": "1998-01-01T00:00",
//                        "duration": 20,
//                        "epicId": 2
//                    }
                    if (!body.isEmpty()) {
                        JsonElement jsonElement = JsonParser.parseString(body);
                        if (!jsonElement.isJsonObject()) {
                            try (OutputStream os = exchange.getResponseBody()) {
                                exchange.sendResponseHeaders(400, 0);
                                os.write(gson.toJson("Нужно отправить подзадачу в теле запроса как Json объект").getBytes(StandardCharsets.UTF_8));
                                exchange.close();
                                return;
                            }
                        }

                        Subtask subtask = createSubtaskFromJson(body);
                        // add or update
                        if (httpTasksManager.isAllSubtasksContainsThisKey(subtask.getId())){
                            // update
                            try (OutputStream os = exchange.getResponseBody()) {
                                httpTasksManager.updateSubtask(subtask);
                                exchange.sendResponseHeaders(201, 0);
                                os.write(gson.toJson("Подзадача обновлена").getBytes(StandardCharsets.UTF_8));
                                exchange.close();
                                return;
                            }

                        } else {
                            // add
                            try (OutputStream os = exchange.getResponseBody()) {
                                httpTasksManager.addSubtask(subtask);
                                exchange.sendResponseHeaders(201, 0);
                                os.write(gson.toJson("Подзадача добавлена").getBytes(StandardCharsets.UTF_8));
                                exchange.close();
                                return;
                            }
                        }
                    } else {
                        try (OutputStream os = exchange.getResponseBody()) {
                            exchange.sendResponseHeaders(400, 0);
                            os.write(gson.toJson("Нужно отправить задачу в теле запроса как Json объект").getBytes(StandardCharsets.UTF_8));
                            exchange.close();
                            return;
                        }
                    }
                } // ++++++++++++++++++++
                case "DELETE": {
                    if (params == null) {
                        try (OutputStream os = exchange.getResponseBody()) {
                            httpTasksManager.removeAllSubtasks();
                            exchange.sendResponseHeaders(200, 0);
                            os.write(gson.toJson("Все подзадачи удалены").getBytes(StandardCharsets.UTF_8));
                            exchange.close();
                            return;
                        }

                    } else {
                        int id = Integer.parseInt(params.substring(3));
                        try (OutputStream os = exchange.getResponseBody()) {
                            httpTasksManager.removeSubtaskById(id);
                            exchange.sendResponseHeaders(200, 0);
                            os.write(gson.toJson("Подзадача с id " + id + " удалена").getBytes(StandardCharsets.UTF_8));
                            exchange.close();
                            return;
                        }

                    }
                } // +++++++++++++++++++
            }
        }
    }

    class EpicHandler implements HttpHandler {
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Subtask.class, new SubtaskAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .create();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // request body
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            // request method
            String method = exchange.getRequestMethod();

            // path request
            String path = exchange.getRequestURI().getPath();
            String[] splitStrings = path.split("/");

            // request parameters
            String params = exchange.getRequestURI().getRawQuery();

            // getAllEpics                                       - GET    "/tasks/epic" +++++++++++++++++++++++
            // getEpicById                                       - GET    "/tasks/epic/?id=" ++++++++++++++++++
            // addEpic(epic), updateEpic(epic)                   - POST   "/tasks/epic/ Body: {epic..}"
            // removeEpicById()                                  - DELETE "/tasks/epic/?id="  ++++++++++++++++++
            // removeAllEpics()                                  - DELETE "/tasks/epic" +++++++++++++++++++++++

            switch (method) {
                case "GET": {
                    if (params == null) {
                        List<Epic> result = httpTasksManager.getAllEpicsAsList();
                        try (OutputStream os = exchange.getResponseBody()) {
                            exchange.sendResponseHeaders(200, 0);
                            os.write(gson.toJson(result).getBytes(StandardCharsets.UTF_8));
                            exchange.close();
                            return;
                        }
                    } else {
                        int id = Integer.parseInt(params.substring(3));
                        Epic result = httpTasksManager.getEpicById(id);

                        try (OutputStream os = exchange.getResponseBody()) {
                            exchange.sendResponseHeaders(200, 0);
                            os.write(gson.toJson(result).getBytes(StandardCharsets.UTF_8));
                            exchange.close();
                            return;
                        }

                    }
                } // +++++++++++++++++++
                case "POST": {
//                    {
//                        "id": 2,
//                        "name": "Epic1",
//                        "desc": "desc",
//                        "status": "IN_PROGRESS",
//                        "type": "EPIC",
//                        "startTime": "2000-01-01T00:00",
//                        "duration": 60
//                    }
                    if (!body.isEmpty()) {
                        JsonElement jsonElement = JsonParser.parseString(body);
                        if (!jsonElement.isJsonObject()) {
                            try (OutputStream os = exchange.getResponseBody()) {
                                exchange.sendResponseHeaders(400, 0);
                                os.write(gson.toJson("Нужно отправить эпик в теле запроса как Json объект").getBytes(StandardCharsets.UTF_8));
                                exchange.close();
                                return;
                            }
                        }

                        Epic epic = createEpicFromJson(body);
                        // add or update
                        if (httpTasksManager.isAllEpicsContainsThisKey(epic.getId())){
                            // update
                            try (OutputStream os = exchange.getResponseBody()) {
                                httpTasksManager.updateEpic(epic);
                                exchange.sendResponseHeaders(201, 0);
                                os.write(gson.toJson("Эпик обновлен").getBytes(StandardCharsets.UTF_8));
                                exchange.close();
                                return;
                            }

                        } else {
                            // add
                            try (OutputStream os = exchange.getResponseBody()) {
                                httpTasksManager.addEpic(epic);
                                exchange.sendResponseHeaders(201, 0);
                                os.write(gson.toJson("Эпик добавлен").getBytes(StandardCharsets.UTF_8));
                                exchange.close();
                                return;
                            }
                        }
                    } else {
                        try (OutputStream os = exchange.getResponseBody()) {
                            exchange.sendResponseHeaders(400, 0);
                            os.write(gson.toJson("Нужно отправить задачу в теле запроса как Json объект").getBytes(StandardCharsets.UTF_8));
                            exchange.close();
                            return;
                        }
                    }
                } // +++++++++++++++++
                case "DELETE": {
                    if (params == null) {
                        try (OutputStream os = exchange.getResponseBody()) {
                            httpTasksManager.removeAllEpics();
                            httpTasksManager.removeAllSubtasks();
                            exchange.sendResponseHeaders(200, 0);
                            os.write(gson.toJson("Все эпики и подзадачи удалены").getBytes(StandardCharsets.UTF_8));
                            exchange.close();
                            return;
                        }

                    } else {
                        int id = Integer.parseInt(params.substring(3));
                        try (OutputStream os = exchange.getResponseBody()) {
                            httpTasksManager.removeEpicById(id);
                            httpTasksManager.removeAllSubtasksOfEpic(id);
                            exchange.sendResponseHeaders(200, 0);
                            os.write(gson.toJson("Эпик с id " + id + " и его подзадачи удалены").getBytes(StandardCharsets.UTF_8));
                            exchange.close();
                            return;
                        }

                    }
                } // +++++++++++++++++++
            }
        }
    }



    private Task createTaskFromJson(String jsonTask) throws IOException {
        int id = 0;
        String name = "";
        String desc = "";
        String strStatus = "";
        String strType = "";
        String startTime = "";
        String duration = "";

        InputStream in = new ByteArrayInputStream(jsonTask.getBytes(Charset.forName("UTF-8")));
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

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

        reader.close();
        return new Task(id, name, desc, defineStatus(strStatus), LocalDateTime.parse(startTime), Long.parseLong(duration));

    }

    private Subtask createSubtaskFromJson(String jsonSubtask) throws IOException {

//        {
//            "id": 4,
//                "name": "subtask1",
//                "desc": "desc of subtask1",
//                "status": "NEW",
//                "type": "SUBTASK",
//                "startTime": "2000-01-01T00:00",
//                "duration": 20,
//                "epicId": 2
//        }

        int id = 0;
        String name = "";
        String desc = "";
        String strStatus = "";
        String strType = "";
        String startTime = "";
        String duration = "";
        String epicId = "";

        InputStream in = new ByteArrayInputStream(jsonSubtask.getBytes(Charset.forName("UTF-8")));
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

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
                    epicId = reader.nextString();
                }
                else {
                    reader.skipValue();
                }
            }
        }
        reader.close();


        return new Subtask(id, name, desc, defineStatus(strStatus), null, Integer.parseInt(epicId),
                LocalDateTime.parse(startTime), Long.parseLong(duration));
    }

    private Epic createEpicFromJson(String jsonEpic) throws IOException{
//        {
//            "id": 2,
//            "name": "Epic1 with tree subtasks",
//            "desc": "desc",
//            "status": "IN_PROGRESS",
//            "type": "EPIC",
//            "startTime": "2000-01-01T00:00",
//            "duration": 60
//        }

        int id = 0;
        String name = "";
        String desc = "";
        String strStatus = "";
        String strType = "";
        String startTime = "";
        String duration = "";

        InputStream in = new ByteArrayInputStream(jsonEpic.getBytes(Charset.forName("UTF-8")));
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

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

    private Status defineStatus(String statusStr) {
        if (statusStr.equals("NEW")) {
            return Status.NEW;
        } else if (statusStr.equals("IN_PROGRESS")) {
            return Status.IN_PROGRESS;
        } else {
            return Status.DONE;
        }
    }




    // адаптер /////////////////////////////////////////////////////////////////////
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
}


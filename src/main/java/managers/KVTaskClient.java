package managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {

    private String token;
    private HttpClient client;
    private URI uri;


    public KVTaskClient() throws IOException, InterruptedException {

        // HTTP-клиент с настройками по умолчанию
        client = HttpClient.newHttpClient();
        uri = URI.create("http://localhost:8078/");

        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(URI.create(uri + "register")) // указываем адрес ресурса
                .version(HttpClient.Version.HTTP_1_1) // указываем версию протокола
                //.header("Accept", "text/html") // указываем заголовок Accept
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // получаем стандартный обработчик тела запроса с конвертацией содержимого в строку
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // отправляем запрос и получаем ответ от сервера
        HttpResponse<String> response = client.send(request, handler);

        // выводим код состояния и тело ответа
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());

        this.token = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        // должен сохранять состояние менеджера задач через запрос POST /save/<ключ>?API_TOKEN=
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(uri + "save/"+key+"?API_TOKEN="+token))
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        client.send(request, handler);
    }

    public String load(String key) throws IOException, InterruptedException {
        // должен возвращать состояние менеджера задач через запрос GET /load/<ключ>?API_TOKEN=
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri + "load/"+key+"?API_TOKEN="+token))
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);

        return response.body();
    }

    public String getToken() {
        return token;
    }
}

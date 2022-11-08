import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.RequestLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static ObjectMapper mapper = new ObjectMapper();
    public static final String REMOTE_SERVICE_URI = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) throws IOException {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("My Test Service")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000) // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000) // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
        ) {

            // создание объекта запроса с произвольными заголовками
            HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
            RequestLine requestLine = request.getRequestLine();
            System.out.println(requestLine);

            // отправка запроса
            CloseableHttpResponse response = httpClient.execute(request);

            //метод преобразования в JSON в Java:
            List<Cats> catsList = mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {
            });

            // чтение тела ответа
            List<Cats> upvotesCats = catsList.stream()
                    .filter(cats -> cats.getUpvotes() != null && cats.getUpvotes() != 0)
                    .collect(Collectors.toList());
            upvotesCats.forEach(System.out::println);
            // чтение тела ответа
            //catsList.forEach(System.out::println);

        }
    }
}
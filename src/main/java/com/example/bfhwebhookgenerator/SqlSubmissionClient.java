package com.example.bfhwebhookgenerator;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class SqlSubmissionClient {
    private final WebClient webClient;

    public SqlSubmissionClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://bfhldevapigw.healthrx.co.in").build();
    }

    public String readSqlQuery() throws IOException {
        // Reads the SQL query from src/main/resources/final-query-q1.sql (works in jar and IDE)
        ClassPathResource resource = new ClassPathResource("final-query-q1.sql");
        try (var in = resource.getInputStream()) {
            return new String(in.readAllBytes());
        }
    }

    public String submitQuery(String sql, String accessToken) {
        // Submits the SQL query to /hiring/testWebhook/JAVA
        String response = webClient.post()
                .uri("/hiring/testWebhook/JAVA")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"finalQuery\":\"" + sql.replace("\"", "\\\"").replace("\n", " ") + "\"}")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;
    }

    public void saveResponseToFile(String response) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path outPath = Path.of("results", "response_" + timestamp + ".json");
        Files.writeString(outPath, response);
    }
}

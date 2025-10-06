package com.example.bfhwebhookgenerator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.io.IOException;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner run(WebhookClient webhookClient, SqlSubmissionClient sqlSubmissionClient) {
        return args -> {
            // Change name, regNo, email here if needed
            GenerateWebhookRequest request = new GenerateWebhookRequest("John Doe", "REG12347", "john@example.com");
            GenerateWebhookResponse response = webhookClient.generateWebhook(request);
            System.out.println("Webhook: " + response.getWebhook());
            System.out.println("Access Token: " + response.getAccessToken());

            try {
                String sql = sqlSubmissionClient.readSqlQuery();
                String testResponse = sqlSubmissionClient.submitQuery(sql, response.getAccessToken());
                System.out.println("\n--- Webhook Test Response ---\n" + testResponse);
                sqlSubmissionClient.saveResponseToFile(testResponse);
                System.out.println("\nResponse saved to results/response_<timestamp>.json");
            } catch (IOException e) {
                System.err.println("Error reading SQL or saving response: " + e.getMessage());
            }
            // Exit after printing
            System.exit(0);
        };
    }
}

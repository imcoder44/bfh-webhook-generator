package com.example.bfhwebhookgenerator;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

@Component
public class WebhookClient {
    private final WebClient webClient;

    public WebhookClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://bfhldevapigw.healthrx.co.in").build();
    }

    public GenerateWebhookResponse generateWebhook(GenerateWebhookRequest request) {
        return webClient.post()
                .uri("/hiring/generateWebhook/JAVA")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GenerateWebhookResponse.class)
                .block();
    }
}

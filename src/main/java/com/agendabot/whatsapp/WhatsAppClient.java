package com.agendabot.whatsapp;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Envia mensagens de texto pela WhatsApp Cloud API.
 * Em teste é mockado; o envio real depende de token válido (ver docs/06).
 */
@Component
public class WhatsAppClient {

    private static final Logger log = LoggerFactory.getLogger(WhatsAppClient.class);
    private final RestClient restClient;
    private final WhatsAppProperties props;

    public WhatsAppClient(RestClient.Builder builder, WhatsAppProperties props) {
        this.props = props;
        this.restClient = builder.baseUrl(props.apiBaseUrl()).build();
    }

    public void sendText(String to, String body) {
        restClient.post()
                .uri("/{phoneNumberId}/messages", props.phoneNumberId())
                .header("Authorization", "Bearer " + props.accessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "messaging_product", "whatsapp",
                        "to", to,
                        "type", "text",
                        "text", Map.of("body", body)))
                .retrieve()
                .toBodilessEntity();
        log.info("Mensagem enviada para {}", to);
    }
}

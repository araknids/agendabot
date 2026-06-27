package com.agendabot.whatsapp;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Extrai as mensagens de texto do payload (bem aninhado) do webhook do WhatsApp.
 * Ignora status updates (delivered/read) e não quebra com payload inválido.
 */
@Component
public class WhatsAppPayloadParser {

    private static final Logger log = LoggerFactory.getLogger(WhatsAppPayloadParser.class);
    private final ObjectMapper mapper = new ObjectMapper();

    public List<InboundMessage> parse(String rawJson) {
        List<InboundMessage> result = new ArrayList<>();
        if (rawJson == null || rawJson.isBlank()) {
            return result;
        }
        try {
            JsonNode root = mapper.readTree(rawJson);
            for (JsonNode entry : root.path("entry")) {
                for (JsonNode change : entry.path("changes")) {
                    JsonNode messages = change.path("value").path("messages");
                    for (JsonNode msg : messages) {
                        String from = msg.path("from").asText(null);
                        if (from == null) {
                            continue;
                        }
                        String type = msg.path("type").asText("unknown");
                        String text = msg.path("text").path("body").asText(null);
                        result.add(new InboundMessage(from, type, text));
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Payload do WhatsApp inválido, ignorando: {}", e.getMessage());
        }
        return result;
    }
}

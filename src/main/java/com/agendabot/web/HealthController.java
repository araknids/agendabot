package com.agendabot.web;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint simples de saúde — prova que o app subiu e responde.
 * É o objetivo do Milestone 1.1. Nada de WhatsApp ainda.
 */
@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}

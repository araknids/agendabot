package com.agendabot.whatsapp;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Credenciais/config da WhatsApp Cloud API. Bind do prefixo "whatsapp" no application.properties.
 * Os valores reais vêm de variável de ambiente — nunca commitar token (ver .gitignore).
 */
@ConfigurationProperties("whatsapp")
public record WhatsAppProperties(
        String verifyToken,
        String accessToken,
        String phoneNumberId,
        String apiBaseUrl) {
}

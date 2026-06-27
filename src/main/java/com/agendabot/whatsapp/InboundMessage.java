package com.agendabot.whatsapp;

/**
 * Uma mensagem recebida do cliente, já extraída do payload do WhatsApp.
 */
public record InboundMessage(String from, String type, String text) {

    public boolean isText() {
        return "text".equals(type) && text != null && !text.isBlank();
    }
}

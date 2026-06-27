package com.agendabot.bot;

import org.springframework.stereotype.Component;

import com.agendabot.config.ConfiguracaoClinica;
import com.agendabot.whatsapp.InboundMessage;
import com.agendabot.whatsapp.WhatsAppClient;

/**
 * Cola entre o WhatsApp e a conversa: recebe a mensagem, pede a resposta ao ConversaService
 * e envia de volta. Quando há agendamento, também avisa o dono (Milestone 4).
 */
@Component
public class MessageHandler {

    private final ConversaService conversa;
    private final WhatsAppClient whatsapp;
    private final ConfiguracaoClinica config;

    public MessageHandler(ConversaService conversa, WhatsAppClient whatsapp, ConfiguracaoClinica config) {
        this.conversa = conversa;
        this.whatsapp = whatsapp;
        this.config = config;
    }

    public void handle(InboundMessage msg) {
        if (!msg.isText()) {
            // Pegadinha docs/09 item 3: cliente manda áudio/foto. Não quebra, pede texto.
            whatsapp.sendText(msg.from(), "Por enquanto eu só entendo mensagens de texto 🙏 Pode me escrever?");
            return;
        }
        Resposta resposta = conversa.processar(msg.from(), msg.text());
        whatsapp.sendText(msg.from(), resposta.paraCliente());
        if (resposta.paraDono() != null) {
            whatsapp.sendText(config.getTelefoneDono(), resposta.paraDono());
        }
    }
}

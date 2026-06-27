package com.agendabot.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.agendabot.bot.MessageHandler;
import com.agendabot.whatsapp.InboundMessage;
import com.agendabot.whatsapp.WhatsAppPayloadParser;
import com.agendabot.whatsapp.WhatsAppProperties;

/**
 * Webhook do WhatsApp.
 *  - GET  /webhook : verificação do Meta (devolve o hub.challenge) — Milestone 1.2.
 *  - POST /webhook : recebe mensagens — Milestone 1.3+.
 */
@RestController
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);

    private final WhatsAppProperties props;
    private final WhatsAppPayloadParser parser;
    private final MessageHandler handler;

    public WebhookController(WhatsAppProperties props, WhatsAppPayloadParser parser, MessageHandler handler) {
        this.props = props;
        this.parser = parser;
        this.handler = handler;
    }

    @GetMapping("/webhook")
    public ResponseEntity<String> verificar(
            @RequestParam(name = "hub.mode", required = false) String mode,
            @RequestParam(name = "hub.verify_token", required = false) String token,
            @RequestParam(name = "hub.challenge", required = false) String challenge) {
        if ("subscribe".equals(mode) && props.verifyToken().equals(token)) {
            log.info("Webhook verificado pelo Meta.");
            return ResponseEntity.ok(challenge);
        }
        log.warn("Verificação de webhook recusada (token incorreto).");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("forbidden");
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> receber(@RequestBody(required = false) String body) {
        // Responde 200 sempre e rápido — senão o Meta reenvia (pegadinha docs/09 item 6).
        for (InboundMessage msg : parser.parse(body)) {
            log.info("Mensagem de {} (type={}): {}", msg.from(), msg.type(), msg.text());
            handler.handle(msg);
        }
        return ResponseEntity.ok("EVENT_RECEIVED");
    }
}

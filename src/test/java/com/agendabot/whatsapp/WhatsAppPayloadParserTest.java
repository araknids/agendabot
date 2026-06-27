package com.agendabot.whatsapp;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class WhatsAppPayloadParserTest {

    private final WhatsAppPayloadParser parser = new WhatsAppPayloadParser();

    @Test
    void extraiMensagemDeTexto() {
        String json = """
                {"object":"whatsapp_business_account","entry":[{"id":"1","changes":[{"value":{
                "messaging_product":"whatsapp","contacts":[{"wa_id":"5511"}],
                "messages":[{"from":"5511999998888","id":"wamid.A","timestamp":"1","type":"text",
                "text":{"body":"oi"}}]},"field":"messages"}]}]}
                """;
        List<InboundMessage> msgs = parser.parse(json);
        assertThat(msgs).hasSize(1);
        assertThat(msgs.get(0).from()).isEqualTo("5511999998888");
        assertThat(msgs.get(0).text()).isEqualTo("oi");
        assertThat(msgs.get(0).isText()).isTrue();
    }

    @Test
    void ignoraStatusUpdate() {
        String json = """
                {"object":"whatsapp_business_account","entry":[{"id":"1","changes":[{"value":{
                "messaging_product":"whatsapp","statuses":[{"id":"wamid.A","status":"delivered"}]},
                "field":"messages"}]}]}
                """;
        assertThat(parser.parse(json)).isEmpty();
    }

    @Test
    void naoQuebraComJsonInvalido() {
        assertThat(parser.parse("isso não é json")).isEmpty();
        assertThat(parser.parse(null)).isEmpty();
        assertThat(parser.parse("")).isEmpty();
    }

    @Test
    void marcaMensagemNaoTextoComoNaoTexto() {
        String json = """
                {"object":"whatsapp_business_account","entry":[{"id":"1","changes":[{"value":{
                "messages":[{"from":"5511","id":"x","type":"audio","audio":{"id":"a"}}]},
                "field":"messages"}]}]}
                """;
        List<InboundMessage> msgs = parser.parse(json);
        assertThat(msgs).hasSize(1);
        assertThat(msgs.get(0).type()).isEqualTo("audio");
        assertThat(msgs.get(0).isText()).isFalse();
    }
}

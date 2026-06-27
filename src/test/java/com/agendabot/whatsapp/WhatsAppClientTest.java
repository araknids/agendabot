package com.agendabot.whatsapp;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class WhatsAppClientTest {

    @Test
    void enviaTextoComBearerEPayloadCorretos() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        WhatsAppProperties props =
                new WhatsAppProperties("vt", "TOKEN123", "555", "https://graph.facebook.com/v21.0");
        WhatsAppClient client = new WhatsAppClient(builder, props);

        server.expect(once(), requestTo("https://graph.facebook.com/v21.0/555/messages"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Authorization", "Bearer TOKEN123"))
                .andExpect(jsonPath("$.to").value("5511"))
                .andExpect(jsonPath("$.text.body").value("oi"))
                .andRespond(withSuccess("{\"messages\":[{\"id\":\"x\"}]}", MediaType.APPLICATION_JSON));

        client.sendText("5511", "oi");

        server.verify();
    }
}

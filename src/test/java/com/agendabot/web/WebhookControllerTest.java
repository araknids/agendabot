package com.agendabot.web;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.agendabot.whatsapp.WhatsAppClient;

/** Teste de integração do webhook (sobe o contexto; WhatsAppClient mockado pra não chamar a Meta). */
@SpringBootTest
@AutoConfigureMockMvc
class WebhookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private WhatsAppClient whatsAppClient;

    @Test
    void verificacaoComTokenCorretoDevolveOChallenge() throws Exception {
        mvc.perform(get("/webhook")
                        .param("hub.mode", "subscribe")
                        .param("hub.verify_token", "dev-verify-token")
                        .param("hub.challenge", "12345"))
                .andExpect(status().isOk())
                .andExpect(content().string("12345"));
    }

    @Test
    void verificacaoComTokenErradoDevolve403() throws Exception {
        mvc.perform(get("/webhook")
                        .param("hub.mode", "subscribe")
                        .param("hub.verify_token", "errado")
                        .param("hub.challenge", "12345"))
                .andExpect(status().isForbidden());
    }

    @Test
    void mensagemRecebidaResponde200EAcionaEnvio() throws Exception {
        String payload = """
                {"object":"whatsapp_business_account","entry":[{"id":"1","changes":[{"value":{
                "messages":[{"from":"5511000000001","id":"x","type":"text","text":{"body":"oi"}}]},
                "field":"messages"}]}]}
                """;
        mvc.perform(post("/webhook").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isOk());

        verify(whatsAppClient).sendText(eq("5511000000001"), anyString());
    }
}

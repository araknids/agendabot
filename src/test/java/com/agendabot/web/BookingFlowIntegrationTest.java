package com.agendabot.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.agendabot.whatsapp.WhatsAppClient;

/**
 * Teste de integração ponta a ponta: simula a conversa inteira chegando pelo webhook e
 * verifica que o bot confirma o agendamento ao cliente e avisa o dono. Relógio fixado pra
 * o fluxo ser determinístico; WhatsAppClient mockado pra não chamar a Meta de verdade.
 */
@SpringBootTest
@AutoConfigureMockMvc
class BookingFlowIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private WhatsAppClient whatsAppClient;

    @TestConfiguration
    static class RelogioFixoConfig {
        @Bean
        @Primary
        Clock clockFixo() {
            // 2024-01-01 (segunda) 09:00 SP -> "amanhã" = terça (dia útil com horários).
            return Clock.fixed(Instant.parse("2024-01-01T12:00:00Z"), ZoneId.of("America/Sao_Paulo"));
        }
    }

    private void enviar(String from, String texto) throws Exception {
        String payload = """
                {"object":"whatsapp_business_account","entry":[{"id":"1","changes":[{"value":{
                "messages":[{"from":"%s","id":"wamid","timestamp":"1","type":"text",
                "text":{"body":"%s"}}]},"field":"messages"}]}]}
                """.formatted(from, texto);
        mvc.perform(post("/webhook").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isOk());
    }

    @Test
    void fluxoCompletoDeAgendamento() throws Exception {
        String cliente = "5511999998888";
        enviar(cliente, "oi quero agendar");
        enviar(cliente, "limpeza");
        enviar(cliente, "amanha");
        enviar(cliente, "10h");
        enviar(cliente, "sim");

        ArgumentCaptor<String> destino = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> corpo = ArgumentCaptor.forClass(String.class);
        verify(whatsAppClient, atLeastOnce()).sendText(destino.capture(), corpo.capture());

        // confirmou pro cliente
        assertThat(corpo.getAllValues()).anyMatch(b -> b.contains("Agendado"));
        // avisou o dono (telefone configurado em ConfiguracaoClinica)
        assertThat(destino.getAllValues()).contains("5511988887777");
    }
}

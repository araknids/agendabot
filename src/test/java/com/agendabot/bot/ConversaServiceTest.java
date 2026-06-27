package com.agendabot.bot;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.agendabot.agenda.AgendaEmMemoria;
import com.agendabot.config.ConfiguracaoClinica;
import com.agendabot.ia.StubAgenteIA;

class ConversaServiceTest {

    private final ConfiguracaoClinica config = new ConfiguracaoClinica();
    private final Clock clock = Clock.fixed(Instant.parse("2024-01-01T12:00:00Z"),
            ZoneId.of("America/Sao_Paulo"));
    private AgendaEmMemoria agenda;
    private ConversaService service;

    @BeforeEach
    void setup() {
        agenda = new AgendaEmMemoria(config);
        service = new ConversaService(new StubAgenteIA(config, clock), agenda,
                new ConversaRepositoryEmMemoria(), config);
    }

    @Test
    void agendaNoCaminhoFeliz() {
        String tel = "5511";
        service.processar(tel, "oi");                  // pergunta serviço
        service.processar(tel, "limpeza");             // pergunta dia
        service.processar(tel, "amanhã");              // mostra horários
        Resposta r4 = service.processar(tel, "10h");   // pede confirmação
        assertThat(r4.paraCliente()).contains("Confirma");

        Resposta r5 = service.processar(tel, "sim");   // marca
        assertThat(r5.paraCliente()).contains("Agendado");
        assertThat(r5.paraDono()).isNotNull();
        assertThat(agenda.agendamentos()).hasSize(1);
        assertThat(agenda.agendamentos().get(0).servico()).isEqualTo("limpeza");
    }

    @Test
    void recusarConfirmacaoVoltaParaEscolhaDeHorario() {
        String tel = "5512";
        service.processar(tel, "agendar");
        service.processar(tel, "limpeza");
        service.processar(tel, "amanhã");
        service.processar(tel, "10h");
        Resposta r = service.processar(tel, "não");
        assertThat(r.paraCliente()).containsIgnoringCase("horário");
        assertThat(agenda.agendamentos()).isEmpty();
    }

    @Test
    void diaSemHorarioPedeOutroDia() {
        String tel = "5513";
        service.processar(tel, "agendar");
        service.processar(tel, "limpeza");
        Resposta r = service.processar(tel, "sábado"); // fim de semana = sem slot
        assertThat(r.paraCliente()).containsIgnoringCase("outro dia");
        assertThat(agenda.agendamentos()).isEmpty();
    }

    @Test
    void servicoInvalidoPedeNovamente() {
        String tel = "5514";
        service.processar(tel, "oi");
        Resposta r = service.processar(tel, "quero um carro");
        assertThat(r.paraCliente()).containsIgnoringCase("não entendi o serviço");
    }
}

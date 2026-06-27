package com.agendabot.ia;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;

import com.agendabot.config.ConfiguracaoClinica;

class StubAgenteIATest {

    // 2024-01-01 é uma segunda-feira; 12:00Z = 09:00 em São Paulo.
    private final Clock clock = Clock.fixed(Instant.parse("2024-01-01T12:00:00Z"),
            ZoneId.of("America/Sao_Paulo"));
    private final StubAgenteIA ia = new StubAgenteIA(new ConfiguracaoClinica(), clock);

    @Test
    void detectaServico() {
        assertThat(ia.interpretar("quero uma limpeza").servico()).isEqualTo("limpeza");
    }

    @Test
    void detectaAmanha() {
        assertThat(ia.interpretar("pode ser amanhã").dia()).isEqualTo(LocalDate.of(2024, 1, 2));
    }

    @Test
    void detectaHora() {
        assertThat(ia.interpretar("às 10h").hora()).isEqualTo(LocalTime.of(10, 0));
        assertThat(ia.interpretar("pode 14:30").hora()).isEqualTo(LocalTime.of(14, 30));
    }

    @Test
    void detectaSimNao() {
        assertThat(ia.interpretar("sim, confirmo").simNao()).isTrue();
        assertThat(ia.interpretar("não quero").simNao()).isFalse();
        assertThat(ia.interpretar("talvez").simNao()).isNull();
    }
}

package com.agendabot.agenda;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.agendabot.config.ConfiguracaoClinica;

class AgendaEmMemoriaTest {

    private final AgendaEmMemoria agenda = new AgendaEmMemoria(new ConfiguracaoClinica());
    private final LocalDate terca = LocalDate.of(2024, 1, 2);
    private final LocalDate sabado = LocalDate.of(2024, 1, 6);

    @Test
    void geraSlotsDeHoraEmHoraNoDiaUtil() {
        // 09:00 até antes das 18:00, de 60 em 60 = 9 slots (09..17).
        assertThat(agenda.horariosLivres(terca)).hasSize(9);
        assertThat(agenda.horariosLivres(terca).get(0)).isEqualTo(terca.atTime(9, 0));
    }

    @Test
    void fimDeSemanaNaoTemSlot() {
        assertThat(agenda.horariosLivres(sabado)).isEmpty();
    }

    @Test
    void marcarTiraOHorarioDaLista() {
        agenda.marcar(terca.atTime(10, 0), "limpeza", "5511");
        assertThat(agenda.horariosLivres(terca))
                .hasSize(8)
                .doesNotContain(terca.atTime(10, 0));
        assertThat(agenda.agendamentos()).hasSize(1);
    }

    @Test
    void marcarHorarioJaOcupadoLancaExcecao() {
        LocalDateTime slot = terca.atTime(10, 0);
        agenda.marcar(slot, "limpeza", "5511");
        assertThatThrownBy(() -> agenda.marcar(slot, "avaliação", "5522"))
                .isInstanceOf(HorarioIndisponivelException.class);
    }
}

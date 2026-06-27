package com.agendabot.config;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Configuração da clínica — hardcoded no MVP (ver docs/05-modelo-dados).
 * Quando virar multi-clínica, isso vira dado por tenant.
 */
@Component
public class ConfiguracaoClinica {

    private final List<String> servicos = List.of("avaliação", "limpeza", "clareamento");
    private final LocalTime abertura = LocalTime.of(9, 0);
    private final LocalTime fechamento = LocalTime.of(18, 0);
    private final int duracaoMinutos = 60;
    private final String telefoneDono = "5511988887777";
    private final ZoneId zona = ZoneId.of("America/Sao_Paulo");

    public List<String> getServicos() {
        return servicos;
    }

    public LocalTime getAbertura() {
        return abertura;
    }

    public LocalTime getFechamento() {
        return fechamento;
    }

    public int getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public String getTelefoneDono() {
        return telefoneDono;
    }

    public ZoneId getZona() {
        return zona;
    }
}

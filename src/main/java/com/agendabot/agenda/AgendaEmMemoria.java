package com.agendabot.agenda;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.agendabot.config.ConfiguracaoClinica;

/**
 * Agenda em memória — implementação ativa do MVP. Calcula horários livres a partir do
 * horário de funcionamento menos o que já está ocupado (ver docs/05-modelo-dados).
 * Os dados somem ao reiniciar o app — ok pro aprendizado. O adaptador real é o Google Agenda.
 */
@Component
public class AgendaEmMemoria implements Agenda {

    private final ConfiguracaoClinica config;
    private final Map<LocalDateTime, Agendamento> ocupados = new ConcurrentHashMap<>();

    public AgendaEmMemoria(ConfiguracaoClinica config) {
        this.config = config;
    }

    @Override
    public List<LocalDateTime> horariosLivres(LocalDate dia) {
        DayOfWeek dow = dia.getDayOfWeek();
        if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
            return List.of();
        }
        List<LocalDateTime> livres = new ArrayList<>();
        LocalTime t = config.getAbertura();
        while (t.isBefore(config.getFechamento())) {
            LocalDateTime slot = dia.atTime(t);
            if (!ocupados.containsKey(slot)) {
                livres.add(slot);
            }
            t = t.plusMinutes(config.getDuracaoMinutos());
        }
        return livres;
    }

    @Override
    public void marcar(LocalDateTime inicio, String servico, String telefoneCliente) {
        Agendamento jaExistente = ocupados.putIfAbsent(
                inicio, new Agendamento(inicio, servico, telefoneCliente));
        if (jaExistente != null) {
            throw new HorarioIndisponivelException("Horário já ocupado: " + inicio);
        }
    }

    /** Exposto pra testes/inspeção. */
    public List<Agendamento> agendamentos() {
        return new ArrayList<>(ocupados.values());
    }
}

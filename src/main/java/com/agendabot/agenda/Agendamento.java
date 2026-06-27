package com.agendabot.agenda;

import java.time.LocalDateTime;

public record Agendamento(LocalDateTime inicio, String servico, String telefoneCliente) {
}

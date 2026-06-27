package com.agendabot.agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Porta da agenda: fonte da verdade dos horários. Quem marca é só o código, via esta porta.
 *
 * Implementação ativa hoje: {@link AgendaEmMemoria} (testável, sem credencial).
 * O adaptador real do Google Agenda (service account) é o próximo passo — ver
 * docs/08-setup-google-agenda. Trocar a implementação não muda o ConversaService.
 */
public interface Agenda {

    /** Horários livres do dia (vazio em fim de semana ou dia cheio). */
    List<LocalDateTime> horariosLivres(LocalDate dia);

    /** Marca o horário. Lança {@link HorarioIndisponivelException} se já estiver ocupado. */
    void marcar(LocalDateTime inicio, String servico, String telefoneCliente);
}

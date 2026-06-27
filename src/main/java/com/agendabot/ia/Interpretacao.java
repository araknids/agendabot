package com.agendabot.ia;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * O que a IA entendeu de uma mensagem. Campos null = não detectado.
 * O ConversaService usa só o campo relevante pro estado atual da conversa.
 */
public record Interpretacao(
        String servico,
        LocalDate dia,
        LocalTime hora,
        Boolean simNao) {
}

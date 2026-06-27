package com.agendabot.bot;

/** Estados da conversa de agendamento (ver docs/04-fluxo-conversa). */
public enum EstadoConversa {
    INICIO,
    ESCOLHENDO_SERVICO,
    ESCOLHENDO_DIA,
    ESCOLHENDO_HORARIO,
    CONFIRMANDO,
    FINALIZADO
}

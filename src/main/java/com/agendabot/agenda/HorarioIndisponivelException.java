package com.agendabot.agenda;

/** Lançada quando se tenta marcar um horário que já está ocupado (corrida entre clientes). */
public class HorarioIndisponivelException extends RuntimeException {

    public HorarioIndisponivelException(String mensagem) {
        super(mensagem);
    }
}

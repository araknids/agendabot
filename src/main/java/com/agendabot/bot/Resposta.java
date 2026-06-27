package com.agendabot.bot;

/**
 * Resultado de processar uma mensagem: o que responder pro cliente e, opcionalmente,
 * o aviso pro dono (preenchido só quando um agendamento é criado — Milestone 4).
 */
public record Resposta(String paraCliente, String paraDono) {

    public static Resposta cliente(String texto) {
        return new Resposta(texto, null);
    }
}

package com.agendabot.bot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/** Guarda o estado das conversas em memória (some ao reiniciar — ok pro MVP). */
@Component
public class ConversaRepositoryEmMemoria {

    private final Map<String, Conversa> conversas = new ConcurrentHashMap<>();

    public Conversa carregarOuCriar(String telefone) {
        return conversas.computeIfAbsent(telefone, Conversa::new);
    }

    public void salvar(Conversa conversa) {
        conversas.put(conversa.getTelefone(), conversa);
    }
}

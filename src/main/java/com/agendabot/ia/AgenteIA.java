package com.agendabot.ia;

/**
 * Porta da IA: recebe a mensagem do cliente e devolve o que entendeu (dado estruturado).
 * A IA NUNCA marca nada — só interpreta. Quem decide e marca é o ConversaService + Agenda
 * (princípio "a IA entende, o código marca", ver docs/03-arquitetura).
 *
 * Implementação ativa hoje: {@link StubAgenteIA} (determinística, sem custo).
 * O adaptador para um LLM real é o próximo passo (decisão de provedor pendente — ver
 * docs/02-decisoes, D10).
 */
public interface AgenteIA {

    Interpretacao interpretar(String mensagem);
}

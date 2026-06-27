package com.agendabot.bot;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.agendabot.agenda.Agenda;
import com.agendabot.agenda.HorarioIndisponivelException;
import com.agendabot.config.ConfiguracaoClinica;
import com.agendabot.ia.AgenteIA;
import com.agendabot.ia.Interpretacao;

/**
 * Máquina de estados da conversa (ver docs/04-fluxo-conversa).
 * Princípio: a IA interpreta, mas é AQUI (código) que se decide e se marca na agenda.
 */
@Service
public class ConversaService {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM 'às' HH:mm");

    private final AgenteIA ia;
    private final Agenda agenda;
    private final ConversaRepositoryEmMemoria repo;
    private final ConfiguracaoClinica config;

    public ConversaService(AgenteIA ia, Agenda agenda, ConversaRepositoryEmMemoria repo,
            ConfiguracaoClinica config) {
        this.ia = ia;
        this.agenda = agenda;
        this.repo = repo;
        this.config = config;
    }

    public Resposta processar(String telefone, String texto) {
        Conversa c = repo.carregarOuCriar(telefone);
        Interpretacao i = ia.interpretar(texto);
        Resposta resposta = avancar(c, i, telefone);
        repo.salvar(c);
        return resposta;
    }

    private Resposta avancar(Conversa c, Interpretacao i, String telefone) {
        String servicos = String.join(", ", config.getServicos());

        switch (c.getEstado()) {
            case INICIO, FINALIZADO -> {
                c.reiniciar();
                c.setEstado(EstadoConversa.ESCOLHENDO_SERVICO);
                return Resposta.cliente(
                        "Oi! 😊 Posso te ajudar a agendar. Qual serviço você quer? (" + servicos + ")");
            }
            case ESCOLHENDO_SERVICO -> {
                String servico = servicoValido(i.servico());
                if (servico == null) {
                    return Resposta.cliente("Não entendi o serviço. Temos: " + servicos + ". Qual você quer?");
                }
                c.setServico(servico);
                c.setEstado(EstadoConversa.ESCOLHENDO_DIA);
                return Resposta.cliente("Boa! " + servico + ". Pra qual dia? (ex.: amanhã, sexta, 30/06)");
            }
            case ESCOLHENDO_DIA -> {
                if (i.dia() == null) {
                    return Resposta.cliente("Não peguei o dia. Me diz uma data (ex.: amanhã, sexta, 30/06).");
                }
                List<LocalDateTime> livres = agenda.horariosLivres(i.dia());
                if (livres.isEmpty()) {
                    return Resposta.cliente("Nesse dia não tenho horário (ou não atendo). Quer tentar outro dia?");
                }
                c.setDia(i.dia());
                c.setHorariosOferecidos(livres);
                c.setEstado(EstadoConversa.ESCOLHENDO_HORARIO);
                return Resposta.cliente("Pro dia " + i.dia().format(DATA) + " tenho: "
                        + formatarHorarios(livres) + ". Qual prefere?");
            }
            case ESCOLHENDO_HORARIO -> {
                LocalDateTime escolhido = casarHorario(i.hora(), c.getHorariosOferecidos());
                if (escolhido == null) {
                    return Resposta.cliente("Esse horário não está na lista. Opções: "
                            + formatarHorarios(c.getHorariosOferecidos()) + ".");
                }
                c.setHorarioEscolhido(escolhido);
                c.setEstado(EstadoConversa.CONFIRMANDO);
                return Resposta.cliente("Confirma " + c.getServico() + " "
                        + escolhido.format(DATA_HORA) + "? (sim/não)");
            }
            case CONFIRMANDO -> {
                if (i.simNao() == null) {
                    return Resposta.cliente("Pode confirmar? Responda sim ou não.");
                }
                if (Boolean.FALSE.equals(i.simNao())) {
                    c.setEstado(EstadoConversa.ESCOLHENDO_HORARIO);
                    return Resposta.cliente("Sem problema! Qual horário prefere então? "
                            + formatarHorarios(c.getHorariosOferecidos()));
                }
                return marcar(c, telefone);
            }
            default -> {
                c.reiniciar();
                c.setEstado(EstadoConversa.ESCOLHENDO_SERVICO);
                return Resposta.cliente("Vamos recomeçar. Qual serviço você quer? (" + servicos + ")");
            }
        }
    }

    private Resposta marcar(Conversa c, String telefone) {
        try {
            agenda.marcar(c.getHorarioEscolhido(), c.getServico(), telefone);
        } catch (HorarioIndisponivelException e) {
            c.setEstado(EstadoConversa.ESCOLHENDO_DIA);
            return Resposta.cliente("Ops, esse horário acabou de ser ocupado 😕 Quer escolher outro dia?");
        }
        c.setEstado(EstadoConversa.FINALIZADO);
        String quando = c.getHorarioEscolhido().format(DATA_HORA);
        String paraCliente = "✅ Agendado! " + c.getServico() + " " + quando + ". Até lá!";
        String paraDono = "Novo agendamento: " + c.getServico() + " " + quando + " (cliente " + telefone + ")";
        return new Resposta(paraCliente, paraDono);
    }

    private String servicoValido(String servico) {
        return servico != null && config.getServicos().contains(servico) ? servico : null;
    }

    private LocalDateTime casarHorario(LocalTime hora, List<LocalDateTime> oferecidos) {
        if (hora == null) {
            return null;
        }
        return oferecidos.stream()
                .filter(dt -> dt.toLocalTime().getHour() == hora.getHour())
                .findFirst()
                .orElse(null);
    }

    private String formatarHorarios(List<LocalDateTime> horarios) {
        return horarios.stream()
                .map(dt -> dt.toLocalTime().format(HORA))
                .collect(Collectors.joining(", "));
    }
}

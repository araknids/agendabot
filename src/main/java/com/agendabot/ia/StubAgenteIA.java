package com.agendabot.ia;

import java.text.Normalizer;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.agendabot.config.ConfiguracaoClinica;

/**
 * Stand-in determinístico do LLM: extrai serviço, dia, hora e sim/não por regra simples.
 * NÃO é inteligência de verdade — é só pra o fluxo rodar e ser testável sem custo/chave.
 * Quando a decisão de provedor for tomada (docs/02, D10), troca-se esta classe por um
 * adaptador real implementando {@link AgenteIA}, sem mexer no resto do bot.
 */
@Component
public class StubAgenteIA implements AgenteIA {

    private static final Pattern HORA = Pattern.compile("(\\d{1,2})\\s*(?::|h)\\s*(\\d{2})?");
    private static final Pattern DATA = Pattern.compile("(\\d{1,2})/(\\d{1,2})");
    private static final List<String> DIAS_SEMANA =
            List.of("segunda", "terca", "quarta", "quinta", "sexta", "sabado", "domingo");

    private final ConfiguracaoClinica config;
    private final Clock clock;

    public StubAgenteIA(ConfiguracaoClinica config, Clock clock) {
        this.config = config;
        this.clock = clock;
    }

    @Override
    public Interpretacao interpretar(String mensagem) {
        String t = normalizar(mensagem);
        return new Interpretacao(detectarServico(t), detectarDia(t), detectarHora(t), detectarSimNao(t));
    }

    private String detectarServico(String t) {
        for (String s : config.getServicos()) {
            if (t.contains(normalizar(s))) {
                return s;
            }
        }
        return null;
    }

    private LocalDate detectarDia(String t) {
        LocalDate hoje = LocalDate.now(clock);
        if (t.contains("hoje")) {
            return hoje;
        }
        if (t.contains("amanha")) {
            return hoje.plusDays(1);
        }
        for (int i = 0; i < DIAS_SEMANA.size(); i++) {
            if (t.contains(DIAS_SEMANA.get(i))) {
                return proximoDiaDaSemana(hoje, DayOfWeek.of(i + 1));
            }
        }
        Matcher m = DATA.matcher(t);
        if (m.find()) {
            try {
                LocalDate d = LocalDate.of(hoje.getYear(),
                        Integer.parseInt(m.group(2)), Integer.parseInt(m.group(1)));
                return d.isBefore(hoje) ? d.plusYears(1) : d;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private LocalDate proximoDiaDaSemana(LocalDate hoje, DayOfWeek alvo) {
        LocalDate d = hoje.plusDays(1);
        while (d.getDayOfWeek() != alvo) {
            d = d.plusDays(1);
        }
        return d;
    }

    private LocalTime detectarHora(String t) {
        Matcher m = HORA.matcher(t);
        if (m.find()) {
            int h = Integer.parseInt(m.group(1));
            int min = m.group(2) != null ? Integer.parseInt(m.group(2)) : 0;
            if (h >= 0 && h < 24 && min >= 0 && min < 60) {
                return LocalTime.of(h, min);
            }
        }
        return null;
    }

    private Boolean detectarSimNao(String t) {
        if (t.contains("nao")) {
            return false;
        }
        if (t.contains("sim") || t.contains("confirmo") || t.contains("pode")
                || t.contains("isso") || t.contains("ok") || t.contains("claro")) {
            return true;
        }
        return null;
    }

    private static String normalizar(String s) {
        if (s == null) {
            return "";
        }
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }
}

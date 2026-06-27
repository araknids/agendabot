package com.agendabot;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AgendabotApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgendabotApplication.class, args);
    }

    /**
     * Relógio único da aplicação, fixado no fuso de São Paulo.
     * Injetar Clock (em vez de chamar LocalDate.now()) deixa as datas testáveis
     * e evita o bug de fuso (ver docs/09-pegadinhas, item 2).
     */
    @Bean
    Clock clock() {
        return Clock.system(ZoneId.of("America/Sao_Paulo"));
    }
}

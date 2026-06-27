package com.agendabot.bot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Estado de uma conversa, chaveada pelo telefone do cliente (ver docs/05-modelo-dados). */
public class Conversa {

    private final String telefone;
    private EstadoConversa estado = EstadoConversa.INICIO;
    private String servico;
    private LocalDate dia;
    private List<LocalDateTime> horariosOferecidos = new ArrayList<>();
    private LocalDateTime horarioEscolhido;

    public Conversa(String telefone) {
        this.telefone = telefone;
    }

    /** Zera os dados pra começar um novo atendimento. */
    public void reiniciar() {
        estado = EstadoConversa.INICIO;
        servico = null;
        dia = null;
        horariosOferecidos = new ArrayList<>();
        horarioEscolhido = null;
    }

    public String getTelefone() {
        return telefone;
    }

    public EstadoConversa getEstado() {
        return estado;
    }

    public void setEstado(EstadoConversa estado) {
        this.estado = estado;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public List<LocalDateTime> getHorariosOferecidos() {
        return horariosOferecidos;
    }

    public void setHorariosOferecidos(List<LocalDateTime> horariosOferecidos) {
        this.horariosOferecidos = horariosOferecidos;
    }

    public LocalDateTime getHorarioEscolhido() {
        return horarioEscolhido;
    }

    public void setHorarioEscolhido(LocalDateTime horarioEscolhido) {
        this.horarioEscolhido = horarioEscolhido;
    }
}

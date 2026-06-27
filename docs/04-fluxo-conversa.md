# 04 – Fluxo da conversa

> ⚠️ **Rascunho a lápis.** Os estados abaixo você vai mexer no primeiro dia de código.
> Esboço pra pensar, não verdade gravada em pedra.

A conversa é uma pequena máquina de estados. Cada cliente (número de telefone) tem um
estado. A cada mensagem, a IA ajuda a entender, e o código decide o próximo estado.

## Os estados

```
        ┌─────────────────────────────────────────────────────┐
        │                                                     │
        ▼                                                     │
   [INICIO] ──"quero marcar"──► [ESCOLHER_SERVICO] ──► [ESCOLHER_DIA]
        │                                                     │
        │ não entendeu / outro assunto                        ▼
        ▼                                            [MOSTRAR_HORARIOS]
   [ESCALAR_HUMANO]                                           │
   (avisa o dono)                                  cliente escolhe slot
                                                              ▼
                                                        [CONFIRMAR]
                                                              │
                                            código cria evento na agenda
                                                              ▼
                                                       [FINALIZADO]
                                                       (avisa o dono)
```

## O que acontece em cada estado

| Estado | O bot faz | Próximo |
|--------|-----------|---------|
| **INICIO** | Cumprimenta, descobre se quer agendar. | `ESCOLHER_SERVICO` ou `ESCALAR_HUMANO` |
| **ESCOLHER_SERVICO** | Mostra/confirma o serviço (lista fixa: ex. avaliação, limpeza). | `ESCOLHER_DIA` |
| **ESCOLHER_DIA** | Pergunta a preferência de dia. | `MOSTRAR_HORARIOS` |
| **MOSTRAR_HORARIOS** | **Código** consulta a agenda e lista os horários livres reais. | `CONFIRMAR` |
| **CONFIRMAR** | Cliente escolhe um horário; bot pede confirmação. | `FINALIZADO` |
| **FINALIZADO** | **Código** cria o evento, confirma ao cliente, avisa o dono. | — |
| **ESCALAR_HUMANO** | Diz que vai chamar um atendente e avisa o dono no WhatsApp. | — |

## O contrato com a IA (o que ela devolve)

A cada mensagem, a `AgenteIA` recebe (histórico + estado atual) e devolve **dado
estruturado**, não texto solto. Exemplo de formato:

```json
{
  "intencao": "agendar",
  "servico": "limpeza",
  "dia_preferido": "2026-06-30",
  "horario_escolhido": null,
  "precisa_humano": false,
  "resposta_sugerida": "Boa! Limpeza na terça. Quer de manhã ou à tarde?"
}
```

O `ConversaService` usa esses campos pra decidir o estado. A `resposta_sugerida` é só um
rascunho — quando envolve horário, **o código confere na agenda antes de mandar**.

## Exemplo de diálogo

```
Cliente: oi, queria marcar uma limpeza
Bot:     Oi! Claro 😊 Pra quando você prefere?
Cliente: pode ser terça de manhã
Bot:     Na terça (30/06) tenho 09:00, 10:00 e 11:00. Qual fica melhor?
Cliente: 10h
Bot:     Fechado! Limpeza terça 30/06 às 10:00. Confirmo? (sim/não)
Cliente: sim
Bot:     ✅ Agendado! Te espero terça às 10h.
         (e o dono recebe no WhatsApp: "Novo agendamento: limpeza, ter 30/06 10:00")
```

## Regra de ouro do fluxo

Sempre que a resposta envolver **horário disponível** ou **confirmação de marcação**, o
código consulta o Google Agenda **antes** de falar com o cliente. A IA nunca é a fonte da
disponibilidade. Ver [01 – Princípios](01-escopo.md#princípios-inegociáveis).

## ⚠️ Restrição: a janela de 24h do WhatsApp

Responder o cliente que **acabou de escrever** é sempre permitido. Mas mandar mensagem
**proativa** (o aviso pro dono no `FINALIZADO`/`ESCALAR_HUMANO`, ou um lembrete futuro) só
funciona dentro de **24h** desde a última mensagem daquele contato — fora disso, **só com
template aprovado pela Meta**. Isso afeta direto o aviso pro dono. Detalhes e como
contornar em [09 – Pegadinhas, item 1](09-pegadinhas.md#1--a-janela-de-24h-do-whatsapp-a-que-mais-dói).

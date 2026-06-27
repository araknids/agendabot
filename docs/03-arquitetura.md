# 03 – Arquitetura

> ⚠️ **Rascunho a lápis.** Este doc descreve código que ainda não existe. Ele **vai mudar**
> quando você começar a codar — trate como esboço pra pensar, não como verdade. Não polisse.

## Visão de cima

```
   Cliente                                                    Dono
 (WhatsApp)                                                (WhatsApp)
     │                                                          ▲
     │ 1. manda mensagem                          5. aviso de   │
     ▼                                               agendamento│
┌──────────────────────────────────────────────────────────────┐
│                    WhatsApp Cloud API (Meta)                   │
└──────────────────────────────────────────────────────────────┘
     │ 2. webhook (POST)                          4. envia resposta
     ▼                                                          ▲
┌──────────────────────────────────────────────────────────────┐
│                  SEU BACKEND — Spring Boot                     │
│                                                                │
│   WebhookController ──► ConversaService ──► AgenteIA (interface)│
│                              │                                 │
│                              │ "a IA entende, o código marca"  │
│                              ▼                                 │
│                         AgendaService ──► Google Agenda  ◄─────┼── fonte da verdade
│                                                                │
└──────────────────────────────────────────────────────────────┘
                 3. valida disponibilidade + cria evento
```

## As peças

| Componente | Responsabilidade |
|------------|------------------|
| **WebhookController** | Recebe mensagens do WhatsApp (`POST /webhook`) e responde a verificação (`GET /webhook`). |
| **WhatsAppClient** | Manda mensagem de volta pela API da Meta. |
| **ConversaService** | O cérebro do fluxo: carrega o estado da conversa, chama a IA, decide o próximo passo. |
| **AgenteIA** (interface) | Recebe a mensagem + estado e devolve **o que entendeu** (intenção + campos). Não cria nada. |
| **AgendaService** | Consulta horários livres e cria eventos no Google Agenda. **Único que pode marcar.** |
| **EstadoConversa** (store) | Guarda em que ponto cada cliente está (por número de telefone). |

## O encanamento genérico vs. a ação específica

A grande ideia de reaproveitamento:

- **Genérico (reusável em qualquer nicho):** WhatsApp ↔ Backend ↔ IA que entende intenção.
  Isso é o "cano".
- **Específico (muda por nicho):** a **ação** na ponta. Aqui é `AgendaService` (marcar
  horário). Numa mecânica seria o mesmo cano com outra ação.

Por isso a ação fica isolada num service próprio. Trocar de nicho = trocar a ação, não o
cano.

## O princípio que não se quebra

> **A IA entende. O código marca.**

Fluxo de uma marcação:
1. `AgenteIA` lê a conversa e devolve: *"o cliente quer limpeza, na terça de manhã"*.
2. `AgendaService` pergunta ao Google Agenda: *terça de manhã tem vaga?*
3. Só o **código** decide e cria o evento. A IA **nunca** chama o Google Agenda direto.

Se a IA alucinar um horário, o código barra na etapa 2. É a rede de segurança.

## O que está fora (no aprendizado)

Banco de dados gerenciado, autenticação (Keycloak), multi-tenant, fila de mensagens, infra
em nuvem. Tudo isso é [decisão futura](02-decisoes.md#d7--infra-de-verdade-só-quando-virar-produto-gcp-são-paulo).
No aprendizado: roda local, exposto por **ngrok**.

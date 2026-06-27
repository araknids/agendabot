# Bot de Agendamento via WhatsApp

Bot que atende clientes no WhatsApp, entende o pedido por IA e marca horários numa agenda.
Piloto: clínica odontológica. A arquitetura é pensada pra ser reaproveitável em outros
nichos que **agendam** (mecânica, salão, barbearia, etc.).

- **Status:** 📐 Documentação / pré-código
- **Tipo:** Projeto de aprendizado (solo)
- **Stack:** Java 21 · Spring Boot 3 · Maven

## A ideia em uma frase

> Cliente manda mensagem → bot entende → mostra horários livres → marca → confirma.
> O dono vê tudo no **Google Agenda** + recebe um aviso no WhatsApp.

## Documentação

| Doc | Pra quê |
|-----|---------|
| [01 – Escopo e fronteiras](docs/01-escopo.md) | O que o projeto faz e o que **NÃO** faz |
| [02 – Decisões e porquês](docs/02-decisoes.md) | Por que cada escolha foi feita (pra não re-discutir) |
| [03 – Arquitetura](docs/03-arquitetura.md) | Como as peças se conectam |
| [04 – Fluxo da conversa](docs/04-fluxo-conversa.md) | Os estados da conversa, com exemplos |
| [05 – Modelo de dados](docs/05-modelo-dados.md) | As poucas entidades do MVP |
| [06 – Setup do WhatsApp](docs/06-setup-whatsapp.md) | Passo a passo Meta + ngrok (guia 1ª vez) |
| [07 – Milestones](docs/07-milestones.md) | A ordem de construção, com critério de pronto |
| [08 – Setup do Google Agenda](docs/08-setup-google-agenda.md) | Passo a passo da agenda (service account) |
| [09 – Pegadinhas conhecidas](docs/09-pegadinhas.md) | Os erros de 1ª vez que vão te morder |

## Como rodar

Pré-requisitos: **Java 21** e **Maven**.

```bash
mvn test            # roda os testes (unitários + integração)
mvn spring-boot:run # sobe a app na porta 8080
```

- Health: `GET http://localhost:8080/health` → `{"status":"UP"}`
- Webhook (verificação do Meta): `GET /webhook?hub.mode=subscribe&hub.verify_token=...&hub.challenge=...`
- Webhook (mensagens): `POST /webhook` com o payload do WhatsApp.

Credenciais reais via variável de ambiente (nunca no código):
`WHATSAPP_VERIFY_TOKEN`, `WHATSAPP_ACCESS_TOKEN`, `WHATSAPP_PHONE_NUMBER_ID`.
Pra testar de verdade no WhatsApp, siga o [docs/06](docs/06-setup-whatsapp.md) (Meta + ngrok).

## Estado atual (o que é real × stub)

| Peça | Hoje | Pra produção |
|------|------|--------------|
| Webhook + envio WhatsApp | ✅ real (Cloud API) | — |
| Máquina de estados da conversa | ✅ real, testada | — |
| Entendimento (IA) | 🟡 `StubAgenteIA` (regra determinística) | adaptador LLM (decisão D10) |
| Agenda | 🟡 `AgendaEmMemoria` | adaptador Google Agenda ([docs/08](docs/08-setup-google-agenda.md)) |

As duas peças 🟡 ficam atrás de interface (`AgenteIA`, `Agenda`) — trocar pela versão real
não mexe no resto do bot.

## Princípio que rege tudo

> **A IA conversa. O código marca.**
> A IA nunca cria um horário sozinha — ela só entende e propõe. Quem valida contra a
> agenda e cria o evento é o código. Isso evita marcar horário que não existe.

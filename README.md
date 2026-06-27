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

⏳ Em breve — será preenchido no **Milestone 1** (echo bot funcionando).

## Princípio que rege tudo

> **A IA conversa. O código marca.**
> A IA nunca cria um horário sozinha — ela só entende e propõe. Quem valida contra a
> agenda e cria o evento é o código. Isso evita marcar horário que não existe.

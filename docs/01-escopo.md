# 01 – Escopo e fronteiras

## Objetivo

Aprender a construir um bot de atendimento no WhatsApp, ponta a ponta, usando um caso
concreto e com **fim bem definido**: agendar um horário.

Não é um projeto pra "vender pra qualquer negócio" agora. É um projeto pra **aprender o
encanamento** (WhatsApp + IA + ação) de forma que dê pra reaproveitar depois. Por isso as
escolhas priorizam **simplicidade de aprendizado**, não abrangência de mercado.

## O que o bot FAZ (o fio mais fino)

1. Recebe a mensagem do cliente no WhatsApp.
2. Entende a intenção (quer agendar? qual serviço? qual dia?).
3. Mostra os horários livres.
4. Marca o horário escolhido na agenda.
5. Confirma pro cliente e avisa o dono.

## O que o bot NÃO FAZ (fronteiras duras — não cruzar na v1)

- ❌ **Pagamento** (nada de cobrança, Pix, cartão).
- ❌ **Dashboard / painel web** — o "painel" é o Google Agenda.
- ❌ **Multi-tenant** (várias clínicas) — uma agenda só, hardcoded.
- ❌ **Cadastro de serviços por tela** — lista fixa no código/config.
- ❌ **Histórico bonito, relatórios, métricas.**
- ❌ **Reagendamento e cancelamento automáticos** (v2; na v1, manda pro dono).

> Regra: bateu vontade de fazer algo dessa lista "só pra já deixar pronto" → **PARE**.
> É exatamente assim que projeto de aprendizado vira um ano de trabalho sem fim.

## Princípios inegociáveis

1. **A IA conversa, o código marca.** A IA nunca cria evento direto. Ela devolve o que
   entendeu; o código valida contra a agenda e cria o evento. Sem isso, o bot marca
   horário que não existe.
2. **A agenda é a fonte da verdade.** Disponibilidade vem sempre do Google Agenda, nunca
   da "memória" da IA.
3. **Quando não entender, escala pro humano.** Em dúvida, o bot passa a conversa pro dono
   em vez de inventar resposta.

## Quando isso deixar de ser aprendizado

Se um dia virar produto de verdade (alguém pagando), aí sim entram: LGPD formal com
advogado, multi-tenant, dashboard, cobrança e infra na GCP São Paulo. Ver
[02 – Decisões](02-decisoes.md). **Nada disso é agora.**

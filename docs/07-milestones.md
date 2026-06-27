# 07 – Milestones

A ordem importa: **ataca primeiro o risco que você NÃO controla** (fazer o WhatsApp falar
com seu código) e deixa por último o que você já domina (lógica em Java).

> Não comece um milestone antes do anterior estar ✅. Especialmente: **não toque na IA antes
> do echo bot funcionar.** 90% dos iniciantes travam no webhook e culpam a IA à toa.

---

## Milestone 1 — Echo bot 🔌

**Meta:** você manda "oi" no WhatsApp e o bot responde "oi" de volta.

**Constrói:**
- `GET /webhook` — responde a verificação do Meta (devolve o `hub.challenge`).
- `POST /webhook` — recebe a mensagem.
- `WhatsAppClient` — envia a resposta pela API da Meta.

**Critério de pronto:**
- [ ] Webhook verificado no painel do Meta (✅ verde).
- [ ] Mensagem enviada do seu zap aparece nos logs do app.
- [ ] O bot responde de volta no seu WhatsApp.

**Por que primeiro:** é a parte que você nunca fez. Provando isso, o resto é Java.

---

## Milestone 2 — Bot entende 🧠

**Meta:** o bot mantém o fio da conversa e entende o que o cliente quer (sem marcar nada
ainda).

**Constrói:**
- `EstadoConversa` + store (um `Map` em memória já serve).
- Interface `AgenteIA` + **uma implementação concreta** (aqui você decide o provedor/modelo
  de IA — ver [D10](02-decisoes.md#d10--provedor-de-ia-decidir-no-milestone-2-atrás-de-uma-interface)).
- `ConversaService` que avança os estados do [fluxo](04-fluxo-conversa.md).

**Critério de pronto:**
- [ ] O bot pergunta serviço e dia e "lembra" das respostas dentro da conversa.
- [ ] A IA devolve dado estruturado (intenção/serviço/dia), não texto solto.
- [ ] Ainda **não** marca nada — só conversa.

---

## Milestone 3 — Bot marca 📅

**Meta:** o bot lê horários livres reais do Google Agenda e cria o evento.

**Constrói:**
- `AgendaService` integrado ao Google Calendar (consultar livres + criar evento).
- Cálculo de horários livres (funcionamento − eventos existentes; ver
  [05 – Modelo](05-modelo-dados.md)).

**Critério de pronto:**
- [ ] Os horários oferecidos batem com a agenda real (sem inventar).
- [ ] Escolher um horário cria o evento no Google Agenda.
- [ ] Tentar marcar um horário ocupado é barrado pelo código.

---

## Milestone 4 — Dono vê 👀

**Meta:** o dono fica sabendo dos agendamentos sem abrir nada.

**Constrói:**
- Ping no WhatsApp do dono a cada marcação (reusa o `WhatsAppClient`).

> ⚠️ **Cuidado com a janela de 24h.** O aviso pro dono é mensagem **proativa**. Ele só sai
> sem template se o dono tiver falado com o bot nas últimas 24h, e o número do dono precisa
> estar na lista de teste. Ver [09 – Pegadinhas](09-pegadinhas.md#1--a-janela-de-24h-do-whatsapp-a-que-mais-dói).

**Critério de pronto:**
- [ ] A cada agendamento, o dono recebe um aviso no WhatsApp.
- [ ] O agendamento aparece no Google Agenda dele.

---

## Depois do M4 (só se quiser continuar)

Reagendar/cancelar pela conversa, lembrete automático no dia anterior, lista fixa de
serviços melhorada. **Tudo opcional** — o projeto de aprendizado já está "completo" no M4.

E se um dia virar produto pago: aí sim os assuntos de
[infra, LGPD e multi-tenant](02-decisoes.md#d7--infra-de-verdade-só-quando-virar-produto-gcp-são-paulo).

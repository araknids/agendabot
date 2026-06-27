# 09 – Pegadinhas conhecidas

Leia antes de culpar seu código. São os erros de 1ª vez que mais comem tempo. Cada um já
derrubou muita gente — agora você sabe de antemão.

---

## 1. ⏰ A janela de 24h do WhatsApp (a que mais dói)

No WhatsApp você só pode mandar mensagem **livre** (texto que você escreve na hora) dentro
de **24h** desde a **última mensagem do contato**. Passou de 24h, só dá pra enviar
**templates pré-aprovados pela Meta**.

**Onde isso te morde:**
- **Aviso pro dono** ([Milestone 4](07-milestones.md)): se o dono não falou com o bot nas
  últimas 24h, o ping livre **não sai**. Precisa de template.
- **Lembrete de consulta** ("você tem consulta amanhã"): é proativo → quase sempre fora da
  janela → **só com template**.

**O que fazer no aprendizado:** pro teste, garanta que o dono mandou um "oi" pro bot antes
(abre a janela). Pra valer, registre um template e use ele fora das 24h.

> Responder o cliente que acabou de escrever = sempre dentro da janela, sem template. O
> problema é só mensagem **proativa**.

## 2. 🌎 Fuso horário

Agenda + Brasil = bug clássico. Bibliotecas costumam assumir **UTC** por padrão; você marca
"10:00" e salva como 10:00 UTC → aparece **07:00** (ou 13:00) pro cliente.

- Defina **explicitamente** `America/Sao_Paulo` ao criar eventos e ao interpretar horários.
- O Brasil **não tem mais horário de verão** (desde 2019), então hoje é UTC−3 fixo — mas
  não confie no default da lib, **seja explícito sempre**.

## 3. 🎙️ Cliente manda áudio/foto/figurinha, não texto

No Brasil, metade manda **áudio**. O [fluxo](04-fluxo-conversa.md) assume texto. Se chegar
áudio/imagem/sticker, o bot tem que responder com elegância:

> "Por enquanto eu só entendo mensagens de texto 🙏 Pode me escrever o que você precisa?"

Não transcrever áudio agora (é escopo de v2) — só **não quebrar** quando vier.

## 4. 🔑 Token de teste expira em ~24h

O token que o Meta te dá na tela de setup é **temporário**. Você vai abrir o app no dia
seguinte e tudo "para de funcionar" sem motivo aparente. Solução: gerar um **token
permanente via System User** (no Business Manager). Faça isso assim que o echo bot
funcionar, pra não perder tempo todo dia.

## 5. 📋 Número de teste só fala com a lista de permitidos

O número de teste grátis **só envia** pra números cadastrados na lista de destinatários de
teste. No [Milestone 4](07-milestones.md), **o número do dono também precisa entrar nessa
lista** — senão o aviso pra ele não sai.

## 6. ↩️ Webhook tem que responder 200 rápido (senão duplica)

Se seu `POST /webhook` demorar ou não responder **200**, a Meta acha que falhou e
**reenvia** a mesma mensagem. Resultado: o cliente recebe resposta **duplicada**.

- Responda **200 na hora**; faça o trabalho pesado depois (ou rápido o suficiente).
- Seja **idempotente**: guarde o `id` da mensagem já processada e ignore repetições.

## 7. 🔁 A URL do ngrok muda a cada reinício

No plano grátis, toda vez que você reinicia o ngrok a URL muda — e o webhook no Meta aponta
pra URL velha. Sintoma: "funcionava ontem, hoje não chega nada". Reconfigure o webhook
quando isso acontecer. (Detalhe em [06 – Setup](06-setup-whatsapp.md).)

## 8. 🤖 A IA nem sempre devolve JSON válido

O [contrato com a IA](04-fluxo-conversa.md) espera dado estruturado. Mas LLM às vezes
devolve texto a mais, campo faltando ou JSON quebrado. **Sempre valide** a resposta e tenha
um plano B (repetir o pedido, ou cair pro estado `ESCALAR_HUMANO`). Nunca confie cegamente
no formato.

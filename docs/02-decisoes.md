# 02 – Decisões e porquês

Registro das decisões tomadas e o **motivo** de cada uma. Serve pra você não re-discutir o
que já foi resolvido. Formato: decisão → por quê → status.

> Datas relativas convertidas: documento iniciado em **26/06/2026**.

---

### D1 — IA na nuvem, não local
**Decisão:** usar um provedor de IA em nuvem (via API), não rodar modelo local.
**Por quê:** rodar LLM local no MVP exige GPU, redundância e virar sysadmin de modelo antes
de ter usuário. Local só compensa com volume alto e constante. O custo por conversa na
nuvem é baixo pra esse estágio.
**Status:** ✅ fechado.

### D2 — Vertical = agendamento (não catálogo)
**Decisão:** o primeiro caso é **agendamento**, não catálogo de loja.
**Por quê:** agendamento tem um **fim limpo** (marcou na agenda, acabou) e um backend de
graça (Google Agenda). Catálogo é maior do que parece (fotos, variações, carrinho) e te
puxa pra virar e-commerce com pagamento — buraco sem fundo pra um primeiro projeto.
**Status:** ✅ fechado.

### D3 — Projeto de aprendizado (Path A)
**Decisão:** o objetivo é **aprender a construir**, não validar mercado agora.
**Por quê:** não há cliente ainda. Em modo aprendizado, as escolhas são por **simplicidade
e clareza**, não por "abrangência" / quantos negócios pega. "Genérico pra todo mundo" é
armadilha — vira produto pra ninguém.
**Status:** ✅ fechado.

### D4 — Controle do dono = Google Agenda + ping no WhatsApp
**Decisão:** o dono enxerga os agendamentos pelo **Google Agenda**, e o bot manda um aviso
no WhatsApp dele a cada marcação. Sem dashboard.
**Por quê:** o dono já tem Google Agenda no celular e sabe usar. Zero tela pra construir.
**Status:** ✅ fechado.

### D5 — "A IA entende, o código marca"
**Decisão:** a IA só interpreta a conversa e propõe; quem valida disponibilidade e cria o
evento é o código.
**Por quê:** se a IA marcar direto, ela pode confirmar horário que não existe → cliente
chega e não tem vaga. Risco real de imagem (e, num cenário pago, de processo).
**Status:** ✅ fechado.

### D6 — Sem pagamento / multi-tenant / dashboard no MVP
**Decisão:** fronteiras duras (ver [01 – Escopo](01-escopo.md)).
**Por quê:** cada um desses é um projeto inteiro. No aprendizado, escopo fechado é o que
faz o projeto terminar.
**Status:** ✅ fechado.

### D7 — Infra de verdade só quando virar produto (GCP São Paulo)
**Decisão:** se um dia virar produto pago, a infra alvo é **Google Cloud, região São Paulo**
(Cloud Run + Cloud SQL + storage). Railway foi descartado por **não ter região no Brasil**.
**Por quê:** proximidade (latência) e serviços gerenciados. Mas **nada disso é agora** — no
aprendizado roda local com ngrok.
**Status:** 🔮 futuro (não implementar agora).

### D8 — Stack: Java 21 · Spring Boot 3 · Maven
**Decisão:** backend em Spring Boot 3 com Java 21 e Maven.
**Por quê:** é a stack que o dev já conhece e a mais documentada pra quando travar.
**Status:** ✅ fechado.

### D9 — WhatsApp via Cloud API oficial (número de teste do Meta)
**Decisão:** usar a **WhatsApp Cloud API** oficial da Meta, começando pelo número de teste
grátis. Sem intermediário (BSP) no aprendizado.
**Por quê:** o número de teste é grátis, não exige verificação de empresa nem comprar chip,
e dá pra mandar mensagem pra números na lista de teste (o seu pessoal).
**Status:** ✅ fechado.

### D10 — Provedor de IA: decidir no Milestone 2, atrás de uma interface
**Decisão:** o código fala com a IA por uma **interface** (`AgenteIA`), e a escolha do
provedor/modelo concreto fica pro Milestone 2.
**Por quê:** assim dá pra trocar de modelo sem reescrever o bot, e não trava o início (echo
bot nem usa IA).
**Status:** 🟡 pendente (decidir no M2).

---

## Decisões ainda em aberto (não precisam de resposta agora)

- Onde guardar o **estado da conversa**: em memória vs. tabela simples. (Decidir no M2.)
- Qual **modelo de IA** concreto. (M2.)
- Como representar **disponibilidade**: horário fixo de funcionamento menos eventos da
  agenda. (Detalhar no M3.)

---

## Restrições conhecidas da plataforma (não são escolhas suas)

São limites do WhatsApp/Google que moldam o que dá pra fazer. Lista completa e como
contornar em [09 – Pegadinhas](09-pegadinhas.md).

- **Janela de 24h do WhatsApp:** mensagem proativa (aviso pro dono, lembrete) só fora das
  24h com **template aprovado**. Afeta o [Milestone 4](07-milestones.md).
- **Número de teste:** só envia pra números na lista de permitidos (inclui o dono).
- **Token de teste:** expira em ~24h; precisa de token permanente (System User).
- **Fuso horário:** sempre `America/Sao_Paulo` explícito, ou vira bug de horário.

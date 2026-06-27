# 06 – Setup do WhatsApp (guia de 1ª vez)

É aqui que quase todo iniciante trava. Vai com calma — cada passo tem um porquê. Você
**não precisa comprar chip nem verificar empresa** pra essa fase.

## O conceito antes dos cliques

```
Seu WhatsApp pessoal  ──mensagem──►  Número de TESTE do Meta  ──webhook──►  Seu PC (ngrok ► Spring Boot)
   (destinatário)                       (remetente/bot)                          (seu código)
```

- O **número de teste** (que o Meta te dá grátis) é o "bot".
- O **seu WhatsApp pessoal** é o cliente de teste — basta adicioná-lo na lista de permitidos.
- O **ngrok** dá ao seu PC um endereço `https://` público pra Meta conseguir entregar a
  mensagem no seu código rodando em `localhost`.

## Passo a passo

### 1. Criar o app no Meta
1. Acesse [developers.facebook.com](https://developers.facebook.com) e faça login.
2. **Meus apps → Criar app → tipo Business.**
3. No painel do app, **adicione o produto "WhatsApp".**

### 2. Pegar os dados do número de teste
No menu **WhatsApp → API Setup**, anote:
- **Phone number ID** (id do número de teste — é o remetente).
- **Token de acesso temporário** (vale ~24h; serve pra começar).
- Em **"To"**, adicione **seu número pessoal** na lista de destinatários de teste e
  confirme o código que chega no seu WhatsApp.

> Guarde esses valores — eles vão pro `application.properties` (nunca commitar token!).

### 3. Instalar e subir o ngrok
1. Baixe em [ngrok.com](https://ngrok.com) e crie a conta (grátis).
2. Com seu Spring Boot rodando na porta `8080`, rode:
   ```
   ngrok http 8080
   ```
3. Ele te dá uma URL tipo `https://abc123.ngrok-free.app`. **Essa é sua URL pública.**

> A URL do ngrok **muda** cada vez que você reinicia (no plano grátis). Toda vez que mudar,
> você atualiza o webhook no passo 4. É o tropeço nº 1 dos iniciantes — agora você já sabe.

### 4. Configurar o webhook no Meta
No menu **WhatsApp → Configuration → Webhook**:
- **Callback URL:** `https://abc123.ngrok-free.app/webhook`
- **Verify token:** uma senha qualquer que você inventa (ex.: `meu-token-secreto`). Ela
  também vai no seu `application.properties` — o Meta vai mandar ela de volta pra confirmar
  que o webhook é seu.
- Assine o campo **messages**.

Quando você salvar, o Meta faz um `GET /webhook` com um desafio. Seu código tem que
devolver o desafio (isso é o **Milestone 1**). Se aparecer ✅, o webhook está ligado.

## Variáveis que o app vai precisar

```properties
# application.properties (NÃO commitar com valores reais)
whatsapp.phone-number-id=SEU_PHONE_NUMBER_ID
whatsapp.access-token=SEU_TOKEN
whatsapp.verify-token=meu-token-secreto
```

## Checklist pra dizer "pronto pra codar o Milestone 1"

- [ ] App criado no Meta com o produto WhatsApp.
- [ ] Phone number ID e token temporário anotados.
- [ ] Seu número pessoal na lista de destinatários de teste (confirmado).
- [ ] ngrok instalado.
- [ ] Você entendeu que a URL do ngrok muda a cada reinício.

> Quando tudo acima estiver ✅, partimos pro código do echo bot
> ([Milestone 1](07-milestones.md)).

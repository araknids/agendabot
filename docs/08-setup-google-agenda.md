# 08 – Setup do Google Agenda (guia de 1ª vez)

O equivalente ao [setup do WhatsApp](06-setup-whatsapp.md), pro outro lado do bot. É o
tropeço de 1ª vez que eu tinha deixado de fora.

## O conceito antes dos cliques

Seu backend precisa mexer na agenda **sem um humano logar toda vez**. A forma certa pra um
servidor é uma **service account** — um "usuário robô" do Google. Você cria esse robô, baixa
a chave dele, e **compartilha a agenda da clínica com ele**. Aí o código usa a chave pra
agir em nome do robô.

```
Seu backend ──usa a chave──► Service Account (robô) ──tem acesso a──► Agenda da clínica
                                                       (porque você compartilhou)
```

> ⚠️ A service account **tem uma "agenda" própria que você NÃO usa**. O pulo do gato é
> compartilhar a **agenda real da clínica** com o e-mail do robô. Quem esquece isso fica
> 1 hora vendo "calendar not found".

## Passo a passo

### 1. Projeto + API
1. Acesse o [Google Cloud Console](https://console.cloud.google.com).
2. Crie um projeto (ou reuse um).
3. **APIs e serviços → Biblioteca → ative a "Google Calendar API".**

### 2. Criar a service account
1. **APIs e serviços → Credenciais → Criar credenciais → Conta de serviço.**
2. Dê um nome (ex.: `bot-agenda`) e crie.
3. Entre na conta criada → aba **Chaves → Adicionar chave → JSON.** Baixa o arquivo `.json`.
   **Essa é a chave — guarde com cuidado, NUNCA commite.**
4. Anote o **e-mail da service account** (algo tipo
   `bot-agenda@seu-projeto.iam.gserviceaccount.com`).

### 3. Compartilhar a agenda com o robô
1. Abra o [Google Agenda](https://calendar.google.com) na conta da clínica.
2. Na agenda desejada → **Configurações → Compartilhar com pessoas específicas.**
3. Adicione o **e-mail da service account** com a permissão
   **"Fazer alterações nos eventos"** (não basta "Ver").

### 4. Pegar o ID da agenda
Ainda nas configurações da agenda → seção **Integrar agenda → "ID da agenda"**. Costuma ser
um e-mail (ex.: `...@group.calendar.google.com`) ou o próprio e-mail da conta.

## Variáveis que o app vai precisar

```properties
# application.properties (NÃO commitar com valores reais; e NÃO commitar o .json)
google.calendar.credentials-path=/caminho/seguro/chave-service-account.json
google.calendar.id=SEU_CALENDAR_ID
google.calendar.timezone=America/Sao_Paulo
```

> Veja [09 – Pegadinhas](09-pegadinhas.md) sobre **fuso horário** — agenda + Brasil é onde
> mais aparece bug bobo de "marquei 10h e salvou 13h".

## Checklist pra dizer "pronto pro Milestone 3"

- [ ] Google Calendar API ativada no projeto.
- [ ] Service account criada e chave `.json` baixada e guardada fora do git.
- [ ] Agenda da clínica compartilhada com o e-mail do robô, com permissão de **alterar eventos**.
- [ ] ID da agenda anotado.
- [ ] Fuso `America/Sao_Paulo` definido na config.

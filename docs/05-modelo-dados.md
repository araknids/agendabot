# 05 – Modelo de dados

> ⚠️ **Rascunho a lápis.** Iniciante quase sempre erra o primeiro modelo. Espere mexer
> nisso quando o código existir — esboço, não verdade.

Quanto menos, melhor. No MVP de aprendizado existem basicamente **duas coisas**: o estado
da conversa e a configuração da clínica. O agendamento em si **mora no Google Agenda**.

## 1. EstadoConversa

Guarda em que ponto cada cliente está. Chaveado pelo número de telefone.

| Campo | Tipo | Exemplo |
|-------|------|---------|
| `telefone` | String (chave) | `5511999998888` |
| `estado` | enum | `ESCOLHER_DIA` |
| `servico` | String? | `limpeza` |
| `diaPreferido` | Data? | `2026-06-30` |
| `horarioEscolhido` | Hora? | `10:00` |
| `atualizadoEm` | timestamp | `2026-06-26T19:40` |

> **Onde guardar?** No início, um `Map` em memória já resolve (some se reiniciar o app —
> tudo bem no aprendizado). Se quiser persistir, uma tabela simples. Decidir no Milestone 2.

## 2. ConfiguracaoClinica (hardcoded no MVP)

Não tem tela de cadastro. É um arquivo de config / constantes.

| Campo | Exemplo |
|-------|---------|
| `horarioAbertura` | `09:00` |
| `horarioFechamento` | `18:00` |
| `duracaoSlot` | `60 min` |
| `diasDeFuncionamento` | seg–sex |
| `servicos` | `["avaliação", "limpeza", "clareamento"]` |
| `googleCalendarId` | `id da agenda da clínica` |
| `telefoneDono` | `5511988887777` (pra receber os avisos) |

## 3. Agendamento → vive no Google Agenda

**Não tem tabela de agendamento.** A fonte da verdade é o Google Agenda:

- **Horário livre** = um slot dentro do funcionamento que **não** tem evento na agenda.
- **Marcar** = criar um evento na agenda (título: serviço + telefone do cliente).
- **Ver os agendamentos** = o dono abre o Google Agenda dele. Esse é o "painel".

Isso elimina o problema de "duas fontes da verdade desincronizadas": só existe uma, a
agenda.

## Como calcular horários livres (resumo)

```
livres(dia) =
    todos os slots entre abertura e fechamento, de duracaoSlot em duracaoSlot
    MENOS
    os slots que batem com algum evento já existente na agenda naquele dia
```

Detalhar a implementação no [Milestone 3](07-milestones.md).

## O que NÃO existe no modelo (de propósito)

- ❌ Tabela de clientes / cadastro de paciente.
- ❌ Tabela de pagamentos.
- ❌ Múltiplas clínicas (tudo é "a clínica", uma só).
- ❌ Histórico de conversas persistido (LGPD formal é assunto de "quando virar produto").

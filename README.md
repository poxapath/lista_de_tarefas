# 🗒️Lista de Tarefas -  To-Do (CRUD)

Projeto Avaliativo do 1º Bimestre de Laboratório de Desenvolvimento Multiplataforma, 6º DSM - Fatec Franca. Criação de uma API REST para gerenciamento de tarefas do dia a dia, permitindo criar, listar, alterar e deletar tarefas.

## ▶️ Como executar

### Banco de dados
1. Crie o banco: `CREATE DATABASE todo_db;`
2. Execute o script `database/script.sql` dentro do banco `todo_db`.

### Configuração
As credenciais do banco ficam em um arquivo `.env` (não versionado, por segurança).

1. Copie o arquivo `.env.example` e renomeie a cópia para `.env`
2. Preencha com os dados do seu PostgreSQL (principalmente `DB_PASSWORD` e, se necessário, `DB_PORT`)

### Execução
```bash
./mvnw spring-boot:run
```
A API sobe em `http://localhost:8080`.

## 🔗 Endpoints
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/tarefas` | Lista todas as tarefas |
| GET | `/tarefas/{id}` | Busca uma tarefa |
| POST | `/tarefas` | Cria uma tarefa |
| PUT | `/tarefas/{id}` | Altera uma tarefa |
| DELETE | `/tarefas/{id}` | Deleta uma tarefa |

Exemplo de corpo (POST/PUT):
```json
{
  "nome": "Estudar Spring",
  "descricao": "Revisar o projeto",
  "status": "PENDENTE",
  "observacoes": "Status aceitos: PENDENTE, EM_ANDAMENTO, CONCLUIDA"
}
```
# Integrantes
Patricia Nogueira Dias

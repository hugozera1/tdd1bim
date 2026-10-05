# To-Do — Backend CRUD

Projeto avaliativo de TDD do 1º bimestre: API REST para tarefas, em Java 21,
Spring Boot e PostgreSQL. Não possui login nem entidade de usuário.

## Executar

Requisitos: JDK 21 e PostgreSQL (ou Docker para subir o banco).
O Maven Wrapper incluído baixa o Maven automaticamente.

1. Suba o banco: `docker compose up -d`.
2. Inicie a API: `./mvnw spring-boot:run` (Windows: `.\mvnw.cmd spring-boot:run`).
3. Acesse `http://localhost:8080/tarefas`.

O Docker expõe o banco na porta 5433 para evitar conflito com um PostgreSQL instalado.
Sem Docker, execute `database/criar-banco.sql` como administrador do PostgreSQL
e configure `DB_URL=jdbc:postgresql://localhost:5432/todo` para a porta padrão.
O script completo da tabela está em `src/main/resources/schema.sql` e é
executado automaticamente na inicialização. O Hibernate valida a estrutura.
As credenciais locais padrão são `todo/todo`, banco `todo`.
Para outro ambiente, configure `DB_URL`, `DB_USER`, `DB_PASSWORD` e, opcionalmente, `PORT`.

## Endpoints

| Método | Rota | Resultado |
| --- | --- | --- |
| POST | /tarefas | Cria uma tarefa; 201 e cabeçalho Location |
| GET | /tarefas | Lista as tarefas em ordem de ID; 200 |
| GET | /tarefas/{id} | Consulta uma tarefa; 200 |
| PUT | /tarefas/{id} | Altera os dados da tarefa; 200 |
| DELETE | /tarefas/{id} | Exclui a tarefa; 204 |

Corpo de POST e PUT:

```json
{
  "nome": "Estudar TDD",
  "descricao": "Revisar testes unitários e de integração",
  "status": "PENDENTE",
  "observacoes": "Capítulo 1"
}
```

Nome e descrição são obrigatórios, com limites de 120 e 2000 caracteres.
Status é obrigatório: `PENDENTE`, `EM_ANDAMENTO` ou `CONCLUIDA`.
Observações são opcionais, limitadas a 2000 caracteres.
O PUT recebe todos os campos obrigatórios; omitir observações limpa esse campo.
ID, data de criação e data de atualização são gerados pelo backend.
As datas são retornadas em UTC, no formato ISO 8601.
Uma alteração efetiva mantém a criação e atualiza a data de atualização.
Dados inválidos retornam 400; tarefas inexistentes retornam 404.

Exemplo no PowerShell:

```powershell
$dados = @{
  nome = "Estudar TDD"
  descricao = "Revisar testes"
  status = "PENDENTE"
  observacoes = "Capítulo 1"
} | ConvertTo-Json
Invoke-RestMethod http://localhost:8080/tarefas -Method Post -ContentType "application/json; charset=utf-8" -Body ([System.Text.Encoding]::UTF8.GetBytes($dados))
Invoke-RestMethod http://localhost:8080/tarefas
```

## Testes

Execute `./mvnw test` (Windows: `.\mvnw.cmd test`).

- Unitários: serviço com Mockito, criação, atualização, exclusão e ausência de tarefa.
- Integração: contexto Spring, endpoints HTTP com MockMvc e persistência real
  em H2 no modo PostgreSQL, executando o mesmo script de tabelas.
- Cobertura funcional: ciclo CRUD, datas automáticas, validação, limites,
  JSON inválido, status inválido e respostas 400/404.

Os testes automatizados usam H2 e não exigem um PostgreSQL instalado.
Eles não substituem a verificação da aplicação conectada ao PostgreSQL.

## Organização

`src/main/java/br/edu/fatec/todo`: modelo, DTO validado, repositório,
serviço transacional, controlador REST e tratamento de erros.
`src/test`: testes unitários e de integração.
`database`: criação do usuário e do banco PostgreSQL.

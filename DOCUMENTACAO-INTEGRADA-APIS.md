# Documentação integrada — Audiência Virtual

## 1. Visão geral

Este documento consolida o contrato dos três projetos do workspace: API de
domínio, BFF Gateway e frontend Angular.

### Componentes implementados

| Componente   | Tecnologia / responsabilidade                              |
| ------------ | ---------------------------------------------------------- |
| API          | Spring Boot, Spring MVC, Spring Data JPA e Bean Validation |
| Persistência | MySQL em execução normal; H2 disponível para testes        |
| Documentação | springdoc OpenAPI 3 e Swagger UI                           |
| Domínio      | Usuários, partes, agendas e audiências virtuais            |
| Porta local  | `8082`                                                     |
| BFF Gateway  | Spring WebFlux, proxy da API, porta `8084`                 |
| Frontend     | Angular 17, proxy `/api` para `8084`, porta `4200`         |

## 2. Execução local

Crie o banco:

```sql
CREATE DATABASE audiencia_virtual
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

Configure as credenciais no PowerShell:

```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "root"
```

`DB_URL` também pode ser informado quando a URL JDBC padrão não for adequada.
Inicie a API:

```powershell
.\mvnw.cmd spring-boot:run
```

Configurações relevantes:

```properties
server.port=8082
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
spring.jpa.hibernate.ddl-auto=update
app.seed.enabled=true
```

Com a aplicação em execução:

- Swagger UI: <http://localhost:8082/swagger-ui.html>
- Especificação OpenAPI JSON: <http://localhost:8082/v3/api-docs>
- Especificação OpenAPI YAML: <http://localhost:8082/v3/api-docs.yaml>

O Swagger usa `http://localhost:8082` como servidor local. Em outros ambientes,
o endereço deve ser substituído pela URL publicada da API ou pela URL do BFF.

## 3. Modelo de dados e relacionamentos

```text
Usuario
  └── Parte (herda Usuario e possui numeroProcesso)
        └── Agenda
              └── AudienciaVirtual (uma audiência por agenda)
```

### Usuário

| Campo    | Tipo     | Regras                                     |
| -------- | -------- | ------------------------------------------ |
| `id`     | `Long`   | Gerado pelo banco                          |
| `nome`   | `String` | Obrigatório                                |
| `cpf`    | `String` | Obrigatório, exatamente 11 dígitos e único |
| `funcao` | `String` | Obrigatório                                |

O CPF é normalizado para conter apenas números antes da validação e gravação.

### Parte

É um usuário com o campo adicional:

| Campo            | Tipo     | Regras                                   |
| ---------------- | -------- | ---------------------------------------- |
| `numeroProcesso` | `String` | Obrigatório no fluxo de criação da parte |

### Agenda

| Campo   | Tipo     | Regras             |
| ------- | -------- | ------------------ |
| `id`    | `Long`   | Gerado pelo banco  |
| `nome`  | `String` | Até 120 caracteres |
| `parte` | `Parte`  | Obrigatória        |

### Audiência virtual

| Campo             | Tipo            | Regras                                                              |
| ----------------- | --------------- | ------------------------------------------------------------------- |
| `id`              | `Long`          | Gerado pelo banco                                                   |
| `agenda`          | `Agenda`        | Obrigatória e exclusiva                                             |
| `email`           | `String`        | E-mail de contato                                                   |
| `dataAudiencia`   | `LocalDateTime` | ISO-8601, por exemplo `2026-10-10T14:00:00`                         |
| `siteAgendamento` | `String`        | Plataforma utilizada, por exemplo `Microsoft Teams`                 |
| `status`          | `String`        | `AGENDADA`, `EM_ANDAMENTO`, `CONCLUIDA`, `ENCERRADA` ou `CANCELADA` |

Uma audiência nova sempre é criada com status `AGENDADA`.

### Exclusão

Use `DELETE /v1/audiencias-virtuais/{id}` para remover uma audiência criada
incorretamente ou já concluída. A operação retorna `204 No Content` quando
concluída e `404 Not Found` quando o ID não existe. A exclusão não exige
confirmação na API; a confirmação é feita pelo frontend antes do envio.
| `DELETE` | `/api/v1/bff/audiencias/{id}` | `DELETE /v1/audiencias-virtuais/{id}` |

## 4. Endpoints da API

Todos os endpoints usam o prefixo `/v1` e JSON, salvo indicação contrária.

### 4.1 Usuários — `/v1/usuarios`

| Método   | Rota                | Sucesso       | Descrição            |
| -------- | ------------------- | ------------- | -------------------- |
| `GET`    | `/v1/usuarios`      | `200`         | Lista usuários       |
| `GET`    | `/v1/usuarios/{id}` | `200` / `404` | Busca usuário por ID |
| `POST`   | `/v1/usuarios`      | `201`         | Cria usuário         |
| `DELETE` | `/v1/usuarios/{id}` | `204`         | Exclui usuário       |

Exemplo de criação:

```json
{
  "nome": "João Silva",
  "cpf": "12345678901",
  "funcao": "ADMIN"
}
```

Se o CPF já estiver cadastrado, a API retorna `409 Conflict` sem corpo.
Campos inválidos retornam `400 Bad Request`.

### 4.2 Partes — `/v1/partes`

| Método   | Rota              | Sucesso       | Descrição          |
| -------- | ----------------- | ------------- | ------------------ |
| `GET`    | `/v1/partes`      | `200`         | Lista partes       |
| `GET`    | `/v1/partes/{id}` | `200` / `404` | Busca parte por ID |
| `POST`   | `/v1/partes`      | `201`         | Cria parte         |
| `DELETE` | `/v1/partes/{id}` | `204`         | Exclui parte       |

Exemplo:

```json
{
  "nome": "Maria da Silva",
  "cpf": "11111111111",
  "funcao": "Autora",
  "numeroProcesso": "0000001-00.2026.8.05.0001"
}
```

Como `Parte` herda de `Usuario`, a resposta contém `id`, `nome`, `cpf`,
`funcao` e `numeroProcesso`.

### 4.3 Agendas — `/v1/agendas`

| Método   | Rota               | Sucesso       | Descrição           |
| -------- | ------------------ | ------------- | ------------------- |
| `GET`    | `/v1/agendas`      | `200`         | Lista agendas       |
| `GET`    | `/v1/agendas/{id}` | `200` / `404` | Busca agenda por ID |
| `POST`   | `/v1/agendas`      | `201`         | Cria agenda         |
| `DELETE` | `/v1/agendas/{id}` | `204`         | Exclui agenda       |

Exemplo:

```json
{
  "nome": "Agenda Maria da Silva",
  "parte": {
    "id": 1
  }
}
```

Para o fluxo de agendamento, é preferível usar o endpoint de audiências, que
localiza a agenda pelo nome ou cria a parte e a agenda quando elas ainda não
existem.

### 4.4 Audiências virtuais — `/v1/audiencias-virtuais`

| Método   | Rota                                         | Sucesso       | Descrição                |
| -------- | -------------------------------------------- | ------------- | ------------------------ |
| `POST`   | `/v1/audiencias-virtuais`                    | `201`         | Agenda uma audiência     |
| `GET`    | `/v1/audiencias-virtuais`                    | `200`         | Lista audiências         |
| `GET`    | `/v1/audiencias-virtuais/{id}`               | `200` / `404` | Busca por ID             |
| `GET`    | `/v1/audiencias-virtuais/buscar?nome={nome}` | `200`         | Busca pelo nome da parte |
| `DELETE` | `/v1/audiencias-virtuais/{id}`               | `204` / `404` | Exclui audiência         |

#### Agendamento

Requisição:

```json
{
  "agendaNome": "Agenda Maria da Silva",
  "parteNome": "Maria da Silva",
  "parteCpf": "111.111.111-11",
  "parteNumeroProcesso": "0000001-00.2026.8.05.0001",
  "email": "maria.silva@exemplo.com",
  "dataAudiencia": "2026-10-10T14:00:00",
  "siteAgendamento": "Microsoft Teams"
}
```

Regras do fluxo:

1. `agendaNome`, `email`, `dataAudiencia` e `siteAgendamento` são obrigatórios.
2. Se a agenda não existir, `parteNome`, `parteCpf` e
   `parteNumeroProcesso` também são usados para criar a parte e a agenda.
3. O CPF é reduzido a dígitos e deve possuir exatamente 11 números.
4. A mesma parte não pode possuir duas audiências na mesma sala e no mesmo horário.
5. Se o nome da agenda já estiver associado a outra audiência, uma nova agenda é criada
   para permitir outro horário ou sala.
6. A audiência é criada com status `AGENDADA`.

Resposta `201`:

```json
{
  "id": 1,
  "agendaNome": "Agenda Maria da Silva",
  "parteId": 1,
  "nomeParte": "Maria da Silva",
  "email": "maria.silva@exemplo.com",
  "dataAudiencia": "2026-10-10T14:00:00",
  "siteAgendamento": "Microsoft Teams",
  "status": "AGENDADA"
}
```

## 5. Erros padronizados

As validações e regras de negócio são tratadas por `ApiExceptionHandler`.

### `400 Bad Request`

```json
{
  "status": 400,
  "titulo": "Dados inválidos",
  "mensagem": "Revise os campos informados.",
  "campos": {
    "email": "Informe um e-mail válido."
  }
}
```

Regras de dados inválidas também retornam `400`, com `status`, `titulo` e
`mensagem`.

### `409 Conflict`

Conflito de agendamento na mesma sala e horário:

```json
{
  "status": 409,
  "titulo": "Conflito de agendamento",
  "mensagem": "Já existe uma audiência virtual para esta agenda."
}
```

Conflito de cadastro:

```json
{
  "status": 409,
  "titulo": "Conflito de cadastro",
  "mensagem": "Já existe uma parte ou agenda com os dados informados."
}
```

### `404 Not Found` e `204 No Content`

Consultas por ID retornam `404` quando o registro não existe. Exclusões bem
executadas retornam `204` sem corpo; a exclusão de audiência inexistente
retorna `404`.

## 6. BFF Gateway

O BFF expõe uma API pública em `http://localhost:8084` e encaminha as
requisições para a API de domínio em `http://localhost:8082`.

| Método | Rota pública                          | Rota de domínio                        |
| ------ | ------------------------------------- | -------------------------------------- |
| `GET`  | `/api/v1/bff/audiencias`              | `/v1/audiencias-virtuais`              |
| `GET`  | `/api/v1/bff/audiencias/{id}`         | `/v1/audiencias-virtuais/{id}`         |
| `GET`  | `/api/v1/bff/audiencias/buscar?nome=` | `/v1/audiencias-virtuais/buscar?nome=` |
| `POST` | `/api/v1/bff/audiencias`              | `/v1/audiencias-virtuais`              |

O BFF documenta as rotas em:

- Swagger UI: <http://localhost:8084/swagger-ui.html>
- OpenAPI JSON: <http://localhost:8084/v3/api-docs>

Ele encaminha `Authorization` e `X-Correlation-Id`. Se a API de domínio não
estiver disponível, retorna `503 Service Unavailable`.

## 7. Integração com front-end

O front-end deve consumir, preferencialmente, uma URL configurada por ambiente,
sem fixar `localhost` no código. Quando houver BFF, o fluxo recomendado é:

```text
Front-end -> BFF -> API Audiência Virtual -> MySQL
```

O frontend usa `src/environments/environment.ts` com base `/api/v1/bff` e o
`proxy.conf.json` encaminha `/api` para `http://localhost:8084`. O mock BFF
fica desligado por padrão (`usarMockBff: false`).

Rotas de tela:

| URL                                 | Finalidade         |
| ----------------------------------- | ------------------ |
| `/audiencias-virtuais/list`         | Listagem e filtros |
| `/audiencias-virtuais/novo`         | Cadastro           |
| `/audiencias-virtuais/detalhe/{id}` | Detalhamento       |

O frontend converte o DTO de domínio para o modelo visual e envia no cadastro
os campos `agendaNome`, `parteNome`, `parteCpf`, `parteNumeroProcesso`,
`email`, `dataAudiencia` e `siteAgendamento`.

Exemplo de chamada direta:

```javascript
const response = await fetch(`${API_URL}/v1/audiencias-virtuais`, {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify(payload),
});

if (!response.ok) {
  throw new Error("Não foi possível agendar a audiência.");
}

const audiencia = await response.json();
```

## 8. Swagger / OpenAPI

O Swagger é gerado automaticamente pelo `springdoc-openapi` a partir dos
controllers, DTOs e configurações OpenAPI. A configuração atual define:

- título: `API Audiência Virtual`;
- versão: `v1`;
- servidor local: `http://localhost:8082`;
- interface: `/swagger-ui.html`;
- contrato JSON: `/v3/api-docs`.

As operações de audiência possuem descrições e respostas anotadas. Os DTOs de
agendamento também exibem exemplos e descrições no Swagger. Para `usuarios`,
`partes` e `agendas`, a documentação é inferida pelas assinaturas dos
controllers e pelos modelos JPA.

## 9. Execução e troubleshooting

Inicie os componentes nesta ordem:

```powershell
# Terminal 1 - API
cd C:\Users\kesia\Desktop\projetos-tjba\api-audiencia-virtual
.\mvnw.cmd spring-boot:run

# Terminal 2 - BFF
cd C:\Users\kesia\Desktop\projetos-tjba\audiencia-virtual-bff
.\mvnw.cmd spring-boot:run

# Terminal 3 - Frontend
cd C:\Users\kesia\Desktop\projetos-tjba\audiencia-virtual-frontend
npm install
npm start
```

Erros comuns:

- `503`: backend não está ativo em `8082` ou o BFF não foi reiniciado.
- `409`: a agenda informada já possui audiência.
- `400`: CPF deve possuir exatamente 11 dígitos e os campos obrigatórios devem
  estar preenchidos.
- Frontend sem dados: reinicie `ng serve` após alterar `proxy.conf.json`.

## 10. Limitações e pontos de evolução

- Não há mecanismo de autenticação ou autorização implementado.
- Não há endpoint para editar registros ou alterar o status da audiência.
- Não há paginação ou filtros para listagens gerais.
- É recomendável substituir injeção por campo por injeção via construtor e
  revisar a configuração de credenciais do perfil MySQL antes de publicar.

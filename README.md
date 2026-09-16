# audiencia-virtual-service

## Executar com MySQL

1. Crie o banco no MySQL:

```sql
CREATE DATABASE audiencia_virtual
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

2. Defina as credenciais (PowerShell), se forem diferentes das padroes:

```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "root"
```

Opcionalmente, use `DB_URL` para informar outra URL JDBC.

3. Inicie a API:

```powershell
.\mvnw.cmd spring-boot:run
```

O MySQL usa `spring.jpa.hibernate.ddl-auto=update`, portanto as tabelas das
entidades são criadas/atualizadas sem apagar os dados existentes. A carga
inicial Java só ocorre quando ainda não existem registros.

O H2 fica restrito aos testes automatizados.
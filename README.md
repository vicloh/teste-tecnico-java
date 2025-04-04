
# Projeto Spring Boot + MySQL - Teste Técnico

Este pacote contém:
- Projeto Spring Boot (`projeto_spring_boot/`)
- Dump do banco de dados MySQL (`ans_db.sql`)
- Este arquivo `README.md`

## Como importar o banco de dados

1. Instale o MySQL 8+.
2. Crie o banco executando o seguinte comando no terminal:

```bash
mysql -u root -p < ans_db.sql
```

3. Verifique se o banco foi importado corretamente:

```bash
mysql -u root -p
SHOW DATABASES;
USE ans_db;
SHOW TABLES;
```

## Rodando o Projeto

1. Navegue até a pasta `projeto_spring_boot/`
2. Compile com:

```bash
./mvnw clean install
```

3. Execute com:

```bash
./mvnw spring-boot:run
```

## Credenciais MySQL

- Usuário: `root`
- Senha: `1234`

Certifique-se de atualizar `application.properties` caso esteja usando outras credenciais.

---

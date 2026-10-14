# AI-Powered Query Translation Layer

A microservices backend built with Spring Boot that turns plain English questions into SQL using the Claude API. The generated SQL is validated, executed on MySQL, and the results are returned as JSON.

Example: `show me electronics under 500` becomes `SELECT * FROM products WHERE category = 'Electronics' AND price < 500` and returns the matching rows.

## Architecture

```
                 +--------------------------+        +-------------+
  Client  -----> |   Translator Service     | -----> |  Claude API |
 (Postman)       |        :8081             | <----- |  (SQL text) |
                 +------------+-------------+        +-------------+
                              |
                              |  REST (RestTemplate)
                              v
                 +--------------------------+
                 |    Executor Service      |
                 |        :8082             |
                 |  validates + runs SQL    |
                 +------------+-------------+
                              |
                              v
                 MySQL: query_executor_db (products)

  Translator Service also writes to its own database:
  MySQL: query_translator_db (query_logs, users)
```

| Service    | Port | Responsibility                                                         | Database              |
|------------|------|------------------------------------------------------------------------|-----------------------|
| Translator | 8081 | Validates the English question, calls Claude, calls Executor, logs requests | `query_translator_db` |
| Executor   | 8082 | Validates the generated SQL, runs it, returns rows                     | `query_executor_db`   |

Each service has its own database.

## Tech Stack

| Layer        | Technology                          |
|--------------|-------------------------------------|
| Framework    | Spring Boot 3.5 (Java 21)           |
| Database     | MySQL + Spring Data JPA / Hibernate |
| SQL Execution| Spring JdbcTemplate (Executor)      |
| AI           | Claude API (Anthropic Messages API) |
| HTTP Client  | RestTemplate                        |
| Validation   | Jakarta Bean Validation             |
| Build Tool   | Maven                               |

## Installation

```bash
git clone https://github.com/hemanthreddycodes/ai-powered-query-translation-layer.git
cd ai-powered-query-translation-layer
```

Create two MySQL databases:

```sql
CREATE DATABASE query_translator_db;
CREATE DATABASE query_executor_db;
```

Tables are created automatically by Hibernate on first run.

## Configuration

`application.properties` is git-ignored because it holds secrets. Create it manually in each service.

**Executor** — `query-executor-service/src/main/resources/application.properties`

```properties
server.port=8082
spring.datasource.url=jdbc:mysql://localhost:3306/query_executor_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
```

**Translator** — `query-translator-service/src/main/resources/application.properties`

```properties
server.port=8081
spring.datasource.url=jdbc:mysql://localhost:3306/query_translator_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false

claude.api.key=your_claude_api_key
claude.api.url=https://api.anthropic.com/v1/messages
claude.api.model=claude-sonnet-4-5
```

| Property             | Description                                    | Example                                  |
|----------------------|------------------------------------------------|------------------------------------------|
| `spring.datasource.url` | JDBC connection URL for MySQL               | `jdbc:mysql://localhost:3306/query_executor_db` |
| `spring.datasource.username` | MySQL username                         | `root`                                   |
| `spring.datasource.password` | MySQL password                         | `your_password`                          |
| `claude.api.key`     | Your Claude API key (Translator only)          | `your_claude_api_key`                    |
| `claude.api.model`   | Claude model used to generate SQL              | `claude-sonnet-4-5`                      |

Never commit your API key.

## Sample Data

Start the Executor once so the `products` table is created, then run:

```sql
USE query_executor_db;

INSERT INTO products (name, price, category, created_at) VALUES
('Laptop', 999.99, 'Electronics', NOW()),
('Phone', 499.99, 'Electronics', NOW()),
('Desk', 149.99, 'Furniture', NOW()),
('Chair', 89.99, 'Furniture', NOW()),
('Headphones', 49.99, 'Electronics', NOW()),
('Monitor', 299.99, 'Electronics', NOW()),
('Bookshelf', 79.99, 'Furniture', NOW()),
('Keyboard', 59.99, 'Electronics', NOW());
```

## Run Locally

Start the Executor first, then the Translator.

```bash
cd query-executor-service
./mvnw clean install
./mvnw spring-boot:run
```

In a second terminal:

```bash
cd query-translator-service
./mvnw clean install
./mvnw spring-boot:run
```

Executor runs at `http://localhost:8082`.
Translator runs at `http://localhost:8081`.

## API Endpoints

Authentication is not enabled yet, so all endpoints are open.

### Translator — `/api/translator`

| Method | Endpoint      | Description                                    |
|--------|---------------|------------------------------------------------|
| POST   | `/translate`  | Convert an English question to SQL and run it  |

Request:

```json
{ "query": "show me all products" }
```

Response (example shape):

```json
{
  "originalQuery": "show me all products",
  "generatedSql": "SELECT * FROM products",
  "results": [
    { "id": 1, "name": "Laptop", "price": 999.99, "category": "Electronics" },
    { "id": 2, "name": "Phone", "price": 499.99, "category": "Electronics" }
  ],
  "rowCount": 8,
  "timestamp": "2026-05-26T00:46:16"
}
```

### Executor — `/api/executor`

| Method | Endpoint    | Description                                  |
|--------|-------------|----------------------------------------------|
| POST   | `/execute`  | Validate and run a SQL query (internal use)  |

Request:

```json
{ "sql": "SELECT * FROM products" }
```

The Executor is meant to be called by the Translator, not by end users.

### Error Response

```json
{
  "error": "Invalid Query",
  "message": "Query contains suspicious keywords",
  "status": 400,
  "timestamp": "2026-05-26T00:51:24"
}
```

| Status | When                                                                                   |
|--------|----------------------------------------------------------------------------------------|
| 400    | The question is empty, too short, too long, or contains suspicious keywords            |
| 422    | Claude could not produce usable SQL, or the Executor call failed (including unsafe SQL)|
| 500    | Unexpected server error                                                                |

## Validation

The AI output is never trusted. Both services validate independently.

**Translator (English question)**
- Must not be empty
- Between 3 and 200 characters
- Must not contain `DROP`, `DELETE`, `TRUNCATE`, `INSERT` or `UPDATE`

**Executor (generated SQL)**
- Must start with `SELECT`
- Must query the `products` table
- Only one statement allowed
- Must not contain `DROP`, `DELETE`, `UPDATE`, `INSERT`, `TRUNCATE`, `ALTER` or `CREATE`

Every translate request (question, generated SQL, status) is saved to the `query_logs` table.

## Example Questions

- show me all electronics
- show me products under 100
- show me the most expensive product
- how many products are there
- what is the average price
- how many products in each category

## Project Structure

```
query-translator-service/   controller, service, repository, model, dto,
                            validator, exception, config, util
query-executor-service/     controller, service, repository, model, dto,
                            validator, exception, config, util
```

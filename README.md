# Coupon API

Uma API RESTful robusta desenvolvida em **Java 21** e **Spring Boot** para o gerenciamento do ciclo de vida de cupões de desconto. O projeto implementa boas práticas de desenvolvimento, incluindo arquitetura em camadas, Soft Delete, validações automáticas e testes de integração.

## Tecnologias Utilizadas

### Core & Framework
* **Java 21 (LTS)**
* **Spring Boot 3.4.1**
* **Gradle** (Build Tool)

### Banco de Dados
* **H2 Database** (Em memória para execução rápida e leve)
* **Spring Data JPA** (Persistência)

### Ferramentas & Utilitários
* **Lombok:** Redução de boilerplate (Getters, Setters, Builders).
* **MapStruct:** Mapeamento de alta performance entre DTOs e Entidades.
* **SpringDoc OpenAPI (Swagger):** Documentação viva da API.
* **Docker & Docker Compose:** Containerização.

### Qualidade de Código (Testes)
* **JUnit 5**
* **Mockito**
* **Spring Boot Test** (Integração e Contexto)


## Arquitetura e Design

O projeto segue uma arquitetura em camadas limpa:

1.  **Controller:** Camada de entrada, responsável apenas por receber HTTP e validar DTOs (`@Valid`).
2.  **Service:** Camada de regras de negócio. Implementa a lógica de **Soft Delete** (mudança de status em vez de exclusão física).
3.  **Repository:** Interface de comunicação com o banco de dados.
4.  **Mapper:** Camada de transformação. Garante que regras de criação (ex: `redeemed = false`, `status = ACTIVE`) sejam aplicadas automaticamente antes de chegar ao banco.

## Como Rodar o Projeto

### Pré-requisitos
* **Docker Desktop** (Recomendado)
* OU Java 21 JDK instalado.

### Opção 1: Via Docker (Zero Configuração)
Esta é a maneira mais fácil, pois não requer Java instalado na máquina local.

1.  Na raiz do projeto, execute:
    ```bash
    docker compose up --build
    ```
2.  Aguarde a inicialização. A API estará disponível em: `http://localhost:8080`.

3.  Para parar a aplicação:
    ```bash
    docker compose down
    ```

### Opção 2: Via Gradle (Local)

1.  Certifique-se de ter o Java 21 configurado.
2.  Execute o comando:

    **Windows:**
    ```powershell
    ./gradlew bootRun
    ```

    **Linux/Mac:**
    ```bash
    ./gradlew bootRun
    ```

## Documentação da API

Com a aplicação rodando, acesse a interface do Swagger UI para testar os endpoints visualmente:

**[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

### Principais Endpoints

| Verbo  | Endpoint       | Descrição                                         |
| :----- | :------------- | :------------------------------------------------ |
| POST   | `/coupon`      | Cria um novo cupão (Status nasce como ACTIVE).    |
| GET    | `/coupon/{id}` | Retorna os detalhes de um cupão específico.       |
| DELETE | `/coupon/{id}` | Realiza a exclusão lógica (muda status para DELETED). |


## Executando os Testes

O projeto conta com uma suíte de testes que cobre desde a unidade até a integração (Jornada do Usuário).

Para rodar todos os testes:

**Windows:**
```powershell
./gradlew test
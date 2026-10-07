# DSCommerce - Plataforma de E-commerce

API REST completa para uma plataforma de e-commerce, desenvolvida com Java e Spring Boot. O projeto demonstra arquitetura em camadas, tratamento de erros robusto e integração com banco de dados relacional.

## Stacks

- **Java 17** - Linguagem de programação
- **Spring Boot 3.5.7** - Framework web
- **Spring Data JPA** - Persistência de dados
- **Hibernate** - ORM
- **Maven** - Gerenciador de dependências
- **H2 Database** - Banco em memória para desenvolvimento

## Funcionalidades

- ✅ CRUD completo de produtos
- ✅ Gestão de carrinho de compras
- ✅ Sistema de pedidos
- ✅ Categorização de produtos
- ✅ Validação de dados com Bean Validation
- ✅ Tratamento customizado de exceções

## Arquitetura

O projeto segue padrão de arquitetura em **3 camadas**:

```
src/main/java/com/lutheone/dscommerce/
├── controller/          # Endpoints REST
├── service/             # Regras de negócio
├── repository/          # Acesso ao banco (JPA)
└── entity/              # Modelos de dados
```

### Fluxo de Requisição
```
HTTP Request → Controller → Service → Repository → Database
```

## Como Executar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+

### Passos

1. **Clone o repositório**
```bash
git clone https://github.com/lutheone/DSCommerce.git
cd DSCommerce
```

2. **Execute a aplicação**
```bash
mvn spring-boot:run
```

3. **A API estará disponível em**
```
http://localhost:8080
```

## Endpoints Principais

### Produtos
- `GET /api/products` - Listar todos os produtos
- `GET /api/products/{id}` - Buscar produto por ID
- `POST /api/products` - Criar novo produto
- `PUT /api/products/{id}` - Atualizar produto
- `DELETE /api/products/{id}` - Deletar produto

### Pedidos
- `GET /api/orders` - Listar pedidos do usuário
- `POST /api/orders` - Criar novo pedido
- `GET /api/orders/{id}` - Detalhes do pedido

## Banco de Dados

O projeto usa H2 Database em memória. Para acessar o console:

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
User: sa
Password: (deixar vazio)
```

## Conceitos Demonstrados

- **Spring Data JPA** - Operações CRUD simplificadas
- **Relacionamentos ORM** - @OneToMany, @ManyToOne, @ManyToMany
- **Validação** - @NotNull, @NotBlank, @Email, etc.
- **Exception Handling** - CustomException e @ControllerAdvice
- **DTO Pattern** - Separação entre entidades e DTOs
- **REST Best Practices** - Status HTTP corretos, URLs semânticas

## Estrutura de Respostas

As respostas seguem um padrão consistente:

**Sucesso (200)**
```json
{
  "id": 1,
  "name": "Produto XYZ",
  "price": 99.99,
  "category": "Eletrônicos"
}
```

**Erro (4xx/5xx)**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 404,
  "error": "Produto não encontrado",
  "message": "Produto com ID 999 não existe"
}
```

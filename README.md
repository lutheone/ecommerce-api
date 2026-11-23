<div align="center">

# DSCommerce

**API REST para operações de uma loja virtual.**

Catálogo de produtos, categorias, usuários e pedidos, com autenticação OAuth 2.0 e autorização por perfil.

[Funcionalidades](#funcionalidades) · [Tecnologias](#tecnologias) · [Segurança](#autenticação-e-segurança) · [Executar](#como-executar) · [Endpoints](#endpoints)

</div>

## Sobre o projeto

O DSCommerce é uma API de e-commerce desenvolvida com Java e Spring Boot. O projeto organiza as regras de negócio em serviços, o acesso a dados em repositórios e a comunicação HTTP em controllers. O catálogo oferece paginação e busca por nome; os endpoints protegidos usam tokens JWT e autorização baseada nos perfis do usuário.

## Funcionalidades

- Consulta de produtos com paginação e filtro por nome
- Cadastro, atualização e remoção de produtos por administradores
- Consulta de categorias
- Criação e consulta de pedidos
- Consulta dos dados do usuário autenticado
- Emissão e validação de tokens JWT
- Autorização por perfil (`CLIENT` e `ADMIN`)
- Validação de requisições e tratamento de exceções
- Banco H2 em memória para desenvolvimento e testes

## Tecnologias

| Tecnologia | Uso |
| --- | --- |
| **Java 17** | Linguagem da aplicação |
| **Spring Boot 3.5.7** | Configuração e execução da API |
| **Spring Web** | Criação dos endpoints REST |
| **Spring Data JPA** | Persistência e consultas |
| **Hibernate** | Mapeamento objeto-relacional |
| **Spring Security** | Autenticação e autorização |
| **Spring Authorization Server** | Emissão de tokens OAuth 2.0 |
| **Spring OAuth2 Resource Server** | Validação de tokens JWT nas requisições |
| **Bean Validation** | Validação de dados de entrada |
| **H2 Database** | Banco em memória para o perfil atual |
| **Maven** | Dependências e build |
| **Spring Boot Test** | Testes automatizados |

## Autenticação e segurança

A aplicação configura dois papéis do Spring Security: um **Authorization Server**, responsável por autenticar credenciais e emitir tokens, e um **Resource Server**, que valida os tokens recebidos e disponibiliza as informações de autenticação para proteger as rotas.

### Fluxo de autenticação

1. O cliente envia suas credenciais OAuth (`CLIENT_ID` e `CLIENT_SECRET`) ao endpoint de token do Authorization Server.
2. No mesmo pedido, envia o grant `password`, o e-mail e a senha do usuário.
3. O servidor busca o usuário e seus perfis, compara a senha com o hash armazenado usando BCrypt e, se as credenciais forem válidas, emite um access token JWT.
4. O JWT inclui as claims `username` e `authorities`, além das informações padrão de emissão e expiração.
5. Nas chamadas seguintes, o cliente envia o token como `Bearer`. O Resource Server valida a assinatura e a validade do JWT; as regras de acesso dos endpoints verificam os perfis do usuário.

O projeto implementa o grant `password` por meio de um conversor e um provider customizados. Esse grant transmite a senha do usuário ao Authorization Server e é considerado legado pelo OAuth 2.0 Security Best Current Practice; não é recomendado para novos sistemas ou clientes públicos. A implementação está aqui para fins de estudo e compatibilidade com o fluxo configurado no projeto. Em produção, prefira um fluxo moderno, como Authorization Code com PKCE, e transporte credenciais somente por HTTPS.

### Solicitar um token

O endpoint de token padrão do Spring Authorization Server é `/oauth2/token`. Exemplo de chamada usando formulário URL-encoded e autenticação HTTP Basic do cliente:

```bash
curl -X POST http://localhost:8080/oauth2/token \
  -u "${CLIENT_ID}:${CLIENT_SECRET}" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "username=usuario@exemplo.com" \
  -d "password=sua-senha"
```

A resposta contém o `access_token` e sua duração. Use o token nas rotas protegidas:

```bash
curl http://localhost:8080/users/me \
  -H "Authorization: Bearer <access_token>"
```

O cliente OAuth registrado aceita os escopos `read` e `write`. O token tem duração configurável por `JWT_DURATION` (padrão: `86400` segundos).

### Regras de acesso

| Recurso | Regra |
| --- | --- |
| Consulta de produtos e categorias | Pública |
| Criar, atualizar ou excluir produtos | Requer perfil `ADMIN` |
| Criar pedido | Requer perfil `CLIENT` |
| Consultar pedido | Requer perfil `CLIENT` ou `ADMIN` |
| Consultar `/users/me` | Requer perfil `CLIENT` ou `ADMIN` |

O projeto também tem uma validação de serviço para restringir o acesso a um usuário ao próprio registro ou a um administrador. As origens CORS permitidas são configuráveis por `CORS_ORIGINS`.

### Observações para produção

A configuração atual é voltada ao desenvolvimento e estudo, não a um ambiente de produção:

- O cliente OAuth, as autorizações e os consentimentos são guardados em memória; reiniciar a aplicação os perde.
- O par de chaves RSA usado para assinar os JWTs é gerado na inicialização. Tokens deixam de ser verificáveis após um reinício, e instâncias diferentes não compartilham a mesma chave.
- O perfil `test` e o H2 em memória estão ativos por padrão.
- `CLIENT_ID` e `CLIENT_SECRET` têm valores padrão para uso local. Configure valores fortes fora do repositório, proteja segredos e use HTTPS.
- O grant `password` deve ser substituído por um fluxo OAuth 2.0 atual antes de expor a aplicação a clientes reais.

## Como executar

### Pré-requisitos

- JDK 17 ou superior
- Git

O Maven Wrapper já está incluído no repositório, então não é necessário instalar o Maven separadamente.

### 1. Clone o projeto

```bash
git clone https://github.com/lutheone/ecommerce-api.git
cd ecommerce-api
```

### 2. Inicie a aplicação

Linux ou macOS:

```bash
./mvnw spring-boot:run
```

Windows (PowerShell ou Prompt de Comando):

```bat
mvnw.cmd spring-boot:run
```

A API inicia em `http://localhost:8080`.

### Executar os testes

Linux ou macOS:

```bash
./mvnw test
```

Windows:

```bat
mvnw.cmd test
```

## Endpoints

Os caminhos abaixo são relativos à base `http://localhost:8080`.

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `POST` | `/oauth2/token` | Cliente OAuth | Emite um token de acesso |
| `GET` | `/products` | Público | Lista produtos paginados; aceita filtro por `name` |
| `GET` | `/products/{id}` | Público | Busca um produto pelo ID |
| `POST` | `/products` | `ADMIN` | Cadastra um produto |
| `PUT` | `/products/{id}` | `ADMIN` | Atualiza um produto |
| `DELETE` | `/products/{id}` | `ADMIN` | Remove um produto |
| `GET` | `/categories` | Público | Lista as categorias |
| `POST` | `/orders` | `CLIENT` | Cria um pedido |
| `GET` | `/orders/{id}` | `CLIENT` ou `ADMIN` | Consulta um pedido |
| `GET` | `/users/me` | `CLIENT` ou `ADMIN` | Consulta o usuário autenticado |

### Exemplos de consulta

Listar produtos com paginação:

```http
GET /products?page=0&size=12&sort=name,asc
```

Filtrar produtos pelo nome:

```http
GET /products?name=computador&page=0&size=12
```

Rotas protegidas recebem o token no cabeçalho `Authorization`:

```http
Authorization: Bearer <seu-token>
```

## Configuração

A aplicação ativa o perfil `test` por padrão, que usa o H2 em memória. As configurações estão em `src/main/resources/application.properties` e `application-test.properties`.

| Variável | Valor padrão | Descrição |
| --- | --- | --- |
| `CLIENT_ID` | `myclientid` | Identificador do cliente OAuth |
| `CLIENT_SECRET` | `myclientsecret` | Segredo do cliente OAuth |
| `JWT_DURATION` | `86400` | Duração do token em segundos |
| `CORS_ORIGINS` | `http://localhost:3000,http://localhost:5173` | Origens permitidas para CORS |

Para desenvolvimento local, o console do H2 fica disponível em `http://localhost:8080/h2-console` usando a URL JDBC `jdbc:h2:mem:testdb`.

## Organização do código

O código principal fica em `src/main/java/com/lutheone/dscommerce` e está separado por responsabilidades:

- `controllers`: endpoints HTTP
- `services`: regras de negócio
- `repositories`: acesso aos dados
- `entities`: entidades persistidas
- `dto`: objetos de entrada e saída da API
- `config`: configurações de segurança e da aplicação, incluindo o Authorization Server, o Resource Server e o grant customizado

O fluxo de uma requisição segue a estrutura `Controller → Service → Repository → Banco de dados`.

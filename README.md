# JWT Code Challenge
Essa aplicação disponibiliza um endpoint que recebe e valida um JWT de acordo com os seguintes critérios:
-   Deve ser um JWT válido
-   Deve conter apenas 3 claims (Name, Role e Seed)
-   A claim Name não pode ter carácter de números
-   A claim Role deve conter apenas 1 dos três valores (Admin, Member e External)
-   A claim Seed deve ser um número primo.
-   O tamanho máximo da claim Name é de 256 caracteres.

## Endpoint
```
GET-> localhost:8080/api/v1/auth
```
### Entrada:
|  Nome          |Tipo                           |Valor de exemplo                         |
|----------------|-------------------------------|-----------------------------|
|Authorization   |Header                         |eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg          |

### Saída:
Caso de sucesso:
Http Status 200 - OK
```json
{
  "valid": true
}
```
Caso de JWT inválido:
Http Status 401- UNAUTHORIZED
```json
{
  "valid": false
}
```
# Requisitos
- JDK 21
- Maven 3

# Como rodar o projeto?
```
mvn clean spring-boot:run
```
**Ou** para execução via Docker:
```
mvn clean package
docker build -t jwt-code-challenge .
docker run -p 8080:8080 jwt-code-challenge
```
Dessa forma a aplicação subirá e o endpoint ficará disponível na porta **8080**.

## Execução dos testes
```
mvn clean test
```
**Ou** para rodar com relatório do JaCoCo e testes de mutação:
```
mvn clean test org.pitest:pitest-maven:mutationCoverage jacoco:report
```

## Collections dos Casos de teste
Para facilitar a execução dos casos de teste, uma collection do Insomnia foi disponibilizada no arquivo **insomnia-collection** localizado na raiz do projeto. Esse arquivo deve ser importado na ferramenta.

# Descrição dos métodos
O projeto está em uma arquitetura MVC simples. A classe **AuthServiceImpl.java** faz o parse do JWT para obter as Claims e realiza a validação dos critérios pedidos a partir do método **authenticate**. Caso ocorra alguma Exception o erro é logado e a API retorna o token como inválido.

# Tratamento de Exceções
Utilizei o ``@ControllerAdvice`` do Spring para realizar o tratamento de exceções da aplicação. Ela possui Handler genérico para tratar ``Exception.class`` impedindo que alguma exceção inesperada na aplicação retorne para o cliente. Assim como adicionei alguns handlers específicos para erros comuns de validação na API, como por exemplo quando o header Authorization não é enviado, esse erro vai ser tratado e uma resposta customizada será retornada na API, impedindo o retorno do stacktrace, como é o padrão nesse caso.


## Premissas assumidas
Observei que nos cenários de teste não foram passados JWT's assinados. Então parametrizei na aplicação uma variável de ambiente chamada **JWT_SIGNED** que caso receba true, apenas aceita JWT's assinados. O valor default é **false**.

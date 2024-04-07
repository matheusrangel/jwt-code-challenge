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

## Swagger UI
A documentação da API também está disponível via Swagger UI. Ao executar a aplicação, acesse no seu browser a página `http://localhost:8080/swagger-ui/index.html`.
> Nessa versão do Swagger, ao tentar usar o a função 'Try it out', ele não passa o header Authorization, mesmo incluindo algum valor. Para realizar os testes então peço que use a collection do Insomnia.

# Descrição dos métodos
O projeto está em uma arquitetura MVC simples. A classe **AuthServiceImpl.java** faz o parse do JWT para obter as Claims e realiza a validação dos critérios pedidos a partir do método **authenticate**. Caso ocorra alguma Exception o erro é logado e a API retorna o token como inválido.

***authenticate*** -> Método principal declarado na Interface ``AuthService``. Na sua implementação ``AuthServiceImpl``, ele é responsável por validar um JWT e retornar um booleano que informa se é válido ou não.

***parseToken*** -> Recebe a string JWT e caso a aplicação esteja configurado para validar JWT's assinados, chama o método ``parseSignedJwt``. Caso não esteja, chama o método ``parseUnsignedJwt``.

***parseSignedJwt*** -> Utilizando a a biblioteca jjwt, realiza o parse do JWT utilizando a chave contida no arquivo ``application.yml`` (ou via variável de ambiente). Realizando o parse, retorna as Claims contidas no JWT.

***parseUnsignedJwt*** -> Dado que o padrão JWT do formato do token ser ``header.payload.signature``, ele remove a parte de assinatura da string e realizar o parse do JWT utilizando a biblioteca jjwt e retorna as Claims obtidas. **OBSERVAÇÃO** - *Essa é uma prática não segura e só foi utilizada devido aos cenários de teste.*

***validateClaims*** -> Recebe as Claims do JWT e valida todas os critérios chamando o método de validação específico de cada critério.

***hasRequiredClaims*** -> Recebe as Claims e valida se contém apenas as Claims **Role, Name e Seed**. Caso não, loga e retorna false.

***validateNameClaim*** -> Recebe as Claims e valida se a Claim Name possui até 256 caracteres e utilizando Regex valida se não contém números. Caso negativo, loga e retorna false.

***validateRoleClaim*** -> Recebe as Claims e valida se a Claim Role está na lista de roles permitidas (Admin,Member,External). Caso não esteja, loga e retorna false. OBSERVAÇÃO: A lista de roles é trazida via propriedades da aplicação e pode ser recebida via variável de ambiente.

***validateSeedClaim*** -> Recebe as Claims e valida se a Claim Seed é um número primo atráves do método ``isPrime``. Caso não seja, loga e retorna false.

***isPrime*** -> Recebe um inteiro e retorna se é um número primo.

# Logging
O pacote observability contém 2 classes. A classe `RequestResponseLogInterceptor` implementa uma inteface de interceptadores servlet do Spring Web. Nesse interceptador implementei os métodos `preHandle` e `afterCompletion` que me dão acesso ao request e response ao chegarem e sairem da aplicação. Usei estes métodos para logar as requests e responses. Todos os logs da aplicação possuem spanId e traceId gerados pela lib *micrometer-tracing-bridge-brave*.

A classe `WebMvcConfig` é um Bean de configuração que dá acesso ao método `addInterceptors`, onde adicionei o interceptador customizado para logs.

# Tratamento de Exceções
Utilizei o ``@ControllerAdvice`` do Spring para realizar o tratamento de exceções da aplicação. Ela possui Handler genérico para tratar ``Exception.class`` impedindo que alguma exceção inesperada na aplicação retorne para o cliente. Assim como adicionei alguns handlers específicos para erros comuns de validação na API, como por exemplo quando o header Authorization não é enviado, esse erro vai ser tratado e uma resposta customizada será retornada na API, impedindo o retorno do stacktrace, como é o padrão nesse caso.


## Premissas assumidas
Observei que nos casos de teste não foram passados JWT's assinados corretamente. Então parametrizei na aplicação uma variável de ambiente chamada **JWT_SIGNED** que caso receba true, apenas aceita JWT's assinados. O valor default é **false**.

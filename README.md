# tinka
Java implementation for Tinkoff Invest Rest API: https://tinkoffcreditsystems.github.io/invest-openapi/rest/

## Requirements

- JDK 11

## Configuring
Add your sandbox and/or exchange tokens in the `application.yaml`:

```
client:
  useSandbox: true
  sandboxToken: 1122334455667788.AAbbCCdd
  exchangeToken: ddEEffGG.0011223344556677
```

## Build
Run `mvnw clean package`

## API
After build will be available the components (Spring):
```
- MarketApi
- OperationsApi
- OrdersApi
- PortfolioApi
- SandboxApi
- UserApi
```

## Examples
You can find examples of using the API in the `MarketApiExample` class and in tests.
To run the `MarketApiExample` use the command `mvnw spring-boot:run` or execute the application from your IDE.
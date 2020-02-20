# tinka
Java implementation for Tinkoff Invest Rest API: https://tinkoffcreditsystems.github.io/invest-openapi/rest/

###

![compile](https://github.com/0x100/tinka/workflows/compile/badge.svg?branch=master)
![compile](https://img.shields.io/github/last-commit/0x100/tinka)

## Requirements

- JDK 11

## Configuring
Add your sandbox and/or exchange tokens in the `application.yaml`:

```
tinkoff:
  invest:
    client:
      useSandbox: true
      sandboxToken: t.123456
      exchangeToken: t.654321
```

## Build
Run `$ mvnw clean package`

## API
After compiling will be available next components (Spring):
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
To run the `MarketApiExample` use the command `$ mvnw spring-boot:run` or execute the application from your IDE.

## How to contribute
Fork the repository, make changes, write a test for your code, send me a pull request. 
I will review your changes and apply them to the master branch shortly, provided they don't violate quality standards. 
To avoid frustration, before sending a pull request please run the Maven build:
```
$ mvnw clean package
```
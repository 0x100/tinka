# tinka
Java client for Tinkoff Invest Rest API: https://tinkoffcreditsystems.github.io/invest-openapi/rest/

###

![build](https://github.com/0x100/tinka/workflows/build/badge.svg?branch=master)
![compile](https://img.shields.io/github/last-commit/0x100/tinka)

## Requirements

- JDK 11

## Configuring
Set your sandbox or exchange token in the `application.yaml` config file, enable or disable a `sandbox` mode:

```
tinkoff:
  invest:
    client:
      sandboxMode: true
      token: t.123456
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

## Indicators

Implemented technical indicators:
- RSI (Relative Strength Index)
- CCI (Commodity Channel Index)
- Williams %R
- Momentum

## Examples
You can find examples of using the API in the `MarketApiExample` class and in tests.
To run the `MarketApiExample` use the command `$ mvnw spring-boot:run` or execute the application from your IDE.

## Logging
To enable logging of http requests apply this config: 

```
logging:
  level:
    ru.ilysenko.tinka: DEBUG
```

## How to contribute
Fork the repository, make changes, write a test for your code, send me a pull request. 
I will review your changes and apply them to the master branch shortly, provided they don't violate quality standards. 
To avoid frustration, before sending a pull request please run the Maven build:
```
$ mvnw clean package
```

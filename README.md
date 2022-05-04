# tinka
Java client for Tinkoff Invest Rest API: https://tinkoff.github.io/investAPI/swagger-ui/

###

![build](https://github.com/0x100/tinka/workflows/build/badge.svg?branch=master)
![compile](https://img.shields.io/github/last-commit/0x100/tinka)

DISCLAIMER: You may use this client only at your own risk. 
You're yourself accept liability for any profit or loss that may arise directly or indirectly from using this client.

## Requirements

- JDK 8 or later

## Configuring
Set your sandbox or exchange token in the `application.yaml` config file:

```
tinkoff:
  invest:
    client:
      token: {your_token_here}
```

## Build
Run `$ mvn clean package`

## API
After compiling the project next components will be available (Spring beans):
```
- InstrumentsServiceApi
- MarketDataServiceApi
- OperationsServiceApi
- OrdersServiceApi
- SandboxServiceApi
- StopOrdersServiceApi
- UsersServiceApi
```

## Indicators

Implemented technical indicators for using withing the API:
- RSI (Relative Strength Index)
- CCI (Commodity Channel Index)
- Williams %R
- DPO (Detrended price oscillator)
- Momentum

## Examples
You can find examples of using the API in the `MarketApiExample` class and in tests.
To run the `MarketApiExample` use the command `$ mvn spring-boot:run` or execute the application from your IDE.

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
$ mvn clean package
```

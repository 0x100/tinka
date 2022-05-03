/*
 * The GNU Affero General Public License v3.0 (AGPL-3.0)
 *
 * Copyright (c) 2020 Ilya Lysenko
 *
 * Permissions of this strongest copyleft license are conditioned on making available complete source code of licensed
 * works and modifications, which include larger works using a licensed work, under the same license.
 * Copyright and license notices must be preserved. Contributors provide an express grant of patent rights.
 * When a modified version is used to provide a service over a network,  the complete source code of the modified
 * version must be made available.
 */
package ru.ilysenko.tinka;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.threeten.bp.OffsetDateTime;
import ru.ilysenko.tinka.config.ApiClientProperties;
import ru.ilysenko.tinka.helper.MarketApiHelper;
import ru.ilysenko.tinka.model.Ticker;
import ru.tinkoff.invest.api.InstrumentsServiceApi;
import ru.tinkoff.invest.api.MarketDataServiceApi;
import ru.tinkoff.invest.api.SandboxServiceApi;
import ru.tinkoff.invest.model.V1Account;
import ru.tinkoff.invest.model.V1CandleInterval;
import ru.tinkoff.invest.model.V1CloseSandboxAccountRequest;
import ru.tinkoff.invest.model.V1CurrenciesResponse;
import ru.tinkoff.invest.model.V1Currency;
import ru.tinkoff.invest.model.V1GetAccountsRequest;
import ru.tinkoff.invest.model.V1GetAccountsResponse;
import ru.tinkoff.invest.model.V1GetCandlesRequest;
import ru.tinkoff.invest.model.V1GetCandlesResponse;
import ru.tinkoff.invest.model.V1GetOrdersRequest;
import ru.tinkoff.invest.model.V1GetOrdersResponse;
import ru.tinkoff.invest.model.V1GetTradingStatusRequest;
import ru.tinkoff.invest.model.V1GetTradingStatusResponse;
import ru.tinkoff.invest.model.V1HistoricCandle;
import ru.tinkoff.invest.model.V1InstrumentStatus;
import ru.tinkoff.invest.model.V1InstrumentsRequest;
import ru.tinkoff.invest.model.V1OpenSandboxAccountRequest;
import ru.tinkoff.invest.model.V1Operation;
import ru.tinkoff.invest.model.V1OperationState;
import ru.tinkoff.invest.model.V1OperationsRequest;
import ru.tinkoff.invest.model.V1OperationsResponse;
import ru.tinkoff.invest.model.V1OrderState;
import ru.tinkoff.invest.model.V1PortfolioPosition;
import ru.tinkoff.invest.model.V1PortfolioRequest;
import ru.tinkoff.invest.model.V1PortfolioResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
@EnabledIf(value = "#{'${spring.profiles.active}' == 'sandbox'}", loadContext = true)
public class ApiClientTest {

    @Autowired
    private MarketDataServiceApi marketApi;

    @Autowired
    private InstrumentsServiceApi instrumentsApi;

    @Autowired
    private SandboxServiceApi sandboxApi;

    @Autowired
    private MarketApiHelper marketApiHelper;

    @Autowired
    private ApiClientProperties properties;

    private ThreadLocal<String> accountId;


    @BeforeEach
    public void before() {
        assertNotNull(properties.getToken());
        accountId = ThreadLocal.withInitial(() -> sandboxApi.sandboxServiceOpenSandboxAccount(new V1OpenSandboxAccountRequest()).getAccountId());
    }

    @AfterEach
    public void after() {
        sandboxApi.sandboxServiceCloseSandboxAccount(new V1CloseSandboxAccountRequest().accountId(getAccountId()));
    }

    @Test
    public void getFigi() {
        Ticker ticker = Ticker.APPLE;
        String figi = marketApiHelper.getFigi(ticker);

        assertNotNull(figi);
        log.info("Figi for the ticker " + ticker + " is " + figi);
    }

    @Test
    public void getCandles() {
        String figi = marketApiHelper.getFigi(Ticker.APPLE);

        OffsetDateTime now = OffsetDateTime.now();
        V1GetCandlesRequest request = new V1GetCandlesRequest()
                .figi(figi)
                .from(now.minusDays(10))
                .to(now)
                .interval(V1CandleInterval.DAY);

        V1GetCandlesResponse response = marketApi.marketDataServiceGetCandles(request);
        assertNotNull(response);

        List<V1HistoricCandle> candles = response.getCandles();
        assertNotNull(candles);
        assertFalse(candles.isEmpty());
    }

    @Test
    public void getUserAccounts() {
        V1GetAccountsResponse response = sandboxApi.sandboxServiceGetSandboxAccounts(new V1GetAccountsRequest());
        assertNotNull(response);

        List<V1Account> accounts = response.getAccounts();
        assertNotNull(accounts);
    }

    @Test
    public void getPortfolio() {
        V1PortfolioRequest request = new V1PortfolioRequest().accountId(getAccountId());
        V1PortfolioResponse response = sandboxApi.sandboxServiceGetSandboxPortfolio(request);
        assertNotNull(response);

        List<V1PortfolioPosition> positions = response.getPositions();
        assertNotNull(positions);
    }

    @Test
    public void getCurrencies() {
        V1InstrumentsRequest request = new V1InstrumentsRequest().instrumentStatus(V1InstrumentStatus.BASE);
        V1CurrenciesResponse response = instrumentsApi.instrumentsServiceCurrencies(request);
        assertNotNull(response);

        List<V1Currency> currencies = response.getInstruments();
        assertNotNull(currencies);
        assertFalse(currencies.isEmpty());
    }

    @Test
    public void getOrders() {
        V1GetOrdersRequest request = new V1GetOrdersRequest().accountId(getAccountId());
        V1GetOrdersResponse response = sandboxApi.sandboxServiceGetSandboxOrders(request);
        assertNotNull(response);

        List<V1OrderState> orders = response.getOrders();
        assertNotNull(orders);
    }

    @Test
    public void getOperations() {
        String figi = marketApiHelper.getFigi(Ticker.TESLA);
        OffsetDateTime now = OffsetDateTime.now();
        V1OperationsRequest request = new V1OperationsRequest()
                .figi(figi)
                .from(now.minusMonths(1))
                .to(now)
                .accountId(getAccountId())
                .state(V1OperationState.UNSPECIFIED);
        V1OperationsResponse response = sandboxApi.sandboxServiceGetSandboxOperations(request);
        assertNotNull(response);

        List<V1Operation> operations = response.getOperations();
        assertNotNull(operations);
        assertFalse(operations.isEmpty());
    }

    @Test
    public void getTradingStatus() {
        String figi = "BBG000N9MNX3";
        V1GetTradingStatusResponse response = marketApi.marketDataServiceGetTradingStatus(new V1GetTradingStatusRequest().figi(figi));
        assertNotNull(response);
        assertNotNull(response.getTradingStatus());
    }

    private String getAccountId() {
        return accountId.get();
    }
}

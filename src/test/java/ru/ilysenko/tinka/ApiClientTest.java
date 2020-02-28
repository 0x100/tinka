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
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.threeten.bp.OffsetDateTime;
import ru.ilysenko.tinka.config.ApiClientProperties;
import ru.ilysenko.tinka.helper.MarketApiHelper;
import ru.ilysenko.tinka.model.Ticker;
import ru.tinkoff.invest.api.MarketApi;
import ru.tinkoff.invest.api.OperationsApi;
import ru.tinkoff.invest.api.OrdersApi;
import ru.tinkoff.invest.api.PortfolioApi;
import ru.tinkoff.invest.api.SandboxApi;
import ru.tinkoff.invest.api.UserApi;
import ru.tinkoff.invest.model.Candle;
import ru.tinkoff.invest.model.CandleResolution;
import ru.tinkoff.invest.model.Candles;
import ru.tinkoff.invest.model.CandlesResponse;
import ru.tinkoff.invest.model.Currencies;
import ru.tinkoff.invest.model.CurrencyPosition;
import ru.tinkoff.invest.model.Empty;
import ru.tinkoff.invest.model.Operations;
import ru.tinkoff.invest.model.OperationsResponse;
import ru.tinkoff.invest.model.Order;
import ru.tinkoff.invest.model.OrdersResponse;
import ru.tinkoff.invest.model.Portfolio;
import ru.tinkoff.invest.model.PortfolioCurrenciesResponse;
import ru.tinkoff.invest.model.PortfolioResponse;
import ru.tinkoff.invest.model.UserAccount;
import ru.tinkoff.invest.model.UserAccounts;
import ru.tinkoff.invest.model.UserAccountsResponse;

import java.util.List;

import static java.util.Optional.ofNullable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeNotNull;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class ApiClientTest {
    private static final String SANDBOX_TOKEN_STUB = "t.123456";

    @Autowired
    private MarketApi marketApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private PortfolioApi portfolioApi;

    @Autowired
    private OrdersApi ordersApi;

    @Autowired
    private OperationsApi operationsApi;

    @Autowired
    private SandboxApi sandboxApi;

    @Autowired
    private MarketApiHelper marketApiHelper;

    @Autowired
    private ApiClientProperties properties;

    @Before
    public void setUp() {
        assumeNotNull(properties.getSandboxToken());
        assumeFalse(SANDBOX_TOKEN_STUB.equals(properties.getSandboxToken()));
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
        String figi = marketApiHelper.getFigi(Ticker.GOOGLE);
        CandlesResponse response = marketApi.marketCandlesGet(figi, OffsetDateTime.now().minusDays(10), OffsetDateTime.now(), CandleResolution.DAY);
        assertNotNull(response);

        Candles payload = response.getPayload();
        assertNotNull(payload);

        List<Candle> candles = payload.getCandles();
        assertNotNull(candles);
        assertFalse(candles.isEmpty());
    }

    @Test
    public void getUserAccounts() {
        UserAccountsResponse userAccounts = userApi.userAccountsGet();
        assertNotNull(userAccounts);

        UserAccounts payload = userAccounts.getPayload();
        assertNotNull(payload);

        List<UserAccount> accounts = payload.getAccounts();
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
    }

    @Test
    public void getPortfolio() {
        PortfolioResponse portfolio = portfolioApi.portfolioGet(getBrokerAccountId());
        assertNotNull(portfolio);

        Portfolio payload = portfolio.getPayload();
        assertNotNull(payload);
        assertNotNull(payload.getPositions());
    }

    @Test
    public void getCurrencies() {
        PortfolioCurrenciesResponse response = portfolioApi.portfolioCurrenciesGet(getBrokerAccountId());
        assertNotNull(response);

        Currencies payload = response.getPayload();
        assertNotNull(payload);

        List<CurrencyPosition> currencies = payload.getCurrencies();
        assertNotNull(currencies);
        assertFalse(currencies.isEmpty());
    }

    @Test
    public void getOrders() {
        OrdersResponse response = ordersApi.ordersGet(getBrokerAccountId());
        assertNotNull(response);

        List<Order> payload = response.getPayload();
        assertNotNull(payload);
    }

    @Test
    public void getOperations() {
        OffsetDateTime from = OffsetDateTime.now().minusMonths(1);
        OffsetDateTime to = OffsetDateTime.now();
        String figi = marketApiHelper.getFigi(Ticker.FACEBOOK);
        String brokerAccountId = getBrokerAccountId();

        OperationsResponse response = operationsApi.operationsGet(from, to, figi, brokerAccountId);
        assertNotNull(response);

        Operations payload = response.getPayload();
        assertNotNull(payload);
        assertNotNull(payload.getOperations());
    }

    @Test
    @Ignore
    public void deleteAllPositions() {
        Empty response = sandboxApi.sandboxClearPost(getBrokerAccountId());
        assertEquals("Ok", response.getStatus());
    }


    private String getBrokerAccountId() {
        return ofNullable(userApi.userAccountsGet())
                .map(UserAccountsResponse::getPayload)
                .map(UserAccounts::getAccounts)
                .flatMap(a -> a.stream().findFirst())
                .orElseThrow(() -> new RuntimeException("User account not found"))
                .getBrokerAccountId();
    }
}

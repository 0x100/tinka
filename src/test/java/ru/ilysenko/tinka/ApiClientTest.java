package ru.ilysenko.tinka;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.threeten.bp.OffsetDateTime;
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
import ru.tinkoff.invest.model.MarketInstrument;
import ru.tinkoff.invest.model.MarketInstrumentList;
import ru.tinkoff.invest.model.MarketInstrumentListResponse;
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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApiClientTest {

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

    @Test
    public void getFigi() {
        String ticker = Ticker.SBERBANK.getValue();
        String figi = getFigi(ticker);

        assertNotNull(figi);
        log.info("Figi for the ticker " + ticker + " is " + figi);
    }

    @Test
    public void getCandles() {
        String figi = getFigi(Ticker.APPLE.getValue());
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
        String figi = getFigi(Ticker.SBERBANK.getValue());
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

    private String getFigi(String ticker) {
        return getMarketInstruments(ticker).stream()
                .filter(instrument -> Objects.equals(ticker, instrument.getTicker()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ticker not found"))
                .getFigi();
    }

    private List<MarketInstrument> getMarketInstruments(String ticker) {
        return ofNullable(marketApi.marketSearchByTickerGet(ticker))
                .map(MarketInstrumentListResponse::getPayload)
                .map(MarketInstrumentList::getInstruments)
                .orElse(Collections.emptyList());
    }
}

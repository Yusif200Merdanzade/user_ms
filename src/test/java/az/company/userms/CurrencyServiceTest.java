package az.company.userms;

import az.company.userms.service.CurrencyRateProvider;
import az.company.userms.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

@SpringBootTest
public class CurrencyServiceTest {

    @InjectMocks
    private CurrencyService currencyService;

    @MockBean
    private CurrencyRateProvider currencyRateProvider;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyService = spy(currencyService);
    }

    @Test
    public void getCurrencyRate_firstTime_fetchFromProvider() {
        String fromCurrency = "USD";
        String toCurrency = "AZN";
        String currencyPair = fromCurrency + "/" + toCurrency;
        double expectedRate = 1.7;

        Mockito.when(currencyRateProvider.fetchCurrencyRate(currencyPair)).thenReturn(expectedRate);

        double actualRate = currencyService.getCurrencyRate(fromCurrency, toCurrency);

        assertEquals(expectedRate, actualRate, 0.0001);
        Mockito.verify(currencyRateProvider, Mockito.times(1)).fetchCurrencyRate(currencyPair);
    }

    @Test
    public void getCurrencyRate_withinExpirationTime_fetchFromCache() {
        String fromCurrency = "USD";
        String toCurrency = "AZN";
        String currencyPair = fromCurrency + "/" + toCurrency;
        double expectedRate = 1.7;

        Mockito.when(currencyRateProvider.fetchCurrencyRate(currencyPair)).thenReturn(expectedRate);

        currencyService.getCurrencyRate(fromCurrency, toCurrency);

        double actualRate = currencyService.getCurrencyRate(fromCurrency, toCurrency);

        assertEquals(expectedRate, actualRate, 0.0001);
        Mockito.verify(currencyRateProvider, Mockito.times(1)).fetchCurrencyRate(currencyPair);
    }

    @Test
    public void getCurrencyRate_afterExpirationTime_fetchFromProviderAgain() throws InterruptedException {
        String fromCurrency = "USD";
        String toCurrency = "AZN";
        String currencyPair = fromCurrency + "/" + toCurrency;
        double firstRate = 1.7;
        double secondRate = 1.8;

        Mockito.when(currencyRateProvider.fetchCurrencyRate(currencyPair)).thenReturn(firstRate);

        currencyService.getCurrencyRate(fromCurrency, toCurrency);

        Mockito.when(currencyRateProvider.fetchCurrencyRate(currencyPair)).thenReturn(secondRate);

        Thread.sleep(60000);

        double actualRate = currencyService.getCurrencyRate(fromCurrency, toCurrency);

        assertEquals(secondRate, actualRate, 0.0001);
        Mockito.verify(currencyRateProvider, Mockito.times(2)).fetchCurrencyRate(currencyPair);
    }


}

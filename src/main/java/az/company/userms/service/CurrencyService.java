package az.company.userms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRateCache cache;

    public double getCurrencyRate(String fromCurrency, String toCurrency) {
        String currencyPair = fromCurrency + "/" + toCurrency;
        return cache.getCurrencyRate(currencyPair);
    }
}

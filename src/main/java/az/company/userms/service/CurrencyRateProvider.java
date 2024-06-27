package az.company.userms.service;

import org.springframework.stereotype.Service;

@Service
public class CurrencyRateProvider {

    public double fetchCurrencyRate(String currencyPair) {
        switch (currencyPair) {
            case "USD/AZN":
                return 1.7;
            case "AZN/TL":
                return 8.0;
            default:
                throw new IllegalArgumentException("Unknown currency pair");
        }
    }


}

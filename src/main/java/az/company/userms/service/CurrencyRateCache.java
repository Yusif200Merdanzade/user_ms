package az.company.userms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CurrencyRateCache {

    private Map<String, Double> cache = new ConcurrentHashMap<>();
    private Map<String, Long> timestamps = new ConcurrentHashMap<>();
    private static final long EXPIRATION_TIME = 60000;

    @Autowired
    private CurrencyRateProvider provider;

    public double getCurrencyRate(String currencyPair) {
        if (cache.containsKey(currencyPair) && (System.currentTimeMillis() - timestamps.get(currencyPair) < EXPIRATION_TIME)) {
            return cache.get(currencyPair);
        }
        double rate = provider.fetchCurrencyRate(currencyPair);
        cache.put(currencyPair, rate);
        timestamps.put(currencyPair, System.currentTimeMillis());
        return rate;
    }
}

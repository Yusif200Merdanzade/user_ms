package az.company.userms.controller;

import az.company.userms.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/currency")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/currency-rate")
    public  Double  getCurrencyRate(@RequestParam String fromCurrency, @RequestParam String toCurrency) {
        return currencyService.getCurrencyRate(fromCurrency, toCurrency);

    }
}

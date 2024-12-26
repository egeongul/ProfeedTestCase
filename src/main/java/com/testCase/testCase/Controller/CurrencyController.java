package com.testCase.testCase.Controller;

import com.testCase.testCase.Model.Currency;
import com.testCase.testCase.Repository.CurrencyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CurrencyController {

    @Autowired
    private CurrencyRepo currRepo;

    @Value("${currencylayer.api.url}")
    private String apiUrl;
    @Value("${fixer.api.url}")
    private String apiUrl2;

    @Value("${currencylayer.access.key}")
    private String accessKey;
    @Value("${fixer.access.key}")
    private String accessKey2;


    @GetMapping("/currencies/USD") //returns all records of USD at the same time
    public List<Currency> getCurrenciesInUSD(@RequestParam String source) {
        return currRepo.findByTypeAndSource("USD", source);
    }

    @GetMapping("/currencies/EUR") //returns all records of EUR at the same time
    public List<Currency> getCurrenciesInEUR(@RequestParam String source) {
        return currRepo.findByTypeAndSource("EUR", source);
    }

    @GetMapping("/lastRates/{currency}") //returns the lates recorded USD or EUR based on currency field
    public Currency getLatestCurrencyRate(@PathVariable String currency) {
        List<Currency> currencies = currRepo.findByTypeOrderByDateDesc(currency);

        if (currencies.isEmpty()) {
            throw new RuntimeException("No records found for currency: " + currency);
        }
        return currencies.get(0);
    }

    @GetMapping("/currencies/pagination/USD")
    public Page<Currency> getCurrenciesInUSDWithPagination(
            @RequestParam String source,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return currRepo.findByTypeAndSource("USD", source, pageable);
    }

    @GetMapping("/currencies/pagination/EUR")
    public Page<Currency> getCurrenciesInEURWithPagination(
            @RequestParam String source,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return currRepo.findByTypeAndSource("EUR", source, pageable);
    }

}

package com.testCase.testCase.Controller;

import com.testCase.testCase.Model.Currency;
import com.testCase.testCase.Model.CurrentLayerResponse;
import com.testCase.testCase.Model.CurrentLayerResponseSource2;
import com.testCase.testCase.Repository.CurrencyRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class MainController {
    @Autowired
    CurrencyRepo currRepo;

    @PostMapping("/addCurrency")
    public void addCurrency(@RequestBody Currency currency) {
        currRepo.save(currency);
    }

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("/fetchCurrencies")
    public List<Currency> fetchCurrencies(@RequestParam String apiUrl, @RequestParam String accessKey) {

        String url = apiUrl
                + "?access_key=" + accessKey
                + "&currencies=" + "USD,EUR"
                + "&source=" + "TRY"
                + "&format=" + "1";

        CurrentLayerResponse response = restTemplate.getForObject(url, CurrentLayerResponse.class);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String currentDate = now.format(formatter);

        List<Currency> currenciesList = new ArrayList<>();
        if (response != null && response.isSuccess()) {
            for (Map.Entry<String, Double> entry : response.getQuotes().entrySet()) {
                String targetCurrency = entry.getKey().substring(3);
                double rate = entry.getValue();
                Currency currencyToBeAdded = new Currency(targetCurrency, "CurrencyLayer", currentDate, 1/rate, 1/rate);
                addCurrency(currencyToBeAdded);
                currenciesList.add(currencyToBeAdded);
                System.out.println("Currencies added for currencyLayer: " + currencyToBeAdded);
            }
        }

        return currenciesList;
    }


    @GetMapping("/fetchCurrenciesSource2")
    public List<Currency> fetchCurrenciesSource2(@RequestParam String apiUrl, @RequestParam String accessKey) {

        String url = apiUrl + "?access_key=" + accessKey;

        CurrentLayerResponseSource2 response = restTemplate.getForObject(url, CurrentLayerResponseSource2.class);

        List<Currency> currenciesList = new ArrayList<>();

        if (response != null && response.isSuccess()) {

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String hourAndMinutes = now.format(formatter);

            String baseCurrency = response.getBase();
            Map<String, Double> rates = response.getRates();

            Double rateEUR_TO_TRY = 0.0;
            Double rateEUR_TO_USD = 0.0;

            for (Map.Entry<String, Double> entry : rates.entrySet()) {
                String targetCurrency = entry.getKey();
                double rate = entry.getValue();

                if(Objects.equals(targetCurrency, "TRY")){ // get the EUR-TRY value and add it to database
                    Currency currencyToBeAdded = new Currency("EUR", "Fixer.io", response.getDate() + " " + hourAndMinutes, rate, rate);
                    rateEUR_TO_TRY = rate;
                    addCurrency(currencyToBeAdded);
                    currenciesList.add(currencyToBeAdded);
                    System.out.println("Currency added for fixer: " + currencyToBeAdded);
                }
                else if(Objects.equals(targetCurrency, "USD")){ // get the EUR-USD value and keep it in memory
                    rateEUR_TO_USD = rate;
                }
            }

            //we know that all the rates have been initialized, now we can calculatethe USD-TRY rate and add it to the database
            double rateUSD_TO_TRY = rateEUR_TO_TRY / rateEUR_TO_USD;
            Currency currencyToBeAdded = new Currency("USD", "Fixer.io", response.getDate() + " " + hourAndMinutes, rateUSD_TO_TRY, rateUSD_TO_TRY);
            addCurrency(currencyToBeAdded);
            currenciesList.add(currencyToBeAdded);
            System.out.println("Currency added for fixer: " + currencyToBeAdded);
        }

        // Return the list of currencies
        return currenciesList;
    }


    @Value("${currencylayer.api.url}")
    private String apiUrl;
    @Value("${fixer.api.url}")
    private String apiUrl2;

    @Value("${currencylayer.access.key}")
    private String accessKey;
    @Value("${fixer.access.key}")
    private String accessKey2;

    @PostConstruct
    public void init() {
        System.out.println("init runned");
        fetchCurrencies(apiUrl, accessKey);
        fetchCurrenciesSource2(apiUrl2, accessKey2);
        System.out.println("init ended");
    }


    @Scheduled(fixedRate = 450000, initialDelay = 450000) // 3600000ms = 1 hour (left it at 9 to easliy see)
    public void fetchCurrenciesAtIntervals() {
        System.out.println("scheduled runned");
        fetchCurrencies(apiUrl, accessKey);
        fetchCurrenciesSource2(apiUrl2, accessKey2);
        System.out.println("scheduled ended");
    }

}

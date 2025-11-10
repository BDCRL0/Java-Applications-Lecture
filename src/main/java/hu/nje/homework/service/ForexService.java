package hu.nje.homework.service;

import hu.nje.homework.model.AccountInfo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ForexService {

    private final Map<String, Double> currentPrices = new HashMap<>();
    private final List<String> availableInstruments = Arrays.asList(
        "EUR_USD", "GBP_USD", "USD_JPY", "USD_CHF", "AUD_USD", 
        "USD_CAD", "NZD_USD", "EUR_GBP", "EUR_JPY", "GBP_JPY"
    );

    public ForexService() {
        initializePrices();
    }

    public AccountInfo getAccountInfo() {
        AccountInfo account = new AccountInfo();
        account.setAccountId("DEMO-123456789");
        account.setAccountType("Demo Account");
        account.setBaseCurrency("USD");
        account.setStatus("Active");
        account.setCreatedDate("2025-01-01");
        account.setBalance(100000.0);
        account.setNav(100250.75);
        account.setUnrealizedPL(250.75);
        account.setMarginUsed(5420.0);
        account.setMarginAvailable(94830.75);
        account.setTotalTrades(47);
        account.setWinningTrades(28);
        account.setLosingTrades(19);
        account.setWinRate(59.6);
        return account;
    }

    public List<String> getAvailableInstruments() {
        return availableInstruments;
    }

    public Double getCurrentPrice(String instrument) {
        // Simulate price fluctuation with small random changes
        if (currentPrices.containsKey(instrument)) {
            double currentPrice = currentPrices.get(instrument);
            double fluctuation = (Math.random() - 0.5) * 0.001; // Small price movement
            double newPrice = currentPrice * (1 + fluctuation);
            currentPrices.put(instrument, newPrice);
            return newPrice;
        }
        return null;
    }

    public Map<String, Double> getAllCurrentPrices() {
        Map<String, Double> prices = new HashMap<>();
        for (String instrument : availableInstruments) {
            prices.put(instrument, getCurrentPrice(instrument));
        }
        return prices;
    }

    private void initializePrices() {
        // Initialize with realistic forex prices
        currentPrices.put("EUR_USD", 1.0850);
        currentPrices.put("GBP_USD", 1.2650);
        currentPrices.put("USD_JPY", 149.50);
        currentPrices.put("USD_CHF", 0.8750);
        currentPrices.put("AUD_USD", 0.6550);
        currentPrices.put("USD_CAD", 1.3650);
        currentPrices.put("NZD_USD", 0.5950);
        currentPrices.put("EUR_GBP", 0.8580);
        currentPrices.put("EUR_JPY", 162.25);
        currentPrices.put("GBP_JPY", 189.15);
    }

    public List<String> getGranularityOptions() {
        return Arrays.asList("M1", "M5", "M15", "M30", "H1", "H4", "D", "W", "M");
    }
}
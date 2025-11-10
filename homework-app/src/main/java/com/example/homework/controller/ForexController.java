package com.example.homework.controller;

import com.example.homework.model.AccountInfo;
import com.example.homework.service.ForexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class ForexController {

    @Autowired
    private ForexService forexService;    @GetMapping("/forex-account")
    public String forexAccount(Model model) {
        AccountInfo accountInfo = forexService.getAccountInfo();
        model.addAttribute("accountInfo", accountInfo);
        model.addAttribute("title", "Forex Account Information");
        model.addAttribute("activePage", "forex-account");
        return "forex-account";
    }

    @PostMapping("/forex-account")
    public String forexAccountPost(Model model) {
        // Redirect POST requests to GET to avoid 405 errors
        return "redirect:/forex-account";
    }@GetMapping("/forex-actprice")
    public String forexActPrice(Model model) {
        model.addAttribute("title", "Forex - Current Prices");
        model.addAttribute("instruments", forexService.getAvailableInstruments());
        model.addAttribute("activePage", "forex-actprice");
        return "forex-actprice";
    }    @PostMapping("/forex-actprice/get")
    public String getActualPrice(@RequestParam String instrument, Model model) {
        Double price = forexService.getCurrentPrice(instrument);
        model.addAttribute("title", "Forex - Current Prices");
        model.addAttribute("instruments", forexService.getAvailableInstruments());
        model.addAttribute("selectedInstrument", instrument);
        model.addAttribute("currentPrice", price);
        model.addAttribute("activePage", "forex-actprice");
        return "forex-actprice";
    }    @GetMapping("/forex-histprice")
    public String forexHistPrice(Model model) {
        model.addAttribute("title", "Forex - Historical Prices");
        model.addAttribute("instruments", forexService.getAvailableInstruments());
        model.addAttribute("granularities", forexService.getGranularityOptions());
        model.addAttribute("activePage", "forex-histprice");
        return "forex-histprice";
    }    @PostMapping("/forex-histprice/get")
    public String getHistoricalPrices(@RequestParam String instrument, @RequestParam String granularity, Model model) {
        try {            Map<String, Object> histData = forexService.getHistoricalPrices(instrument, granularity, 10);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> candles = (List<Map<String, Object>>) histData.get("candles");
            
            // Transform candles for template (rename 'time' to 'dateTime' and add change calculation)
            List<Map<String, Object>> transformedCandles = new ArrayList<>();
            Map<String, Object> previousCandle = null;
            
            for (Map<String, Object> candle : candles) {
                Map<String, Object> transformedCandle = new HashMap<>();
                transformedCandle.put("dateTime", candle.get("time"));
                transformedCandle.put("open", candle.get("open"));
                transformedCandle.put("high", candle.get("high"));
                transformedCandle.put("low", candle.get("low"));
                transformedCandle.put("close", candle.get("close"));
                
                // Calculate change from previous candle
                if (previousCandle != null) {
                    double prevClose = ((Number) previousCandle.get("close")).doubleValue();
                    double currentClose = ((Number) candle.get("close")).doubleValue();
                    double change = currentClose - prevClose;
                    transformedCandle.put("change", String.format("%.4f", change));
                } else {
                    transformedCandle.put("change", "0.0000");
                }
                
                transformedCandles.add(transformedCandle);
                previousCandle = candle;
            }
            
            model.addAttribute("title", "Forex - Historical Prices");
            model.addAttribute("instruments", forexService.getAvailableInstruments());
            model.addAttribute("granularities", forexService.getGranularityOptions());
            model.addAttribute("selectedInstrument", instrument);
            model.addAttribute("selectedGranularity", granularity);
            model.addAttribute("historicalData", transformedCandles);
            
            // Add chart data
            List<String> chartLabels = new ArrayList<>();
            List<Double> chartPrices = new ArrayList<>();
            for (Map<String, Object> candle : transformedCandles) {
                chartLabels.add((String) candle.get("dateTime"));
                chartPrices.add(((Number) candle.get("close")).doubleValue());
            }
            model.addAttribute("chartLabels", chartLabels);
            model.addAttribute("chartPrices", chartPrices);
            
        } catch (Exception e) {
            model.addAttribute("error", "Error fetching historical data: " + e.getMessage());
        }
        model.addAttribute("activePage", "forex-histprice");
        return "forex-histprice";
    }@GetMapping("/forex-open")
    public String forexOpen(Model model) {
        model.addAttribute("title", "Forex - Open Position");
        model.addAttribute("instruments", forexService.getAvailableInstruments());
        model.addAttribute("activePage", "forex-open");
        return "forex-open";
    }    @PostMapping("/forex-open/trade")
    public String openPosition(@RequestParam String instrument, @RequestParam int quantity, Model model) {
        try {
            String result = forexService.openPosition(instrument, quantity);
            model.addAttribute("title", "Forex - Open Position");
            model.addAttribute("instruments", forexService.getAvailableInstruments());
            model.addAttribute("tradeResult", result);
            model.addAttribute("selectedInstrument", instrument);
            model.addAttribute("selectedQuantity", quantity);
        } catch (Exception e) {
            model.addAttribute("error", "Error opening position: " + e.getMessage());
        }
        model.addAttribute("activePage", "forex-open");
        return "forex-open";
    }    @GetMapping("/forex-pos")
    public String forexPos(Model model) {
        try {
            Map<String, Object> positions = forexService.getOpenPositions();
            model.addAttribute("title", "Forex - Open Positions");
            model.addAttribute("positions", positions);
        } catch (Exception e) {
            model.addAttribute("error", "Error fetching positions: " + e.getMessage());
        }
        model.addAttribute("activePage", "forex-pos");
        return "forex-pos";
    }

    @PostMapping("/forex-pos")
    public String forexPosPost(Model model) {
        // Redirect POST requests to GET to avoid 405 errors
        return "redirect:/forex-pos";
    }@GetMapping("/forex-close")
    public String forexClose(Model model) {
        model.addAttribute("title", "Forex - Close Position");
        model.addAttribute("activePage", "forex-close");
        return "forex-close";
    }

    @PostMapping("/forex-close/trade")
    public String closePosition(@RequestParam String tradeId, Model model) {
        try {
            String result = forexService.closePosition(tradeId);
            model.addAttribute("title", "Forex - Close Position");
            model.addAttribute("closeResult", result);
            model.addAttribute("tradeId", tradeId);
        } catch (Exception e) {
            model.addAttribute("error", "Error closing position: " + e.getMessage());
        }
        model.addAttribute("activePage", "forex-close");
        return "forex-close";
    }
}

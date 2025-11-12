package com.example.homework.controller;

import com.example.homework.service.HungarianNationalBankService;
import com.example.homework.model.CurrencyData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SoapController {

    @Autowired
    private HungarianNationalBankService bankService;

    @PostMapping("/soap/fetch")
    public String fetchCurrencyData(
            @RequestParam String currency,
            @RequestParam String startDate,
            @RequestParam String endDate,
            Model model) {

        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            // Validate date range
            if (start.isAfter(end)) {
                model.addAttribute("error", "Start date must be before end date");
                return "soap";
            }

            if (start.isBefore(LocalDate.now().minusYears(1))) {
                model.addAttribute("error", "Start date cannot be more than 1 year ago");
                return "soap";
            }

            if (end.isAfter(LocalDate.now())) {
                model.addAttribute("error", "End date cannot be in the future");
                return "soap";
            }

            // Fetch currency data
            List<CurrencyData> currencyData = bankService.getCurrencyData(currency, start, end);

            if (currencyData.isEmpty()) {
                model.addAttribute("error", "No data found for the selected currency and date range");
                return "soap";
            }

            // Prepare data for the chart
            List<String> dates = currencyData.stream()
                    .map(data -> data.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .collect(Collectors.toList());

            List<Double> rates = currencyData.stream()
                    .map(CurrencyData::getRate)
                    .collect(Collectors.toList());

            model.addAttribute("currencyData", currencyData);
            model.addAttribute("selectedCurrency", currency);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("dates", dates);
            model.addAttribute("rates", rates);

        } catch (Exception e) {
            model.addAttribute("error", "Error fetching currency data: " + e.getMessage());
        }

        return "soap";
    }
}

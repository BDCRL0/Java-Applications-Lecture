package hu.nje.homework.controller;

import hu.nje.homework.model.AccountInfo;
import hu.nje.homework.service.ForexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class ForexController {

    @Autowired
    private ForexService forexService;

    @GetMapping("/forex-account")
    public String forexAccount(Model model) {
        AccountInfo accountInfo = forexService.getAccountInfo();
        model.addAttribute("accountInfo", accountInfo);
        model.addAttribute("title", "Forex Account Information");
        return "forex-account";
    }

    @GetMapping("/forex-actprice")
    public String forexActPrice(Model model) {
        model.addAttribute("title", "Forex - Current Prices");
        model.addAttribute("instruments", forexService.getAvailableInstruments());
        return "forex-actprice";
    }

    @PostMapping("/forex-actprice/get")
    public String getActualPrice(@RequestParam String instrument, Model model) {
        Double price = forexService.getCurrentPrice(instrument);
        model.addAttribute("title", "Forex - Current Prices");
        model.addAttribute("instruments", forexService.getAvailableInstruments());
        model.addAttribute("selectedInstrument", instrument);
        model.addAttribute("currentPrice", price);
        return "forex-actprice";
    }

    @GetMapping("/forex-histprice")
    public String forexHistPrice(Model model) {
        model.addAttribute("title", "Forex - Historical Prices");
        model.addAttribute("instruments", forexService.getAvailableInstruments());
        model.addAttribute("granularities", forexService.getGranularityOptions());
        return "forex-histprice";
    }

    @GetMapping("/forex-open")
    public String forexOpen(Model model) {
        model.addAttribute("title", "Forex - Open Position");
        model.addAttribute("instruments", forexService.getAvailableInstruments());
        return "forex-open";
    }

    @GetMapping("/forex-pos")
    public String forexPos(Model model) {
        model.addAttribute("title", "Forex - Open Positions");
        return "forex-pos";
    }

    @GetMapping("/forex-close")
    public String forexClose(Model model) {
        model.addAttribute("title", "Forex - Close Position");
        return "forex-close";
    }
}
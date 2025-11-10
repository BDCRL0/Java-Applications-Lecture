package hu.nje.homework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Homework Company - Welcome");
        model.addAttribute("companyName", "Homework Financial Services");
        model.addAttribute("companyDescription", "Your trusted partner in financial services and currency trading");
        return "index";
    }

    @GetMapping("/soap")
    public String soap(Model model) {
        model.addAttribute("title", "Hungarian National Bank - Currency Data");
        return "soap";
    }

    @GetMapping("/forex-account")
    public String forexAccount(Model model) {
        model.addAttribute("title", "Forex Account Information");
        return "forex-account";
    }

    @GetMapping("/forex-actprice")
    public String forexActPrice(Model model) {
        model.addAttribute("title", "Forex - Current Prices");
        return "forex-actprice";
    }

    @GetMapping("/forex-histprice")
    public String forexHistPrice(Model model) {
        model.addAttribute("title", "Forex - Historical Prices");
        return "forex-histprice";
    }

    @GetMapping("/forex-open")
    public String forexOpen(Model model) {
        model.addAttribute("title", "Forex - Open Position");
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
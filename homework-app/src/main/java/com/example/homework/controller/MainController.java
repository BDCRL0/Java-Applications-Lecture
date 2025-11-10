package com.example.homework.controller;

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
        model.addAttribute("activePage", "home");
        return "index";
    }
    
    @GetMapping("/soap")
    public String soap(Model model) {
        model.addAttribute("title", "Hungarian National Bank - Currency Data");
        model.addAttribute("activePage", "soap");
        return "soap";
    }

    // All Forex mappings are handled by ForexController
}

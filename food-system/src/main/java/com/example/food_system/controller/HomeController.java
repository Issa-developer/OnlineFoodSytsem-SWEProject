package com.example.food_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homePage() {
        return "index"; // loads templates/index.html
    }

    // ADD THIS METHOD FOR ADMIN ACCESS
    @GetMapping("/admin")
    public String adminRedirect() {
        return "redirect:/admin/dashboard";
    }
}

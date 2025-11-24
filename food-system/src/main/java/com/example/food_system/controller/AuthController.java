package com.example.food_system.controller;

import com.example.food_system.entity.User;
import com.example.food_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password, 
                       Model model) {
        
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // In real app, use password encoder. For now, simple comparison
            if (user.getPassword().equals(password)) {
                if ("ADMIN".equals(user.getRole())) {
                    return "redirect:/admin/dashboard";
                } else {
                    return "redirect:/"; // Redirect customers to homepage
                }
            }
        }
        
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        // In a real app, you'd invalidate the session here
        return "redirect:/login?logout";
    }

    @GetMapping("/auth/dashboard")
    public String adminDashboard(Model model) {
        // Check if user is admin (in real app, use session)
        model.addAttribute("totalCustomers", userRepository.countCustomers());
        model.addAttribute("totalOrders", 1247L);
        model.addAttribute("todayRevenue", 8921.0);
        model.addAttribute("activeOrders", 42L);
        
        return "admin/dashboard";
    }
}
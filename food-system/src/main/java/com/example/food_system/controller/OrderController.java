package com.example.food_system.controller;

import com.example.food_system.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.lang.NonNull;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

        @GetMapping("/list")
        public String listOrders(Model model) {
            model.addAttribute("orders", orderService.getAllOrders());
            return "orders";
        }

    @PostMapping("/delete")
    public String deleteOrder(@RequestParam @NonNull Long id) {
        orderService.deleteOrder(id);
        return "redirect:/admin/orders";
    }

    @GetMapping("/view")
    public String viewOrder(@RequestParam @NonNull Long id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        return "order-details";
    }
}

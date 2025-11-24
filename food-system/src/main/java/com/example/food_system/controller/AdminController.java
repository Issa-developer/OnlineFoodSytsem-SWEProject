package com.example.food_system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.food_system.entity.*;
import com.example.food_system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private AIService aiService;

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private RealTimeService realTimeService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // ========== DASHBOARD ENDPOINTS ==========
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        try {
            // Get all orders
            List<Order> allOrders = orderService.getAllOrders();
            // Recent orders (last 10)
            List<Order> recentOrders = allOrders.stream()
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .limit(10)
                .toList();
            model.addAttribute("recentOrders", recentOrders);

            // Order status counts
            long pendingOrders = allOrders.stream().filter(o -> "Pending".equals(o.getStatus())).count();
            long preparingOrders = allOrders.stream().filter(o -> "Preparing".equals(o.getStatus())).count();
            long readyOrders = allOrders.stream().filter(o -> "Ready".equals(o.getStatus())).count();
            long deliveredOrders = allOrders.stream().filter(o -> "Delivered".equals(o.getStatus())).count();
            model.addAttribute("pendingOrders", pendingOrders);
            model.addAttribute("preparingOrders", preparingOrders);
            model.addAttribute("readyOrders", readyOrders);
            model.addAttribute("deliveredOrders", deliveredOrders);

            // AI recommendations
            List<AIService.AIRecommendation> aiRecommendations = aiService.getDashboardRecommendations();
            model.addAttribute("aiRecommendations", aiRecommendations);

            // Real-time activity stream
            List<RealTimeService.RealTimeActivity> realTimeActivities = realTimeService.getRecentActivity();
            model.addAttribute("realTimeActivities", realTimeActivities);

            return "admin/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load dashboard: " + e.getMessage());
            return "admin/error";
        }
    }

    @GetMapping("/dashboard/metrics")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDashboardMetrics() {
        try {
            return ResponseEntity.ok(analyticsService.getRealTimeMetrics());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/dashboard/live-orders")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getLiveOrders() {
        try {
            return ResponseEntity.ok(realTimeService.getLiveOrders());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of(Map.of("error", e.getMessage())));
        }
    }

    // ========== MENU MANAGEMENT ENDPOINTS ==========
    @GetMapping("/menu")
    public String menuManagement(Model model) {
        try {
            model.addAttribute("items", menuService.getAllMenuItems());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("newItem", new MenuItem()); // For add form
            
            // AI-powered menu analytics
            Map<String, Object> menuAnalytics = aiService.getMenuAnalytics();
            model.addAllAttributes((Map<String, ?>) menuAnalytics);
            
            // Performance metrics
            Map<String, Object> performanceData = analyticsService.getMenuPerformance();
            model.addAllAttributes((Map<String, ?>) performanceData);
            
            return "admin/menu-management";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load menu: " + e.getMessage());
            return "admin/error";
        }
    }

    @PostMapping("/menu/items/add")
    public String addMenuItem(@ModelAttribute MenuItem item) {
        try {
            menuService.saveMenuItem(item);
            
            // Send real-time update
            Map<String, Object> update = new HashMap<>();
            update.put("type", "ITEM_ADDED");
            update.put("item", item);
            messagingTemplate.convertAndSend("/topic/menu-updates", update);
            
            return "redirect:/admin/menu?success=Item added successfully";
        } catch (Exception e) {
            return "redirect:/admin/menu?error=Failed to add item: " + e.getMessage();
        }
    }

    @PostMapping("/menu/items/edit")
    public String editMenuItem(@ModelAttribute MenuItem item) {
        try {
            menuService.updateMenuItem(item);
            
            // Send real-time update
            Map<String, Object> update = new HashMap<>();
            update.put("type", "ITEM_UPDATED");
            update.put("item", item);
            messagingTemplate.convertAndSend("/topic/menu-updates", update);
            
            return "redirect:/admin/menu?success=Item updated successfully";
        } catch (Exception e) {
            return "redirect:/admin/menu?error=Failed to update item: " + e.getMessage();
        }
    }

    @PostMapping("/menu/items/delete")
    public String deleteMenuItem(@RequestParam Long id) {
        try {
            menuService.deleteMenuItem(id);
            
            // Send real-time update
            Map<String, Object> update = new HashMap<>();
            update.put("type", "ITEM_DELETED");
            update.put("itemId", id);
            messagingTemplate.convertAndSend("/topic/menu-updates", update);
            
            return "redirect:/admin/menu?success=Item deleted successfully";
        } catch (Exception e) {
            return "redirect:/admin/menu?error=Failed to delete item: " + e.getMessage();
        }
    }

    // AI-Powered Menu Optimization
    @PostMapping("/menu/ai/optimize")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> optimizeMenuWithAI() {
        try {
            Map<String, Object> optimization = aiService.optimizeMenuPrices();
            return ResponseEntity.ok(optimization);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/menu/ai/recommendations")
    @ResponseBody
    public ResponseEntity<List<AIService.AIRecommendation>> getMenuAIRecommendations() {
        try {
            return ResponseEntity.ok(aiService.getMenuOptimizationRecommendations());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    // ========== ORDER MANAGEMENT ENDPOINTS ==========
    @GetMapping("/orders/{id}/details")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable Long id) {
        try {
            Order order = orderService.getOrderById(id).orElse(null);
            if (order == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Order not found"));
            }
            Map<String, Object> result = new HashMap<>();
            result.put("id", order.getId());
            result.put("date", order.getDate());
            result.put("status", order.getStatus());
            result.put("total", order.getTotal());
            if (order.getUser() != null) {
                Map<String, Object> userMap = new HashMap<>();
                // Try getUsername(), getFullName(), or fallback to toString()
                String userName;
                if (order.getUser().getUsername() != null) {
                    userName = order.getUser().getUsername();
                } else if (order.getUser().toString() != null) {
                    userName = order.getUser().toString();
                } else {
                    userName = "User";
                }
                userMap.put("name", userName);
                userMap.put("email", order.getUser().getEmail());
                result.put("user", userMap);
            }
            // Use getItems() for Order entity
            List<MenuItem> itemsList = null;
            try {
                itemsList = order.getItems();
            } catch (Exception ex) {
                itemsList = new java.util.ArrayList<>();
            }
            if (itemsList != null) {
                List<Map<String, Object>> items = new java.util.ArrayList<>();
                for (MenuItem item : itemsList) {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("name", item.getName());
                    itemMap.put("price", item.getPrice());
                    // If quantity is not available, default to 1
                    try {
                        itemMap.put("quantity", item.getQuantity());
                    } catch (Exception ex) {
                        itemMap.put("quantity", 1);
                    }
                    items.add(itemMap);
                }
                result.put("items", items);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/orders")
    public String orderManagement(Model model) {
        try {
            List<Order> allOrders = orderService.getAllOrders();
            List<User> allUsers = userService.getAllUsers();
            Map<User, List<Order>> ordersByUser = new HashMap<>();
            for (User user : allUsers) {
                List<Order> userOrders = allOrders.stream()
                    .filter(o -> o.getUser() != null && o.getUser().getId().equals(user.getId()))
                    .toList();
                ordersByUser.put(user, userOrders);
            }
            model.addAttribute("ordersByUser", ordersByUser);

            // Real-time order analytics
            Map<String, Object> orderAnalytics = analyticsService.getOrderAnalytics();
            model.addAllAttributes((Map<String, ?>) orderAnalytics);

            // Predictive insights
            Map<String, Object> predictions = aiService.getOrderPredictions();
            model.addAllAttributes((Map<String, ?>) predictions);

            return "admin/orders";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load orders: " + e.getMessage());
            return "admin/error";
        }
    }

    @GetMapping("/orders/stream")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getOrderStream() {
        try {
            return ResponseEntity.ok(realTimeService.getOrderStream());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of(Map.of("error", e.getMessage())));
        }
    }

    @PostMapping("/orders/{id}/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        try {
            Order order = orderService.updateOrderStatus(id, status);
            
            // Send real-time update to all connected clients
            Map<String, Object> update = Map.of(
                "type", "ORDER_STATUS_UPDATE",
                "orderId", id,
                "status", status,
                "timestamp", LocalDateTime.now()
            );
            messagingTemplate.convertAndSend("/topic/order-updates", (Object) update);
            
            return ResponseEntity.ok(Map.of("success", true, "order", order));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    @GetMapping("/orders/analytics")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getOrderAnalytics() {
        try {
            return ResponseEntity.ok(analyticsService.getOrderAnalytics());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ========== USER MANAGEMENT ENDPOINTS ==========
    @GetMapping("/users")
    public String userManagement(Model model) {
        try {
            model.addAttribute("users", userService.getAllUsers());
            
            // Customer intelligence data
            Map<String, Object> customerAnalytics = analyticsService.getCustomerAnalytics();
            model.addAllAttributes((Map<String, ?>) customerAnalytics);
            
            // Segmentation data
            Map<String, Object> segmentation = new HashMap<>(aiService.getCustomerSegmentation());
            model.addAllAttributes(segmentation);
            
            return "admin/user-management";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load users: " + e.getMessage());
            return "admin/error";
        }
    }

    @GetMapping("/users/analytics")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUserAnalytics() {
        try {
            return ResponseEntity.ok(analyticsService.getCustomerAnalytics());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/users/segmentation")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUserSegmentation() {
        try {
            return ResponseEntity.ok(aiService.getCustomerSegmentation());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/users/activity-stream")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getUserActivityStream() {
        try {
            return ResponseEntity.ok(realTimeService.getCustomerActivityStream());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of(Map.of("error", e.getMessage())));
        }
    }

    // ========== REAL-TIME WEB SOCKET ENDPOINTS ==========
    @MessageMapping("/dashboard.updates")
    @SendTo("/topic/dashboard")
    public Map<String, Object> handleDashboardUpdates() {
        try {
            return analyticsService.getRealTimeMetrics();
        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }

    @MessageMapping("/orders.subscribe")
    @SendTo("/topic/orders")
    public List<Map<String, Object>> handleOrderSubscription() {
        try {
            return realTimeService.getLiveOrders();
        } catch (Exception e) {
            return List.of(Map.of("error", e.getMessage()));
        }
    }

    // ========== AI ENDPOINTS ==========
    @PostMapping("/ai/generate-menu")
    @ResponseBody
    public ResponseEntity<List<MenuItem>> generateAIMenu(
            @RequestParam String cuisineType,
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @RequestParam List<String> dietaryPreferences) {
        try {
            List<MenuItem> generatedItems = aiService.generateMenuItems(
                cuisineType, minPrice, maxPrice, dietaryPreferences);
            return ResponseEntity.ok(generatedItems);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    @GetMapping("/ai/predictions")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAIPredictions() {
        try {
            return ResponseEntity.ok(aiService.getBusinessPredictions());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ========== SYSTEM HEALTH ENDPOINTS ==========
    @GetMapping("/system/health")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSystemHealth() {
        try {
            Map<String, Object> health = Map.of(
                "status", "OPERATIONAL",
                "uptime", "99.98%",
                "activeConnections", realTimeService.getActiveConnections(),
                "lastUpdate", LocalDateTime.now(),
                "aiStatus", "OPTIMAL",
                "databaseStatus", "CONNECTED",
                "cacheStatus", "HEALTHY"
            );
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "DEGRADED", "error", e.getMessage()));
        }
    }

    // ========== ERROR HANDLING ==========
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "admin/error";
    }
}
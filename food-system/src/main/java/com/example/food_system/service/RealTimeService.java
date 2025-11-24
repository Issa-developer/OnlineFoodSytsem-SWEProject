package com.example.food_system.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RealTimeService {

    public static class RealTimeActivity {
        private String type;
        private String description;
        private LocalDateTime timestamp;
        private String user;

        public RealTimeActivity(String type, String description, LocalDateTime timestamp, String user) {
            this.type = type;
            this.description = description;
            this.timestamp = timestamp;
            this.user = user;
        }

        // Getters and setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
        public String getUser() { return user; }
        public void setUser(String user) { this.user = user; }
    }

    public List<RealTimeActivity> getRecentActivity() {
        List<RealTimeActivity> activities = new ArrayList<>();
        activities.add(new RealTimeActivity("ORDER", "New order #2847 received", LocalDateTime.now().minusMinutes(5), "Customer"));
        activities.add(new RealTimeActivity("MENU", "Menu item updated: Spicy Tuna Roll", LocalDateTime.now().minusMinutes(12), "Admin"));
        activities.add(new RealTimeActivity("SYSTEM", "AI optimization completed", LocalDateTime.now().minusMinutes(25), "System"));
        return activities;
    }

    public List<Map<String, Object>> getLiveOrders() {
        List<Map<String, Object>> liveOrders = new ArrayList<>();
        
        Map<String, Object> order1 = new HashMap<>();
        order1.put("id", 2847);
        order1.put("customer", "John Doe");
        order1.put("items", Arrays.asList("Spicy Tuna Roll", "Miso Soup"));
        order1.put("status", "Preparing");
        order1.put("timestamp", LocalDateTime.now().minusMinutes(8));
        liveOrders.add(order1);

        Map<String, Object> order2 = new HashMap<>();
        order2.put("id", 2846);
        order2.put("customer", "Jane Smith");
        order2.put("items", Arrays.asList("Chicken Teriyaki", "Edamame"));
        order2.put("status", "Pending");
        order2.put("timestamp", LocalDateTime.now().minusMinutes(15));
        liveOrders.add(order2);

        return liveOrders;
    }

    public List<Map<String, Object>> getOrderStream() {
        return getLiveOrders(); // For simplicity, reuse live orders
    }

    public List<Map<String, Object>> getCustomerActivityStream() {
        List<Map<String, Object>> activities = new ArrayList<>();
        
        Map<String, Object> activity1 = new HashMap<>();
        activity1.put("userId", 101);
        activity1.put("action", "PLACED_ORDER");
        activity1.put("details", "Order #2847 - $42.50");
        activity1.put("timestamp", LocalDateTime.now().minusMinutes(10));
        activities.add(activity1);

        Map<String, Object> activity2 = new HashMap<>();
        activity2.put("userId", 102);
        activity2.put("action", "ACCOUNT_CREATED");
        activity2.put("details", "New customer registration");
        activity2.put("timestamp", LocalDateTime.now().minusMinutes(25));
        activities.add(activity2);

        return activities;
    }

    public int getActiveConnections() {
        return 42; // Mock active WebSocket connections
    }
}
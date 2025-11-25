package com.example.food_system.service;

import com.example.food_system.entity.MenuItem;
import com.example.food_system.entity.Order;
import com.example.food_system.repository.MenuItemRepository;
import com.example.food_system.repository.OrderRepository;
import com.example.food_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        // Real-time metrics calculation
        List<Order> todayOrders = orderRepository.findByDate(LocalDate.now());
        List<Order> allOrders = orderRepository.findAll();

        double todayRevenue = todayOrders.stream().mapToDouble(Order::getTotal).sum();
        double totalRevenue = allOrders.stream().mapToDouble(Order::getTotal).sum();
        
        long activeOrders = todayOrders.stream()
                .filter(order -> "Pending".equals(order.getStatus()) || "Preparing".equals(order.getStatus()))
                .count();

        metrics.put("activeOrders", activeOrders);
        metrics.put("todayRevenue", todayRevenue);
        metrics.put("totalRevenue", totalRevenue);
        metrics.put("menuItems", menuItemRepository.count());
        metrics.put("totalUsers", userRepository.count());
        metrics.put("completionRate", calculateCompletionRate());
        metrics.put("customerSatisfaction", 94.2);
        metrics.put("aiOptimizationScore", 88.7);

        return metrics;
    }

    public Map<String, Object> getRealTimeMetrics() {
        Map<String, Object> realTimeData = new HashMap<>();

        // Simulate real-time fluctuating data
        Random random = new Random();
        
        realTimeData.put("activeOrders", 1247 + random.nextInt(50));
        realTimeData.put("menuItems", 342 + random.nextInt(10));
        realTimeData.put("todayRevenue", 8921 + random.nextInt(500));
        realTimeData.put("aiScore", (random.nextDouble() * 5 + 95));
        realTimeData.put("completionRate", (random.nextDouble() * 3 + 96));
        realTimeData.put("timestamp", LocalDateTime.now());

        return realTimeData;
    }

    public Map<String, Object> getMenuPerformance() {
        Map<String, Object> performance = new HashMap<>();
        List<MenuItem> menuItems = menuItemRepository.findAll();

        // Calculate various performance metrics
        double totalRevenue = menuItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity() * 0.3) // Simulated revenue
                .sum();

        Map<String, Double> categoryPerformance = menuItems.stream()
                .collect(Collectors.groupingBy(
                    item -> item.getCategory().getName(),
                    Collectors.summingDouble(item -> item.getPrice() * item.getQuantity() * 0.2)
                ));

        // Top performers
        List<Map<String, Object>> topPerformers = menuItems.stream()
                .sorted((a, b) -> Double.compare(b.getPrice() * b.getQuantity(), a.getPrice() * a.getQuantity()))
                .limit(5)
                .map(item -> {
                    Map<String, Object> performer = new HashMap<>();
                    performer.put("name", item.getName());
                    performer.put("revenue", item.getPrice() * item.getQuantity() * 0.3);
                    performer.put("performance", Math.random() * 50 + 50);
                    return performer;
                })
                .collect(Collectors.toList());

        performance.put("totalRevenue", totalRevenue);
        performance.put("categoryPerformance", categoryPerformance);
        performance.put("topPerformers", topPerformers);
        performance.put("avgProfitMargin", 28.4);
        performance.put("optimizationPotential", 23.7);

        return performance;
    }

    public Map<String, Object> getOrderAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        List<Order> orders = orderRepository.findAll();

        // Order status distribution
        Map<String, Long> statusDistribution = orders.stream()
                .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));

        // Revenue by day (last 7 days)
        Map<LocalDate, Double> revenueByDay = orders.stream()
                .filter(order -> order.getDate().isAfter(LocalDate.now().minusDays(7)))
                .collect(Collectors.groupingBy(
                    Order::getDate,
                    Collectors.summingDouble(Order::getTotal)
                ));

        // Average order value
        double avgOrderValue = orders.stream()
                .mapToDouble(Order::getTotal)
                .average()
                .orElse(0.0);

        analytics.put("statusDistribution", statusDistribution);
        analytics.put("revenueByDay", revenueByDay);
        analytics.put("avgOrderValue", avgOrderValue);
        analytics.put("totalOrders", orders.size());
        analytics.put("completionTime", 24.3);
        analytics.put("onTimeDelivery", 98.7);

        return analytics;
    }

    public Map<String, Object> getCustomerAnalytics() {
        Map<String, Object> analytics = new HashMap<>();

        // Simulate customer analytics
        analytics.put("totalCustomers", 2657);
        analytics.put("activeThisMonth", 1842);
        analytics.put("newCustomers", 568);
        analytics.put("retentionRate", 87.3);
        analytics.put("avgLifetimeValue", 1247.0);
        analytics.put("avgOrderFrequency", 4.2);

        // Customer preferences simulation
        Map<String, Double> cuisinePreferences = new HashMap<>();
        cuisinePreferences.put("Asian Fusion", 0.34);
        cuisinePreferences.put("Mediterranean", 0.28);
        cuisinePreferences.put("American", 0.22);
        cuisinePreferences.put("Other", 0.16);

        analytics.put("cuisinePreferences", cuisinePreferences);
        analytics.put("satisfactionScore", 94.2);

        return analytics;
    }

    private double calculateCompletionRate() {
        List<Order> recentOrders = orderRepository.findByDate(LocalDate.now().minusDays(7));
        long completed = recentOrders.stream().filter(o -> "Completed".equals(o.getStatus())).count();
        return recentOrders.isEmpty() ? 0 : (double) completed / recentOrders.size() * 100;
    }

    public Map<String, Object> getPredictiveInsights() {
        Map<String, Object> insights = new HashMap<>();

        // Simulate predictive insights
        insights.put("nextPeakHour", "19:00-21:00");
        insights.put("expectedOrders", 284);
        insights.put("confidence", 0.87);
        insights.put("revenueForecast", 15420.0);
        insights.put("growthRate", 12.4);
        insights.put("recommendedStaffing", 8);

        return insights;
    }
}
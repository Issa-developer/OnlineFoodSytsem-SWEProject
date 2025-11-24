package com.example.food_system.service;

import com.example.food_system.entity.MenuItem;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AIService {

    public static class AIRecommendation {
        private String title;
        private String description;
        private String type;
        private Double impactScore;
        private String action;

        // Constructors, getters, and setters
        public AIRecommendation(String title, String description, String type, Double impactScore, String action) {
            this.title = title;
            this.description = description;
            this.type = type;
            this.impactScore = impactScore;
            this.action = action;
        }

        // Getters and setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Double getImpactScore() { return impactScore; }
        public void setImpactScore(Double impactScore) { this.impactScore = impactScore; }
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
    }

    public List<AIRecommendation> getDashboardRecommendations() {
        List<AIRecommendation> recommendations = new ArrayList<>();
        recommendations.add(new AIRecommendation(
            "Optimize Menu Prices",
            "AI suggests adjusting prices for 5 items to increase profit margin by 12%",
            "PRICE_OPTIMIZATION",
            0.85,
            "optimize_prices"
        ));
        recommendations.add(new AIRecommendation(
            "Staff Allocation",
            "Predicted peak hours: 18:00-20:00. Recommend increasing staff by 2 members",
            "STAFFING",
            0.78,
            "adjust_staffing"
        ));
        return recommendations;
    }

    public Map<String, Object> getMenuAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("popularItems", Arrays.asList("Spicy Tuna Roll", "Chicken Teriyaki", "Miso Soup"));
        analytics.put("slowMoving", Arrays.asList("Seaweed Salad", "Edamame"));
        analytics.put("profitMargin", 32.5);
        analytics.put("optimizationPotential", 18.7);
        return analytics;
    }

    public Map<String, Object> optimizeMenuPrices() {
        Map<String, Object> result = new HashMap<>();
        result.put("optimizedItems", 5);
        result.put("estimatedRevenueIncrease", 12.4);
        result.put("confidence", 0.87);
        result.put("executionTime", "2.3s");
        return result;
    }

    public List<AIRecommendation> getMenuOptimizationRecommendations() {
        return Arrays.asList(
            new AIRecommendation("Increase Spicy Tuna Roll price", "Current margin low", "PRICING", 0.92, "increase_price"),
            new AIRecommendation("Bundle appetizers", "Increase average order value", "BUNDLING", 0.85, "create_bundle")
        );
    }

    public Map<String, Object> getOrderPredictions() {
        Map<String, Object> predictions = new HashMap<>();
        predictions.put("nextHourOrders", 47);
        predictions.put("peakTime", "19:30");
        predictions.put("recommendedPrep", Arrays.asList("Chicken", "Rice", "Vegetables"));
        predictions.put("confidence", 0.89);
        return predictions;
    }

    public Map<String, Object> getCustomerSegmentation() {
        Map<String, Object> segmentation = new HashMap<>();
        segmentation.put("segments", Arrays.asList("Frequent Diners", "Weekend Warriors", "New Customers"));
        segmentation.put("loyaltyScore", 87.3);
        segmentation.put("retentionRate", 92.1);
        return segmentation;
    }

    public List<MenuItem> generateMenuItems(String cuisineType, Double minPrice, Double maxPrice, List<String> dietaryPreferences) {
        // Mock implementation - in real scenario, this would call an AI API
        List<MenuItem> items = new ArrayList<>();
        Random random = new Random();
        
        for (int i = 1; i <= 5; i++) {
            MenuItem item = new MenuItem();
            item.setName(cuisineType + " Special " + i);
            item.setDescription("AI-generated " + cuisineType + " dish");
            item.setPrice(minPrice + (maxPrice - minPrice) * random.nextDouble());
            item.setCategory(null); // Set appropriate category
            items.add(item);
        }
        
        return items;
    }

    public Map<String, Object> getBusinessPredictions() {
        Map<String, Object> predictions = new HashMap<>();
        predictions.put("weeklyRevenue", 15420.0);
        predictions.put("growthTrend", 12.4);
        predictions.put("busyDays", Arrays.asList("Friday", "Saturday"));
        predictions.put("recommendedInventory", Map.of("Chicken", 45, "Rice", 120, "Vegetables", 85));
        return predictions;
    }
}
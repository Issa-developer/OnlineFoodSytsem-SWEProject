package controllers;

import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/promo")
public class PromoController {

    private static final Map<String, Map<String, Object>> PROMOS = Map.of(
            "SAVE10", Map.of("discount", 0.10, "type", "percentage", "description", "10% off"),
            "SAVE20", Map.of("discount", 0.20, "type", "percentage", "description", "20% off"),
            "FLAT50", Map.of("discount", 50, "type", "fixed", "description", "KES 50 off"),
            "WELCOME15", Map.of("discount", 0.15, "type", "percentage", "description", "15% off"),
            "FREEDELIV", Map.of("discount", "FREE_DELIVERY", "type", "special", "description", "Free Delivery")
    );

    @PostMapping("/apply")
    public Map<String, Object> applyPromo(@RequestBody Map<String, String> body) {
        String code = body.get("code").toUpperCase();

        Map<String, Object> res = new HashMap<>();

        if (PROMOS.containsKey(code)) {
            res.put("status", "success");
            res.put("message", "Promo applied");
            res.put("data", PROMOS.get(code));
        } else {
            res.put("status", "error");
            res.put("message", "Invalid promo code");
        }

        return res;
    }
}

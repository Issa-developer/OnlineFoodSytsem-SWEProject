package controllers;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @PostMapping("/add")
    public Map<String, Object> addItem(@RequestBody Map<String, Object> item) {
        Map<String, Object> res = new HashMap<>();

        // Expected fields based on SQL structure
        // menu_item_id
        // quantity
        // price

        // No DB implementation yet, just matching structure
        res.put("status", "success");
        res.put("message", "Item added to cart");
        res.put("data", Map.of(
                "menu_item_id", item.get("menu_item_id"),
                "quantity", item.get("quantity"),
                "price", item.get("price")
        ));

        return res;
    }

    @PostMapping("/update")
    public Map<String, Object> updateQuantity(@RequestBody Map<String, Object> data) {
        Map<String, Object> res = new HashMap<>();

        // Expected fields: order_item_id, quantity
        res.put("status", "success");
        res.put("message", "Quantity updated");
        res.put("data", data);

        return res;
    }

    @PostMapping("/remove")
    public Map<String, Object> removeItem(@RequestBody Map<String, Object> body) {
        Map<String, Object> res = new HashMap<>();

        // Expected field: order_item_id
        res.put("status", "success");
        res.put("message", "Item removed");
        res.put("order_item_id", body.get("order_item_id"));

        return res;
    }

    @PostMapping("/clear")
    public Map<String, Object> clearCart() {
        Map<String, Object> res = new HashMap<>();
        res.put("status", "success");
        res.put("message", "Cart cleared");

        return res;
    }
}

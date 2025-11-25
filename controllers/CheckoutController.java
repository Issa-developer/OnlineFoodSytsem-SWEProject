package controllers;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    @PostMapping("/proceed")
    public Map<String, Object> proceedToCheckout(@RequestBody Map<String, Object> checkoutData) {

        // Expected:
        // user_id
        // total
        // items: [ { menu_item_id, quantity, price } ]

        Map<String, Object> res = new HashMap<>();
        res.put("status", "success");
        res.put("message", "Checkout started");
        res.put("order_received", checkoutData);

        return res;
    }
}

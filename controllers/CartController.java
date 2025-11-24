import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @PostMapping("/add")
    public Map<String, Object> addItem(@RequestBody Map<String, Object> item) {
        Map<String, Object> res = new HashMap<>();
        res.put("status", "success");
        res.put("message", "Item added to cart");
        res.put("data", item);

        // TODO: Insert item into DB (if using SQL)
        return res;
    }

    @PostMapping("/update")
    public Map<String, Object> updateQuantity(@RequestBody Map<String, Object> data) {
        Map<String, Object> res = new HashMap<>();
        res.put("status", "success");
        res.put("message", "Quantity updated");
        res.put("data", data);

        // TODO: Update cart quantity in DB
        return res;
    }

    @PostMapping("/remove")
    public Map<String, Object> removeItem(@RequestBody Map<String, Object> body) {
        String itemId = body.get("itemId").toString();

        Map<String, Object> res = new HashMap<>();
        res.put("status", "success");
        res.put("message", "Item removed");
        res.put("itemId", itemId);

        // TODO: Remove from DB
        return res;
    }

    @PostMapping("/clear")
    public Map<String, Object> clearCart() {
        Map<String, Object> res = new HashMap<>();
        res.put("status", "success");
        res.put("message", "Cart cleared");

        // TODO: Delete all cart items from DB
        return res;
    }
}

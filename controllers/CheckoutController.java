import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    @PostMapping("/proceed")
    public Map<String, Object> proceedToCheckout(@RequestBody Map<String, Object> checkoutData) {

        Map<String, Object> res = new HashMap<>();
        res.put("status", "success");
        res.put("message", "Checkout started");
        res.put("data", checkoutData);

        // TODO: Save order into database
        // TODO: Redirect to payment processor or order summary

        return res;
    }
}

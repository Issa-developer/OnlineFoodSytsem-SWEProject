package com.example.food_system;

import com.example.food_system.entity.Category;
import com.example.food_system.entity.MenuItem;
import com.example.food_system.entity.Order;
import com.example.food_system.entity.User;
import com.example.food_system.repository.CategoryRepository;
import com.example.food_system.repository.MenuItemRepository;
import com.example.food_system.repository.OrderRepository;
import com.example.food_system.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create admin user first
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setEmail("admin@foodhub.com");
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println("✅ Admin user created: admin / admin123");
        }

        // Create sample customers
        if (userRepository.count() < 10) {
            List<User> customers = Arrays.asList(
                createUser("john_doe", "password123", "john@example.com", "CUSTOMER"),
                createUser("jane_smith", "password123", "jane@example.com", "CUSTOMER"),
                createUser("mike_johnson", "password123", "mike@example.com", "CUSTOMER"),
                createUser("sarah_wilson", "password123", "sarah@example.com", "CUSTOMER"),
                createUser("alex_chen", "password123", "alex@example.com", "CUSTOMER"),
                createUser("emma_brown", "password123", "emma@example.com", "CUSTOMER"),
                createUser("david_lee", "password123", "david@example.com", "CUSTOMER"),
                createUser("lisa_garcia", "password123", "lisa@example.com", "CUSTOMER"),
                createUser("ryan_taylor", "password123", "ryan@example.com", "CUSTOMER"),
                createUser("olivia_martin", "password123", "olivia@example.com", "CUSTOMER")
            );
            userRepository.saveAll(customers);
            System.out.println("✅ Created " + customers.size() + " sample customers");
        }

        // Create categories
        if (categoryRepository.count() == 0) {
            List<Category> categories = Arrays.asList(
                createCategory("Burgers"),
                createCategory("Pizzas"),
                createCategory("Drinks"),
                createCategory("Wraps"),
                createCategory("Sides"),
                createCategory("Starters"),
                createCategory("Salads"),
                createCategory("Tacos"),
                createCategory("Main Course")
            );
            categoryRepository.saveAll(categories);
            System.out.println("✅ Created " + categories.size() + " categories");
        }

        // Create menu items
        if (menuItemRepository.count() < 20) {
            Category burgers = categoryRepository.findByName("Burgers");
            Category pizzas = categoryRepository.findByName("Pizzas");
            Category drinks = categoryRepository.findByName("Drinks");
            Category wraps = categoryRepository.findByName("Wraps");
            Category sides = categoryRepository.findByName("Sides");
            Category starters = categoryRepository.findByName("Starters");
            Category salads = categoryRepository.findByName("Salads");
            Category tacos = categoryRepository.findByName("Tacos");
            Category mains = categoryRepository.findByName("Main Course");

            List<MenuItem> items = Arrays.asList(
                createMenuItem("Classic Burger", burgers, 8.99, 50, true),
                createMenuItem("Cheese Burger", burgers, 9.99, 45, true),
                createMenuItem("Bacon Burger", burgers, 11.99, 30, true),
                createMenuItem("Margherita Pizza", pizzas, 12.99, 40, true),
                createMenuItem("Pepperoni Pizza", pizzas, 14.99, 35, true),
                createMenuItem("Veggie Pizza", pizzas, 13.99, 25, true),
                createMenuItem("Coca Cola", drinks, 2.99, 100, true),
                createMenuItem("Orange Juice", drinks, 3.49, 80, true),
                createMenuItem("Iced Tea", drinks, 2.79, 90, true),
                createMenuItem("Chicken Wrap", wraps, 7.49, 30, true),
                createMenuItem("Veggie Wrap", wraps, 6.99, 25, true),
                createMenuItem("French Fries", sides, 3.99, 60, true),
                createMenuItem("Onion Rings", sides, 4.49, 40, true),
                createMenuItem("Chicken Wings", starters, 8.99, 35, true),
                createMenuItem("Mozzarella Sticks", starters, 6.99, 30, true),
                createMenuItem("Caesar Salad", salads, 7.99, 20, true),
                createMenuItem("Greek Salad", salads, 8.49, 15, true),
                createMenuItem("Beef Tacos", tacos, 9.99, 25, true),
                createMenuItem("Grilled Chicken", mains, 13.99, 20, true),
                createMenuItem("BBQ Ribs", mains, 15.99, 15, true)
            );
            menuItemRepository.saveAll(items);
            System.out.println("✅ Created " + items.size() + " menu items");
        }

        // Create sample orders
        if (orderRepository.count() < 20) {
            Random rand = new Random();
            List<User> customers = userRepository.findByRole("CUSTOMER");
            List<String> statuses = Arrays.asList("Pending", "Preparing", "Ready", "Delivered");
            
            for (int i = 0; i < 20; i++) {
                Order order = new Order();
                if (!customers.isEmpty()) {
                    order.setUser(customers.get(rand.nextInt(customers.size())));
                }
                order.setStatus(statuses.get(rand.nextInt(statuses.size())));
                order.setTotal(15 + rand.nextInt(50)); // $15-$65 orders
                order.setDate(LocalDate.now().minusDays(rand.nextInt(30)));
                orderRepository.save(order);
            }
            System.out.println("✅ Created 20 sample orders");
        }
        
        System.out.println("🎉 Database seeding completed!");
    }

    private User createUser(String username, String password, String email, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole(role);
        return user;
    }

    private Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }

    private MenuItem createMenuItem(String name, Category category, double price, int quantity, boolean available) {
        MenuItem item = new MenuItem();
        item.setName(name);
        item.setCategory(category);
        item.setPrice(price);
        item.setQuantity(quantity);
        item.setAvailable(available);
        return item;
    }
}
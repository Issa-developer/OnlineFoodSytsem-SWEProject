package com.example.food_system.service;

import com.example.food_system.entity.Order;
import com.example.food_system.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.lang.NonNull;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrderStatus(@NonNull Long id, String status) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found with id: " + id);
    }

    public Optional<Order> getOrderById(@NonNull Long id) {
        return orderRepository.findById(id);
    }

        public void deleteOrder(@NonNull Long id) {
            orderRepository.deleteById(id);
        }
}
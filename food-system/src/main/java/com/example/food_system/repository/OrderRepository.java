package com.example.food_system.repository;

import com.example.food_system.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByDate(LocalDate date);
    
    List<Order> findByStatus(String status);
    
    @Query("SELECT o FROM Order o ORDER BY o.date DESC LIMIT :count")
    List<Order> findTop10ByOrderByDateDesc();
    
    @Query("SELECT SUM(o.total) FROM Order o WHERE o.date = :date")
    Double getTotalRevenueByDate(LocalDate date);
}
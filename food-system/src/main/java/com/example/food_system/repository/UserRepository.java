package com.example.food_system.repository;

import com.example.food_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
    List<User> findByRole(String role);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'CUSTOMER'")
    Long countCustomers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'ADMIN'")
    Long countAdmins();
}
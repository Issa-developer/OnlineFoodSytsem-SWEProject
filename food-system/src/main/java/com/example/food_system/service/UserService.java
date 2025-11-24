package com.example.food_system.service;

import com.example.food_system.entity.User;
import com.example.food_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.lang.NonNull;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(@NonNull Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(@NonNull User user) {
        return userRepository.save(user);
    }

    public void deleteUser(@NonNull Long id) {
        userRepository.deleteById(id);
    }
}
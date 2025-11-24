package com.example.food_system.service;

import com.example.food_system.entity.MenuItem;
import com.example.food_system.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    public MenuItem saveMenuItem(MenuItem item) {
        return menuItemRepository.save(item);
    }

    public MenuItem updateMenuItem(MenuItem item) {
        if (item.getId() == null || !menuItemRepository.existsById(item.getId())) {
            throw new RuntimeException("Menu item not found");
        }
        return menuItemRepository.save(item);
    }

    public void deleteMenuItem(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new RuntimeException("Menu item not found");
        }
        menuItemRepository.deleteById(id);
    }

    public Optional<MenuItem> getMenuItemById(Long id) {
        return menuItemRepository.findById(id);
    }
}
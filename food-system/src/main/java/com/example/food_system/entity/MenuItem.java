

package com.example.food_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "menu_items")
public class MenuItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 2, max = 100)
	private String name;


	@Min(0)
	private double price;

	@Min(0)
	private int quantity;

	private boolean available;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@Size(max = 255)
	private String description;

	// Getters and setters
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public double getPrice() { return price; }
	public void setPrice(double price) { this.price = price; }
	public Category getCategory() { return category; }
	public void setCategory(Category category) { this.category = category; }

	public int getQuantity() { return quantity; }
	public void setQuantity(int quantity) { this.quantity = quantity; }

	public boolean isAvailable() { return available; }
	public void setAvailable(boolean available) { this.available = available; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
}

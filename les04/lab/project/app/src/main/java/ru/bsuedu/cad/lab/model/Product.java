package ru.bsuedu.cad.lab.model;

import java.time.LocalDateTime;

/*
 * Класс Product представляет продукт с его свойствами.
 */
public class Product {
    // Уникальный идентификатор продукта.
    private int productId;
    // Название продукта.
    private String name;
    // Описание продукта.
    private String description;
    // Идентификатор категории продукта.
    private int categoryId;
    // Цена продукта.
    private double price;
    // Количество единиц товара на складе.
    private int stockQuantity;
    // URL изображения продукта.
    private String imageUrl;
    // Дата и время создания продукта.
    private LocalDateTime createdAt;
    // Дата и время последнего обновления продукта.
    private LocalDateTime updatedAt;

    public Product(int productId, String name, String description, int categoryId, double price, 
                  int stockQuantity, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        // Инициализация полей продукта.
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Геттеры для доступа к полям продукта.
    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
} 
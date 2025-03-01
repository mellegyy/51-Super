package com.example.model;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cart {
    private UUID id;
    private UUID userId;
    private List<Product> products;

    public Cart() {
        this.id = UUID.randomUUID();
        this.products = new ArrayList<>();
    }

    public Cart(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.products = new ArrayList<>();
    }

    public Cart(UUID id, UUID userId, List<Product> products) {
        this.id = id;
        this.userId = userId;
        this.products = products;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
    }
}

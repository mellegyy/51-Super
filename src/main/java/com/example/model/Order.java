package com.example.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class Order {
    private UUID id;
    private UUID userId;
    private double totalPrice;
    private List<Product> products=new ArrayList<>();


    public Order() {
        this.id = UUID.randomUUID();
        this.totalPrice = products.stream().mapToDouble(Product::getPrice).sum();
    }

    public Order(UUID userId, double totalPrice, List<Product> products) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.products = products;
    }
    public Order(UUID userId, List<Product> products) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.products = products;
    }

    public Order(UUID id, UUID userId, double totalPrice, List<Product> products) {
        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.products = products;
    }

    @JsonProperty("id")
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @JsonProperty("userId")
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @JsonProperty("totalPrice")
    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @JsonProperty("products")
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}

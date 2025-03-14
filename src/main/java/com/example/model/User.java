package com.example.model;

import org.springframework.stereotype.Component;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class User {
    private UUID id;
    private String name;
    private List<Order> orders = new ArrayList<>();

    public User() {
        this.id = UUID.randomUUID();
        this.orders = new ArrayList<>();
    }

    public User(UUID userId, String s) {
        this.id = userId;
        this.name = s;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders != null ? orders : new ArrayList<>();
    }

    @JsonProperty("id")
    public UUID getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("orders")
    public List<Order> getOrders() {
        return orders;
    }

    public void setId(UUID uuid) {
        this.id = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }
}

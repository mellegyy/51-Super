package com.example.model;
import org.springframework.stereotype.Component;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;import com.fasterxml.jackson.annotation.JsonProperty;



@Component
public class User {
    private final UUID id;
    private  String name;
    private  List<Order> orders;

    public User() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders); // Return a copy to ensure immutability
    }
}
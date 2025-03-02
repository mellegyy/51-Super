package com.example.model;
import org.springframework.stereotype.Component;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;import com.fasterxml.jackson.annotation.JsonProperty;



@Component
public class User {
    private  UUID id;
    private  String name;
    private  List<Order> orders;

    public User() {
        this.id = UUID.randomUUID();
    }
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public List<Order> getOrders() {
        return new ArrayList<>(orders); // Return a copy to ensure immutability
    }
}
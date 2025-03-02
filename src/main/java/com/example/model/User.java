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
        this.orders = new ArrayList<>();
    }

    public User(UUID id, String name) {
        this.id = id;
        this.name = name;
        this.orders = new ArrayList<>();
    }

    public void setOrders(List<Order> orders) {
        this.orders = (orders != null) ? orders : new ArrayList<>();
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
        if (this.orders == null) {
            this.orders = new ArrayList<>();
        }
        return orders;
    }
}
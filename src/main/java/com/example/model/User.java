package com.example.model;
import org.springframework.stereotype.Component;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;


@Component
public class User {
    private UUID id;
    private String name;
    private List<Order> orders=new ArrayList<>();


    public List<Order> getOrders() {
        return orders;
    }

    public UUID getId() {
        return id;
    }
}
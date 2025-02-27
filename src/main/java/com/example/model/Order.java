package com.example.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Component
public class Order {
    private UUID id;
    private UUID userId;
    private double totalPrice;
    private List<Product> products=new ArrayList<>();
    public UUID getId() {
        return id;
    }
}
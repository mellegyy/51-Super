package com.example.model;
import org.springframework.stereotype.Component;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Component
public class Product {
    private UUID id;
    private String name;
    private double price;
}
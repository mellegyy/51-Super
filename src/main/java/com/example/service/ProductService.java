package com.example.service;

import com.example.model.Product;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class ProductService extends MainService<Product> {

    ProductRepository productRepository;
    @Autowired

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Product addProduct(Product product){
        return productRepository.addProduct(product);
    }

    public ArrayList<Product> getProducts(){
        return productRepository.getProducts();
    }

    public Product getProductById(UUID productId){
        return productRepository.getProductById(productId);
    }

    public Product updateProduct(UUID productId, String newName, double newPrice){
        return productRepository.updateProduct(productId, newName, newPrice);
    }

    public void applyDiscount(double discount, ArrayList<UUID> productIds){
        productRepository.applyDiscount(discount, productIds);
    }

    public void deleteProductById(UUID productId){
        productRepository.deleteProductById(productId);
    }










//The Dependency Injection Variables
//The Constructor with the requried variables mapping the Dependency Injection.
}


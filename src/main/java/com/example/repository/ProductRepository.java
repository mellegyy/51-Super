package com.example.repository;

import com.example.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class ProductRepository extends MainRepository<Product> {
    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/products.json/";
    }

    @Override
    protected Class<Product[]> getArrayType() {
        return Product[].class;
    }

    public ProductRepository() {

    }
    public Product addProduct (Product product){
        boolean exists = findAll().stream()
                .anyMatch(p -> p.getId().equals(product.getId())
                        || p.getName().equalsIgnoreCase(product.getName()));

        if (!exists) {
            save(product);
            return product; // Product added successfully
        } else {
            return null; // Product already exists, do nothing
        }
    }

    public ArrayList<Product> getProducts(){
        return findAll();
    }

    public Product getProductById(UUID productId){

        return findAll().stream().filter(product -> product.getId().equals(productId))
                .findFirst().orElse(null);
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) {
        ArrayList<Product> products = findAll();

        for (Product product : products) {
            if (product.getId().equals(productId)) {
                product.setName(newName);
                product.setPrice(newPrice);
                saveAll(products); // Save the updated list
                return product; // Return the updated product
            }
        }

        return null; // Product not found
    }

    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }

        ArrayList<Product> products = findAll();

        for (Product product : products) {
            if (productIds.contains(product.getId())) {
                double newPrice = product.getPrice() * (discount / 100.0);
                product.setPrice(newPrice);
            }
        }

        saveAll(products); // Save the updated products
    }

    public void deleteProductById(UUID productId) {
        ArrayList<Product> products = findAll();

        boolean removed = products.removeIf(product -> product.getId().equals(productId));

        if (removed) {
            saveAll(products); // Save the updated list only if a product was removed
        } else {
            throw new RuntimeException("Product with ID " + productId + " not found.");
        }
    }


}


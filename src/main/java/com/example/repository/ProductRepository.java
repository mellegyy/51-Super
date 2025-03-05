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
        if (product.getName() == null || product.getName().trim().isEmpty() || product.getPrice() < 0) {
            throw new IllegalArgumentException("Invalid product data");
        }
        boolean exists = findAll().stream()
                .anyMatch(p -> p.getId().equals(product.getId())
                        || p.getName().equalsIgnoreCase(product.getName()));

        if (!exists) {
            save(product);
            return product; // Product added successfully
        } else {
            throw new RuntimeException("Product with the same ID already exists.");
        }
    }

    public ArrayList<Product> getProducts(){
        return findAll();
    }

    public Product getProductById(UUID productId){
        if (productId == null) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        return findAll().stream().filter(product -> product.getId().equals(productId))
                .findFirst().orElse(null);
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) {


        ArrayList<Product> products = findAll();

        for (Product product : products) {
            if (product.getId().equals(productId)) {
                product.setName(newName);
                product.setPrice(newPrice);
                if (product.getName() == null || product.getName().trim().isEmpty() || product.getPrice() < 0) {
                    throw new IllegalArgumentException("Invalid product data");
                }else {
                    saveAll(products); // Save the updated list
                    return product; // Return the updated product
                }
            }
        }

        throw new RuntimeException("Product not found"); // Throw exception if product does not exist
    }

    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }

        ArrayList<Product> products = findAll();

        boolean allExist = productIds.stream().allMatch(id -> products.stream().anyMatch(p -> p.getId().equals(id)));

        if (!allExist) {
            throw new RuntimeException("Some products not found");
        }

        for (Product product : products) {
            if (productIds.contains(product.getId())) {
                double newPrice = product.getPrice() * (1 - discount / 100.0);
                product.setPrice(newPrice);
            }
        }

        saveAll(products); // Save the updated products
    }

    public void deleteProductById(UUID productId) {

        if (productId == null) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        ArrayList<Product> products = findAll();

        boolean removed = products.removeIf(product -> product.getId().equals(productId));

        if (removed) {
            saveAll(products); // Save the updated list only if a product was removed
        } else {
            throw new RuntimeException("Product not found");
        }
    }


}


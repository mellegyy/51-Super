package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public class CartRepository extends MainRepository<Cart> {

    // Define JSON file path
    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/carts.json"; // Make sure the file path is correct
    }

    // Define the expected array type for deserialization
    @Override
    protected Class<Cart[]> getArrayType() {
        return Cart[].class;
    }

    // 1️⃣ Add New Cart
    public Cart addCart(Cart cart) {
        save(cart); // Use the save() method from MainRepository
        return cart;
    }

    // 2️⃣ Get All Carts
    public ArrayList<Cart> getCarts() {
        return findAll(); // Use the findAll() method from MainRepository
    }

    // 3️⃣ Get a Specific Cart by ID
    public Cart getCartById(UUID cartId) {
        return findAll().stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst()
                .orElse(null);
    }

    // 4️⃣ Get a User's Cart by User ID
    public Cart getCartByUserId(UUID userId) {
        return findAll().stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    // 5️⃣ Add Product to Cart
    public void addProductToCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = findAll();
        for (Cart cart : carts) {
            if (cart.getId().equals(cartId)) {
                cart.addProduct(product);
                saveAll(carts); // Update JSON file
                return;
            }
        }
    }

    // 6️⃣ Delete Product from Cart
    public void deleteProductFromCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = findAll();
        for (Cart cart : carts) {
            if (cart.getId().equals(cartId)) {
                cart.removeProduct(product);
                saveAll(carts); // Update JSON file
                return;
            }
        }
    }

    // 7️⃣ Delete Cart by ID
    public void deleteCartById(UUID cartId) {
        ArrayList<Cart> carts = findAll();
        carts.removeIf(cart -> cart.getId().equals(cartId));
        saveAll(carts); // Update JSON file
    }
}
package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class CartService extends MainService<Cart> {

    private final CartRepository cartRepository;

    // Constructor for Dependency Injection
    public CartService(CartRepository cartRepository) {
        super(cartRepository); // Pass the repository to MainService
        this.cartRepository = cartRepository;
    }

    // 1️⃣ Add a New Cart
    public Cart addCart(Cart cart) {
        return cartRepository.addCart(cart);
    }

    // 2️⃣ Get All Carts
    public ArrayList<Cart> getCarts() {
        return cartRepository.getCarts();
    }

    // 3️⃣ Get a Specific Cart by ID
    public Cart getCartById(UUID cartId) {
        return cartRepository.getCartById(cartId);
    }

    // 4️⃣ Get a User's Cart
    public Cart getCartByUserId(UUID userId) {
        return cartRepository.getCartByUserId(userId);
    }

    // 5️⃣ Add Product to Cart
    public void addProductToCart(UUID cartId, Product product) {
        cartRepository.addProductToCart(cartId, product);
    }

    // 6️⃣ Remove Product from Cart
    public void deleteProductFromCart(UUID cartId, Product product) {
        cartRepository.deleteProductFromCart(cartId, product);
    }

    // 7️⃣ Delete Cart by ID
    public void deleteCartById(UUID cartId) {
        cartRepository.deleteCartById(cartId);
    }
}

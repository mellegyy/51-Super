package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class CartService extends MainService<Cart> {

    @Autowired
    private CartRepository cartRepository;

    public CartService() {
        super();
    }

    public Cart addCart(Cart cart) {
        if (cart == null) {
            throw new NullPointerException("Cart cannot be null");
        }
        return cartRepository.addCart(cart);
    }


    public ArrayList<Cart> getCarts() {
        return cartRepository.getCarts();
    }

    public Cart getCartById(UUID cartId) {
        return cartRepository.getCartById(cartId);
    }

    public Cart getCartByUserId(UUID userId) {
        return cartRepository.getCartByUserId(userId);
    }

    public void addProductToCart(UUID cartId, Product product) {
        cartRepository.addProductToCart(cartId, product);
    }

    public void deleteProductFromCart(UUID cartId, Product product) {
        cartRepository.deleteProductFromCart(cartId, product);
    }


    public void deleteCartById(UUID cartId) {
        cartRepository.deleteCartById(cartId);
    }
}

package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class CartRepository extends MainRepository<Cart> {
    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/carts.json";
    }
    @Override
    protected Class<Cart[]> getArrayType() {
        return Cart[].class;
    }
    public Cart addCart(Cart cart) {
        save(cart); // Using the save() method from MainRepository
        return cart;
    }

    public ArrayList<Cart> getCarts() {
        return findAll(); // Using the findAll() method from MainRepository
    }

    public Cart getCartById(UUID cartId) {
        return findAll().stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst()
                .orElse(null);
    }

    public Cart getCartByUserId(UUID userId) {
        return findAll().stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public void addProductToCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = findAll();
        for (Cart cart : carts) {
            if (cart.getId().equals(cartId)) {
                cart.getProducts().add(product);
                saveAll(carts); // Update JSON file
                return;
            }
        }
    }

    public void deleteProductFromCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = findAll();
        for (Cart cart : carts) {
            if (cart.getId().equals(cartId)) {
                cart.getProducts().removeIf(p -> p.getId().equals(product.getId()));
                saveAll(carts); // Update JSON file
                return;
            }
        }
    }

    public void deleteCartById(UUID cartId) {
        ArrayList<Cart> carts = findAll();
        carts.removeIf(cart -> cart.getId().equals(cartId));
        saveAll(carts); // Update JSON file
    }

//    public void updateCart(Cart updatedCart) {
//        ArrayList<Cart> carts = findAll();
//        for (int i = 0; i < carts.size(); i++) {
//            if (carts.get(i).getId().equals(updatedCart.getId())) {
//                carts.set(i, updatedCart);
//                saveAll(carts);
//                return;
//            }
//        }
//    }
}
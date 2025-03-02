package com.example.service;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.CartRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class UserService extends MainService<User>{

    private UserRepository userRepository;
    private CartRepository cartRepository;
    private ProductRepository productRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    public ArrayList<User> getUsers() {
        return userRepository.getUsers();
    }

    public User getUserById(UUID userId) {
        return userRepository.getUserById(userId);
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        return userRepository.getOrdersByUserId(userId);
    }

    public void addOrderToUser(UUID userId) {} //Call Methods from CartService


    public void addProductToCart(UUID userId, UUID productId) {
        Cart userCart = cartRepository.getCartByUserId(userId);
        Product product = productRepository.getProductById(productId);

        if (userCart != null && product != null) {
            userCart.addProduct(product);
            cartRepository.updateCart(userCart);
        }
    }
    public void emptyCart(UUID userId) {
        Cart userCart = cartRepository.getCartByUserId(userId);
        if (userCart != null) {
            userCart.getProducts().clear();
            cartRepository.updateCart(userCart);
        }
    } //Call Methods from CartService

    public void deleteProductFromCart(UUID userId, UUID productId) {
        Cart userCart = cartRepository.getCartByUserId(userId);
        if (userCart != null) {
            userCart.removeProduct(new Product(productId));
            cartRepository.updateCart(userCart);
        }
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) {
        userRepository.removeOrderFromUser(userId, orderId);
    }

    public void deleteUserById(UUID userId) {
           userRepository.deleteUserById(userId);
    }
}

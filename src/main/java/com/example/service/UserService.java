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
    public UserService(UserRepository userRepository, CartRepository cartRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    public ArrayList<User> getUsers() {
        return userRepository.getUsers();
    }

    public User getUserById(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepository.getUserById(userId);
    }


    public List<Order> getOrdersByUserId(UUID userId) {
        if(userId == null)
            throw new IllegalArgumentException("User ID cannot be null");
        return userRepository.getOrdersByUserId(userId);
    }

    public void addOrderToUser(UUID userId) {
       return ;
    } //Call Methods from CartService


    public String addProductToCart(UUID userId, UUID productId) {
        Cart userCart = cartRepository.getCartByUserId(userId);
        Product product = productRepository.getProductById(productId);

        if (userCart == null) {
            userCart = new Cart(userId);
            cartRepository.save(userCart);
        }

        if (product == null) {
            return "Product not found";
        }

        userCart.addProduct(product);
        cartRepository.updateCart(userCart);
        return "Product added to cart";
    }
    public String emptyCart(UUID userId) {
        Cart userCart = cartRepository.getCartByUserId(userId);
        if (userCart != null) {
            userCart.getProducts().clear();
            cartRepository.updateCart(userCart);
            return "Cart emptied successfully";
        }
        return "Cart not found";
    }

    public String deleteProductFromCart(UUID userId, UUID productId) {
        System.out.println("Checking cart for userId: " + userId);
        Cart userCart = cartRepository.getCartByUserId(userId);

        if (userCart == null) {
            System.out.println("No cart found for userId: " + userId);
            return "Cart is empty";
        }
        System.out.println("Cart found: " + userCart);

        System.out.println("Cart before removal: " + userCart.getProducts());
        for (Product p : userCart.getProducts()) {
            System.out.println("Product in cart: " + p.getId());
        }
        System.out.println("Requested removal productId: " + productId);

        boolean removed = userCart.removeProduct(new Product(productId));

        if (!removed) {
            return "Product not found in cart"; // This means the product is not in the list
        }

        cartRepository.updateCart(userCart);
        System.out.println("Cart after removal: " + userCart.getProducts());
        return "Product deleted from cart";
    }


    public void removeOrderFromUser(UUID userId, UUID orderId) {
        userRepository.removeOrderFromUser(userId, orderId);
    }

    public void deleteUserById(UUID userId) {
           userRepository.deleteUserById(userId);
    }
}

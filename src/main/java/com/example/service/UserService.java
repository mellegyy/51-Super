package com.example.service;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.CartRepository;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service
@SuppressWarnings("rawtypes")
public class UserService extends MainService<User>{

    private UserRepository userRepository;
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    @Autowired
    public UserService(UserRepository userRepository, CartRepository cartRepository,
                       ProductRepository productRepository,OrderRepository  orderRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
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
        User user = userRepository.getUserById(userId);
        if (userId == null) {
            throw new IllegalStateException("User ID is Null");
        }
        if (user == null) {
            throw new IllegalStateException("invalid user ID");
        }
        Cart userCart = cartRepository.getCartByUserId(userId);
        if (userCart == null || userCart.getProducts().isEmpty()) {
            throw new IllegalStateException("Cart is empty, cannot create an order.");
        }

        List<Product> validatedProducts = new ArrayList<>();
        for (Product product : userCart.getProducts()) {
            Product foundProduct = productRepository.getProductById(product.getId());
            if (foundProduct == null) {
                productRepository.addProduct(product);
                foundProduct = product;
            }
            validatedProducts.add(foundProduct);
        }
        double totalPrice = validatedProducts.stream().mapToDouble(Product::getPrice).sum();
        Order newOrder = new Order(UUID.randomUUID(), userId, totalPrice, validatedProducts);
        orderRepository.addOrder(newOrder);
        List<Order> allOrders = orderRepository.getOrders();
        System.out.println("📦 Debug: Orders in system after adding:");
        for (Order o : allOrders) {
            System.out.println("🛒 Order ID: " + o.getId() + " | User ID: " + o.getUserId());
        }
        if (user.getOrders() == null) {
            user.setOrders(new ArrayList<>());
        }
        userRepository.addOrderToUser(userId, newOrder);
        User updatedUserCheck = userRepository.getUserById(userId);
        userCart.setProducts(new ArrayList<>());
        cartRepository.deleteCartById(userCart.getId());
        cartRepository.addCart(userCart);
    }

    public void emptyCart(UUID userId) {
        if (userId == null) {
            throw new NullPointerException("User ID cannot be null");
        }
        Cart userCart = cartRepository.getCartByUserId(userId);
        if (userCart != null) {
            cartRepository.deleteCartById(userCart.getId());
            userCart.getProducts().clear();
            cartRepository.addCart(userCart);
        }
    }

    public void removeOrderFromUser(UUID userId, UUID orderId){
        userRepository.removeOrderFromUser(userId, orderId);
    }


    public void deleteUserById(UUID userId) {
           userRepository.deleteUserById(userId);
    }
}

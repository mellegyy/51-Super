package com.example.controller;

import com.example.model.Order;
import com.example.model.User;
import com.example.model.Product;
import com.example.model.Cart;
import com.example.repository.CartRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import com.example.service.CartService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;



    public UserController(UserService userService, CartRepository cartRepository, ProductRepository productRepository) {
        this.userService = userService;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }


    @PostMapping("/")
    public User addUser(@RequestBody User user) {
       return userService.addUser(user);
    }//done

    @GetMapping("/")
    public ArrayList<User> getUsers(){
        return userService.getUsers();
    }//done

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId) {
        return userService.getUserById(userId);
    }//done

    @GetMapping("/{userId}/orders")
    public List<Order> getOrdersByUserId(@PathVariable UUID userId) {
        return userService.getOrdersByUserId(userId);
    }//done

    @PostMapping("/{userId}/checkout")
    public String addOrderToUser(@PathVariable UUID userId) {
        if (userId == null) {
            return "Invalid user ID";
        }
        try {
            userService.addOrderToUser(userId);
            return "Order added successfully";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/{userId}/removeOrder")
    public String removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID orderId) {

        try {
            userService.removeOrderFromUser(userId, orderId);
            return "Order removed successfully";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }//done


    @DeleteMapping("/{userId}/emptyCart")
    public String emptyCart(@PathVariable UUID userId) {
        userService.emptyCart(userId);
        return "Cart emptied successfully";
    }

    @PutMapping("/addProductToCart")
    public String addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        try {
            Product product = productRepository.getProductById(productId);
            if (product == null) {
                return "Error: Product not found";
            }
            Cart userCart = cartRepository.getCartByUserId(userId);
            if (userCart == null) {
                userCart = new Cart(userId);
                cartRepository.addCart(userCart);
            }
            cartRepository.addProductToCart(userCart.getId(), product);
            return "Product added to cart";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }


    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        try {
            Cart userCart = cartRepository.getCartByUserId(userId);
            if (userCart == null || userCart.getProducts().isEmpty()) {
                return "Cart is empty";
            }
            Product product = productRepository.getProductById(productId);
            if (product == null) {
                return "Error: Product not found";
            }
            boolean removed = userCart.getProducts().removeIf(p -> p.getId().equals(productId));
            if (!removed) {
                return "Product not found in cart";
            }
            cartRepository.deleteCartById(userCart.getId());
            cartRepository.addCart(userCart);
            return "Product deleted from cart";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }



    @DeleteMapping("/delete/{userId}")
    public String deleteUserById(@PathVariable UUID userId) {
        try {
            userService.deleteUserById(userId);
            return "User deleted successfully";
        } catch (RuntimeException e) {
            return "User not found";
        }
    }//done
}





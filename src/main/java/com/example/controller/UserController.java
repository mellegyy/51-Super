package com.example.controller;

import com.example.model.Order;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
        try {
            userService.addOrderToUser(userId);
            return "Order Added Successfully";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/{userId}/removeOrder")
    public String removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID orderId) {

        try {
            userService.removeOrderFromUser(userId, orderId);
            return "Order Removed Successfully";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }//done


    @DeleteMapping("/{userId}/emptyCart")
    public String emptyCart(@PathVariable UUID userId) {
        userService.emptyCart(userId);
        return "Cart emptied successfully for user: " + userId;
    }

    @PutMapping("/addProductToCart")
    public String addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        userService.addProductToCart(userId, productId);
        return "Product added successfully to cart for user: " + userId;
    }

    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        userService.deleteProductFromCart(userId, productId);
        return "Product removed successfully from cart for user: " + userId;
    }

    @DeleteMapping("/delete/{userId}")
    public String deleteUserById(@PathVariable UUID userId) {
        try {
            userService.deleteUserById(userId);
            return "User Removed Successfully";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }//done
}





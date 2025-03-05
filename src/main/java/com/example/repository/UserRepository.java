package com.example.repository;

import com.example.model.Order;
import com.example.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@SuppressWarnings("rawtypes")
public class UserRepository extends MainRepository<User> {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/users.json/";
    }

    public void saveAll(List<User> users) {
        try {
            objectMapper.writeValue(new File(getDataPath()), users);
        } catch (IOException e) {
            throw new RuntimeException("Error saving users data", e);
        }
    }

    @Override
    protected Class<User[]> getArrayType() {
        return User[].class;
    }
    public UserRepository() {
    }

    public ArrayList<User> getUsers() {
        return findAll();
    }

    public User getUserById(UUID userId) {
        return findAll().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public User addUser(User user) {
        if (user == null) {
            throw new NullPointerException("User cannot be null");
        }

        if (user.getOrders() == null) {
            user.setOrders(new ArrayList<>());  // Prevent null issues
        }

        ArrayList<User> users = findAll();
        boolean exists = users.stream()
                .anyMatch(u -> u.getId().equals(user.getId())
                        || u.getName().trim().equalsIgnoreCase(user.getName().trim()));

        if (exists) {
            throw new RuntimeException("User already exists");
        }
        if (user.getOrders() == null) {
            user.setOrders(new ArrayList<>());
        }

        System.out.println("✅ Debug: User saved successfully with " + user.getOrders().size() + " orders.");
        users.add(user);
        saveAll(users);
        return user;
    }



    public List<Order> getOrdersByUserId(UUID userId) {
        User user = getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        if (user.getOrders() == null) {
            user.setOrders(new ArrayList<>());  // Prevents null pointer exceptions
        }
        System.out.println("📦 Debug: Retrieved " + user.getOrders().size() + " orders for user " + userId);

        return user.getOrders();
    }

    public void addOrderToUser(UUID userId, Order order) {
        ArrayList<User> users = findAll();
        User user = getUserById(userId);
        if (user != null) {
            user.getOrders().add(order);
            System.out.println("Order added successfully for user: " + user.getName());
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId().equals(userId)) {
                    users.set(i, user);
                    break;
                }
            }
            saveAll(users);
            System.out.println("Users list updated successfully!");
        } else {
            System.out.println("Order Canceled: User Not Found");
        }
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) {
        User user = getUserById(userId);
        if(userId == null){
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        List<Order> orders = user.getOrders().stream()
                .filter(order -> !order.getId().equals(orderId))
                .collect(Collectors.toList());

        if (orders.size() == user.getOrders().size()) {
            throw new IllegalArgumentException("Order not found");
        }
        user.setOrders(orders);
        deleteOrderById(orderId);
        save(user);
        System.out.println("Order removed successfully!");
    }

    public void deleteOrderById(UUID orderId) {
        OrderRepository orderRepository = new OrderRepository();
        orderRepository.deleteOrderById(orderId);
    }

    public void deleteUserById(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        ArrayList<User> users = findAll();

        boolean removed = users.removeIf(user -> user.getId().equals(userId));

        if (removed) {
            saveAll(users);
            System.out.println("User deleted successfully");
        } else {
            throw new RuntimeException("User not found");
        }
    }
}

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

        ArrayList<User> users = findAll();
        boolean exists = users.stream()
                .anyMatch(u -> u.getId().equals(user.getId())
                        || u.getName().trim().equalsIgnoreCase(user.getName().trim()));

        if (exists) {
            throw new RuntimeException("User already exists");
        }

        users.add(user);
        saveAll(users);
        return user;
    }


    public List<Order> getOrdersByUserId(UUID userId) {
        User user = getUserById(userId);
        return user != null ? user.getOrders() : null;
    }

    public void addOrderToUser(UUID userId, Order order) {
        ArrayList<User> users = findAll();
        User user = getUserById(userId);
        if (user != null) {
            user.getOrders().add(order);
            saveAll(users);
            System.out.println("Order added successfully");
        } else {
            System.out.println("Order addition failed: User not found");
        }
        System.out.println("Order Canceled: User Not Found");
    }
    public void removeOrderFromUser(UUID userId, UUID orderId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        ArrayList<User> users = findAll();

        User user = users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElse(null);

        if (user == null) {
            throw new RuntimeException("User not found");
        }
        List<Order> updatedOrders = new ArrayList<>(user.getOrders());
        boolean removedFromUser = updatedOrders.removeIf(order -> {
            return order.getId().equals(orderId) && order.getUserId().equals(userId);
        });

        if (removedFromUser) {
            user.setOrders(updatedOrders);
            saveAll(users);
            deleteOrderById(orderId);

        } else {
            throw new RuntimeException("Order not found");
        }
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

package com.example.repository;

import com.example.model.Order;
import com.example.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;  // ✅ Correct import
import org.springframework.context.annotation.Primary;
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
        return "src/main/data/users.json";
    }

    @Override
    protected Class<User[]> getArrayType() {
        return User[].class;
    }

    public UserRepository() {
    }

    public ArrayList<User> getUsers() {
        try (InputStream inputStream = new ClassPathResource(getDataPath()).getInputStream()) {
            User[] usersArray = objectMapper.readValue(inputStream, getArrayType());
            return new ArrayList<>(Arrays.asList(usersArray));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load users from file", e);
        }
    }

    public User getUserById(UUID userId) {
        for (User user : getUsers()) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public User addUser(User user) {
        try {
            ArrayList<User> users = getUsers();

            if (users.contains(user)) {
                throw new RuntimeException("User Already Added");
            }
            users.add(user);
            writeUsersToFile(users);
            return user;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save user", e);
        }
    }

    private void writeUsersToFile(List<User> users) throws IOException {
        File file = new FileSystemResource("src/main/resources/" + getDataPath()).getFile(); // Use a writable path
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, users);
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        User user = getUserById(userId);
        if (user != null) {
            return user.getOrders();
        }
        return null;
    }

    public void addOrderToUser(UUID userId, Order order) {
        ArrayList<User> users = getUsers();
        User user = getUserById(userId);
        if (user != null) {
            user.getOrders().add(order);
            try {
                writeUsersToFile(users); //to Write the Updated Order for the User
                System.out.println("Order Saved");
            } catch (IOException e) {
                throw new RuntimeException("Failed to save order", e);
            }
        }
        System.out.println("Order Canceled: User Not Found");
    }


    public void removeOrderFromUser(UUID userId, UUID orderId) {
        ArrayList<User> users = getUsers();
        User user = getUserById(userId);
        List<Order> orders = getOrdersByUserId(userId);
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                user.getOrders().remove(order);
                try {
                    writeUsersToFile(users); //to Write the Updated User After Deletion
                    System.out.println("Order Saved");
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save order", e);
                }
                break;
            }
        }
        System.out.println("Order Canceled: User Not Found");
    }

    public void deleteUserById(UUID userId) {
        try {
            ArrayList<User> users = getUsers();
            User user = getUserById(userId);
            users.remove(user);
            writeUsersToFile(users);
            System.out.println("User Deleted");
        } catch (IOException e) {
            throw new RuntimeException("User Not Found", e);
        }
    }
}

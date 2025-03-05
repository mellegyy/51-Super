package com.example.repository;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class OrderRepository extends MainRepository<Order> {
    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/orders.json/";
    }


    @Override
    protected Class<Order[]> getArrayType() { return Order[].class; }

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;


    public OrderRepository() {

    }

    public void addOrder(Order order) {
        // Ensure user exists
        User user = userRepository.getUserById(order.getUserId());
        if (user == null) {
            userRepository.addUser(new User(order.getUserId(), "Auto-Created User"));
        }

        // Ensure all products exist
        boolean productExists = order.getProducts().stream()
                .allMatch(product -> productRepository.getProductById(product.getId()) != null);

        if (!productExists) {
            throw new RuntimeException("One or more products in the order do not exist.");
        }

        boolean orderExists = findAll().stream().anyMatch(o -> o.getId().equals(order.getId()));
        if (orderExists) {
            throw new RuntimeException("Order with the same ID already exists.");
        }

        order.setTotalPrice(order.getProducts().stream().mapToDouble(Product::getPrice).sum());
        save(order);
    }

    public ArrayList<Order> getOrders(){
        return findAll();
    }

    public Order getOrderById(UUID orderId){
        if (orderId == null) {
            throw new IllegalArgumentException("Invalid order ID");
        }
        return findAll().stream().filter(order -> order.getId().equals(orderId))
                .findFirst().orElse(null);
    }

    public void deleteOrderById(UUID orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Invalid order ID");
        }
        ArrayList<Order> orders = findAll();

        boolean removed = orders.removeIf(order -> order.getId().equals(orderId));

        if (removed) {
            saveAll(orders); // Save the updated list only if a product was removed
        } else {
            throw new RuntimeException("Order not found");
        }
    }

}
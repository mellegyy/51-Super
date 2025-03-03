package com.example.repository;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
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


    public OrderRepository() {

    }

    public void addOrder(Order order){
        boolean exists = findAll().stream().anyMatch(o -> o.getId().equals(order.getId()));
        ProductRepository productRepository = new ProductRepository();
        UserRepository userRepository = new UserRepository();
        List<Product> allProducts = productRepository.getProducts();
        List<User> allUsers = userRepository.getUsers();

        boolean productExists = order.getProducts().stream()
                .allMatch(orderProduct -> allProducts.stream()
                        .anyMatch(dbProduct -> dbProduct.getId().equals(orderProduct.getId())));
        boolean userExists = allUsers.stream()
                .anyMatch(dbUser -> dbUser.getId().equals(order.getUserId()));

//        if (!userExists) {
//            throw new RuntimeException("User not found.");
//        }
        if (!productExists) {
            throw new RuntimeException("One or more products in the order do not exist.");
        }
        if (!exists) {
            order.setTotalPrice(order.getProducts().stream().mapToDouble(Product::getPrice).sum());
            save(order);
        } else {
            throw new RuntimeException("Order with ID " + order.getId() + " already exists.");
        }
    }

    public ArrayList<Order> getOrders(){
        return findAll();
    }

    public Order getOrderById(UUID orderId){
        return findAll().stream().filter(order -> order.getId().equals(orderId))
                .findFirst().orElse(null);
    }

    public void deleteOrderById(UUID orderId) {
        ArrayList<Order> orders = findAll();

        boolean removed = orders.removeIf(order -> order.getId().equals(orderId));

        if (removed) {
            saveAll(orders); // Save the updated list only if a product was removed
        } else {
            throw new RuntimeException("Order not found");
        }
    }




}
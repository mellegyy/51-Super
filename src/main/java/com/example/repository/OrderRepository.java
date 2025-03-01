package com.example.repository;
import com.example.model.Order;
import com.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

        if (!exists) {
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
            throw new RuntimeException("Product with ID " + orderId + " not found.");
        }
    }




}

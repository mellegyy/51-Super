package com.example.MiniProject1;

import static org.junit.jupiter.api.Assertions.*;

import com.example.model.Order;
import com.example.model.Product;
import com.example.repository.OrderRepository;
import com.example.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class OrderServiceTest {
    private OrderRepository orderRepository;
    private OrderService orderService;
    private static final String ORDER_JSON_PATH = "src/main/java/com/example/data/orders.json";

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository();
        orderService = new OrderService(orderRepository);
    }

    @AfterEach
    void cleanUp() {
        try (FileWriter writer = new FileWriter(ORDER_JSON_PATH)) {
            writer.write("[]");  // Reset file content to an empty JSON array
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to reset JSON file after test execution.");
        }
    }

//    @Test
//    void testAddOrder_Success() {
//        UUID userId = UUID.randomUUID();
//        List<Product> products = new ArrayList<>();
//        products.add(new Product(UUID.randomUUID(), "Phone", 800.00));
//
//        Order order = new Order(UUID.randomUUID(), userId, 800.00, products);
//        orderService.addOrder(order);
//
//        Order fetchedOrder = orderService.getOrderById(order.getId());
//        assertNotNull(fetchedOrder);
//        assertEquals(800.00, fetchedOrder.getTotalPrice());
//    }

    @Test
    void testGetOrders_EmptyList() {
        List<Order> result = orderService.getOrders();
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetOrders_NonEmptyList() {
        UUID userId = UUID.randomUUID();
        orderService.addOrder(new Order(UUID.randomUUID(), userId, 150.00, new ArrayList<>()));

        List<Order> result = orderService.getOrders();
        assertFalse(result.isEmpty());
    }

//    @Test
//    void testGetOrderById_Valid() {
//        UUID userId = UUID.randomUUID();
//        Order order = new Order(UUID.randomUUID(), userId, 500.00, new ArrayList<>());
//
//        orderService.addOrder(order);
//        Order fetchedOrder = orderService.getOrderById(order.getId());
//
//        assertNotNull(fetchedOrder);
//        assertEquals(500.00, fetchedOrder.getTotalPrice());
//    }

    @Test
    void testGetOrderById_NotFound() {
        Order result = orderService.getOrderById(UUID.randomUUID());
        assertNull(result);
    }

    @Test
    void testDeleteOrder_Success() {
        UUID userId = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), userId, 200.00, new ArrayList<>());

        orderService.addOrder(order);
        orderService.deleteOrderById(order.getId());

        assertNull(orderService.getOrderById(order.getId()));
    }

    @Test
    void testDeleteOrder_InvalidId() {
        UUID invalidId = UUID.randomUUID();
        Exception exception = assertThrows(RuntimeException.class, () -> orderService.deleteOrderById(invalidId));
        assertEquals("Order not found.", exception.getMessage());
    }
}
package com.example.MiniProject1;

import static org.junit.jupiter.api.Assertions.*;

import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.CartRepository;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import com.example.service.CartService;
import com.example.service.OrderService;
import com.example.service.ProductService;
import com.example.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class OrderServiceTests {
    private OrderRepository orderRepository;
    private OrderService orderService;
    private ProductRepository productRepository;
    private ProductService productService;
    private UserRepository userRepository;
    private UserService userService;
    private CartRepository cartRepository;
    private static final String ORDER_JSON_PATH = "src/main/java/com/example/data/orders.json";
    private static final String PRODUCT_JSON_PATH = "src/main/java/com/example/data/products.json";
    private static final String USER_JSON_PATH = "src/main/java/com/example/data/users.json";
    private static final String CART_JSON_PATH = "src/main/java/com/example/data/carts.json";
    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository();
        orderService = new OrderService(orderRepository);
        productRepository = new ProductRepository();
        productService = new ProductService(productRepository);
        userRepository = new UserRepository();
        userService = new UserService(userRepository, cartRepository, productRepository, orderRepository);
    }

    @AfterEach
    void cleanUp() {
        try (FileWriter writer = new FileWriter(ORDER_JSON_PATH)) {
            writer.write("[]");  // Reset file content to an empty JSON array
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to reset JSON file after test execution.");
        }
        try (FileWriter writer = new FileWriter(PRODUCT_JSON_PATH)) {
            writer.write("[]");  // Reset file content to an empty JSON array
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to reset JSON file after test execution.");
        }
        try (FileWriter writer = new FileWriter(USER_JSON_PATH)) {
            writer.write("[]");  // Reset file content to an empty JSON array
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to reset JSON file after test execution.");
        }
        try (FileWriter writer = new FileWriter(CART_JSON_PATH)) {
            writer.write("[]");  // Reset file content to an empty JSON array
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to reset JSON file after test execution.");
        }
    }

    @Test
    void testAddOrder_Success() {
        User user = new User();
        UUID userId = user.getId();
        userService.addUser(user);
        List<Product> products = new ArrayList<>();
        Product product = new Product(UUID.randomUUID(), "Phone", 800.00);
        productService.addProduct(product);
        products.add(product);


        Order order = new Order(UUID.randomUUID(), userId, 800.00, products);
        orderService.addOrder(order);

        Order fetchedOrder = orderService.getOrderById(order.getId());
        assertNotNull(fetchedOrder);
        assertEquals(800.00, fetchedOrder.getTotalPrice());
    }

    @Test
    void testAddOrder_DuplicateId() {
        User user = new User();
        UUID userId = user.getId();
        userService.addUser(user);
        UUID id = UUID.randomUUID();
        List<Product> products = new ArrayList<>();
        Order order1 = new Order(id, userId, 800.00, products);
        Order order2 = new Order(id, userId, 800.00, products);


        orderService.addOrder(order1);
        Exception exception = assertThrows(RuntimeException.class, () -> orderService.addOrder(order2));
        assertEquals("Order with the same ID already exists.", exception.getMessage());
    }

    @Test
    void testAddOrder_InvalidUserId() {
        UUID invalidUserId = null; // Simulating missing user ID
        List<Product> products = new ArrayList<>();
        products.add(new Product(UUID.randomUUID(), "Phone", 800.00));

        Order order = new Order(UUID.randomUUID(), invalidUserId, 800.00, products);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(order));
        assertEquals("User not found.", exception.getMessage());
    }

//    @Test
//    void testAddOrder_EmptyProductList() {
//        UUID userId = UUID.randomUUID();
//        Order order = new Order(UUID.randomUUID(), userId, 0.00, new ArrayList<>());
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(order));
//        assertEquals("Order must contain at least one product.", exception.getMessage());
//    }

    @Test
    void testGetOrders_EmptyList() {
        List<Order> result = orderService.getOrders();
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetOrders_NonEmptyList() {
        User user = new User();
        UUID userId = user.getId();
        userService.addUser(user);
        orderService.addOrder(new Order(UUID.randomUUID(), userId, 150.00, new ArrayList<>()));

        List<Order> result = orderService.getOrders();
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetOrders_MatchesExpectedData() {
        User user = new User();
        UUID userId = user.getId();
        userService.addUser(user);
        UUID order1Id = UUID.randomUUID();
        UUID order2Id = UUID.randomUUID();

        Order order1 = new Order(order1Id, userId, 0.00, new ArrayList<>());
        Order order2 = new Order(order2Id, userId, 0.00, new ArrayList<>());

        orderService.addOrder(order1);
        orderService.addOrder(order2);

        List<Order> orders = orderService.getOrders();
        assertEquals(2, orders.size());
        assertTrue(orders.get(0).getId().equals(order1Id));
        assertTrue(orders.get(1).getId().equals(order2Id));
    }

    @Test
    void testGetOrderById_Valid() {
        User user = new User();
        UUID userId = user.getId();
        userService.addUser(user);
        Order order = new Order(UUID.randomUUID(), userId, 0.00, new ArrayList<>());

        orderService.addOrder(order);
        Order fetchedOrder = orderService.getOrderById(order.getId());

        assertNotNull(fetchedOrder);
        assertEquals(0.00, fetchedOrder.getTotalPrice());
    }

    @Test
    void testGetOrderById_NotFound() {
        Order result = orderService.getOrderById(UUID.randomUUID());
        assertNull(result);
    }

    @Test
    void testGetOrderById_NullId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.getOrderById(null));
        assertEquals("Invalid order ID", exception.getMessage());
    }

    @Test
    void testDeleteOrder_Success() {
        User user = new User();
        UUID userId = user.getId();
        userService.addUser(user);
        Order order = new Order(UUID.randomUUID(), userId, 200.00, new ArrayList<>());

        orderService.addOrder(order);
        orderService.deleteOrderById(order.getId());

        assertNull(orderService.getOrderById(order.getId()));
    }

    @Test
    void testDeleteOrder_InvalidId() {
        UUID invalidId = UUID.randomUUID();
        Exception exception = assertThrows(RuntimeException.class, () -> orderService.deleteOrderById(invalidId));
        assertEquals("Order not found", exception.getMessage());
    }

    @Test
    void testDeleteOrder_NullId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.deleteOrderById(null));
        assertEquals("Invalid order ID", exception.getMessage());
    }
}

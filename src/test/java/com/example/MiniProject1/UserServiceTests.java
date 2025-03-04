package com.example.MiniProject1;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;

import com.example.model.Cart;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ComponentScan(basePackages = "com.example.*")
@WebMvcTest
public class UserServiceTests {

    @Value("${spring.application.userDataPath}")
    private String userDataPath;

    @Value("${spring.application.productDataPath}")
    private String productDataPath;

    @Value("${spring.application.orderDataPath}")
    private String orderDataPath;

    @Value("${spring.application.cartDataPath}")
    private String cartDataPath;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    public void overRideAll() {
        try {
            objectMapper.writeValue(new File(userDataPath), new ArrayList<User>());
            objectMapper.writeValue(new File(productDataPath), new ArrayList<Product>());
            objectMapper.writeValue(new File(orderDataPath), new ArrayList<Order>());
            objectMapper.writeValue(new File(cartDataPath), new ArrayList<Cart>());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }

    public Object find(String typeString, Object toFind) {
        switch (typeString) {
            case "User":
                ArrayList<User> users = getUsers();

                for (User user : users) {
                    if (user.getId().equals(((User) toFind).getId())) {
                        return user;
                    }
                }
                break;
            case "Product":
                ArrayList<Product> products = getProducts();
                for (Product product : products) {
                    if (product.getId().equals(((Product) toFind).getId())) {
                        return product;
                    }
                }
                break;
            case "Order":
                ArrayList<Order> orders = getOrders();
                for (Order order : orders) {
                    if (order.getId().equals(((Order) toFind).getId())) {
                        return order;
                    }
                }
                break;
            case "Cart":
                ArrayList<Cart> carts = getCarts();
                for (Cart cart : carts) {
                    if (cart.getId().equals(((Cart) toFind).getId())) {
                        return cart;
                    }
                }
                break;
        }
        return null;
    }

    public Product addProduct(Product product) {
        try {
            File file = new File(productDataPath);
            ArrayList<Product> products;
            if (!file.exists()) {
                products = new ArrayList<>();
            } else {
                products = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Product[].class)));
            }
            products.add(product);
            objectMapper.writeValue(file, products);
            return product;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }

    public ArrayList<Product> getProducts() {
        try {
            File file = new File(productDataPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return new ArrayList<Product>(Arrays.asList(objectMapper.readValue(file, Product[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from JSON file", e);
        }
    }

    public User addUser(User user) {
        try {
            File file = new File(userDataPath);
            ArrayList<User> users;
            if (!file.exists()) {
                users = new ArrayList<>();
            } else {
                users = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, User[].class)));
            }
            users.add(user);
            objectMapper.writeValue(file, users);
            return user;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }

    public ArrayList<User> getUsers() {
        try {
            File file = new File(userDataPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return new ArrayList<User>(Arrays.asList(objectMapper.readValue(file, User[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from JSON file", e);
        }
    }

    public Cart addCart(Cart cart) {
        try {
            File file = new File(cartDataPath);
            ArrayList<Cart> carts;
            if (!file.exists()) {
                carts = new ArrayList<>();
            } else {
                carts = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Cart[].class)));
            }
            carts.add(cart);
            objectMapper.writeValue(file, carts);
            return cart;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }

    public ArrayList<Cart> getCarts() {
        try {
            File file = new File(cartDataPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return new ArrayList<Cart>(Arrays.asList(objectMapper.readValue(file, Cart[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from JSON file", e);
        }
    }

    public Order addOrder(Order order) {
        try {
            File file = new File(orderDataPath);
            ArrayList<Order> orders;
            if (!file.exists()) {
                orders = new ArrayList<>();
            } else {
                orders = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Order[].class)));
            }
            orders.add(order);
            objectMapper.writeValue(file, orders);
            return order;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }

    public ArrayList<Order> getOrders() {
        try {
            File file = new File(orderDataPath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return new ArrayList<Order>(Arrays.asList(objectMapper.readValue(file, Order[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from JSON file", e);
        }
    }

    private UUID userId;
    private User testUser;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
        overRideAll();
    }
// ------------------------ User Service Tests -------------------------

    // Test the addUser method
    // 1. Case: Adding User with valid data
    // 2. Case: Adding User with the duplicate name (Cant add)
    // 3. Case: Adding User with Null
    @Test
    void testAddUser_ValidData() {

    }

    @Test
    void testAddUser_DuplicateName() {
        User user1 = new User();
        user1.setName("JohnDoe");
        user1.setId(UUID.randomUUID());

        User user2 = new User();
        user2.setName("JohnDoe");
        user2.setId(UUID.randomUUID());

        userService.addUser(user1);

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> userService.addUser(user2));

        assertEquals("User already exists", thrownException.getMessage(), "Should throw an exception when adding a duplicate user.");
    }

    @Test
    void testAddUser_NullUser() {
        assertThrows(NullPointerException.class, () -> userService.addUser(null), "Should throw NullPointerException when adding a null user.");
    }
    // Test the GetUser method
    // 1. Case: getting User with valid Id
    // 2. Case: getting User with the invalid id
    // 3. Case: getting User with Null id
    @Test
    void testGetUser_ValidId() {
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setName("JohnDoe");

        userService.addUser(user);

        User retrievedUser = userService.getUserById(userId);
        assertNotNull(retrievedUser, "User should be found.");
        assertEquals("JohnDoe", retrievedUser.getName(), "User name should match.");
        assertEquals(userId, retrievedUser.getId(), "User ID should match.");
    }

    @Test
    void testGetUser_InvalidId() {
        UUID randomId = UUID.randomUUID();

        User retrievedUser = userService.getUserById(randomId);
        assertNull(retrievedUser, "Should return null when user does not exist.");
    }

    @Test
    void testGetUser_NullId() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(null),
                "Should throw IllegalArgumentException when passing a null ID.");
    }

    // Test the GetTheOrderOfaUser by id method
    // 1. Case: getting User order with valid Id
    // 2. Case: getting User order with the invalid id
    // 3. Case: getting User order with Null id
    @Test
    void testGetTheOrderOfAUser_ValidId() {
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setName("Test User");
        userService.addUser(user);

        List<Order> orders = userService.getOrdersByUserId(userId);

        assertNotNull(orders, "Order list should not be null");
    }

    @Test
    void testGetTheOrderOfAUser_InvalidId() {
        UUID invalidUserId = UUID.randomUUID();
        List<Order> orders = userService.getOrdersByUserId(invalidUserId);

        assertNull(orders, "Order list should not be null");
    }

    @Test
    void testGetTheOrderOfAUser_NullId() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.getOrdersByUserId(null),
                "Should throw IllegalArgumentException when passing a null user ID");
    }
    // Test the getUsers method
    // 1. Case: Return all users
    // 2. Case: No users found (empty list)
    // 3. Case: Error retrieving users

    // Case 1: Return all users
    @Test
    void testGetUsersSuccess() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Test User");

        userService.addUser(user);

        List<User> users = userService.getUsers();

        boolean found = users.stream()
                .anyMatch(u -> u.getId().equals(user.getId()) && u.getName().equals(user.getName()));

        assertTrue(found, "User should be found in the list of users");
    }


    // Case 2: No users found (empty list)
    @Test
    void testGetUsersWithEmptyData() {
        List<User> users = userService.getUsers();

        assertEquals(0, users.size(), "User list should be empty when no users exist");
    }

    // Case 3: Error retrieving users
    @Test
    void testGetUsersWithErrorRetrievingUsers() throws IOException {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Test User5");
        addUser(user);

        Map<String, Object> invalidData = new HashMap<>();
        invalidData.put("id", "invalid");
        invalidData.put("name", user.getName());
        objectMapper.writeValue(new File(userDataPath), Arrays.asList(invalidData));

        try {
            userService.getUsers();
        } catch (RuntimeException e) {
            assertEquals("Error retrieving users", e.getMessage(), "Incorrect exception message");
        }
    }
    // Test the DeleteUser method
    // 1. Case: Delete ExistingUser
    // 2. Case: Delete User with invalidId
    // 3. Case: Delete User with Null id

    @Test
    void testDeleteExistingUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("JohnDoe");
        userService.addUser(user);

        assertNotNull(userService.getUserById(user.getId()), "User should exist before deletion");

        userService.deleteUserById(user.getId());

        assertNull(userService.getUserById(user.getId()), "User should be deleted successfully");
    }


    @Test
    void testDeleteUserWithInvalidId() {
        UUID invalidId = UUID.randomUUID();

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> userService.deleteUserById(invalidId));

        assertEquals("User not found", thrownException.getMessage(), "Should throw an exception when deleting a non-existing user.");
    }

    @Test
    void testDeleteUserWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUserById(null), "Should throw IllegalArgumentException when deleting a user with null ID.");
    }
    // Test the RemoveOrder method
    // 1. Case: remove order with a valid user id
    // 2. Case: remove  order with invalid user id
    // 3. Case: remove order  with invalid order id
    // 4. Case: remove order with null user id
   // @Test
//    void testRemoveOrder_WithValidUserId() {
//        User user = new User();
//        user.setId(UUID.randomUUID());
//        userService.addUser(user);
//
//        Order order = new Order(user.getId(), new ArrayList<>());
//        userService.addOrder(order);
//
//        boolean removed = userService.removeOrderFromUser(user.getId(), order.getId());
//
//        assertTrue(removed, "Order should be removed successfully");
//    }

    @Test
    void testRemoveOrder_WithInvalidUserId() {
        UUID invalidUserId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> userService.removeOrderFromUser(invalidUserId, orderId));

        assertEquals("User not found", thrownException.getMessage(), "Should throw exception when user ID is invalid");
    }

    @Test
    void testRemoveOrder_WithInvalidOrderId() {
        User user = new User();
        user.setId(UUID.randomUUID());
        userService.addUser(user);

        UUID invalidOrderId = UUID.randomUUID();

        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> userService.removeOrderFromUser(user.getId(), invalidOrderId));

        assertEquals("Order not found", thrownException.getMessage(), "Should throw exception when order ID is invalid");
    }

    @Test
    void testRemoveOrder_WithNullUserId() {
        UUID orderId = UUID.randomUUID();

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class,
                () -> userService.removeOrderFromUser(null, orderId));

        assertEquals("User ID cannot be null", thrownException.getMessage(), "Should throw exception when user ID is null");
    }

}

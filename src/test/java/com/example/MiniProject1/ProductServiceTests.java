package com.example.MiniProject1;
import static org.junit.jupiter.api.Assertions.*;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.example.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class ProductServiceTests {
    private ProductRepository productRepository;
    private ProductService productService;
    private static final String PRODUCT_JSON_PATH = "src/main/java/com/example/data/products.json";

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
        productService = new ProductService(productRepository);
    }

    @AfterEach
    void cleanUp() {
        try (FileWriter writer = new FileWriter(PRODUCT_JSON_PATH)) {
            writer.write("[]");  // Reset file content to an empty JSON array
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to reset JSON file after test execution.");
        }
    }

    @Test
    void testAddProduct_Success() {
        Product product = new Product(UUID.randomUUID(), "Laptop", 1200.00);
        Product addedProduct = productService.addProduct(product);
        assertNotNull(addedProduct);
        assertEquals("Laptop", addedProduct.getName());
    }

    @Test
    void testAddProduct_DuplicateId() {
        UUID id = UUID.randomUUID();
        Product product1 = new Product(id, "Phone", 800.00);
        Product product2 = new Product(id, "Tablet", 500.00);

        productService.addProduct(product1);
        Exception exception = assertThrows(RuntimeException.class, () -> productService.addProduct(product2));
        assertEquals("Product with the same ID already exists.", exception.getMessage());
    }

    @Test
    void testAddProduct_InvalidData() {
        Product invalidProduct = new Product(UUID.randomUUID(), "", -50.00);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.addProduct(invalidProduct));
        assertEquals("Invalid product data", exception.getMessage());
    }

    @Test
    void testGetProducts_EmptyList() {
        List<Product> result = productService.getProducts();
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetProducts_NonEmptyList() {
        productService.addProduct(new Product(UUID.randomUUID(), "Laptop", 1200.00));
        List<Product> result = productService.getProducts();
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetProducts_MultipleProducts() {
        productService.addProduct(new Product(UUID.randomUUID(), "Laptop", 1200.00));
        productService.addProduct(new Product(UUID.randomUUID(), "Smartphone", 800.00));

        List<Product> products = productService.getProducts();
        assertEquals(2, products.size());
    }

    @Test
    void testGetProductById_Valid() {
        Product product = new Product(UUID.randomUUID(), "Mouse", 25.00);
        productService.addProduct(product);

        Product fetchedProduct = productService.getProductById(product.getId());
        assertNotNull(fetchedProduct);
        assertEquals("Mouse", fetchedProduct.getName());
    }

    @Test
    void testGetProductById_NotFound() {
        Product result = productService.getProductById(UUID.randomUUID());
        assertNull(result);
    }

    @Test
    void testGetProductById_MalformedId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.getProductById(null));
        assertEquals("Invalid product ID", exception.getMessage());
    }

    @Test
    void testUpdateProduct_Success() {
        Product product = new Product(UUID.randomUUID(), "Headphones", 50.00);
        productService.addProduct(product);

        Product updatedProduct = productService.updateProduct(product.getId(), "Wireless Headphones", 70.00);
        assertEquals("Wireless Headphones", updatedProduct.getName());
        assertEquals(70.00, updatedProduct.getPrice());
    }

    @Test
    void testUpdateProduct_NonExistent() {
        UUID nonExistentId = UUID.randomUUID();
        Exception exception = assertThrows(RuntimeException.class, () -> productService.updateProduct(nonExistentId, "Tablet", 300.00));
        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void testUpdateProduct_InvalidData() {
        Product product = new Product(UUID.randomUUID(), "Mouse", 25.00);
        productService.addProduct(product);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(product.getId(), "", -20.00));
        assertEquals("Invalid product data", exception.getMessage());
    }

    @Test
    void testApplyDiscount_ValidProducts() {
        Product p1 = productService.addProduct(new Product(UUID.randomUUID(), "Keyboard", 50.00));
        Product p2 = productService.addProduct(new Product(UUID.randomUUID(), "Monitor", 200.00));

        List<UUID> productIds = List.of(p1.getId(), p2.getId());
        productService.applyDiscount(20, new ArrayList<>(productIds));

        Product updatedP1 = productService.getProductById(p1.getId());
        Product updatedP2 = productService.getProductById(p2.getId());

        assertEquals(40.00, updatedP1.getPrice());
        assertEquals(160.00, updatedP2.getPrice());
    }

    @Test
    void testApplyDiscount_NonExistentProduct() {
        List<UUID> productIds = List.of(UUID.randomUUID());
        Exception exception = assertThrows(RuntimeException.class, () -> productService.applyDiscount(15, new ArrayList<>(productIds)));
        assertEquals("Some products not found", exception.getMessage());
    }

    @Test
    void testApplyDiscount_InvalidPercentage() {
        Product p1 = productService.addProduct(new Product(UUID.randomUUID(), "Headset", 75.00));

        List<UUID> productIds = List.of(p1.getId());

        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> productService.applyDiscount(-10, new ArrayList<>(productIds)));
        assertEquals("Discount must be between 0 and 100", exception1.getMessage());

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> productService.applyDiscount(110, new ArrayList<>(productIds)));
        assertEquals("Discount must be between 0 and 100", exception2.getMessage());
    }

    @Test
    void testDeleteProduct_Success() {
        Product product = new Product(UUID.randomUUID(), "Smartwatch", 200.00);
        productService.addProduct(product);

        productService.deleteProductById(product.getId());
        assertNull(productService.getProductById(product.getId()));
    }

    @Test
    void testDeleteProduct_InvalidId() {
        UUID invalidId = UUID.randomUUID();
        Exception exception = assertThrows(RuntimeException.class, () -> productService.deleteProductById(invalidId));
        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void testDeleteProduct_NotAppearing() {
        Product product = productService.addProduct(new Product(UUID.randomUUID(), "Gaming Chair", 250.00));
        UUID productId = product.getId();

        productService.deleteProductById(productId);
        assertNull(productService.getProductById(productId));
    }

    @Test
    void testDeleteProduct_NullId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.deleteProductById(null));
        assertEquals("Invalid product ID", exception.getMessage());
    }





}


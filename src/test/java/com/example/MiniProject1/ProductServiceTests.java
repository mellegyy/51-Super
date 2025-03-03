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
    void testUpdateProduct_Success() {
        Product product = new Product(UUID.randomUUID(), "Headphones", 50.00);
        productService.addProduct(product);

        Product updatedProduct = productService.updateProduct(product.getId(), "Wireless Headphones", 70.00);
        assertEquals("Wireless Headphones", updatedProduct.getName());
        assertEquals(70.00, updatedProduct.getPrice());
    }

    @Test
    void testDeleteProduct_Success() {
        Product product = new Product(UUID.randomUUID(), "Smartwatch", 200.00);
        productService.addProduct(product);

        productService.deleteProductById(product.getId());
        assertNull(productService.getProductById(product.getId()));
    }
}


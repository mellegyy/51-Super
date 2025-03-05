package com.example.MiniProject1;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import com.example.service.CartService;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private UserService userService;


    @InjectMocks
    private CartService cartService;

    private UUID cartId;
    private UUID userId;
    private Cart cart;
    private Product product;


    @BeforeEach
    void setUp() {
        cartId = UUID.randomUUID();
        userId = UUID.randomUUID();
        cart = new Cart(cartId, userId, new ArrayList<>());
        product = new Product(UUID.randomUUID(), "Test Product", 25.0);
    }

    // --- Tests for addCart ---

    @Test
    void addCart_Success() {
        when(cartRepository.addCart(cart)).thenReturn(cart);
        Cart result = cartService.addCart(cart);
        assertNotNull(result);
        assertEquals(cart.getId(), result.getId());
    }

    @Test
    void addCart_NullCart_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> cartService.addCart(null));
    }

    @Test
    void addCart_ShouldCallRepositoryMethod() {
        cartService.addCart(cart);
        verify(cartRepository, times(1)).addCart(cart);
    }

    // --- Tests for getCarts ---

    @Test
    void getCarts_ShouldReturnListOfCarts() {
        Cart cart = new Cart();
        List<Cart> carts = new ArrayList<>(List.of(cart));
        when(cartRepository.getCarts()).thenReturn((ArrayList<Cart>) carts);
        List<Cart> result = cartService.getCarts();
        assertEquals(1, result.size());
        assertEquals(cart, result.get(0));
    }

    @Test
    void getCarts_EmptyList_ShouldReturnEmpty() {
        when(cartRepository.getCarts()).thenReturn(new ArrayList<>());
        List<Cart> result = cartService.getCarts();
        assertTrue(result.isEmpty());
    }

    @Test
    void getCarts_ShouldCallRepositoryMethod() {
        cartService.getCarts();
        verify(cartRepository, times(1)).getCarts();
    }

    // --- Tests for getCartById ---

    @Test
    void getCartById_Success() {
        when(cartRepository.getCartById(cartId)).thenReturn(cart);
        Cart result = cartService.getCartById(cartId);
        assertNotNull(result);
        assertEquals(cartId, result.getId());
    }

    @Test
    void getCartById_NotFound_ShouldReturnNull() {
        when(cartRepository.getCartById(cartId)).thenReturn(null);
        Cart result = cartService.getCartById(cartId);
        assertNull(result);
    }

    @Test
    void getCartById_ShouldCallRepositoryMethod() {
        cartService.getCartById(cartId);
        verify(cartRepository, times(1)).getCartById(cartId);
    }

    // --- Tests for getCartByUserId ---

    @Test
    void getCartByUserId_Success() {
        when(cartRepository.getCartByUserId(userId)).thenReturn(cart);
        Cart result = cartService.getCartByUserId(userId);
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }

    @Test
    void getCartByUserId_NotFound_ShouldReturnNull() {
        when(cartRepository.getCartByUserId(userId)).thenReturn(null);
        Cart result = cartService.getCartByUserId(userId);
        assertNull(result);
    }

    @Test
    void getCartByUserId_ShouldCallRepositoryMethod() {
        cartService.getCartByUserId(userId);
        verify(cartRepository, times(1)).getCartByUserId(userId);
    }

    // --- Tests for addProductToCart ---

    @Test
    void addProductToCart_Success() {
        doNothing().when(cartRepository).addProductToCart(cartId, product);
        cartService.addProductToCart(cartId, product);
        verify(cartRepository, times(1)).addProductToCart(cartId, product);
    }

    @Test
    void addProductToCart_NullProduct_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> cartService.addProductToCart(cartId, null));
    }

    @Test
    void addProductToCart_ShouldCallRepositoryMethod() {
        cartService.addProductToCart(cartId, product);
        verify(cartRepository, times(1)).addProductToCart(cartId, product);
    }

    // --- Tests for deleteProductFromCart ---

    @Test
    void deleteProductFromCart_Success() {
        doNothing().when(cartRepository).deleteProductFromCart(cartId, product);
        cartService.deleteProductFromCart(cartId, product);
        verify(cartRepository, times(1)).deleteProductFromCart(cartId, product);
    }

    @Test
    void deleteProductFromCart_NullProduct_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> cartService.deleteProductFromCart(cartId, null));
    }

    @Test
    void deleteProductFromCart_ShouldCallRepositoryMethod() {
        cartService.deleteProductFromCart(cartId, product);
        verify(cartRepository, times(1)).deleteProductFromCart(cartId, product);
    }

    // --- Tests for deleteCartById ---

    @Test
    void deleteCartById_Success() {
        doNothing().when(cartRepository).deleteCartById(cartId);
        cartService.deleteCartById(cartId);
        verify(cartRepository, times(1)).deleteCartById(cartId);
    }

    @Test
    void deleteCartById_NullCartId_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> cartService.deleteCartById(null));
    }

    @Test
    void deleteCartById_ShouldCallRepositoryMethod() {
        cartService.deleteCartById(cartId);
        verify(cartRepository, times(1)).deleteCartById(cartId);
    }

    // --- Tests for emptyCart (which is inside UserService) ---

    @Test
    void emptyCart_UserHasProducts_CartShouldBeEmpty() {
        Cart userCart = new Cart(cartId, userId, new ArrayList<>());
        userCart.getProducts().add(new Product(UUID.randomUUID(), "Test Product", 10.0));
        when(cartRepository.getCartByUserId(userId)).thenReturn(userCart);
        userService.emptyCart(userId);
        System.out.println("Cart size after emptyCart: " + userCart.getProducts().size());
        assertTrue(userCart.getProducts().isEmpty(), "Cart should be empty after calling emptyCart()");
        verify(cartRepository, times(1)).getCartByUserId(userId);
    }

    @Test
    void emptyCart_UserHasNoCart_ShouldDoNothing() {
        lenient().when(cartRepository.getCartByUserId(userId)).thenReturn(null); // Use lenient()
        userService.emptyCart(userId);
        verify(cartRepository, never()).deleteCartById(any());
        verify(cartRepository, never()).addCart(any());
    }

    @Test
    void emptyCart_NullUserId_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> userService.emptyCart(null),
                "Should throw NullPointerException when userId is null");
    }

}

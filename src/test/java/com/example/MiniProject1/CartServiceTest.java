package com.example.MiniProject1;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import com.example.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    private UUID userId;
    private UUID cartId;
    private Cart cart;
    private Product product;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        cartId = UUID.randomUUID();
        product = new Product(UUID.randomUUID(), "Test Product", 100.0);
        cart = new Cart(cartId, userId, new ArrayList<>());
    }

    // Test addCart()
    @Test
    void addCart_Success() {
        when(cartRepository.addCart(cart)).thenReturn(cart);

        Cart result = cartService.addCart(cart);

        assertNotNull(result);
        assertEquals(cart.getId(), result.getId());
        verify(cartRepository, times(1)).addCart(cart);
    }

    @Test
    void addCart_NullCart_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> cartService.addCart(null));
    }

    // Test getCarts()
    @Test
    void getCarts_ReturnsListOfCarts() {
        ArrayList<Cart> carts = new ArrayList<>();
        carts.add(cart);
        when(cartRepository.getCarts()).thenReturn(carts);

        ArrayList<Cart> result = cartService.getCarts();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(cartRepository, times(1)).getCarts();
    }

    // Test getCartById()
    @Test
    void getCartById_ExistingCart_ReturnsCart() {
        when(cartRepository.getCartById(cartId)).thenReturn(cart);

        Cart result = cartService.getCartById(cartId);

        assertNotNull(result);
        assertEquals(cartId, result.getId());
    }

    @Test
    void getCartById_NonExistentCart_ReturnsNull() {
        when(cartRepository.getCartById(cartId)).thenReturn(null);

        Cart result = cartService.getCartById(cartId);

        assertNull(result);
    }

    // Test getCartByUserId()
    @Test
    void getCartByUserId_ExistingCart_ReturnsCart() {
        when(cartRepository.getCartByUserId(userId)).thenReturn(cart);

        Cart result = cartService.getCartByUserId(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }

    // Test addProductToCart()
    @Test
    void addProductToCart_ValidCart_AddsProduct() {
        doNothing().when(cartRepository).addProductToCart(cartId, product);

        cartService.addProductToCart(cartId, product);

        verify(cartRepository, times(1)).addProductToCart(cartId, product);
    }

    // Test deleteProductFromCart()
    @Test
    void deleteProductFromCart_ValidProduct_DeletesProduct() {
        doNothing().when(cartRepository).deleteProductFromCart(cartId, product);

        cartService.deleteProductFromCart(cartId, product);

        verify(cartRepository, times(1)).deleteProductFromCart(cartId, product);
    }

    // Test deleteCartById()
    @Test
    void deleteCartById_ExistingCart_DeletesCart() {
        doNothing().when(cartRepository).deleteCartById(cartId);

        cartService.deleteCartById(cartId);

        verify(cartRepository, times(1)).deleteCartById(cartId);
    }
}

package io.github.deepakjvm.omnigrocer.services;

import io.github.deepakjvm.omnigrocer.models.Cart;
import io.github.deepakjvm.omnigrocer.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartService(cartRepository);
    }

    @Test
    void testAddCart() {
        Cart cart = new Cart();
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart saved = cartService.addCart(cart);
        assertSame(cart, saved);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testGetCarts() {
        Cart c1 = new Cart();
        Cart c2 = new Cart();
        when(cartRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Cart> carts = cartService.getCarts();
        assertEquals(2, carts.size());
        verify(cartRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCart() {
        Cart cart = new Cart();
        when(cartRepository.save(cart)).thenReturn(cart);

        cartService.updateCart(cart);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testDeleteCart() {
        Cart cart = new Cart();
        doNothing().when(cartRepository).delete(cart);

        cartService.deleteCart(cart);
        verify(cartRepository, times(1)).delete(cart);
    }
}

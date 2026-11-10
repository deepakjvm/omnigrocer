package io.github.deepakjvm.ecommerce.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    @Test
    void testCartGettersAndSetters() {
        Cart cart = new Cart();
        User customer = new User();
        customer.setId(7);
        customer.setUsername("buyer1");

        cart.setId(101);
        cart.setCustomer(customer);

        assertEquals(101, cart.getId());
        assertEquals(customer, cart.getCustomer());
        assertEquals("buyer1", cart.getCustomer().getUsername());
    }

    @Test
    void testCartDefaultValues() {
        Cart cart = new Cart();
        assertEquals(0, cart.getId());
        assertNull(cart.getCustomer());
    }
}

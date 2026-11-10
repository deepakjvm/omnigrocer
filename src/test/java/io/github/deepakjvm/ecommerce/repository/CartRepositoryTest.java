package io.github.deepakjvm.ecommerce.repository;

import io.github.deepakjvm.ecommerce.models.Cart;
import io.github.deepakjvm.ecommerce.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindCart() {
        User user = new User();
        user.setUsername("cartowner");
        user.setEmail("owner@example.com");
        User savedUser = userRepository.save(user);

        Cart cart = new Cart();
        cart.setCustomer(savedUser);

        Cart saved = cartRepository.save(cart);
        assertTrue(saved.getId() > 0);

        Optional<Cart> found = cartRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("cartowner", found.get().getCustomer().getUsername());
    }

    @Test
    void testDeleteCart() {
        Cart cart = new Cart();
        Cart saved = cartRepository.save(cart);

        cartRepository.delete(saved);
        assertFalse(cartRepository.findById(saved.getId()).isPresent());
    }
}

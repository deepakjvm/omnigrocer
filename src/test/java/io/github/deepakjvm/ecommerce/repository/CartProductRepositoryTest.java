package io.github.deepakjvm.ecommerce.repository;

import io.github.deepakjvm.ecommerce.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CartProductRepositoryTest {

    @Autowired
    private CartProductRepository cartProductRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveAndFindCartProduct() {
        Cart cart = new Cart();
        Cart savedCart = cartRepository.save(cart);

        Product product = new Product();
        product.setName("Milk");
        Product savedProduct = productRepository.save(product);

        CartProduct cartProduct = new CartProduct(savedCart, savedProduct);
        CartProduct saved = cartProductRepository.save(cartProduct);

        CartProductId id = new CartProductId(savedCart.getId(), savedProduct.getId());
        Optional<CartProduct> found = cartProductRepository.findById(id);

        assertTrue(found.isPresent());
        assertEquals("Milk", found.get().getProduct().getName());

        cartProductRepository.delete(saved);
        assertFalse(cartProductRepository.findById(id).isPresent());
    }
}

package io.github.deepakjvm.omnigrocer.services;

import io.github.deepakjvm.omnigrocer.models.Product;
import io.github.deepakjvm.omnigrocer.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @Test
    void testGetProducts() {
        Product p1 = new Product();
        Product p2 = new Product();
        when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Product> products = productService.getProducts();
        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testAddProduct() {
        Product product = new Product();
        product.setName("Tablet");
        when(productRepository.save(product)).thenReturn(product);

        Product saved = productService.addProduct(product);
        assertEquals("Tablet", saved.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testGetProduct() {
        Product product = new Product();
        product.setId(10);
        when(productRepository.findById(10)).thenReturn(Optional.of(product));
        when(productRepository.findById(99)).thenReturn(Optional.empty());

        assertEquals(product, productService.getProduct(10));
        assertNull(productService.getProduct(99));
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product();
        product.setName("Laptop");
        when(productRepository.save(product)).thenReturn(product);

        Product updated = productService.updateProduct(15, product);
        assertEquals(15, updated.getId());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testDeleteProductWhenExists() {
        when(productRepository.existsById(5)).thenReturn(true);

        boolean result = productService.deleteProduct(5);
        assertTrue(result);
        verify(productRepository, times(1)).deleteById(5);
    }

    @Test
    void testDeleteProductWhenDoesNotExist() {
        when(productRepository.existsById(99)).thenReturn(false);

        boolean result = productService.deleteProduct(99);
        assertFalse(result);
        verify(productRepository, never()).deleteById(anyInt());
    }
}

package io.github.deepakjvm.omnigrocer.repository;

import io.github.deepakjvm.omnigrocer.models.Category;
import io.github.deepakjvm.omnigrocer.models.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveAndFindProduct() {
        Category category = new Category();
        category.setName("Fresh Produce");
        Category savedCategory = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Banana");
        product.setPrice(2);
        product.setQuantity(50);
        product.setWeight(120);
        product.setDescription("Organic banana");
        product.setImage("banana.png");
        product.setCategory(savedCategory);

        Product saved = productRepository.save(product);
        assertTrue(saved.getId() > 0);

        Optional<Product> found = productRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Banana", found.get().getName());
        assertEquals(2, found.get().getPrice());
        assertEquals("Fresh Produce", found.get().getCategory().getName());
    }

    @Test
    void testFindAllAndCount() {
        Product p1 = new Product();
        p1.setName("Item 1");
        productRepository.save(p1);

        Product p2 = new Product();
        p2.setName("Item 2");
        productRepository.save(p2);

        List<Product> products = productRepository.findAll();
        assertTrue(products.size() >= 2);
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setName("Disposable Item");
        Product saved = productRepository.save(product);

        productRepository.deleteById(saved.getId());
        assertFalse(productRepository.existsById(saved.getId()));
    }
}

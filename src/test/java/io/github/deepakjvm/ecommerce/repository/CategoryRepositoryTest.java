package io.github.deepakjvm.ecommerce.repository;

import io.github.deepakjvm.ecommerce.models.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveAndFindCategory() {
        Category category = new Category();
        category.setName("Dairy");

        Category saved = categoryRepository.save(category);
        assertTrue(saved.getId() > 0);

        Optional<Category> found = categoryRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Dairy", found.get().getName());
    }

    @Test
    void testFindAllCategories() {
        Category cat1 = new Category();
        cat1.setName("Bakery");
        categoryRepository.save(cat1);

        Category cat2 = new Category();
        cat2.setName("Beverages");
        categoryRepository.save(cat2);

        List<Category> all = categoryRepository.findAll();
        assertTrue(all.size() >= 2);
    }

    @Test
    void testDeleteCategory() {
        Category category = new Category();
        category.setName("Snacks");
        Category saved = categoryRepository.save(category);

        categoryRepository.deleteById(saved.getId());
        assertFalse(categoryRepository.existsById(saved.getId()));
    }
}
